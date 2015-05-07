package com.mycompany.myfirstapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


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
        Intent intent = new Intent(this, DisplayGithubActivity.class);
        EditText editText = (EditText) findViewById(R.id.github_url);
        String githubURL = editText.getText().toString();
        String commitInformation = readCommits(githubURL);
        intent.putExtra(GITHUB_REPO, commitInformation);
        startActivity(intent);
    }
    public String readCommits(String githubURL){
        return githubURL;
    }
}