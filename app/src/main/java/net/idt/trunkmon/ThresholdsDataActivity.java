package net.idt.trunkmon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.content.DialogInterface;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.graphics.Color;
import android.widget.TableRow.LayoutParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ThresholdsDataActivity extends AppCompatActivity {
    TableLayout tl;
    TableRow tr;
    TextView column_name, column_value;

    TableRow record_header, record_tail;
    TextView head_info;
    Button edit;
    private GoogleApiClient client;
    String[] columns = {"Location", "Division", "Tod", "Auto CCR", "Auto ALOC",
        "Auto Attempts", "Auto Memo", "Rev CCR", "Rev ALOC", "Rev Attempts",
        "Rev Memo"};
    // the filtered data sent from ThresholdsFilterActivity
    JSONObject request;
    // tje result data get from the AWS
    JSONObject response;
    // the legendSet contains the column names that need to be colored if necessary
    Set<String> legendSet = new HashSet<String>();
    // the global information to get the user role
    MyApplication myApplication;

    /**
     * The inner class to record whether the colored columns.
     * A grid on the table needs to be colored if their corresponding flag in this class is true.
     * Otherwisel flase.
     */
    private class LegendFlag {
        boolean CCR;
        boolean ALOC;
    }

    /**
     * The method check if a grid needs to be colored for a given JSONObject
     * @param cur the given JSONObject
     * @return flags the inner class containing the highlighted grids information
     * @throws JSONException
     */
    private LegendFlag legendHighlighterLogic(JSONObject cur) throws JSONException {
        LegendFlag flags = new LegendFlag();
        legendSet.add("Auto CCR");
        legendSet.add("Auto ALOC");
        if (cur.get("Auto CCR").toString().compareTo((cur.get("Rev CCR").toString())) > 0) {
            flags.CCR = true;
        }
        if (cur.get("Auto ALOC").toString().compareTo(cur.get("Rev ALOC").toString()) > 0) {
            flags.ALOC = true;
        }
        return flags;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication =(MyApplication)getApplicationContext();
        try {
            recreate_json();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * The method create the JSONObjec using the String sent from the THresholdsFilterAcitivity
     * @throws JSONException
     */
    public void recreate_json() throws JSONException {
        Intent intent = getIntent();
        if (intent.getExtras().get("prevActivity").equals("ThresholdsEditActivity")) {
            response = new JSONObject(intent.getExtras().getString("response"));
            setContentView(R.layout.activity_thresholds_data);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            tl = (TableLayout) findViewById(R.id.thresholds_table);
            Log.i("response from edit", intent.getExtras().getString("response"));
            showResult();
        }
        else {
            AWSResponse resp = new AWSResponse();
            request = new JSONObject(intent.getExtras().getString("request"));
            Log.i("request", intent.getExtras().getString("request"));
            //response = new JSONObject(intent.getExtras().getString("response"));
            //Log.i("response", intent.getExtras().getString("response"));
            resp.execute("https://l7o8agu92l.execute-api.us-east-1.amazonaws.com/violations/violations");
        }
    }

    /**
     * The method shows the result data in table format when it's redirected from ThresholdsEditActivitys
     * @throws JSONException
     */
    public void showResult() throws JSONException {
        JSONArray receivedArray = (JSONArray) response.get("records");
        for (int i = 0; i < receivedArray.length(); i++) {
            final int index = i;
            JSONObject cur = receivedArray.getJSONObject(i);
            LegendFlag flags = legendHighlighterLogic(cur);
            // adding header to each json object
            record_header = new TableRow(this);

            head_info = new TextView(this);
            head_info.setText("record " + (i + 1) + " of " + receivedArray.length());
            head_info.setTextColor(Color.BLUE);
            head_info.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            head_info.setPadding(5, 5, 5, 5);
            record_header.addView(head_info);

            tl.addView(record_header, new TableLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            // draw each rocords row by row using the column_name and column value
            for (int j = 0; j < columns.length; j++) {
                tr = new TableRow(this);
                tr.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
                // column name in the first column
                column_name = new TextView(this);
                column_name.setText(columns[j]);
                column_name.setTextColor(Color.BLACK);
                column_name.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                column_name.setPadding(5, 5, 5, 5);
                tr.addView(column_name);
                // column value in the second column
                column_value = new TextView(this);
                column_value.setText(cur.get(columns[j]).toString());
                column_value.setTextColor(Color.BLACK);
                // manipulate the representation of color highlighter in data table
                if (legendSet.contains(columns[j])) {
                    switch(columns[j]) {
                        case "Auto CCR":
                            if (flags.CCR) {
                                column_value.setBackgroundColor(Color.LTGRAY);
                            }
                            break;
                        case "Auto ALOC":
                            if (flags.ALOC) {
                                column_value.setBackgroundColor(Color.DKGRAY);
                                column_value.setTextColor(Color.LTGRAY);
                            }
                            break;
                        // add more if needed
                    }
                }
                column_value.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                column_value.setPadding(5, 5, 5, 5);
                tr.addView(column_value);

                tl.addView(tr, new TableLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
            }
            // adding buttons to modify the current record
            record_tail = new TableRow(this);
            edit = new Button(this);
            edit.setText("Edit");
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(getApplicationContext(), ThresholdsEditActivity.class);
                    intent.putExtra("response", response.toString());
                    intent.putExtra("index", Integer.toString(index));
                    startActivity(intent);
                }
            });
            edit.setVisibility(View.VISIBLE);
            edit.setPadding(0, 0, 0, 0);
            edit.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            record_tail.addView(edit);

            tl.addView(record_tail, new TableLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));


        }

    }

    /**
     * The method shows the result data in table format when it's redirected from ThresholdsFilterActivitys
     * @throws JSONException
     */
    public void showResult(ProgressDialog progressDialog) throws JSONException {
        JSONArray receivedArray = (JSONArray) response.get("records");
        for (int i = 0; i < receivedArray.length(); i++) {
            final int index = i;
            JSONObject cur = receivedArray.getJSONObject(i);
            LegendFlag flags = legendHighlighterLogic(cur);
            // adding header to each json object
            record_header = new TableRow(this);

            head_info = new TextView(this);
            head_info.setText("record " + (i + 1) + " of " + receivedArray.length());
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
                // column name in the first column
                column_name = new TextView(this);
                column_name.setText(columns[j]);
                column_name.setTextColor(Color.BLACK);
                column_name.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                column_name.setPadding(5, 5, 5, 5);
                tr.addView(column_name);
                // column value in the second column
                column_value = new TextView(this);
                column_value.setText(cur.get(columns[j]).toString());
                column_value.setTextColor(Color.BLACK);
                // manipulate the representation of color highlighter in data table
                if (legendSet.contains(columns[j])) {
                    switch(columns[j]) {
                        case "Auto CCR":
                            if (flags.CCR) {
                                column_value.setBackgroundColor(Color.LTGRAY);
                            }
                            break;
                        case "Auto ALOC":
                            if (flags.ALOC) {
                                column_value.setBackgroundColor(Color.DKGRAY);
                            }
                            break;
                        // add more if needed
                    }
                }
                column_value.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                column_value.setPadding(5, 5, 5, 5);
                tr.addView(column_value);

                tl.addView(tr, new TableLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
            }
            if ("manager".equals(myApplication.getUserRole())) {
                // adding buttons to modify the current record
                record_tail = new TableRow(this);
                edit = new Button(this);
                edit.setText("Edit");
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(getApplicationContext(), ThresholdsEditActivity.class);
                        intent.putExtra("response", response.toString());
                        intent.putExtra("index", Integer.toString(index));
                        startActivity(intent);
                    }
                });
                edit.setVisibility(View.VISIBLE);
                edit.setPadding(0, 0, 0, 0);
                edit.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                record_tail.addView(edit);

                tl.addView(record_tail, new TableLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
            }
        }
        progressDialog.dismiss();
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
        } else if (id == R.id.vioLegendBt) {
            startActivity(new Intent(this, thresPopLegend.class));
            return true;
        } else {
            //id == R.id.action_about
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * The inner class that get the JSONObject from the AWS
     */
    class AWSResponse extends AsyncTask<String, Void, String> {

        private Exception exception;
        ProgressDialog progressDialog;
        String res;
        protected String doInBackground(String... urls) {
            BufferedReader reader = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                StringBuilder sb = new StringBuilder();

                InputStreamReader is = new InputStreamReader(connection.getInputStream());

                reader = new BufferedReader(is);

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                res = sb.toString();
                return res;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ThresholdsDataActivity.this,"Loading...",
                    "Loading application View, please wait...", false, false);

        }

        protected void onPostExecute(String response1) {
            setContentView(R.layout.activity_thresholds_data);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            try {
                response = new JSONObject(response1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("response", res);
            tl = (TableLayout) findViewById(R.id.thresholds_table);
            try {
                //recreate_json();
                //generateColumns();
                showResult(progressDialog);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            // progressDialog.dismiss();
            //initialize the View

            client = new GoogleApiClient.Builder(ThresholdsDataActivity.this).addApi(AppIndex.API).build();

        }
    }
}
