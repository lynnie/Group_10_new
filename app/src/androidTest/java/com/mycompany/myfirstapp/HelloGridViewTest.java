package com.mycompany.myfirstapp;

import android.app.Application;
import android.app.Instrumentation;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class HelloGridViewTest extends ActivityInstrumentationTestCase2<HelloGridView> {

    private HelloGridView HelloGridViewActivity;

    private Button playPoker;

    public HelloGridViewTest() {
        super(HelloGridView.class);
    }

    protected void setUp() throws Exception{
        super.setUp();
        HelloGridViewActivity = getActivity();

    }

    public void testDisplayCorrectActivity(){
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(DisplaySelectDeckActivity.class.getName(), null, false);

        // Check that we are in the correct activity
        String button = "Play Poker";
        playPoker = (Button) HelloGridViewActivity.findViewById(R.id.pokerButton);
        String actual = playPoker.getText().toString();
        assertEquals(button, actual);

        // Click button
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playPoker.performClick();
            }
        });

        // Check that we got an activity and...
        DisplaySelectDeckActivity displaySelectDeckActivity = (DisplaySelectDeckActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 10000);
        assertNotNull(displaySelectDeckActivity);

        // that it has three buttons with proper texts
        Button standardButton = (Button) displaySelectDeckActivity.findViewById(R.id.standardButton);
        Button fibButton = (Button) displaySelectDeckActivity.findViewById(R.id.fibButton);
        Button tshirtButton = (Button) displaySelectDeckActivity.findViewById(R.id.tshirtButton);
        assertNotNull(standardButton);
        assertNotNull(fibButton);
        assertNotNull(tshirtButton);
        assertEquals("Standard", standardButton.getText());
        assertEquals("Fibonacci", fibButton.getText());
        assertEquals("T-Shirt", tshirtButton.getText());
    }
}