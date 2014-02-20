package com.cronom.mobile.armetaioex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.GestureHandlerAndroid;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.GestureHandler;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.IVisualSearchCallback;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

public class MyARActivity extends ARViewActivity {
	private IGeometry				model;
	private IGeometry				imageModel;
	private GestureHandlerAndroid	mGestureHandler;
	private int						mGestureMask;
	private IMetaioSDKCallback		sdkCallBack;
	private IVisualSearchCallback	visualSeacrchCallback;
	private LinearLayout			popup;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sdkCallBack = new IMetaioSDKCallback();
		visualSeacrchCallback = new IVisualSearchCallback();
		metaioSDK.registerVisualSearchCallback(visualSeacrchCallback);
		mGestureMask = GestureHandler.GESTURE_ALL;
		mGestureHandler = new GestureHandlerAndroid(metaioSDK, mGestureMask);
		popup = (LinearLayout) mGUIView.findViewById(R.id.llPopupDialog);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		super.onTouch(v, event);
		mGestureHandler.onTouch(v, event);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sdkCallBack.delete();
		sdkCallBack = null;
		visualSeacrchCallback.delete();
		visualSeacrchCallback = null;
	}

	@Override
	protected int getGUILayout() {
		return R.layout.activity_my_ar;
	}

	@Override
	protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
		return sdkCallBack;
	}

	@Override
	protected void loadContents() {
		try {
			// Getting a file path for tracking configuration XML file
			String trackingConfigFile = AssetsManager.getAssetPath("TrackingData_MarkerlessFast.xml");

			// Assigning tracking configuration
			boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile);
			MetaioDebug.log("Tracking data loaded: " + result);

			// Getting a file path for a 3D geometry
			String robotModel = AssetsManager.getAssetPath("RB-BumbleBee.obj");
			if (robotModel != null) {
				// Loading 3D geometry
				model = metaioSDK.createGeometry(robotModel);
				if (model != null) {
					// Set geometry properties
					Vector3d scale = new Vector3d(0.3f, 0.3f, 0.3f);
					Vector3d translation = new Vector3d(-300f, -200f, 0f);
					Rotation rotation = new Rotation(0f, 0f, -(float) Math.PI / 2f);

					model.setRotation(rotation);
					model.setScale(scale);
					model.setTranslation(translation);

					model.setName("RB-BumbleBee");

					mGestureHandler.addObject(model, 1);
				}
				else {
					MetaioDebug.log(Log.ERROR, "Error loading geometry: " + robotModel);
				}
			}

			String extra = AssetsManager.getAssetPath("metaioman_target.png");
			if (extra != null) {
				imageModel = metaioSDK.createGeometryFromImage(extra, false, true);
				if (imageModel != null) {
					Vector3d v = new Vector3d(0f, 0f, 0f);
					imageModel.setTranslation(v);
					mGestureHandler.addObject(imageModel, 2);
				}
				else {
					MetaioDebug.log(Log.ERROR, "Error loading geometry: " + imageModel);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onGeometryTouched(IGeometry geometry) {
		popup.setVisibility(View.VISIBLE);
	}

	public void onExitClicked(View v) {
		finish();
	}

	public void onPopupCloseClicked(View v) {
		((View) v.getParent()).setVisibility(View.GONE);
	}

	public void onPopupButtonsClicked(View v) {
		Intent i;
		switch (v.getId()) {
			case R.id.button1:
				i = new Intent(this, DetailActivity.class);
				startActivity(i);
				break;
			case R.id.button12:
				i = new Intent(this, GalleryActivity.class);
				startActivity(i);
				break;
			default:
				break;
		}
	}
}
