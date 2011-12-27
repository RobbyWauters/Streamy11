//
//  Notification.m
//  STREAMY 3
//
//  Created by student on 24/10/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "Notification.h"

@implementation Notification

@synthesize notificationSender;
@synthesize notificationReciever;
@synthesize notId;
@synthesize type;
@synthesize date;
@synthesize title;
@synthesize message;
@synthesize breadcrumb;
@synthesize author;
@synthesize course;
@synthesize link;

- (NSComparisonResult)compare:(Notification *)otherObject {
    return [otherObject.date compare:self.date];
}

+(NSString*) typeToString:(NotificationType) notType {
    NSString *result = nil;
    
    switch(notType) {
        case Twitter:
            result = @"Twitter";
            break;
        case RSS:
            result = @"RSS";
            break;
        case Comment:
            result = @"Comment";
            break;
        case Schedule:
            result = @"Schedule";
            break;   
        case File:
            result = @"File";
            break;  
    }
    
    return result;
}

+(NotificationType*) getNotificationByString:(NSString*) noti{
    
    if ( [noti isEqualToString:(@"Tweet")]) {
        return Twitter;
    }else if([noti isEqualToString:(@"Blog")]) {
        return RSS;
    }else if([noti isEqualToString:(@"Comment")]) {
        return Comment;
    }else if([noti isEqualToString:(@"Schedule")]) {
        return Schedule;
    }else if([noti isEqualToString:(@"File")]) {
         return File;
    }else{
        return 0;
    }
}

@end
