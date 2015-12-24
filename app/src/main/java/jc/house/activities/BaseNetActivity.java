package jc.house.activities;

import android.os.Bundle;
import android.app.Activity;

import jc.house.R;

public class BaseNetActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_net);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
