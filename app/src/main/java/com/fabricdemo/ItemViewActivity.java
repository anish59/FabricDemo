package com.fabricdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ItemViewActivity extends AppCompatActivity {

    private android.widget.TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        this.text = (TextView) findViewById(R.id.text);
        if (this.getIntent().hasExtra("item_id")) {
            // read the item id from the intent
            String itemId = this.getIntent().getStringExtra("item_id");
            String doneText = this.getIntent().getStringExtra("VerifyText");
            text.setText(this.getIntent().getStringExtra("VerifyText"));
            // load the item associated with item id
            Log.i("MyTestApp", "Just deep linked with item id: " + itemId);
            System.out.println("itemId=" + itemId);
            System.out.println("verifyText" + doneText);
            Toast.makeText(this, "verifyText:" + doneText, Toast.LENGTH_SHORT).show();
        }

    }
}
