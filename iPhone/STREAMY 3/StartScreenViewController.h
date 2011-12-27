//
//  StartScreenViewController.h
//  STREAMY 3
//
//  Created by student on 26/10/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NotificationViewController.h"


@interface StartScreenViewController : UIViewController {
    float progress;
}

/*
 A float with values between 0.00 and 1.00, representing a percentage that indicates how good you are up-to-date.
 */
@property float progress;

/*
 * Array with slogans for the Streamy Meter
 */
@property NSArray *slogans;

/*
 * An array containing all notifications (not only the ones that are selected)
 */
@property (nonatomic, retain) NSMutableArray *notifications;

-(void)updateProgressMeter:(float)progress;
-(BOOL)isValidProgress:(float)localprogress;

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender;
-(void)fetchedData:(NSData *)responseData;

-(NSString*) displayContent;

@end