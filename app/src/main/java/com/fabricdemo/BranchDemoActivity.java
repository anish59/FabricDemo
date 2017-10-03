package com.fabricdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.Branch.BranchReferralInitListener;
import io.branch.referral.BranchError;
import io.branch.referral.BranchViewHandler;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

public class BranchDemoActivity extends AppCompatActivity {
    BranchUniversalObject branchUniversalObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Hook up your share button to initiate sharing
        findViewById(R.id.buttonShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                initiateSharing();
            }
        });
        contentLoaded();

    }

    // This would be your own method where you've loaded the content for this page
    void contentLoaded() {
        // Initialize a Branch Universal Object for the page the user is viewing
        branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier("item_id_12345")
                .setTitle("My Content Title")
                .setContentDescription("Check out this awesome piece of content")
                .setContentImageUrl("https://example.com/mycontent-12345.png")
                .addContentMetadata("item_id", "12345")
                .addContentMetadata("user_id", "678910")
                .addContentMetadata("VerifyText", "done");

        // Trigger a view on the content for analytics tracking
        branchUniversalObject.registerView();

        // List on Google App Indexing
        branchUniversalObject.listOnGoogleSearch(this);
    }

    // This is the function to handle sharing when a user clicks the share button
    void initiateSharing() {
        // Create your link properties
        // More link properties available at https://dev.branch.io/getting-started/configuring-links/guide/#link-control-parameters
        LinkProperties linkProperties = new LinkProperties()
                .setFeature("sharing");

        // Customize the appearance of your share sheet
        ShareSheetStyle shareSheetStyle = new ShareSheetStyle(this, "Check this out!", "Hey friend - I know you'll love this: ")
                .setCopyUrlStyle(getResources().getDrawable(android.R.drawable.ic_menu_send), "Copy link", "Link added to clipboard!")
                .setMoreOptionStyle(getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.TWITTER);

        // Show the share sheet for the content you want the user to share. A link will be automatically created and put in the message.
        branchUniversalObject.showShareSheet(this, linkProperties, shareSheetStyle, new Branch.BranchLinkShareListener() {
            @Override
            public void onShareLinkDialogLaunched() {
            }

            @Override
            public void onShareLinkDialogDismissed() {
            }

            @Override
            public void onChannelSelected(String channelName) {
            }

            @Override
            public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                // The link will be available in sharedLink
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();
        branch.initSession(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError error) {
                if (error == null && branchUniversalObject != null) {
                    // This code will execute when your app is opened from a Branch deep link, which
                    // means that you can route to a custom activity depending on what they clicked.
                    // In this example, we'll just print out the data from the link that was clicked.

                    Log.i("BranchTestBed", "referring Branch Universal Object: " + branchUniversalObject.toString());

                    // check if the item is contained in the metadata
                    if (branchUniversalObject.getMetadata().containsKey("item_id")) {
                        Intent i = new Intent(getApplicationContext(), ItemViewActivity.class);
                        i.putExtra("item_id", branchUniversalObject.getMetadata().get("item_id"));
                        i.putExtra("VerifyText", branchUniversalObject.getMetadata().get("VerifyText"));
                        startActivity(i);
                    }
                }
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

}
