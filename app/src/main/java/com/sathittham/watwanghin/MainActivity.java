package com.sathittham.watwanghin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class MainActivity extends Activity {

    private ListView myListView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Array of Menu
        String[] menuList = { "ข่าวสาร", "คลิปวีดีโอ", "คลิปเสียง", "หนังสือ", "ปฎิทินธรรม" };

        // Array of integers points to Menu's image
        int[] menuImages = {R.drawable.ic_news,R.drawable.ic_vdo,R.drawable.ic_sound,R.drawable.ic_ebook,R.drawable.ic_calendar,};

        // View Matching
        myListView = (ListView) findViewById(R.id.listview);

        context = this;

        myListView = (ListView) findViewById(R.id.listview);
        myListView.setAdapter(new CustomListView(this, menuList, menuImages));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
