//
//  NotificationViewController.h
//  STREAMY 3
//
//  Created by student on 25/10/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "StartScreenViewController.h"
#import "Notification.h"
#import "Communicator.h"

@interface NotificationViewController : UITableViewController

/*
 * An array containing all notifications (not only the ones that are selected)
 */
@property (nonatomic, retain) NSMutableArray *notifications;

/*
 * An array containing all seen notifications.
 */
@property (nonatomic, retain) NSArray *seen;

/*
 * Six booleans to represent whether each notoficationType is selected.
 */
@property (nonatomic) BOOL twitterSelected;
@property (nonatomic) BOOL scheduleSelected;
@property (nonatomic) BOOL commentSelected;
@property (nonatomic) BOOL fileSelected;
@property (nonatomic) BOOL rssSelected;


-(void)updateButtons;

-(BOOL)isNotificationTypeSelected:(NotificationType)notificationType;

-(NSMutableArray *)getAllRelevantNotifications;

-(void)invertSelected:(id)sender;

-(IBAction)clickButton:(id)sender;

-(void) writeToTextFile:(NSString*)notId;
-(NSString*) displayContent;

@end