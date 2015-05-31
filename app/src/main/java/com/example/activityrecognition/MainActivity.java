package com.example.activityrecognition;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;

public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener{

    private static ActivityRecognitionClient mActivityRecognitionClient;
    private static PendingIntent callbackIntent;
    MyBroadcastReceiver myBroadcastReceiver;
    TextView activityValueTextView;
    TextView confidenceValueTextView;
    TextView activityTextView2;
    TextView activityTextView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityValueTextView = (TextView)findViewById(R.id.activityValueTextView);
        confidenceValueTextView = (TextView)findViewById(R.id.confidenceValueTextView);
        activityTextView2 = (TextView)findViewById(R.id.secondActivityTextView);
        activityTextView3 = (TextView)findViewById(R.id.thirdActivityTextView);

        if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) != ConnectionResult.SUCCESS){

            Toast.makeText(getApplicationContext(), "Sorry, Google Play Services is not available", Toast.LENGTH_LONG).show();
        }

        mActivityRecognitionClient = new ActivityRecognitionClient(getApplicationContext(), this, this);
        mActivityRecognitionClient.connect();

        myBroadcastReceiver = new MyBroadcastReceiver();

        //Register BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(ActivityRecognitionService.ACTION_UPDATE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    /**
     * Connection established - start listening now
     */
    @Override
    public void onConnected(Bundle connectionHint) {

        Intent intent = new Intent(getApplicationContext(), ActivityRecognitionService.class);
        callbackIntent = PendingIntent.getService(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mActivityRecognitionClient.requestActivityUpdates(0, callbackIntent); // 0 sets it to update as fast as possible, just use this for testing!

        IntentFilter intentFilter_update = new IntentFilter();
        intentFilter_update.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter_update);

    }

    @Override
    public void onDisconnected() {
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int confidence = 0;
            String activity = "Unknown";

            try {
                confidence = intent.getIntExtra("Confidence0", 0);
                activity = intent.getStringExtra("Activity0");
                activityValueTextView.setText(activity);
                confidenceValueTextView.setText("Confidence: " + String.valueOf(confidence));
            } catch (Exception e) {
                activityValueTextView.setText("Waiting...");
                confidenceValueTextView.setText("Confidence: 0");
            }

            try {
                confidence = intent.getIntExtra("Confidence1", 0);
                activity = intent.getStringExtra("Activity1");
                activityTextView2.setText("2. " + activity + " Confidence: " + confidence);
                activityTextView2.setVisibility(View.VISIBLE);

                if(activity == null)
                    activityTextView2.setVisibility(View.INVISIBLE);

            } catch (Exception e) {
                activityTextView2.setVisibility(View.INVISIBLE);
            }

            try {
                confidence = intent.getIntExtra("Confidence2", 0);
                activity = intent.getStringExtra("Activity2");
                activityTextView3.setText("3. " + activity + " Confidence: " + confidence);
                activityTextView3.setVisibility(View.VISIBLE);

                if(activity == null)
                    activityTextView3.setVisibility(View.INVISIBLE);

            } catch (Exception e) {
                activityTextView3.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
        mActivityRecognitionClient.removeActivityUpdates(callbackIntent);
        mActivityRecognitionClient.disconnect();
    }
}