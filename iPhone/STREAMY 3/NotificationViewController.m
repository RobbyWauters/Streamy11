//
//  NotificationViewController.m
//  STREAMY 3
//
//  Created by student on 25/10/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//
#define kBgQueue dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)
#define NotificationUrl [NSURL URLWithString: @"http://streamy11.appspot.com/api/entries/tweet"]


#import "NotificationViewController.h"
#import "Notification.h"
#import "TwitterDetailtViewController.h"
#import "FileDetailViewController.h"
#import "ScheduleDetailViewController.h"
#import "BlogDetailViewController.h"
#import "CommentDetailViewController.h"
#import "JSONParser.h"


@implementation NotificationViewController {
    
    //Kan dit niet in de funtie updateImages staan of wordt dit nog ergens anders gebruikt?
    // nee, anders krijg je een reeks warnings in ifs die de image overschrijven.
    UIImage *twitterImage;
    UIImage *scheduleImage;
    UIImage *commentsImage;
    UIImage *filesImage;
    UIImage *rssImage;
    
    UIButton *twitterButton;
    UIButton *scheduleButton;
    UIButton *commentsButton;
    UIButton *filesButton;
    UIButton *rssButton;

    UIBarButtonItem *barItem1;
    UIBarButtonItem *barItem2;
    UIBarButtonItem *barItem3;
    UIBarButtonItem *barItem4;
    UIBarButtonItem *barItem5;
    
}

/*
 * An array containing all notifications (not only the ones that are selected)
 */
@synthesize notifications;

/*
 * An array containing all seennotifications
 */
@synthesize seen;

/*
 * Six booleans to represent whether each notoficationType is selected.
 */
@synthesize twitterSelected, scheduleSelected, commentSelected, fileSelected, rssSelected;

/*
 * This function is executed every time the view is loaded. It makes sure that the buttons on the
 * bottom are updated.
 */
-(void)loadView {
    [super loadView];
    
    [self updateButtons];
}

/*
 * Updates the image of the buttons on the bottom, depending on the values of the six booleans. 
 */
-(void)updateButtons {
    // Load the right images
    twitterImage = [UIImage imageNamed:@"nav_twitter_off.png"];
    scheduleImage = [UIImage imageNamed:@"nav_schedule_off.png"];
    commentsImage = [UIImage imageNamed:@"nav_comments_off.png"];
    filesImage = [UIImage imageNamed:@"nav_files_off.png"];
    rssImage = [UIImage imageNamed:@"nav_rss_off.png"];
    if (twitterSelected) {
        twitterImage = [UIImage imageNamed:@"nav_twitter.png"];
    }
    if (scheduleSelected){
        scheduleImage = [UIImage imageNamed:@"nav_schedule.png"];
    }
    if (commentSelected){
        commentsImage = [UIImage imageNamed:@"nav_comments.png"];
    }
    if (fileSelected){
        filesImage = [UIImage imageNamed:@"nav_files.png"]; 
    }
    if (rssSelected) {
        rssImage = [UIImage imageNamed:@"nav_rss.png"];
    }
    
    // Assign the images to the button
    twitterButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [twitterButton setImage:twitterImage forState:UIControlStateNormal];
    scheduleButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [scheduleButton setImage:scheduleImage forState:UIControlStateNormal];
    commentsButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [commentsButton setImage:commentsImage forState:UIControlStateNormal];
    filesButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [filesButton setImage:filesImage forState:UIControlStateNormal];
    rssButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [rssButton setImage:rssImage forState:UIControlStateNormal];
    
    // Set the size of the buttons
    twitterButton.frame = CGRectMake(0, 0, 55, 55);
    scheduleButton.frame = CGRectMake(0, 0,55, 55);
    commentsButton.frame = CGRectMake(0, 0, 55, 55);
    filesButton.frame = CGRectMake(0, 0, 55, 55);
    rssButton.frame = CGRectMake(0, 0, 55, 55);
    
    // Create a UIBarButtonItem with the button as a custom view
    barItem1 = [[UIBarButtonItem alloc] initWithCustomView:twitterButton];
    barItem2 = [[UIBarButtonItem alloc] initWithCustomView:scheduleButton];
    barItem3 = [[UIBarButtonItem alloc] initWithCustomView:commentsButton];
    barItem4 = [[UIBarButtonItem alloc] initWithCustomView:filesButton];
    barItem5 = [[UIBarButtonItem alloc] initWithCustomView:rssButton];
    
    // Create an array to hold the list of bar button items
    NSMutableArray *items = [[NSMutableArray alloc] init];
    
    // Add the items, including the custom button
    [items addObject:barItem1];
    [items addObject:barItem2];
    [items addObject:barItem3];
    [items addObject:barItem4];
    [items addObject:barItem5];
    
    // Set the items on the current toolbar
    self.toolbarItems = items;
    
    // Release the local array
    [items release];
    
    // Listen for clicks
    [twitterButton addTarget:self action:@selector(clickButton:) 
            forControlEvents:UIControlEventTouchUpInside];
    [scheduleButton addTarget:self action:@selector(clickButton:) 
             forControlEvents:UIControlEventTouchUpInside];
    [commentsButton addTarget:self action:@selector(clickButton:) 
             forControlEvents:UIControlEventTouchUpInside];
    [filesButton addTarget:self action:@selector(clickButton:) 
          forControlEvents:UIControlEventTouchUpInside];
    [rssButton addTarget:self action:@selector(clickButton:) 
        forControlEvents:UIControlEventTouchUpInside];
}

// Auto-generated function
- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
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

// Auto-generated function
- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    
    //backend coupling
    
    /*dispatch_async(kBgQueue, ^{
        NSData* data = [NSData dataWithContentsOfURL: 
                        NotificationUrl];
        [self performSelectorOnMainThread:@selector(fetchedData:) 
                               withObject:data waitUntilDone:YES];
    });*/
    self.seen = [[self displayContent] componentsSeparatedByString:@"\n"];
    NSLog(@"Notification DID LOAD");
    
    
    /*UINavigationBar *nb = [[self navigationController] navigationBar];
    [nb insertSubview:[[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"header_small.png"]] autorelease] atIndex:3];
    [nb setHidden:false];*/
    
    
    
      
    //[self writeToTextFile:@"701"];
    //[self displayContent];
}


//Method writes a string to a text file

-(void) writeToTextFile:(NSString*)notId{
    
    //get the documents directory:
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains
    
    (NSDocumentDirectory, NSUserDomainMask, YES);
    
    NSString *documentsDirectory = [paths objectAtIndex:0];
    
    //make a file name to write the data to using the documents directory:
    
    NSString *fileName = [NSString stringWithFormat:@"%@/streamyData.txt", 
                          
                          documentsDirectory];
    
    //create content - four lines of text
    NSString *content = [self displayContent];
    if (content == nil) {
        content = @"1";
    }
    content = [content stringByAppendingString:[@"\n" stringByAppendingString:notId]];

    NSLog(@"%@",documentsDirectory);
    
    //save content to the documents directory
    
    [content writeToFile:fileName
     
              atomically:NO
     
                encoding:NSStringEncodingConversionAllowLossy 
     
                   error:nil];
    
    
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
    NSLog(@"Notification DID UNLOAD");
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

// Auto-generated function
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    UINavigationBar *nb = [[self navigationController] navigationBar];
    [nb insertSubview:[[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"header_small.png"]] autorelease] atIndex:1];
    //UIImageView *imgview = [[nb subviews]objectAtIndex:1];
    //[nb bringSubviewToFront:imgview];
    [nb setHidden:false];
    
    UIToolbar *tb = [[self navigationController] toolbar];
    [tb insertSubview:[[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"toolbar2.png"]] autorelease] atIndex:1];
    [tb setHidden:false];
}

// Auto-generated function
- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];

    NSLog(@"Notification DID APPEAR");
    /*UINavigationBar *nb = [[self navigationController] navigationBar];
    //[nb removeFromSuperview];
    UIImageView *imgview = [[nb subviews]objectAtIndex:1];
    imgview.image = [UIImage imageNamed:@"header_small.png"];*/
    
    
    
    
}

// Auto-generated function
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    UINavigationBar *nb = [[self navigationController] navigationBar];
    UIImageView *imgview = [[nb subviews]objectAtIndex:0];
    [imgview removeFromSuperview];
}

// Auto-generated function
- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
}

// Auto-generated function
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

/*
 * Returns the number of rows in the table. This function is used by the OS to construct the
 * table.
 */
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [[self getAllRelevantNotifications]count];
}

/*
 * This function loads the information from the notifications in the cells of the table. 
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    //NSArray *seenNots = [[self displayContent] componentsSeparatedByString:@"\n"];
    
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"NotificationCell2"];
    Notification *notification = [[self getAllRelevantNotifications] objectAtIndex:indexPath.row];
    //??? hoe niet werken met standaardcell
	/*cell.textLabel.text = notification.title;
	cell.detailTextLabel.text = notification.message;*/
    //cell.timeLabel.text = @"10:00";
    
    //title
    UILabel *title = (UILabel*)[cell viewWithTag:1];
    title.text = notification.title;
    //text
    UILabel *message = (UILabel*)[cell viewWithTag:2];
    message.text = notification.message;
    //Time
    UILabel *time = (UILabel*)[cell viewWithTag:3];
        //Today's date at 00:00 used for comparison
        NSDate *today = [NSDate date];
        NSDateComponents* comps = [[NSCalendar currentCalendar] 
                               components:NSYearCalendarUnit|NSMonthCalendarUnit|NSDayCalendarUnit fromDate:today];
        today = [[NSCalendar currentCalendar] dateFromComponents:comps];
        
    //format different for notifications today
    NSDateFormatter *df = [NSDateFormatter new];
    if([[today earlierDate:notification.date] isEqualToDate:today]){
        [df setDateFormat:@"HH:mm"];
        
    }else{
        [df setDateFormat:@"dd/MM"];
    }
     
    time.text = [df stringFromDate:notification.date];
    
    
    //time.text = @"10:00";
    //comments
    UILabel *comments = (UILabel*)[cell viewWithTag:4];
    NSString* cmts = [NSString stringWithFormat:@"%d", notification.type];
    comments.text = cmts;
    //image
    UIImageView *sideImage = (UIImageView*)[cell viewWithTag:5];
    NSString *prefix = @"li_";
    NSString *extension = @".png";
    NSString *liType = nil;
    switch(notification.type) {
        case Twitter:
            liType = @"twitter";
            break;
        case RSS:
            liType = @"rss";
            break;
        case Comment:
            liType = @"comments";
            break;
        case Schedule:
            liType = @"schedule";
            break;   
        case File:
            liType = @"files";
            break;  
    }
    
    //NSString *liType = [NSString stringWithFormat:@"%d", indexPath.row];
    NSString *img1 = [prefix stringByAppendingString: liType];
    NSString *imageName = [img1 stringByAppendingString: extension];
    
    sideImage.image = [UIImage imageNamed:imageName];
    //NSString *liType = [notification type]
    
    /*UIImage *rowBackground;
    rowBackground = [UIImage imageNamed:@"cell_background.png"];
    ((UIImageView *)cell.backgroundView).image = rowBackground;*/
    
    //cell.backgroundView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"cell_background.png"]];
    UIImageView *bg = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"cell.png"]];
    bg.contentMode = UIViewContentModeBottom;
    cell.backgroundView = bg;
    cell.selectedBackgroundView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"cell.png"]];
    [[cell textLabel] setBackgroundColor:[UIColor clearColor]];
    [[cell detailTextLabel] setBackgroundColor:[UIColor clearColor]];
    
    //if read => alpha 0.6
    for (uint i = 0; i< [self.seen count]; i++) {
        NSString *nId = notification.notId;
        NSString *se = [self.seen objectAtIndex:i];
        [[cell viewWithTag:6] setAlpha:0];
        if([notification.notId isEqualToString:[self.seen objectAtIndex:i]]){
            NSLog(notification.title);
            NSLog(nId);
            [[cell viewWithTag:6] setAlpha:0.6];
            break;
        }
    }
    
    
    return cell;
}

/*
 // Override to support conditional editing of the table view.
 - (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the specified item to be editable.
 return YES;
 }
 */

/*
 // Override to support editing the table view.
 - (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
 {
 if (editingStyle == UITableViewCellEditingStyleDelete) {
 // Delete the row from the data source
 [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
 }   
 else if (editingStyle == UITableViewCellEditingStyleInsert) {
 // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
 }   
 }
 */

/*
 // Override to support rearranging the table view.
 - (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
 {
 }
 */

/*
 // Override to support conditional rearranging of the table view.
 - (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the item to be re-orderable.
 return YES;
 }
 */

#pragma mark - Table view delegate

/* 
 * This function is executed every time there is a click on a cell.
 * It gets the noticationType of the selected notification and makes a connection to the corresponding detailViewController.
 */
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Gets the notification of the selected cell.
    Notification *localnotification = [[self getAllRelevantNotifications] objectAtIndex:indexPath.row];
    
    NSString *notId = localnotification.notId;
    [self writeToTextFile:notId];
    [[[tableView cellForRowAtIndexPath:indexPath] viewWithTag:6] setAlpha:0.6];
    
    // Initializes the correct detailViewController to go to, depending on the notificationType of the
    // notification in the selected cell. 
    if (localnotification.type == Twitter) {
        TwitterDetailtViewController *detailViewController = [[TwitterDetailtViewController alloc] initWithNibName:@"TwitterDetailViewController" bundle:nil notification:localnotification ];
        [self.navigationController pushViewController:detailViewController animated:YES];
    } else  if (localnotification.type == File) {
        FileDetailViewController *detailViewController = [[FileDetailViewController alloc]initWithNibName:@"FileDetailViewController" bundle:nil notification:localnotification];
        [self.navigationController pushViewController:detailViewController animated:YES];
    }else  if (localnotification.type == Schedule) {
        ScheduleDetailViewController *detailViewController = [[ScheduleDetailViewController alloc]initWithNibName:@"ScheduleDetailViewController" bundle:nil notification:localnotification];
        [self.navigationController pushViewController:detailViewController animated:YES];
    }else  if (localnotification.type == RSS) {
        BlogDetailViewController *detailViewController = [[BlogDetailViewController alloc]initWithNibName:@"BlogDetailViewController" bundle:nil notification:localnotification];
        [self.navigationController pushViewController:detailViewController animated:YES];
    }else  if (localnotification.type == Comment) {
        CommentDetailViewController *detailViewController = [[CommentDetailViewController alloc]initWithNibName:@"CommentDetailViewController" bundle:nil notification:localnotification];
        [self.navigationController pushViewController:detailViewController animated:YES];
    }
}

/*
 Checks whether the boolean that corresponds to the given NotificationType is set to true
 */
-(BOOL)isNotificationTypeSelected:(NotificationType)notificationType {
    if (notificationType == Twitter && self.twitterSelected == YES) {
        return YES;
    } else if (notificationType == Schedule && self.scheduleSelected == YES) {
        return YES;
    } else     if (notificationType == Comment && self.commentSelected == YES) {
        return YES;
    } else    if (notificationType == File && self.fileSelected == YES) {
        return YES;
    } else    if (notificationType == RSS && self.rssSelected == YES) {
        return YES;
    } else {
        return NO;
    }
}

/*
 * Returns an array of all notifications that correspond to the selected notifcationTypes.
 */
-(NSMutableArray *)getAllRelevantNotifications {
    //??? array langer maken.
    NSMutableArray *localarray = [[NSMutableArray alloc]initWithCapacity:20];
    for (int i=0; i< [self.notifications count]; i++) {
        Notification *notif = [self.notifications objectAtIndex:i];
        if ([self isNotificationTypeSelected:notif.type]) {
            [localarray addObject:[self.notifications objectAtIndex:i]];
        }
    }
    return localarray;
}

/*
 * Inverts the boolean that corresponds to the buton that was clicked.
 */
-(void)invertSelected:(id)sender {
    if (sender == twitterButton) {
        twitterSelected = !twitterSelected;
    } else if (sender == scheduleButton) {
        scheduleSelected = !scheduleSelected;
    } else if (sender == commentsButton) {
        commentSelected = !commentSelected;
    } else if (sender == filesButton) {
        fileSelected = !fileSelected;
    } else if (sender == rssButton) {
        rssSelected = !rssSelected;
    } else {
        NSLog(@"incorrect sender - this is not one of the five buttons");
    }
}

/*
 * This function is executed every time one of the buttons is clicked. It makes sure that the
 * corresponding boolean is adjutsted, the table is updated correspondingly and the button
 * images are also updated.
 */
-(IBAction)clickButton:(id)sender{
    [self invertSelected:sender];
    [self.tableView reloadData];
    [self updateButtons];
}

@end