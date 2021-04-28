package com.development.covidstats;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Calendar minDate = Calendar.getInstance();
        minDate.set(2020, 3, 1);

        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DAY_OF_MONTH, -1);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog finalDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        finalDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        finalDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        return finalDialog;
        //return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String selectedDateTxt;

        if (month + 1 < 10)
            selectedDateTxt = day + ".0" + (month + 1) + "." + year;
        else
            selectedDateTxt = day + "." + (month + 1) + "." + year;

        //Date selectedDate;
        SimpleDateFormat dateButtonFormatter = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat jsonDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date selectedDate = dateButtonFormatter.parse(selectedDateTxt);

            Button selectedDateButton = getActivity().findViewById(R.id.date_button);
            selectedDateButton.setText(dateButtonFormatter.format(selectedDate));

            TextView casesView = getActivity().findViewById(R.id.cases_text_view);
            TextView testsView = getActivity().findViewById(R.id.tests_text_view);
            TextView deathView = getActivity().findViewById(R.id.death_text_view);
            TextView hospitalizationView = getActivity().findViewById(R.id.hospitalization_text_view);

            CovidDataService service = new CovidDataService(getActivity(), selectedDate);
            service.getCasesCount(jsonDateFormatter.format(selectedDate), new CovidDataService.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response) {
                    casesView.setText(response);
                }
            });

            service.getTestsCount(jsonDateFormatter.format(selectedDate), new CovidDataService.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response) {
                    testsView.setText(response);
                }
            });

            service.getDeathCount(jsonDateFormatter.format(selectedDate), new CovidDataService.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response) {
                    deathView.setText(response);
                }
            });

            service.getHospitalizationCount(jsonDateFormatter.format(selectedDate), new CovidDataService.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response) {
                    hospitalizationView.setText(response);
                }
            });

        } catch (ParseException e) {
            Toast.makeText(getContext(), "Nastala chyba při výběru data", Toast.LENGTH_SHORT).show();
        }


    }
}