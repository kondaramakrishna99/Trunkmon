package net.idt.trunkmon;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class ViolationsDataActivity extends AppCompatActivity {
    // need to change later to add filter data columns
    String columns[] = {"Division", "Account Names", "CLLI", "Location"};

    // simulated data part
    String values[] = {"Gold", "Verizon", "NP1", "USA",
            "Gold", "ATT", "NP4", "UK",
            "Silver", "Tmobile", "NP3", "Canada",
            "USDebit", "Sprint", "NP2", "Nigeria"};

    // decided by the JSON length passed back from the server
    int JSON_count = 86;

    TableLayout tl;
    TableRow tr;
    TextView column_name, column_value;

    TableRow record_header, record_tail;
    TextView head_info, tail_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_violations_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tl = (TableLayout) findViewById(R.id.violations_table);
        showData();
    }

    public void showData() {
        for (int i = 0; i < values.length/columns.length; i++) {
            // adding header to each json object
            record_header = new TableRow(this);
            head_info = new TextView(this);
            head_info.setText("record " + (i+1) + " of " + JSON_count);
            head_info.setTextColor(Color.BLUE);
            head_info.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            head_info.setPadding(5, 5, 5, 5);
            record_header.addView(head_info);

            tl.addView(record_header, new TableLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));


            for (int j = 0; j < columns.length; j++) {
                tr = new TableRow(this);
                tr.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));

                column_name = new TextView(this);
                column_name.setText(columns[j % columns.length]);
                column_name.setTextColor(Color.BLACK);
                column_name.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                column_name.setPadding(5, 5, 5, 5);
                tr.addView(column_name);

                column_value = new TextView(this);
                column_value.setText(values[j]);
                column_value.setTextColor(Color.BLACK);
                column_value.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                column_value.setPadding(5, 5, 5, 5);
                tr.addView(column_value);

                tl.addView(tr, new TableLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
            }
            // adding
            record_tail = new TableRow(this);
            tail_info = new TextView(this);
            tail_info.setText("----------------------------------");
            tail_info.setTextColor(Color.BLUE);
            tail_info.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            tail_info.setPadding(5, 5, 5, 5);
            record_tail.addView(tail_info);

            tl.addView(record_tail, new TableLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_content) {
            startActivity(new Intent(this, SelectionsActivity.class));
        } else if (id == R.id.action_violations) {
            startActivity(new Intent(this, ViolationsFilterActivity.class));
        } else if (id == R.id.action_thresholds) {
            startActivity(new Intent(this, ThresholdsFilterActivity.class));
        } else if (id == R.id.action_logout) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.action_about){
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        else if (id == R.id.action_search) {
            // implemented later
            return true;
        }
        else { //(id == R.id.legend)
             //TODO
            // implemented the legend image here
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
