package com.mycompany.myfirstapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class DisplaySelectDeckActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_deck);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_deck, menu);
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
    public void displayDeckNormal(View view){
    /*
    Add window here.
     */
        displayDeck(view, R.layout.activity_display_normal_cards);
    }
    public void displayDeckFibonacci(View view){
    /*
    Add window here.
     */
        displayDeck(view, R.layout.activity_display_fibonacci_cards);
    }

    public void displayDeckTShirt(View view){
    /*
    Add window here.
     */
        displayDeck(view, R.layout.activity_display_tshirt_cards);
    }

    private void displayDeck(View view, int layout) {
        Intent intent = new Intent (this,DisplayCardsActivity.class);
        Bundle b = new Bundle();
        b.putInt("layout", layout);
        intent.putExtras(b);
        startActivity(intent);
    }
}
