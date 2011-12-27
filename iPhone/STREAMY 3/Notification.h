//
//  Notification.h
//  STREAMY 3
//
//  Created by student on 24/10/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Communicator.h"

typedef enum {Twitter, RSS, Comment, Schedule, File} NotificationType;

@interface Notification : NSObject

@property (nonatomic, retain) NSString *notId;
@property (nonatomic, retain) Communicator *notificationSender;
@property (nonatomic, retain) Communicator *notificationReciever;
@property NotificationType type;
@property (nonatomic, retain) NSDate *date;
@property (nonatomic, retain) NSString *title;
@property (nonatomic, retain) NSString *message;
@property (nonatomic, retain) NSString *breadcrumb;
@property (nonatomic, retain) NSString *author;
@property (nonatomic, retain) NSString *course;
@property (nonatomic, retain) NSString *link;

+(NSString*) typeToString:(NotificationType) notType;
+(NotificationType*) getNotificationByString:(NSString*) noti;

@end
