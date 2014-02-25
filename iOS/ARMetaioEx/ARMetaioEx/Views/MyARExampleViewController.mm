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
    
    m_gestures = 0xFF; //enables all gestures
    m_gestureHandler = [[GestureHandlerIOS alloc] initWithSDK:m_metaioSDK withView:glView withGestures:m_gestures];
    
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
            theLoadedModel->setTranslation(metaio::Vector3d(-100,-200,0));
            [m_gestureHandler addObject:theLoadedModel andGroup:1];
        }
        else
        {
            NSLog(@"error, could not load %@", bumblebee);
        }
    }
}

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // record the initial states of the geometries with the gesture handler
    [m_gestureHandler touchesBegan:touches withEvent:event withView:glView];
}

- (void) touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event
{
    // handles the drag touch
    [m_gestureHandler touchesMoved:touches withEvent:event withView:glView];
}

- (void) touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    [m_gestureHandler touchesEnded:touches withEvent:event withView:glView];
}

@end
