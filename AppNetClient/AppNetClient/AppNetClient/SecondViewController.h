//
//  SecondViewController.h
//  AppNetClient
//
//  Created by Rueben Anderson on 11/18/13.
//  Copyright (c) 2013 Rueben Anderson. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SecondViewController : UIViewController
{
    UIImage *image;
    NSString *user;
    NSString *date;
    NSString *post;
    
    IBOutlet UIButton *doneBtn;
    IBOutlet UIImageView *userImage;
    IBOutlet UILabel *username;
    IBOutlet UILabel *postDate;
    IBOutlet UITextView *postText;

    
}

@property (nonatomic, strong) UIImage *image;
@property (nonatomic, strong) NSString *user;
@property (nonatomic, strong) NSString *post;
@property (nonatomic, strong) NSString *date;

-(IBAction)onClick:(id)sender;

@end
