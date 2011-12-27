//
//  JSONParser.m
//  STREAMY 3
//
//  Created by student on 09/12/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "JSONParser.h"
#import "Notification.h"

@implementation JSONParser

+(Notification*)getNotification:(NSDictionary*)notiJson{
    Notification *noti = [[Notification alloc] init];
    //noti.id = [notiJson objectForKey:@"gui"];
    
    NSString *type = [notiJson objectForKey:@"type"];
    
    /*UInt32 *myId = [notiJson objectForKey:@"id"];
    
    noti.notId = [NSString stringWithFormat:@"%d", [notiJson objectForKey:@"id"]];*/
        
    NotificationType result;
    //06 Dec 2011 22:51:09 +0000
    NSDateFormatter *df = [NSDateFormatter new];
    [df setDateFormat:@"dd MMM yyyy HH:mm:ss Z"];
    
    
    if ( [type isEqualToString:(@"Tweet")]) {
        result = Twitter;
        NSString *author = [notiJson objectForKey:@"author"];
        NSString *set = [notiJson objectForKey:@"set"];
        noti.title = [[author stringByAppendingString: @" in "] stringByAppendingString:set];
        noti.date = [df dateFromString:[notiJson objectForKey:@"datestamp"]];
        noti.link = [notiJson objectForKey:@"link"];
    }else
    if([type isEqualToString:(@"Blog")]) {
        result = RSS;
        NSString *author = [notiJson objectForKey:@"author"];
        NSString *set = [notiJson objectForKey:@"set"];
        noti.title = [[author stringByAppendingString: @" in "] stringByAppendingString:set];
        noti.date = [df dateFromString:[notiJson objectForKey:@"datestamp"]];
        noti.link = [notiJson objectForKey:@"link"];
    }else
    if([type isEqualToString:(@"Comment")]) {
        result = Comment;
        NSString *author = [notiJson objectForKey:@"author"];
        NSString *set = [notiJson objectForKey:@"set"];
        noti.title = [[author stringByAppendingString: @" in "] stringByAppendingString:set];
        noti.date = [df dateFromString:[notiJson objectForKey:@"datestamp"]];
        noti.link = [notiJson objectForKey:@"link"];
    }else
    if([type isEqualToString:(@"Schedule")]) {
        result = Schedule;
        noti.title = [notiJson objectForKey:@"author"];
        noti.author = [notiJson objectForKey:@"author"];
        noti.date = [df dateFromString:[notiJson objectForKey:@"datestamp"]];
    }else
    if([type isEqualToString:(@"File")]) {
        result = File;
        noti.title = [notiJson objectForKey:@"author"];
        noti.author = [notiJson objectForKey:@"author"];
        NSString *breadcrumb = [notiJson objectForKey:@"breadcrumb"];
        noti.breadcrumb = breadcrumb;
        noti.date = [df dateFromString:[notiJson objectForKey:@"datestamp"]];

    }
    noti.type = result;
    noti.message = [notiJson objectForKey:@"text"];
    
    NSDateFormatter *df2 = [NSDateFormatter new];
    [df2 setDateFormat:@"ddMMyyyyHHmmss"];
    NSString *myDateId = [df2 stringFromDate:noti.date];
    //myDateId = [myDateId stringByAppendingString: noti.author];
    
    noti.notId = myDateId;
    
    return noti;
}

@end
