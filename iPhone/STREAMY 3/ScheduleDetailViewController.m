//
//  ScheduleDetailViewController.m
//  STREAMY 3
//
//  Created by student on 19/12/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "ScheduleDetailViewController.h"

@implementation ScheduleDetailViewController

@synthesize notification;
@synthesize messageTxt;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil notification:
(Notification *)inputNotification {
    //[self initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    [self setNotification:inputNotification];
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self)
    {
        // this will appear as the title in the navigation bar
        UILabel *label = [[[UILabel alloc] initWithFrame:CGRectZero] autorelease];
        label.backgroundColor = [UIColor clearColor];
        label.font = [UIFont boldSystemFontOfSize:20.0];
        label.bounds = CGRectMake(150, 35, 320, 44);
        label.shadowColor = [UIColor whiteColor];
        label.textAlignment = UITextAlignmentCenter;
        label.textColor = [UIColor colorWithRed:45/255 green:45/255 blue:45/255 alpha:1];
        self.navigationItem.titleView = label;
        label.text = notification.title;
        //[label sizeToFit];
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

/*
// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView
{
}
*/

//*
// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
    
    //Fill in the values for the labels on this screen
    //titleLabel.text = notification.title;
    //messageLabel.text = notification.message;
    messageTxt.text = notification.message;
    
    UIToolbar *tb = [[self navigationController] toolbar];
    [tb setHidden:true];
    //[tb removeFromSuperview];
    
}
//*/

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    UINavigationBar *nb = [[self navigationController] navigationBar];
    [nb insertSubview:[[[UIImageView alloc] initWithImage:[UIImage imageNamed:@"detail_header_schedule_small.png"]] autorelease] atIndex:1];
    UIImageView *imgview = [[nb subviews]objectAtIndex:1];
    [imgview setContentMode:UIViewContentModeScaleAspectFill];
    
}

// Auto-generated function
- (void)viewWillDisappear:(BOOL)animated
{ 
    [super viewWillDisappear:animated];
    UINavigationBar *nb = [[self navigationController] navigationBar];
    UIImageView *imgview = [[nb subviews]objectAtIndex:1];
    [imgview removeFromSuperview];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
