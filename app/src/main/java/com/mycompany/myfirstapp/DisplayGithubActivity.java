package com.mycompany.myfirstapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class DisplayGithubActivity extends ActionBarActivity {

    public static final String GITHUB_REPO = "com.mycompany.myfirstapp.GITHUB_REPO";
    public static final String PREFS_NAME = "MonitorRepos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String listrepo = intent.getStringExtra(DisplayEnterGithubActivity.GITHUB_REPO);
        // TextView textView = new TextView(this);
        // textView.setText(listrepo);
        // setContentView(textView);
        try {
            JSONArray jsonArray = new JSONArray(listrepo);
            GridLayout layout = new GridLayout(this);
            layout.setColumnCount(1);
            int[] zebra = {Color.rgb(153,204,255), Color.rgb(153,183,255)};

            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();

            Map<String, ArrayList<CheckBox>> cboxes = new HashMap<String, ArrayList<CheckBox>>();
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                String fullName = jsonObject.getString("full_name");
                final String username = (fullName.split("/"))[0];
                final String repo = (fullName.split("/"))[1];
                String id = jsonObject.getString("id");

                final CheckBox cbox = new CheckBox(getApplicationContext());
                cbox.setText(repo);
                cbox.setTextColor(zebra[i%2]);
                cbox.setTag(id);

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                boolean c = settings.getBoolean(id, false);
                cbox.setChecked(c);

                cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        String id = (String)((CheckBox)v).getTag();

                        if (isChecked) {
                            //Intent intent = new Intent(v.getContext(), CheckUpdatesService.class);
                            //intent.putExtra("username", username);
                            //intent.putExtra("repo", repo);
                            //startService(intent);
                            editor.putBoolean(id, true);
                            //
                            Intent intent = new Intent(v.getContext(), CheckUpdatesService.class);
                            intent.putExtra("username",username);
                            intent.putExtra("repo",repo);
                            startService(intent);
                            //
                        }else{
                            editor.putBoolean(id, false);
                        }

                        // Commit the edits!
                        editor.commit();
                    }
                });

                ArrayList<CheckBox> aList = cboxes.get(username);
                if (aList == null) {
                    aList = new ArrayList<CheckBox>();
                }

                aList.add(cbox);
                cboxes.put(username, aList);

                //final Button button = new Button(this);
                //button.setText(jsonObject.getString("full_name"));
                /*button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), CheckUpdatesService.class);
                        String username = ((String)button.getText()).split("/")[0];
                        String repo=((String)button.getText()).split("/")[1];
                        intent.putExtra("username",username);
                        intent.putExtra("repo",repo);
                        startService(intent);
                    }
                });*/
                //layout.addView(button);
            }

            Iterator it = cboxes.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                TextView tv = new TextView(this);
                tv.setText((String) pair.getKey());
                layout.addView(tv);

                Iterator<CheckBox> userRepos = ((ArrayList<CheckBox>)pair.getValue()).iterator();
                while (userRepos.hasNext()) {
                    layout.addView(userRepos.next());
                    userRepos.remove();
                }

                it.remove(); // avoids a ConcurrentModificationException
            }

            setContentView(layout);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_github, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
