package com.example.coursework.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coursework.R;

import java.util.ArrayList;
import java.util.Calendar;

public class TimePicker {
    private DataBaseHelper dbHelper;
    public static void GetDateLayout(@NonNull View view) {

    EditText editTextDate = view.findViewById(R.id.editTextDate);
        editTextDate.setInputType(InputType.TYPE_NULL); // Prevent keyboard
        editTextDate.setFocusable(false);
        editTextDate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                    // Format date as you like, e.g., DD/MM/YYYY
                    String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    editTextDate.setText(formattedDate);
                }
            }, year, month, day);

            datePickerDialog.show();
        }
    });
    }
    public static void GetTimeLayout(@NonNull View view) {

        EditText editTextTimeStart = view.findViewById(R.id.editTextTimeStart);
        editTextTimeStart.setInputType(InputType.TYPE_NULL);
        editTextTimeStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(calendar.HOUR_OF_DAY);
                int minute = calendar.get(calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker view, int selectedHour, int selectedMinute) {
                        String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        editTextTimeStart.setText(formattedTime);
                    }
                }, hour, minute, true);

                timePickerDialog.show();
            }
        });
    }
    public static void GetDayOfTheWeek(@NonNull View view){
        EditText editTextDay = view.findViewById(R.id.editTextDate);
        editTextDay.setInputType(InputType.TYPE_NULL); // Prevent keyboard
        editTextDay.setFocusable(false); // Optional

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        boolean[] selectedDays = new boolean[days.length];
        ArrayList<String> selectedList = new ArrayList<>();

        editTextDay.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Select Days");

            builder.setMultiChoiceItems(days, selectedDays, (dialog, which, isChecked) -> {
                if (isChecked) {
                    selectedList.add(days[which]);
                } else {
                    selectedList.remove(days[which]);
                }
            });

            builder.setPositiveButton("OK", (dialog, which) -> {
                String result = TextUtils.join(", ", selectedList);
                editTextDay.setText(result);
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.create().show();
        });
    }
    public static void showDatePicker(Context context) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Convert selected date to a Calendar object
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);

                    // Check if the selected date is Monday (DAY_OF_WEEK = 2 for Monday)
                    if (selectedDate.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                        // If it's Monday, proceed with the date
                        Toast.makeText(context, "Selected Monday: " + selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear, Toast.LENGTH_SHORT).show();
                    } else {
                        // If it's not Monday, show a message and reset the date picker
                        Toast.makeText(context, "Please select a Monday!", Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }
}
