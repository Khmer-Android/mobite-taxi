package com.angkorcab.service_hotel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.angkorcab.taxi.R;

public class FormActivit extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        SaveHotelTask saveHotelTask = new SaveHotelTask();

        saveHotelTask.execute();


    }
}
