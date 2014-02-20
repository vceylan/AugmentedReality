package com.cronom.mobile.armetaioex;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.metaio.sdk.MetaioDebug;
import com.metaio.tools.io.AssetsManager;

public class ARMetaioExActivity extends Activity {
	private AssetsExtracter	assets;
	private ProgressDialog	progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_armetaio_ex);

		// Enable metaio SDK debug log messages based on build configuration
		MetaioDebug.enableLogging(BuildConfig.DEBUG);
		progress = new ProgressDialog(this);
		progress.setMessage("Loading assets...");
		assets = new AssetsExtracter();
		assets.execute(0);
	}

	public void onBeginArClicked(View v) {
		Intent intent = new Intent(getApplicationContext(), MyARActivity.class);
		startActivity(intent);
	}

	/**
	 * This task extracts all the assets to an external or internal location to make them accessible to metaio SDK
	 */
	private class AssetsExtracter extends AsyncTask<Integer, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			progress.show();
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			try {
				// Extract all assets and overwrite existing files if debug build
				AssetsManager.extractAllAssets(getApplicationContext(), BuildConfig.DEBUG);
			}
			catch (IOException e) {
				MetaioDebug.log(Log.ERROR, "Error extracting assets: " + e.getMessage());
				MetaioDebug.printStackTrace(Log.ERROR, e);
				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
		}

	}
}
