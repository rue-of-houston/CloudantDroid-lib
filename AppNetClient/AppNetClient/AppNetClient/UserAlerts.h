//
//  UserAlerts.h
//  BabbleOn
//
//  Created by Rueben Anderson on 4/18/13.
//  Copyright (c) 2013 Rueben Anderson. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface UserAlerts : NSObject
{
    // alertView block statement
    UIAlertView* (^makeShowAlert)(NSString*, NSString*, NSString*, NSString*, BOOL, id, int);
    UIAlertView* (^makeShowActivity)(NSString*, NSString*, BOOL, UIViewController*);
}

@property (nonatomic, strong) UIAlertView* (^makeShowAlert)(NSString*, NSString*, NSString*, NSString*, BOOL, id, int);
@property (nonatomic, strong) UIAlertView* (^makeShowActivity)(NSString*, NSString*, BOOL, UIViewController*);

@end
