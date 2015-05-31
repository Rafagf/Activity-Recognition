package com.rippll.motiontest;
import android.app.IntentService;
import android.content.Intent;
import android.util.Config;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

/**
 * Created by rippll on 11/03/2015.
 */
public class ActivityRecognitionService extends IntentService {

    private static final String TAG = "ActivityRecognition";
    public static final String ACTION_UPDATE = "com.rippll.motiontest.NEWUPDATE";

    public ActivityRecognitionService() {
        super("ActivityRecognitionService");
    }

    /**
     * Google Play Services calls this once it has analysed the sensor data
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            DetectedActivity detectedActivity = result.getMostProbableActivity();
            result.getProbableActivities();
            int activity = detectedActivity.getType();

            List<DetectedActivity> listActivities = result.getProbableActivities();

            //Send update
            Intent intentUpdate = new Intent();
            intentUpdate.setAction(ACTION_UPDATE);
            intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);

            String activityString = "";
            int confidence = 0;

            for(int i = 0; i < listActivities.size() && i < 3; i++){

                int currentActivity = listActivities.get(i).getType();

                switch (currentActivity) {

                    case DetectedActivity.IN_VEHICLE:
                        if(i == 0)
                            activityString = "In vehicle";
                        intentUpdate.putExtra("Activity" + String.valueOf(i), "In vehicle");
                        intentUpdate.putExtra("Confidence"  + String.valueOf(i), listActivities.get(i).getConfidence());
                        break;
                    case DetectedActivity.ON_BICYCLE:
                        if(i == 0)
                            activityString = "On bycicle";
                        intentUpdate.putExtra("Activity" + String.valueOf(i), "On bycicle");
                        intentUpdate.putExtra("Confidence" + String.valueOf(i), listActivities.get(i).getConfidence());
                        break;
                    case DetectedActivity.RUNNING:
                        if(i == 0)
                            activityString = "Running";
                        intentUpdate.putExtra("Activity" + String.valueOf(i), "Running");
                        intentUpdate.putExtra("Confidence" + String.valueOf(i), listActivities.get(i).getConfidence());
                        break;
                    case DetectedActivity.ON_FOOT:
                        if(i == 0)
                            activityString = "On foot";
                        intentUpdate.putExtra("Activity" + String.valueOf(i), "On foot");
                        intentUpdate.putExtra("Confidence" + String.valueOf(i), listActivities.get(i).getConfidence());
                        break;
                    case DetectedActivity.TILTING:
                        if(i == 0)
                            activityString = "Tilting";
                        intentUpdate.putExtra("Activity" + String.valueOf(i), "Tilting");
                        intentUpdate.putExtra("Confidence" + String.valueOf(i), listActivities.get(i).getConfidence());
                        break;
                    case DetectedActivity.STILL:
                        if(i == 0)
                            activityString = "Still";
                        intentUpdate.putExtra("Activity" + String.valueOf(i), "Still");
                        intentUpdate.putExtra("Confidence" + String.valueOf(i), listActivities.get(i).getConfidence());
                        break;
                    case DetectedActivity.WALKING:
                        if(i == 0)
                            activityString = "Walking";
                        intentUpdate.putExtra("Activity" + String.valueOf(i), "Walking");
                        intentUpdate.putExtra("Confidence" + String.valueOf(i), listActivities.get(i).getConfidence());
                        break;
                    default:
                        if(i == 0)
                            activityString = "Unknown";
                        intentUpdate.putExtra("Activity" + String.valueOf(i), "Unknown");
                        intentUpdate.putExtra("Confidence" + String.valueOf(i), listActivities.get(i).getConfidence());

                        break;
                }

            }

            Log.d("Activity", activityString + " Confidence: " + String.valueOf(detectedActivity.getConfidence()));
            sendBroadcast(intentUpdate);
        }
    }
}
