//
//  ViewController.m
//  AppNetClient
//
//  Created by Rueben Anderson on 11/1/13.
//  Copyright (c) 2013 Rueben Anderson. All rights reserved.
//

#import "ViewController.h"
#import "SecondViewController.h"

@interface ViewController ()

@end

typedef enum {
    LOADING_TIMELINE = 0,
    LOADING_IMAGES
} connectionTypes;

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
    imageUrls = [[NSMutableDictionary alloc] init];
    userImages = [[NSMutableDictionary alloc] init];
    storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    
    connectionType = LOADING_TIMELINE;
    
    alert = [[UserAlerts alloc] init];
    
    // retrieve the timelined data
   [self makeRequest:@"https://alpha-api.app.net/stream/0/posts/stream/global"];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // return a cell for each of the objects held in the array
    int timeLineCount = 0;
    
    if (timelineDetails != nil)
    {
        timeLineCount = (int) [timelineDetails count];
    }
    
    return timeLineCount;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // initialize the details view from the storyboard
    SecondViewController *detailsView = [storyBoard instantiateViewControllerWithIdentifier:@"secondView"];
    
    if (detailsView != nil)
    {
        // gets the userdata for the clicked table cell
        NSDictionary *userData = [timelineDetails objectAtIndex:indexPath.row];
        NSString *username = [userData objectForKey:@"poster_username"];
        NSString *postDate = [userData objectForKey:@"post_date"];
        NSString *postText = [userData objectForKey:@"post_text"];
        
        // set the data to the initialized view
        detailsView.user = username;
        detailsView.date = postDate;
        detailsView.post = postText;
        detailsView.image = [userImages objectForKey:username];
        
        
        [self presentViewController:detailsView animated:YES completion:^{
            
        }];
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    // set the cell identifier
    NSString *cellID = @"cell";
    
    // try to reuse a previously created tableview cell
    CustomCell *cell = [postTable dequeueReusableCellWithIdentifier:cellID];
    
    // no cell returned
    if (cell == nil)
    {
        // load the custom cell nib file
        NSArray *bundle = [[NSBundle mainBundle] loadNibNamed:@"CustomCell" owner:self options:nil];
        
        // verify the array is valid
        if (bundle != nil)
        {
            // create tableview cell from the nib
            cell = (CustomCell *) [bundle objectAtIndex:0];
        }
    }
    
    NSString *username = [[timelineDetails objectAtIndex:indexPath.row] objectForKey:@"poster_username"];
    NSString *post = [[timelineDetails objectAtIndex:indexPath.row] objectForKey:@"post_text"];
    NSString *date = [[timelineDetails objectAtIndex:indexPath.row] objectForKey:@"post_date"];
    
    // set the cell data
    cell.username.text = username;
    cell.postText.text = post;
    [cell.postText sizeToFit];
    cell.date.text = date;
    cell.cellImage.image = [userImages objectForKey:username];
    
    // round the corners of the images
    CALayer *layer = [cell.cellImage layer];
    [layer setMasksToBounds:YES];
    [layer setCornerRadius:10.0f];
    [layer setBorderColor:[[UIColor lightGrayColor] CGColor]];
    [layer setBorderWidth:1.25f];

    // return the table cell
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    int cellHeight = 80;
    
    return cellHeight;
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate
{
    
    // grab the scroll offset
    float offset = scrollView.contentOffset.y;
    
    // if the scroll is decelerating and the pull 90pts from top, do a refresh
    if (decelerate && offset <= -90.0f)
    {
        connectionType = LOADING_TIMELINE;
        
        // retrieve the timelined data
        [self makeRequest:@"https://alpha-api.app.net/stream/0/posts/stream/global"];
    }
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    // grab the scroll offset
    float offset = scrollView.contentOffset.y;
    
    NSString *releaseText = @"RELEASE TO REFRESH";
    NSString *pullText = @"PULL TO REFRESH";
    
    // if the scroll is decelerating and the pull 90pts from top, do a refresh
    if (offset <= -90.0f && [pullToRefreshLabel.text isEqualToString:pullText])
    {
        pullToRefreshLabel.text = releaseText;
    }
    else if (offset > -90.0f && [pullToRefreshLabel.text isEqualToString:releaseText])
    {
        pullToRefreshLabel.text = pullText;
    }
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    if (connectionType == LOADING_TIMELINE)
    {
        if (timelineData == nil)
        {
            // set the timeline data to equal the first bit of data received
            timelineData = (NSMutableData *) [data mutableCopy];
        }
        else
        {
            // append the received data
            [timelineData appendData:data];
        }
    }
    else if (connectionType == LOADING_IMAGES)
    {
        if (avatarData == nil)
        {
            // set the avatar image to equal the first bit of data
            avatarData = (NSMutableData *) [data mutableCopy];
        }
        else
        {
            // append the received data
            [avatarData appendData:data];
        }
    }
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    // nullify the connection object
    connectionManager = nil;

    if (connectionType == LOADING_TIMELINE)
    {
        NSError *err;
        
        // create json object from the returned data
        timelineJSON = [NSJSONSerialization JSONObjectWithData:timelineData options:NSJSONReadingMutableLeaves error:&err];
        
        // reset the timeline data
        timelineData = nil;
        
        // set the connection loading type to allow for dynamic loading of user avatars
        connectionType = LOADING_IMAGES;
        
        [self sortJSON];
        
    }
    else if (connectionType == LOADING_IMAGES)
    {
        UIImage *avatar = [UIImage imageWithData:avatarData];

        if (avatar != nil)
        {
            // add the avatar image to dictionary of poster images
            [userImages setObject:avatar forKey:currentPoster];
            
            // reset the data
            avatarData = nil;
        }
        
        if ([userImages count] < [imageUrls count])
        {
            // create the url from the url stored in the dictionary at the current image index in the details array
            NSString *url = [[timelineDetails objectAtIndex:currentImage] objectForKey:@"poster_avatar_url"];
            
            // set the currentPoster string for image caching
            currentPoster = [[timelineDetails objectAtIndex:currentImage] objectForKey:@"poster_username"];
            
            // verify that the url is valid
            if (url != nil)
            {
                // initiate the request for downloading user avatar
                [self makeRequest:url];
            }
            
            // increment the current image index
            currentImage++;
        }
        else
        {
            // dismiss the loading message
            if (loadMsg != nil)
            {
                [loadMsg dismissWithClickedButtonIndex:0 animated:YES];
                loadMsg = nil;
            }
            
            if (pullToRefreshLabel.hidden)
            {
                pullToRefreshLabel.hidden = NO;
            }
            
            // reload the table
            [postTable reloadData];
            
            currentImage = 0;
        }
        
    }
    
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
    // dismiss the loading message
    if (loadMsg != nil)
    {
        [loadMsg dismissWithClickedButtonIndex:0 animated:YES];
        loadMsg = alert.makeShowAlert(@"Connection Error", @"Error fetching timeline. Please try again.", @"Cancel", @"Retry", YES, self, 99);
        
        NSLog(@"Error: %@", error);
    }
}

- (void)makeRequest:(NSString *) urlString
{
    // display the loading message
    if (loadMsg == nil)
    {
        loadMsg = alert.makeShowActivity(@"Please Wait", @"Loading your timeline...", NO, self);
        
        if (loadMsg != nil)
        {
            [loadMsg show];
        }
    }
    
    // create the url object pointing to the timeline data
    NSURL *url = [NSURL URLWithString: urlString];
    
    if (connectionManager)
    {
        // stop any active requests
        [connectionManager cancel];
    }
    
    // verify that the url is valid
    if (url != nil)
    {
        // create a request object from the url
        NSURLRequest *request = [[NSURLRequest alloc] initWithURL:url];
        
        // verify that the request is valid
        if (request != nil)
        {
            // create a new connection from the request object
            connectionManager = [[NSURLConnection alloc] initWithRequest:request delegate:self];
            
            // verify that the connection object is valid
            if (connectionManager != nil)
            {
                // start the connection
                [connectionManager start];
            }
        }
    }
    
}

- (void)sortJSON
{
    // retrieve the data array from the returned json object
    NSArray *rawPosts = [timelineJSON objectForKey:@"data"];

    NSMutableArray *postTracker = [[NSMutableArray alloc] init];
    NSMutableArray *dates = [[NSMutableArray alloc] init];
    
    // iterate over the rawposts to retrieve the created at dates
    for (int i = 0; i < [rawPosts count]; i++)
    {
        // create a dictionary of the individual post data at the current index of the rawposts array
        NSDictionary *postData = [rawPosts objectAtIndex:i];
        
        // get the current posts created at string date
        NSString *postDate = [[NSString alloc] initWithString:[postData objectForKey:@"created_at"]];
        
        //  set the date to the index in the date array
        dates[i] = [self getDate:postDate];
        
        // set the index to the index tracking array purposely matching the order of the dates for
        // date matching later on
        postTracker[i] = [NSNumber numberWithInt:i];
    }
    
    // sort the post dates in ascending order
    NSArray *sorts = [dates sortedArrayUsingSelector:@selector(compare:)];
    
    // create a mutable array for holding the sorted json post data
    NSMutableArray *posts = [[NSMutableArray alloc] init];
    
    // iterate over each date in the sorted dates array to compare dates
    for (NSDate *date in sorts)
    {
        // loop through the sorted dates array for comparing
        for (int i = 0; i < [sorts count]; i++)
        {
            // grab the date at the i-th index of the dates array (unsorted but index tracked)
            NSDate *dateB = [dates objectAtIndex:i];
            
            // check if the dates have a match
            if ([date isEqualToDate:dateB])
            {
                // date match found add the post object at this index of the matched date to the post array
                [posts addObject:rawPosts[i]];
                
                // set the matched date to the current date to remove from matching
                dates[i] = [[NSDate alloc] init];
                
                // break out from the date matching
                break;
            }
        }
    }
    
    
    // call the method to retrieve the table data from the json returned
    [self extractJSONCellData: (NSArray *) posts];
}

- (void)extractJSONCellData:(NSArray *)posts
{
    
    // create an empy array for holding the extracted dictionaries
    timelineDetails = [[NSMutableArray alloc] init];
    
    // verify that the posts object is valid
    if (posts != nil)
    {
        // iterate over the posts array to extract the required information from each of its housed dictionaries
        for (int i = 0; i < [posts count]; i++)
        {
            // create a container dictionary object for holding the extracted post data
            NSMutableDictionary *postDetails = [[NSMutableDictionary alloc] init];
            
            // create an nsdicitonary object for each object in the returned array
            NSDictionary *postData = [posts objectAtIndex:i];
            
            // extract the required data and set them to strings
            NSString *post = [postData objectForKey:@"text"];
            NSString *username = [[postData objectForKey:@"user"] objectForKey:@"username"];
            NSString *name = [[postData objectForKey:@"user"] objectForKey:@"name"];
            NSString *details = [[[postData objectForKey:@"user"] objectForKey:@"description"] objectForKey:@"text"];
            NSString *postCreation = [postData objectForKey:@"created_at"];
            NSString *avatarUrl = [[[postData objectForKey:@"user"] objectForKey:@"avatar_image"] objectForKey:@"url"];
            
            // set blank text where text returns null
            if (details == nil)
            {
                details = @"";
            }
            
            NSDate *date = [self getDate:postCreation];
            postCreation = [self getDateString:date format:@"EEE, MMM d, yyyy hh:mm:ss"];
            
            // add the extracted json data to the dictionary
            [postDetails setObject:post forKey:@"post_text"];
            [postDetails setObject:username forKey:@"poster_username"];
            [postDetails setObject:name forKey:@"poster_name"];
            [postDetails setObject:details forKey:@"poster_details"];
            [postDetails setObject:postCreation forKey:@"post_date"];
            [postDetails setObject:avatarUrl forKey:@"poster_avatar_url"];
            
            if (imageUrls != nil)
            {
                // add the url to the global url dictionary object
                [imageUrls setObject:avatarUrl forKey:username];
            }
            
            if (timelineDetails != nil)
            {
                // add the posting details dictionary to the timeline array
                [timelineDetails addObject:postDetails];
            }
        }
    }
    
   if (userImages != nil && [userImages count] < [imageUrls count])
   {
       // create the url from the url stored in the dictionary at the current image index in the details array
       NSString *url = [[timelineDetails objectAtIndex:currentImage] objectForKey:@"poster_avatar_url"];
       
       // set the currentPoster string for image caching
       currentPoster = [[timelineDetails objectAtIndex:currentImage] objectForKey:@"poster_username"];
       
       // increment the current image index
       currentImage++;
       
       // verify that the url is valid
       if (url != nil)
       {
           // initiate the request for downloading user avatar
           [self makeRequest:url];
       }
   }
   else
   {
       // dismiss the loading message
       if (loadMsg != nil)
       {
           [loadMsg dismissWithClickedButtonIndex:0 animated:YES];
           loadMsg = nil;
       }
   }
}

- (NSDate *)getDate:(NSString *)dateString
{
    // create the date formatting object
    DateFormatter *formatter = [[DateFormatter alloc] init];

    // the date format has to be modified to properly be created into an NSDate object
    // by removing the T & Z string formatting handles from the date string
    NSArray *splitDate = [dateString componentsSeparatedByString:@"T"];
    NSString *dateTime = [splitDate[1] componentsSeparatedByString:@"Z"][0];
    
    // recreate the datestring from the two modified strings
    NSString *newDateString = [[NSString alloc] initWithFormat:@"%@ %@", splitDate[0], dateTime];
    NSString *format = [[NSString alloc] initWithFormat:@"YYYY-MM-dd HH:mm:ss"];
    
    // create a proper NSDate object from the date string with specified format
    NSDate *date = formatter.formatDate(newDateString, format);
    
    return date;
}
                            
- (NSString *)getDateString:(NSDate *)date format:(NSString *)format
{
    // create the date formatting object
    DateFormatter *formatter = [[DateFormatter alloc] init];
    
    return formatter.formatDateString(date, format);
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    // verify that the alert is the retry alert
    if (alertView.tag == 99)
    {
        // check if the button clicked is the "retry" button
        if (buttonIndex == 1)
        {
            // reset the connection type
            connectionType = LOADING_TIMELINE;
            
            // retrieve the timelined data
            [self makeRequest:@"https://alpha-api.app.net/stream/0/posts/stream/global"];
        }
    }
}

@end
