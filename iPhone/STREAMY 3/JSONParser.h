//
//  JSONParser.h
//  STREAMY 3
//
//  Created by student on 09/12/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Notification.h"

@interface JSONParser : NSObject

+(Notification*)getNotification:(NSDictionary*)notiJson;

@end
