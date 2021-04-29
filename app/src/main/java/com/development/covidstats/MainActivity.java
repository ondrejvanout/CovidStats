package com.development.covidstats;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.VoiceInteractor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final String DATE_PATTERN = "dd.MM.yyyy";
    private String selectedDateText;

    // Components
    private TextView count_text_view;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedDateText = "";
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);

        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DAY_OF_MONTH, -1);
        Date selectedDate = currentDate.getTime();

        setInitialValues(selectedDate);

        Button dateButton = findViewById(R.id.date_button);
        dateButton.setText(formatter.format(selectedDate));
        dateButton.setOnClickListener(view -> {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
            dateButton.setText(selectedDateText);
        });
    }

    private void setInitialValues(Date selectedDate) {
        TextView casesView = findViewById(R.id.cases_text_view);
        TextView testsView = findViewById(R.id.tests_text_view);
        TextView deathView = findViewById(R.id.death_text_view);
        TextView hospitalizationView = findViewById(R.id.hospitalization_text_view);

        SimpleDateFormat jsonDateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        CovidDataService service = new CovidDataService(MainActivity.this, selectedDate);
        service.getCasesCount(jsonDateFormatter.format(selectedDate), new CovidDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                if (response.equals("Data nebyla ještě zveřejněna."))
                    casesView.setTextSize(20);
                else
                    casesView.setTextSize(50);
                casesView.setText(response);
            }
        });

        service.getTestsCount(jsonDateFormatter.format(selectedDate), new CovidDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                if (response.equals("Data nebyla ještě zveřejněna."))
                    testsView.setTextSize(20);
                else
                    testsView.setTextSize(50);
                testsView.setText(response);
            }
        });

        service.getDeathCount(jsonDateFormatter.format(selectedDate), new CovidDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                if (response.equals("Data nebyla ještě zveřejněna."))
                    deathView.setTextSize(20);
                else
                    deathView.setTextSize(50);
                deathView.setText(response);
            }
        });

        service.getHospitalizationCount(jsonDateFormatter.format(selectedDate), new CovidDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                if (response.equals("Data nebyla ještě zveřejněna."))
                    hospitalizationView.setTextSize(20);
                else
                    hospitalizationView.setTextSize(50);
                hospitalizationView.setText(response);
            }
        });
    }

}