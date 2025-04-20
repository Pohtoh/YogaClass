package com.example.coursework.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.coursework.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TimePicker {
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
    public interface DatePickerCallback {
        void onDatePicked(String formattedDate, String dayOfWeek);
    }
    public static void showDatePicker(@NonNull View view, DatePickerCallback callback) {
        EditText editTextDate = view.findViewById(R.id.editTextDate);
        editTextDate.setInputType(InputType.TYPE_NULL);
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
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);
                        String dayOfWeek =  selectedDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                        assert dayOfWeek != null;
                            String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear + " " + selectedDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) ;
                            editTextDate.setText(formattedDate);
                            callback.onDatePicked(formattedDate, dayOfWeek);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
    }
}
