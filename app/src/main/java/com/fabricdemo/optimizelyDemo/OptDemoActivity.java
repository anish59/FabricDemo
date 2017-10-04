package com.fabricdemo.optimizelyDemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fabricdemo.R;
import com.optimizely.Optimizely;
import com.optimizely.Variable.LiveVariable;

import io.fabric.sdk.android.Fabric;

public class OptDemoActivity extends AppCompatActivity {
    private static LiveVariable<String> myMessageVariable;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        init();
    }

    private void init() {
        setContentView(R.layout.activity_opt_demo);
        Fabric.with(this);
        Optimizely.startOptimizelyWithAPIToken("AANlBDUBcVuak0a4ryLXz-fURdLO3cHz~8905164942", getApplication());
//        Optimizely.startOptimizelyWithAPIToken(getString(R.string.com_optimizely_api_key), getApplication());
        // Creates a Live Variable with a given key and default value
        myMessageVariable = Optimizely.stringForKey("message", "Hello World!");

    }

    public void showMessage(View view) {
        Toast.makeText(context, myMessageVariable.get(), Toast.LENGTH_LONG).show();
    }

}
