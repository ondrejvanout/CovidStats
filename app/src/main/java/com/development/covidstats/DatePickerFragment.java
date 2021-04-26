package com.development.covidstats;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
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

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String selectedDateTxt;

        if (month + 1 < 10)
            selectedDateTxt = day + ".0" + (month + 1) + "." + year;
        else
            selectedDateTxt = day + "." + (month + 1) + "." + year;

        //Date selectedDate;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date selectedDate = dateFormatter.parse(selectedDateTxt);

            Button selectedDateButton = getActivity().findViewById(R.id.date_button);
            selectedDateButton.setText(dateFormatter.format(selectedDate));

            ((MainActivity) getActivity()).showSelectedDate(dateFormatter.format(selectedDate));

        } catch (ParseException e) {
            Toast.makeText(getContext(), "Nastala chyba při výběru data", Toast.LENGTH_SHORT).show();
        }


    }
}