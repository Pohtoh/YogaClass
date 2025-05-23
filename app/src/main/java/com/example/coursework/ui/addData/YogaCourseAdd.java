package com.example.coursework.ui.addData;

import static com.example.coursework.ui.TimePicker.GetDayOfTheWeek;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.coursework.R;
import com.example.coursework.ui.DataBaseHelper;


public class YogaCourseAdd extends Fragment {
    private DataBaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.yoga_course_add, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DataBaseHelper(getContext());
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setView(view);

        EditText editTextDate = view.findViewById(R.id.editTextDate);
        GetDayOfTheWeek(view);

        EditText editTextTimeStart = view.findViewById(R.id.editTextTimeStart);
        com.example.coursework.ui.TimePicker.GetTimeLayout(view);

        EditText editTextNumberOfPeople = view.findViewById(R.id.editTextNumberOfPeople);
        EditText editTextDuration = view.findViewById(R.id.editTextDuration);
        EditText editTextPrice = view.findViewById(R.id.editTextPrice);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        EditText editTextTextMultiLine = view.findViewById(R.id.editTextTextMultiLine);
        Button buttonAdd = view.findViewById(R.id.buttonAdd);

        String[] options = {"Hatha Yoga", "Pilates", "Restorative Yoga", "Vinyasa Yoga", "Kundalini Yoga"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, options);
        autoCompleteTextView.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (editTextDate.getText().toString().isEmpty() ||
                            editTextTimeStart.getText().toString().isEmpty() ||
                            editTextNumberOfPeople.getText().toString().isEmpty() ||
                            editTextDuration.getText().toString().isEmpty() ||
                            editTextPrice.getText().toString().isEmpty() ||
                            autoCompleteTextView.getText().toString().isEmpty()){
                        Toast.makeText(getContext(), "Please fill in all required fields!", Toast.LENGTH_SHORT).show();
                    }else {
                        String day = editTextDate.getText().toString();
                        String time = editTextTimeStart.getText().toString();
                        int duration = Integer.parseInt(editTextDuration.getText().toString());
                        int numberOfPeople = Integer.parseInt(editTextNumberOfPeople.getText().toString());
                        int price = Integer.parseInt(editTextPrice.getText().toString());
                        String classType = autoCompleteTextView.getText().toString();
                        String description = editTextTextMultiLine.getText().toString();

                        new AlertDialog.Builder(getContext())
                            .setTitle("Are you sure you want to add this Yoga Class?")
                            .setMessage("Day: "+ day + " " + time + " " + duration +
                                    "\nNumber of People: " + numberOfPeople + " " + price + " £"+
                                    "\n" + classType +
                                    "\nNote: " + description)
                            .setPositiveButton("Yes", (dialog, which) -> {
                                long newRowId = dbHelper.insertYogaClass(day, time, duration, numberOfPeople, price, classType, description);
                                if (newRowId != -1) {
                                    Toast.makeText(getContext(), "New Yoga Class Created!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Add Yoga Class Failed!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", ((dialog, which) -> {
                                dialog.dismiss();
                                    }))
                                .show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Invalid input! Please enter valid data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}