//
//  SCAppUtils.m
//  STREAMY 3
//
//  Created by student on 05/12/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "SCAppUtils.h"

@implementation SCAppUtils

+ (void)customizeNavigationController:(UINavigationController *)navController
{
    UINavigationBar *navBar = [navController navigationBar];
    [navBar setTintColor:kSCNavBarColor];
    
    if ([navBar respondsToSelector:@selector(setBackgroundImage:forBarMetrics:)])
    {
        [navBar setBackgroundImage:[UIImage imageNamed:@"navigation-bar-bg.png"] forBarMetrics:UIBarMetricsDefault];
    }
    else
    {
        UIImageView *imageView = (UIImageView *)[navBar viewWithTag:kSCNavBarImageTag];
        if (imageView == nil)
        {
            imageView = [[UIImageView alloc] initWithImage:
                         [UIImage imageNamed:@"navigation-bar-bg.png"]];
            [imageView setTag:kSCNavBarImageTag];
            [navBar insertSubview:imageView atIndex:0];
            [imageView release];
        }
    }
}

@end
