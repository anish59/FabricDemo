package com.fabricdemo;

import android.app.Application;

import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;

/**
 * Created by anish on 28-09-2017.
 */

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this);
        // Initialize Branch automatic session tracking
        Branch.getAutoInstance(this);

    }
}
