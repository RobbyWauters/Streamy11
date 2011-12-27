//
//  FileDetailViewController.h
//  STREAMY 3
//
//  Created by student on 15/11/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Notification.h"

@interface FileDetailViewController : UIViewController

@property(retain) Notification *notification;
@property(assign) IBOutlet UITextView *messageTxt;
@property(assign) IBOutlet UILabel *bread1Label;
@property(assign) IBOutlet UILabel *bread2Label;
@property(assign) IBOutlet UILabel *bread3Label;

-(id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil notification:(Notification *)notification;

@end
