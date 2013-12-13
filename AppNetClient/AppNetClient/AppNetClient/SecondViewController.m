//
//  SecondViewController.m
//  AppNetClient
//
//  Created by Rueben Anderson on 11/18/13.
//  Copyright (c) 2013 Rueben Anderson. All rights reserved.
//

#import "SecondViewController.h"

@interface SecondViewController ()

@end

@implementation SecondViewController
@synthesize image, date, post, user;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	
    // set the passed in values
    userImage.image = self.image;
    username.text = self.user;
    postDate.text = self.date;
    postText.text = self.post;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onClick:(id)sender
{
    UIButton *btn = (UIButton *) sender;
    
    if (btn != nil)
    {
        [self dismissViewControllerAnimated:YES completion:^{
            
        }];
    }
}

@end
