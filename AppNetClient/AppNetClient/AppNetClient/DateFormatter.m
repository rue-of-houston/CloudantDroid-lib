//
//  DateFormatter.m
//  
//
//  Created by Rueben Anderson on 4/18/13.
//  Copyright (c) 2013 Rueben Anderson. All rights reserved.
//

#import "DateFormatter.h"

@implementation DateFormatter
@synthesize formatDate, formatDateString;

- (id)init
{
    self = [super init];
    
    if (self)
    {
        // block that takes two NSString objects. The thisdate parameter will be formatted
        // by NSDateFormatter to the format of the formatString parameter. The new date object will then be returned
        formatDate = ^(NSString *thisDate, NSString *formatString)
        {
            NSDate *newDate;
            
            if (thisDate != nil)
            {
                NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
                
                if (formatter)
                {
                    // set the date formatting attributes
                    [formatter setTimeStyle:NSDateFormatterLongStyle];
                    [formatter setDateStyle:NSDateFormatterMediumStyle];
                    [formatter setDateFormat:formatString];
                    
                    // the return string as a date object
                    newDate = [formatter dateFromString:thisDate];
                }
            }
            
            return newDate;
        };
        
        
        // block that takes two parameters - an NSDate object an an NSString object. The NSDate parameter will be formatted
        // by NSDateFormatter to the format of the formatString parameter. The new date object will then be returned
        formatDateString = ^(NSDate *date, NSString *formatString)
        {
            
            NSString *stringDate = [[NSString alloc] init];
            
            if (stringDate && formatString)
            {
                NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
                
                if (formatter)
                {
                    // set the date format
                    [formatter setDateFormat:formatString];
                    
                    // return the date as a string
                    stringDate = [formatter stringFromDate:date];
                }
            }
           
            return stringDate;
        };
        
    }
    
    return self;
}
@end
