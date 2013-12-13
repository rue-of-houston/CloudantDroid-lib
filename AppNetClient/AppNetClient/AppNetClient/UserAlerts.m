//
//  UserAlerts.m
//  BabbleOn
//
//  Created by Rueben Anderson on 4/18/13.
//  Copyright (c) 2013 Rueben Anderson. All rights reserved.
//

#import "UserAlerts.h"

@implementation UserAlerts
@synthesize makeShowAlert, makeShowActivity;

- (id)init
{
    self = [super init];
    
    if (self)
    {
        // block takes six parameters - four NSString objects, one BOOL and an id. The NSString objects are used to create
        // the UIAlertView text data (title, message, cancel text, accept text). The BOOL checks if the alert should be shown
        // from within the block (YES) or not (NO). The block also returns the UIAlertView object that has been instantiated. The id is passed in as the owner of the delegate or nil. To remove button use pass in nil. The alertID will be assigned to the tag value and is for tracking the alertview for identifying purposes such as for button clicks.
        makeShowAlert = ^(NSString *title, NSString *message, NSString *cancelBtn, NSString *confirmBtn, BOOL doShow, UIViewController *controller, int alertID)
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:message delegate:controller cancelButtonTitle:cancelBtn otherButtonTitles:confirmBtn, nil];
            
            // set the alert tag to the passed in alertID
            alert.tag = alertID;
            
            // verify that the alert is made properly before showing
            if (alert && doShow)
            {
                // show the alert
                [alert show];
            }
            
            return alert;
        };
        
        // block that takes 4 parameters - 2 NSStrings. The NSString objects are used to create the text that will be displayed in the alert title and body. The BOOL is used to determine if the alert should be shown from within the class. The UIViewController object is the passed in reference to the view that should be the owner of the delegate
        makeShowActivity = ^(NSString *title, NSString *message, BOOL doShow, UIViewController *controller)
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title message:message delegate:controller cancelButtonTitle:nil otherButtonTitles:nil, nil];
            
            UIActivityIndicatorView *indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
            
            if (indicator)
            {
                float width = 140.0f;
                float height = 95.0f;
                
                // set the indicator center
                indicator.center = CGPointMake(width, height);
                
                // begin animation
                [indicator startAnimating];
                
                // add the indicator to the alert
                [alert addSubview:indicator];
            }
            
            if (alert && doShow)
            {
                // show the alert
                [alert show];
            }
            
            return alert;
        };
    }
    return self;
}

@end
