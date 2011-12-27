//
//  SCAppUtils.h
//  STREAMY 3
//
//  Created by student on 05/12/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

#define kSCNavBarImageTag 6183746
#define kSCNavBarColor [UIColor colorWithRed:0.54 green:0.18 blue:0.03 alpha:1.0]

@interface SCAppUtils : NSObject
{
}

+ (void)customizeNavigationController:(UINavigationController *)navController;

@end
