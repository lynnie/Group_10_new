package com.mycompany.myfirstapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class CheckUpdatesService extends Service {
    public CheckUpdatesService() {
    }

    private static final String TAG = "HelloService";

    private boolean isRunning  = false;

    private static final int MY_NOTIFICATION_ID = 1;

    // Notification Count
    private int mNotificationCount;

    // Notification Text Elements
    private final CharSequence tickerText = "New pushes are available on GitHub. Make your repo up-to-date!";
    private final CharSequence contentTitle = "GitHub Updates";
    private final CharSequence contentText = "You have following updates!\n";

    // Notification Action Elements
    private Intent mNotificationIntent;
    private PendingIntent mContentIntent;

    // Notification Sound and Vibration on Arrival

    private long[] mVibratePattern = { 0, 200, 200, 300 };

    private String username;

    private ArrayList<String> ids=new ArrayList<String>();

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");

        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");
        username=intent.getStringExtra("username");
        String chechk="tempo";

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        new Thread(new Runnable() {
            @Override
            public void run() {


                //Your logic that service will perform will be placed here
                //In this example we are just looping and waits for 1000 milliseconds in each loop.


                fetchCurrent();

                //for (int i = 0; i < 1; i++) {/**********have to change**********/
                while(true){
                    try {
                        Thread.sleep(5000);
                        checkUpdates();
                    } catch (Exception e) {
                    }
                    //checkUpdates();

                    if(isRunning){
                        Log.i(TAG, "Service running");
                    }
                }

                //Stop service once it finishes its task
                //stopSelf();
            }
        }).start();

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;

        Log.i(TAG, "Service onDestroy");
    }

    public void sendNotification(String line1, String line2, String line3,String line4){

            mNotificationIntent = new Intent(getApplicationContext(),
                    NotificationSubActivity.class);
            mContentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                    mNotificationIntent, 0);

            Notification notificationBuilder = new Notification.Builder(
                            getApplicationContext())
                            .setTicker(tickerText)
                            .setSmallIcon(android.R.drawable.stat_sys_warning)
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setLights(Color.GREEN, 100, 300)
                            .setContentTitle(contentTitle)
                            .setContentText(
                                    contentText + " (" + ++mNotificationCount + ")\n")
                            .setStyle(new Notification.InboxStyle()
                                            .addLine(line1)
                                            .addLine(line2)
                                            .addLine(line3)
                                            .addLine(line4)
                                /*.setSummaryText("+3 more")*/)
                            .setContentIntent(mContentIntent)
                            .setVibrate(mVibratePattern)
                            .build();

                    // Pass the Notification to the NotificationManager:
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(MY_NOTIFICATION_ID,
                            notificationBuilder);

                }


    public void checkUpdates(){
        String reposInfo = "https://api.github.com/users/"+username+"/received_events";
        String listrepo=fetchWeb(reposInfo);


        try {
            JSONArray jsonArray = new JSONArray(listrepo);

            for(int i = 0; i < jsonArray.length(); i++) {
                  try {
                      //Thread.sleep(5000);
                      JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                      //result=result+jsonObject.getString("id")+"\n"/*+jsonObject.getString("commits")*/;
                      if (((jsonObject.getString("type")).equals("PushEvent")) && !ids.contains(jsonObject.getString("id"))) {
                          ids.add(jsonObject.getString("id"));
                          String tempString=jsonObject.getJSONObject("payload").getString("commits");
                          JSONArray tempArray=new JSONArray(tempString);
                          JSONObject tempObject=(JSONObject)tempArray.get(0);


                          sendNotification("push id = "+ jsonObject.getString("id")
                                  ,"author = "+ tempObject.getString("author").substring(tempObject.getString("author").indexOf("name")+7)
                                  ,"message = "+ tempObject.getString("message")
                                  ,"created at = "+ jsonObject.getString("created_at"));
                      }
                  }
                  catch (Exception e){}
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void fetchCurrent(){
        String reposInfo = "https://api.github.com/users/"+username+"/received_events";
        String listrepo=fetchWeb(reposInfo);


        try {
            JSONArray jsonArray = new JSONArray(listrepo);

            for(int i = 0; i < jsonArray.length(); i++) {/*************have to change here***********/
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if( ((jsonObject.getString("type")).equals("PushEvent")) && !ids.contains(jsonObject.getString("id"))){
                    ids.add(jsonObject.getString("id"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String fetchWeb(String url) {
        String result = "";
        DefaultHttpClient http = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = http.execute(get);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
                sb.append(line+ NL);
            }
            in.close();
            result = sb.toString();
        } catch (Exception e) {
            result = "";
        }

        return result;
    }

}
