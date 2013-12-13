//
//  ViewController.h
//  AppNetClient
//
//  Created by Rueben Anderson on 11/1/13.
//  Copyright (c) 2013 Rueben Anderson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import "CustomCell.h"
#import "DateFormatter.h"
#import "UserAlerts.h"

@interface ViewController : UIViewController <UIScrollViewDelegate, UITableViewDataSource, UITableViewDelegate, NSURLConnectionDelegate, UIAlertViewDelegate>
{
    IBOutlet UITableView *postTable;
    IBOutlet UILabel *pullToRefreshLabel;
    NSMutableData *timelineData;
    NSDictionary *timelineJSON;
    NSURLConnection *connectionManager;
    NSMutableDictionary *userImages;
    NSMutableDictionary *imageUrls;
    NSMutableArray *timelineDetails;
    int currentImage;
    int connectionType;
    NSMutableData *avatarData;
    NSString *currentPoster;
    BOOL doRefresh;
    int textHeight;
    UserAlerts *alert;
    UIAlertView *loadMsg;
    UIStoryboard *storyBoard;
    
}

- (void)makeRequest:(NSString *)urlString;
- (void)extractJSONCellData:(NSArray *)posts;
- (NSDate *)getDate:(NSString *)dateString;
- (NSString *)getDateString:(NSDate *)date format:(NSString *)format;
- (void)sortJSON;

@end
