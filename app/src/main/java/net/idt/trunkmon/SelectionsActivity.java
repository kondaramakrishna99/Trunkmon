package net.idt.trunkmon;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.util.Log;

public class SelectionsActivity extends AppCompatActivity {
    private static final String TAG = "selection log message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "selection activity onCreate");
        setContentView(R.layout.activity_selections);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button vbutton = (Button)findViewById(R.id.vbutton);
        vbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(),ViolationsFilterActivity.class);
                startActivity(intent);
                Log.i(TAG, "violations button clicked");
            }
        });

        Button tbutton = (Button)findViewById(R.id.tbutton);
        tbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(),ThresholdsFilterActivity.class);
                startActivity(intent);
                Log.i(TAG, "thresholds button clicked");
            }
        });
    }

}
