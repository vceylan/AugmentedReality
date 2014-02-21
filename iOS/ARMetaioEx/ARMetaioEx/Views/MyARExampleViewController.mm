//
//  MyARExampleViewController.m
//  ARMetaioEx
//
//  Created by ceylanv on 2/21/14.
//  Copyright (c) 2014 ceylanv. All rights reserved.
//

#import "MyARExampleViewController.h"
#import "EAGLView.h"

@interface MyARExampleViewController ()

@end

@implementation MyARExampleViewController

-(void)dealloc
{
    [super dealloc];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    if( !m_metaioSDK )
    {
        NSLog(@"SDK instance is 0x0. Please check the license string");
        return;
    }
    
    // load our tracking configuration
    NSString* trackingDataFile = [[NSBundle mainBundle] pathForResource:@"TrackingData_MarkerlessFast" ofType:@"xml" inDirectory:@"Assets"];
	if(trackingDataFile)
	{
		bool success = m_metaioSDK->setTrackingConfiguration([trackingDataFile UTF8String]);
		if( !success)
			NSLog(@"No success loading the tracking configuration");
	}
    
    // load content
    NSString* bumblebee = [[NSBundle mainBundle] pathForResource:@"VH-BumbleBee" ofType:@"obj" inDirectory:@"Assets"];
    
	if(bumblebee)
	{
		// if this call was successful, theLoadedModel will contain a pointer to the 3D model
        metaio::IGeometry* theLoadedModel =  m_metaioSDK->createGeometry([bumblebee UTF8String]);
        if( theLoadedModel )
        {
            // scale it a bit down
            theLoadedModel->setScale(metaio::Vector3d(0.8,0.8,0.8));
        }
        else
        {
            NSLog(@"error, could not load %@", bumblebee);
        }
    }
}

@end
