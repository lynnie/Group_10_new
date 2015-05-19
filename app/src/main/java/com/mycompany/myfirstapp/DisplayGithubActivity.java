package com.mycompany.myfirstapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
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


public class DisplayGithubActivity extends ActionBarActivity {

    public static final String GITHUB_REPO = "com.mycompany.myfirstapp.GITHUB_REPO";

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
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            for(int i = 0; i < jsonArray.length(); i++) {
                final Button button = new Button(this);
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                button.setText(jsonObject.getString("full_name"));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), CheckUpdatesService.class);
                        String username = ((String)button.getText()).split("/")[0];
                        String repo=((String)button.getText()).split("/")[1];
                        intent.putExtra("username",username);
                        intent.putExtra("repo",repo);
                        startService(intent);
                    }
                });
                layout.addView(button);
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
