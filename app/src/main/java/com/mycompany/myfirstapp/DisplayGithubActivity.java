package com.mycompany.myfirstapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DisplayGithubActivity extends ActionBarActivity {

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
                final DisplayGithubActivity displayGithub = this;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView textView = new TextView(displayGithub);
                        textView.setText(button.getText());
                        setContentView(textView);
//                        Intent intent = new Intent(displayGithub, DisplayEnterGithubActivity.class);
//                        startActivity(intent);
                    }
                });
                layout.addView(button);
            }
            setContentView(layout);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // WebView webview = new WebView(this);
        // setContentView(webview);
        // for example, https://api.github.com/repos/lynnie/Group_10_new/branches/master
        // webview.loadUrl(repo);
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
