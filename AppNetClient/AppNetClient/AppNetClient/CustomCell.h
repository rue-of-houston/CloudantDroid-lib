//
//  Cell.h
//  AppNetClient
//
//  Created by Rueben Anderson on 11/2/13.
//  Copyright (c) 2013 Rueben Anderson. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CustomCell : UITableViewCell

@property (nonatomic, strong) IBOutlet UILabel *username;
@property (nonatomic, strong) IBOutlet UILabel *postText;
@property (nonatomic, strong) IBOutlet UIImageView *cellImage;
@property (nonatomic, strong) IBOutlet UILabel *date;

@end
