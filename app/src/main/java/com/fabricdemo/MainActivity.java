package com.fabricdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.appsee.Appsee;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    Pubnub pubnub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Answers(), new Crashlytics());
        setContentView(R.layout.activity_main);

        init();

    }

    private void init() {
        initAppSee();
        setEventForAnswer();//For analytics : Answers

        /******PubNub********/
        pubnub = new Pubnub(getString(R.string.com_pubnub_publishKey), getString(R.string.com_pubnub_subscribeKey));
        publishMessageToAChannel();//Send simple message to the channel.
        subscribeToChannel();//Start receiving real-time messages by subscribing on channel(s).
        observePresenceEvents();//Start receiving real-time channel(s) presence events.
        fetchHistory();//For fetching past history messages
        /******PubNub********/}

    private void initAppSee() {
        Appsee.start(getString(R.string.com_appsee_apikey));
        Appsee.setUserId("User01");
        Appsee.addEvent("UserDepositFinished");
        Appsee.addEvent("ItemPurchased", new HashMap<String, Object>() {{
            put("Price", 100);
            put("Country", "USA");
        }});
//        Appsee.stop(); only when you want to stop the video recording
    }

    private void fetchHistory() {


        pubnub.history("demo_channel", 10, new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                System.out.println(message);
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error);
            }
        });
    }

    private void observePresenceEvents() {


        try {
            pubnub.presence("demo_channel", new Callback() {

                        @Override
                        public void connectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : CONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : " + channel + " : "
                                    + message.getClass() + " : " + message.toString());
                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                        }
                    }
            );
        } catch (PubnubException e) {
            e.printStackTrace();
        }


    }

    private void publishMessageToAChannel() {


        pubnub.publish("demo_channel", "demo_message", new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                System.out.println(message);
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error);
            }
        });

    }

    private void subscribeToChannel() {


        try {
            pubnub.subscribe("demo_channel", new Callback() {

                        @Override
                        public void connectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : CONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : " + channel + " : "
                                    + message.getClass() + " : " + message.toString());
                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                        }
                    }
            );
        } catch (PubnubException e) {
            e.printStackTrace();
        }
    }

    private void setEventForAnswer() {
        // TODO: Use your own attributes to track content views in your app
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("DemoTweet")
                .putContentType("Video")
                .putContentId("1234")
                .putCustomAttribute("Favorites Count", 20)
                .putCustomAttribute("Screen Orientation", "portrait"));

//        throw new RuntimeException("exceptionTest: init onCreate");
    }

    public void forceCrash(View view) {
        Toast.makeText(this, "exp Test 2: buttonClick", Toast.LENGTH_SHORT).show();
//        throw new RuntimeException("exp Test 2: buttonClick");

    }

    public void traceEvent(View view) {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("btnClick:- traceEvent")
                .putContentType("Btn")
                .putContentId("btn01")
                .putCustomAttribute("demoAttribute", "test1")
                .putCustomAttribute("Screen Orientation", "portrait"));
    }

    public void sendMessage(View view) {
    }

    public void clickGetRealTimeMsg(View view) {

    }
}


//  [
//    "demo_message",
//    {"text": "hi"},
//    "demo_message",
//    {"text": "hi"},
//    "demo_message"
//  ]
