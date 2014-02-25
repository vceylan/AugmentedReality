//
//  MyARExampleViewController.h
//  ARMetaioEx
//
//  Created by ceylanv on 2/21/14.
//  Copyright (c) 2014 ceylanv. All rights reserved.
//

#import "MetaioSDKViewController.h"
#import <metaioSDK/GestureHandlerIOS.h>

@interface MyARExampleViewController : MetaioSDKViewController
{
    // GestureHandler handles the dragging/pinch/rotation touches
    GestureHandlerIOS* m_gestureHandler;
    //gesture mask to specify the gestures that are enabled
    int m_gestures;
    //indicate if a camera image has been requested from the user
    bool m_imageTaken;
}
@end
