package com.development.covidstats;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final String DATE_PATTERN = "dd.MM.yyyy";

    private String selectedDateText;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedDateText = "";

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);

        Button dateButton = findViewById(R.id.date_button);
        dateButton.setText(formatter.format(new Date()));
        dateButton.setOnClickListener(view -> {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
            dateButton.setText(selectedDateText);
        });

    }

    public void showSelectedDate(String date) {
        Toast.makeText(MainActivity.this, date, Toast.LENGTH_SHORT).show();
    }
}