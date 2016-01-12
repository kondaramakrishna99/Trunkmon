package net.idt.trunkmon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import me.kaede.tagview.OnTagDeleteListener;
import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;
import util.MultiSelectionSpinner;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.TextView;

import util.MultiSelectionSpinner;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViolationsFilterActivity extends AppCompatActivity implements Communicator {
    private static final String TAG = "vio filter log message";
    private MultiSelectionSpinner startCountrySpinner;
    private MultiSelectionSpinner divisionSpinner;
    private MultiSelectionSpinner additionalSpinner;
    private MultiSelectionSpinner showFieldsSpinner;
    private MultiSelectionSpinner timeDropdown;
    String[] startCountryItems = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    String[] timeItems = new String[]{"1/4/2015 17:00", "1/5/2015 17:00", "1/6/2015 17:00","1/7/2015 17:00"};
    String[] divisionItems = {"Gold", "USDebit", "Silver", "UKDebit", "Carriers"};
    String[] additionalItems = {"review-pulled", "auto-pulled", "cross division saved", "excluded locations", "managed countries only"};
    String[] showFieldsItems = {"Attempts", "Completed", "Failed", "Minutes", "CCR"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "vio filter onCreate");
        setContentView(R.layout.activity_violations_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button applyBt = (Button)findViewById(R.id.vApplyButton);


        timeDropdown = (MultiSelectionSpinner)findViewById(R.id.timeSpinner);
        timeDropdown.spinner_title = "Time";
        timeDropdown.setItems(timeItems);



        startCountrySpinner = (MultiSelectionSpinner) findViewById(R.id.startCountrySpinner);
        startCountrySpinner.spinner_title = "Start Country";
        startCountrySpinner.setItems(startCountryItems);
        //startCountrySpinner.setSelection(new int[]{2, 6});


        divisionSpinner = (MultiSelectionSpinner) findViewById(R.id.divisionSpinner);
        divisionSpinner.spinner_title= "Division";
        divisionSpinner.setItems(divisionItems);


        additionalSpinner = (MultiSelectionSpinner) findViewById(R.id.additionalSpinner);
        additionalSpinner.spinner_title = "Additional items";
        additionalSpinner.setItems(additionalItems);


        showFieldsSpinner = (MultiSelectionSpinner) findViewById(R.id.showFieldsSpinner);
        showFieldsSpinner.spinner_title = "Showfields";
        showFieldsSpinner.setItems(showFieldsItems);
    }


    @Override
    public void responseTime(ArrayList<String> text) {

        TagView tagview_time = (TagView)findViewById(R.id.tagview_time);
        tagview_time.removeAllTags();
        List<String> selection = new ArrayList<String>(text);
        //selection.add("check1");
        //List<String> selection = startCountrySpinner.getSelectedStrings();
        for(String s:selection)
        {
            Tag tag = new Tag(s);
            tag.isDeletable = true;
            tagview_time.addTag(tag);
        }
        tagview_time.setOnTagDeleteListener(new OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag tag, int i) {
                for(int k=0;k<timeItems.length;k++)
                {
                    if(timeItems[k].contains(tag.text.toString()))
                    {
                        timeDropdown.mSelection[k] = false;
                    }
                }

            }
        });
    }

    @Override
    public void responseCountry(ArrayList<String> text) {

        TagView tagview_country = (TagView)findViewById(R.id.tagview_country);
        tagview_country.removeAllTags();
        List<String> selection = new ArrayList<String>(text);
        //selection.add("check1");
        //List<String> selection = startCountrySpinner.getSelectedStrings();
        for(String s:selection)
        {
            Tag tag = new Tag(s);
            tag.isDeletable = true;
            tagview_country.addTag(tag);
        }
        tagview_country.setOnTagDeleteListener(new OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag tag, int i) {
                for(int k=0;k<startCountryItems.length;k++)
                {
                    if(startCountryItems[k].contains(tag.text.toString()))
                    {
                        startCountrySpinner.mSelection[k] = false;
                    }
                }

            }
        });
    }

    @Override
    public void responseDivision(ArrayList<String> text) {

        TagView tagview_division = (TagView)findViewById(R.id.tagview_division);
        tagview_division.removeAllTags();
        List<String> selection = new ArrayList<String>(text);
        //selection.add("check1");
        //List<String> selection = startCountrySpinner.getSelectedStrings();
        for(String s:selection)
        {
            Tag tag = new Tag(s);
            tag.isDeletable = true;
            tagview_division.addTag(tag);
        }
        tagview_division.setOnTagDeleteListener(new OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag tag, int i) {
                for(int k=0;k<divisionItems.length;k++)
                {
                    if(divisionItems[k].contains(tag.text.toString()))
                    {
                        divisionSpinner.mSelection[k] = false;
                    }
                }

            }
        });
    }

    @Override
    public void responseAddItems(ArrayList<String> text) {

        TagView tagview_addition= (TagView)findViewById(R.id.tagview_addition);
        tagview_addition.removeAllTags();
        List<String> selection = new ArrayList<String>(text);
        //selection.add("check1");
        //List<String> selection = startCountrySpinner.getSelectedStrings();
        for(String s:selection)
        {
            Tag tag = new Tag(s);
            tag.isDeletable = true;
            tagview_addition.addTag(tag);
        }
        tagview_addition.setOnTagDeleteListener(new OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag tag, int i) {
                for (int k = 0; k < additionalItems.length; k++) {
                    if (additionalItems[k].contains(tag.text.toString())) {
                        additionalSpinner.mSelection[k] = false;
                    }
                }

            }
        });
    }

    @Override
    public void responseShowFields(ArrayList<String> text) {

        TagView tagview_showFields= (TagView)findViewById(R.id.tagview_showFields);
        tagview_showFields.removeAllTags();
        List<String> selection = new ArrayList<String>(text);
        //selection.add("check1");
        //List<String> selection = startCountrySpinner.getSelectedStrings();
        for(String s:selection)
        {
            Tag tag = new Tag(s);
            tag.isDeletable = true;
            tagview_showFields.addTag(tag);
        }
        tagview_showFields.setOnTagDeleteListener(new OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag tag, int i) {
                for(int k=0;k<showFieldsItems.length;k++)
                {
                    if(showFieldsItems[k].contains(tag.text.toString()))
                    {
                        showFieldsSpinner.mSelection[k] = false;
                    }
                }

            }
        });
    }

}
