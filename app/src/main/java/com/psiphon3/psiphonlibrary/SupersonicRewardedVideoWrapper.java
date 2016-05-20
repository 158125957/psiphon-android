package com.psiphon3.psiphonlibrary;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.supersonic.mediationsdk.sdk.RewardedVideoListener;
import com.supersonic.mediationsdk.sdk.Supersonic;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class SupersonicRewardedVideoWrapper {
    private boolean mIsInitialized = false;
    private Supersonic mMediationAgent;
    private  String mPlacement;
    private WeakReference<Activity> mWeakActivity;

    private AsyncTask mGAIDRequestTask;

    //Set the Application Key - can be retrieved from Supersonic platform
    private final String mAppKey = "49a684d5";

    public SupersonicRewardedVideoWrapper(Activity activity, String placement) {
        mPlacement = placement;
        mWeakActivity = new WeakReference<Activity>(activity);
        mMediationAgent = SupersonicFactory.getInstance();
//        mMediationAgent.setRewardedVideoListener(SupersonicRewardedVideoWrapper.this);
        initialize();
    }

    public void initialize() {
        if(mIsInitialized) {
            return;
        }

        if (mGAIDRequestTask != null && !mGAIDRequestTask.isCancelled()) {
            mGAIDRequestTask.cancel(false);
        }
        mGAIDRequestTask = new UserIdRequestTask().execute();
    }

    public void setRewardedVideoListener(RewardedVideoListener listener) {
        mMediationAgent.setRewardedVideoListener(listener);
    }

    public void playVideo() {
        if(isRewardedVideoAvailable()) {
            mMediationAgent.showRewardedVideo();
        }
    }

    public boolean isRewardedVideoAvailable() {
        return (mMediationAgent != null && mMediationAgent.isRewardedVideoAvailable());
    }

    private final class UserIdRequestTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            Activity activity = SupersonicRewardedVideoWrapper.this.mWeakActivity.get();
            if (activity != null) {
                try {
                    String GAID = AdvertisingIdClient.getAdvertisingIdInfo(activity).getId();
                    return new String("unique_user_id");
                } catch (final IOException e) {

                } catch (final GooglePlayServicesNotAvailableException e) {

                } catch (final GooglePlayServicesRepairableException e) {

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String GAID) {
            if (GAID != null) {
                Activity activity = SupersonicRewardedVideoWrapper.this.mWeakActivity.get();
                if (activity != null) {
                    mMediationAgent.initRewardedVideo(activity, SupersonicRewardedVideoWrapper.this.mAppKey, GAID);
                    SupersonicRewardedVideoWrapper.this.mIsInitialized = true;
                }
            }
        }
    }

    public void onPause() {
        Activity activity = mWeakActivity.get();
        if (mMediationAgent != null && activity != null) {
            mMediationAgent.onPause(activity);
        }
    }
    public void onResume() {
        Activity activity = mWeakActivity.get();
        if (mMediationAgent != null && activity != null) {
            mMediationAgent.onResume(activity);
        }
    }

    public void onDestroy() {
        if (mMediationAgent != null) {
            mMediationAgent.setLogListener(null);
        }
        if (mGAIDRequestTask != null && !mGAIDRequestTask.isCancelled()) {
            mGAIDRequestTask.cancel(true);
            mGAIDRequestTask = null;
        }
        mWeakActivity = null;
    }
}

