//
//  TwitterDetailtViewController.h
//  STREAMY 3
//
//  Created by student on 15/11/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Notification.h"

@interface TwitterDetailtViewController : UIViewController

@property Notification *notification;
@property IBOutlet UITextView *messageTxt;
@property IBOutlet UITextView *linkTxt;

-(id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil notification:(Notification *)notification;

@end
