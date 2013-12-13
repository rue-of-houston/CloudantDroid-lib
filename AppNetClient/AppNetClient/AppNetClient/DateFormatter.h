//
//  DateFormatter.h
//  
//
//  Created by Rueben Anderson on 4/18/13.
//  Copyright (c) 2013 Rueben Anderson. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DateFormatter : NSObject
{
    NSDate * (^formatDate)(NSString *, NSString *);
    NSString * (^formatDateString)(NSDate *, NSString *);
}

@property (nonatomic, strong) NSDate * (^formatDate)(NSString *, NSString *);
@property (nonatomic, strong) NSString * (^formatDateString)(NSDate *, NSString *);

@end
