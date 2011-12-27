//
//  StartScreenViewController.m
//  STREAMY 3
//
//  Created by student on 26/10/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//
#define kBgQueue dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)
#define NotificationUrl [NSURL URLWithString: @"http://streamy11.appspot.com/api/entity/"]

#import "StartScreenViewController.h"
#import "NotificationViewController.h"
#import "JSONParser.h"


@implementation StartScreenViewController{
	// A local array containing all notifications
	NSMutableArray *notifications;
    
	// The imageView for the arrow of the streamy-meter. Needs to be added in the code, so it can be animated.
    UIImageView *myImage;
}

/*
 A float with values between 0.00 and 1.00, representing a percentage that indicates how good you are up-to-date.
 */
@synthesize progress;

@synthesize slogans;

/*
 * An array containing all notifications (not only the ones that are selected)
 */
@synthesize notifications;

// Auto-generated function
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

// Auto-generated function
- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

/*
 * This function is executed every time the view is loaded. Because progress meter needs to be
 * updated every time the view is loaded, this is done here.
 */
- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // ??? kunnen de volgende 4 regels niet naar de init-functie?
    // Gets the correct imageView 
    UIImageView *meterView = (UIImageView *)[self.view viewWithTag:66];
    // Set the image (arrow) of the streamy-meter
    [meterView setImage:[UIImage imageNamed:@"streamy-arrow.png"]];
    
    UINavigationBar *nb = [[self navigationController] navigationBar];
    [nb insertSubview:[[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"header_small.png"]] autorelease] atIndex:1];

    
    //[self.slogans initWithObjects:@"You are hopelessly neglecting schoolwork"];
    
    // Every time the view is loaded, the progress meter needs to be updated
    // ??? moet dit niet geïnitialiseerd worden met self.progress ipv een concrete waarde?
        
    //backend coupling
    if ([self.notifications count] == 0) {
        NSLog(@"Loading JSON data");
        dispatch_async(kBgQueue, ^{
            NSData* data = [NSData dataWithContentsOfURL: 
                            NotificationUrl];
            [self performSelectorOnMainThread:@selector(fetchedData:) 
                                   withObject:data waitUntilDone:YES];
        });
    }
    
   }

-(void)fetchedData:(NSData *)responseData {
    
    NSError* error;
    
    @try
    {
        NSDictionary* json = [NSJSONSerialization 
                              JSONObjectWithData:responseData //1
                              
                              options:kNilOptions 
                              error:&error];
        
        NSArray* notiJson = [json objectForKey:@"results"]; //2
        
        //fill local array with Notification objects
        self.notifications = [NSMutableArray arrayWithCapacity:20];
        for (int i = 0; i<[notiJson count]; i++) {
            
            Notification* noti = [JSONParser getNotification:[notiJson objectAtIndex:i]];
            [self.notifications addObject:noti];
        }
        
        [self.notifications sortUsingSelector:@selector(compare:)];
        NSLog(@"JSON data loaded and sorted");
        UIImageView *splash = (UIImageView*)[[self view] viewWithTag:10];
        [splash setAlpha:0];
        
        NSString *streamyData = [self displayContent];
        
        uint seen = [[streamyData componentsSeparatedByString:@"\n"] count];
        uint nots = [self.notifications count];
        
        
        [self updateProgressMeter:((float) seen / (float)nots)];

        
    }
    @catch(NSException* ex)
    {
        NSString *message = [NSString stringWithFormat:@"Bad responce from server.\nPleas try again later."];
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Server Error"
                              
                                                        message: message delegate:self cancelButtonTitle:@"Close" otherButtonTitles:nil];
        
        [alert show];
        
    }
    
    //[vakkenSource fetchedData:responseData];
    //Notify the tableView that new data is available
    //[TableView reloadData];
    
}

//Method retrieves content from documents directory and

//displays it in an alert

-(NSString*) displayContent{
    
    //get the documents directory:
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains
    
    (NSDocumentDirectory, NSUserDomainMask, YES);
    
    NSString *documentsDirectory = [paths objectAtIndex:0];
    
    //make a file name to write the data to using the documents directory:
    
    NSString *fileName = [NSString stringWithFormat:@"%@/streamyData.txt", 
                          
                          documentsDirectory];
    
    NSString *content = [[NSString alloc] initWithContentsOfFile:fileName
                         
                                                    usedEncoding:nil
                         
                                                           error:nil];
    
    //use simple alert from my library (see previous post for details)
    NSLog(content);
    return content;
    
    [content release];
    
}

// Auto-generated function
- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

// Auto-generated function
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

/*
 * This function is executed before a segue (transition to the next screen) is executed. 
 *
 * When one of the six buttons is pressed, the segue is executed and before the segue is
 * executed, this function is executed (standard, done by the operating system). 
 * It makes a connection to the next screen, and initializes the booleans of that next sreen (which
 * indicate which types of notifications are selected).
 */
-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{  
    // Get NotificationViewController (this viewController is embedded in a navigationController so
    // that needs to be accessed before the notificationViewController can be accessed).
    UINavigationController *navigationcontroller = [segue destinationViewController];
    
    NotificationViewController *notif = [[navigationcontroller viewControllers]objectAtIndex:0];
    
    // Sets the booleans of the notificationViewController to the correct value
    if ([(UIButton *)sender tag] == 100) {
        notif.twitterSelected = YES;
    } else if ([(UIButton *)sender tag] == 101) {
        notif.scheduleSelected = YES;
    } else     if ([(UIButton *)sender tag] == 102) {
        notif.commentSelected = YES;
    } else     if ([(UIButton *)sender tag] == 103) {
        notif.fileSelected = YES;
    } else     if ([(UIButton *)sender tag] == 105) {
        notif.rssSelected = YES;
    } else     if ([(UIButton *)sender tag] == 104) {
        notif.twitterSelected = YES;
        notif.scheduleSelected = YES;
        notif.commentSelected= YES;
        notif.fileSelected = YES;
        notif.rssSelected = YES;
    }
    
    //??? op hoeveel zou ik capaciteit moeten zetten?     
    //notifications = [NSMutableArray arrayWithCapacity:20];
    
    //Creation of some test objects
   /* Communicator *communicator1 = [[Communicator alloc] init];
    communicator1.communicatorName = @"Roeland";
    Communicator *communicator2 = [[Communicator alloc] init];
    communicator2.communicatorName = @"Mume11";
    Notification *notification1 = [[Notification alloc] init];
    notification1.notificationSender = communicator1;
    notification1.notificationReciever = communicator2;
    notification1.type = Twitter;
    notification1.title = @"Nieuw Twitterbericht";
    notification1.message = @"Als alles goed gaat komt deze tekst in het label van de TwitterDetailViewController";
    [notifications addObject:notification1];
    Notification *notification2 = [[Notification alloc] init];
    notification2.notificationSender = communicator2;
    notification2.notificationReciever = communicator1;
    notification2.type = File;
    notification2.title = @"File-update";
    notification2.message = @"Als alles goed gaat komt deze tekst in het label van de FileDetailViewController";
    [notifications addObject:notification2];
    Notification *notification3 = [[Notification alloc] init];
    notification3.notificationSender = communicator1;
    notification3.notificationReciever = communicator2;
    notification3.type = Twitter;
    notification3.title = @"Nieuw Twitterbericht";
    notification3.message = @"Als alles goed gaat komt deze tekst in het label van de TwitterDetailViewController";
    [notifications addObject:notification3];
    */
    // Passes the notifications to the next viewController
    notif.notifications = self.notifications;
    
}

/*
 * Checks wether the given progess is a valid value for the progress property (between 0 and 1).
 */
-(BOOL)isValidProgress:(float)localprogress {
    NSLog(@"Validating Progress");
    BOOL localbool = NO;
    if ((localprogress >= 0) && (localprogress <= 1)) {
        localbool = YES;
    }
    return localbool;
}

/*
 * Updates the arrow of the progress meter.
 */
-(void)updateProgressMeter:(float)inputProgress {
    if ([self isValidProgress:inputProgress]) {
        self.progress = inputProgress;
    } else {
         NSLog(@"This is not a valid progress you're trying to set");
    }
    
    NSString* slogs[10] = {@"You're hopelessly neglecting schoolwork", 
                            @"Start spending some time on school now",
                            @"It's bad, try to get some work done fast",
                            @"It's getting worse, try to focus",
                            @"Below average, you can do better",
                            @"Average performance, could be much better",
                            @"You are doing fine",
                            @"Great work",
                            @"Doing great, perfection is around the corner",
                            @"perfect score! Keep up the good work"};
    
    //set text
    UILabel *meterLabel = (UILabel *) [self.view viewWithTag:221];
    
    uint index = (uint)(self.progress * 10);
    
    
    meterLabel.text = slogs[index];
    
    
    
    // Gets the imageView that contains the arrow
    UIImageView *meterView = (UIImageView *)[self.view viewWithTag:66];
        
    // Transform the imageView, so the image that represents the arrow is rotated
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:1.5];
    meterView.transform = CGAffineTransformMakeRotation(M_PI*inputProgress);
    [UIView commitAnimations];

}

@end