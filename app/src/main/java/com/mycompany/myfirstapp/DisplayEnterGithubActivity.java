package com.mycompany.myfirstapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class DisplayEnterGithubActivity extends ActionBarActivity {

    public static final String GITHUB_REPO = "com.mycompany.myfirstapp.GITHUB_REPO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_enter_github);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_enter_github, menu);
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

    public void getGithubRepository(View view) {
        // Ask [username]
        // https://api.github.com/users/[username]/repos?type=all
        // List repos and select from which you'll receive notifications
        // Create a service that runs and waits for updates
        // https://api.github.com/users/[username]/received_events
        // [We have username and repos] Get received events and notify on new events

        Intent intent = new Intent(this, DisplayGithubActivity.class);
        EditText editText = (EditText) findViewById(R.id.github_url);
        String username = editText.getText().toString();
        String reposInfo = "https://api.github.com/users/"+username+"/repos?type=all";

        System.out.println(fetchWeb(reposInfo));

        //intent.putExtra(GITHUB_REPO, reposInfo);
        //startActivity(intent);
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
                sb.append(line + NL);
            }
            in.close();
            result = sb.toString();
        } catch (Exception e) {
            result = "";
        }

        return result;
    }

}
