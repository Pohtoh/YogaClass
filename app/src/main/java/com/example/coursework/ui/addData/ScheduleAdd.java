package com.example.coursework.ui.addData;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coursework.R;
import com.example.coursework.YogaClassData;
import com.example.coursework.ui.DataBaseHelper;
import com.example.coursework.ui.TimePicker;

import java.util.ArrayList;

public class ScheduleAdd extends Fragment {

    private DataBaseHelper dbHelper;
    private String dateOfWeek;
    private String WDAY;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.yoga_course_schedule_add, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DataBaseHelper(getContext());

        EditText editTextDate = view.findViewById(R.id.editTextDate);
        //showDatePicker(view);
        TimePicker.showDatePicker(view, new TimePicker.DatePickerCallback() {
            @Override
            public void onDatePicked(String formattedDate, String dayOfWeek) {
                editTextDate.setText(formattedDate);
                WDAY = dayOfWeek;
            }
        });

        AutoCompleteTextView autoCompleteTextViewTeacher = view.findViewById(R.id.autoCompleteTextViewTeacher);
        String[] teacherOptions = {"Miss A", "Miss B", "Miss C", "Miss D", "Miss E"};
        ArrayAdapter<String> teacherAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, teacherOptions);
        autoCompleteTextViewTeacher.setAdapter(teacherAdapter);

        EditText editTextTextMultiLine = view.findViewById(R.id.editTextTextMultiLine);
        Button buttonAdd = view.findViewById(R.id.buttonAdd);

        ArrayList<YogaClassData> yogaClassDataList = dbHelper.getAllYogaClasses();
        ArrayList<String> yogaClassNames = new ArrayList<>();
        for (YogaClassData yogaClassData : yogaClassDataList) {
            yogaClassNames.add(yogaClassData.getClassType() + ": " + yogaClassData.getDay() + " " + yogaClassData.getPrice() + "£");
        }
        ArrayAdapter<String> yogaClassAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, yogaClassNames);
        Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(yogaClassAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int selectedPosition = spinner.getSelectedItemPosition();
                    YogaClassData selectedYogaClass = yogaClassDataList.get(selectedPosition);
                    int yogaClassID = selectedYogaClass.getId();
                    dateOfWeek = dbHelper.getYogaClassTypeById(yogaClassID);

                    if (editTextDate.getText().toString().isEmpty() ||
                            autoCompleteTextViewTeacher.getText().toString().isEmpty()
                            ){
                        Toast.makeText(getContext(), "Please fill in all required fields!", Toast.LENGTH_LONG).show();
                    }else {
                        String day = editTextDate.getText().toString();
                        String teacher = autoCompleteTextViewTeacher.getText().toString();
                        String description = editTextTextMultiLine.getText().toString();
                        new AlertDialog.Builder(getContext())
                                .setTitle("Are you sure you want to add this Schedule on this day?")
                                .setMessage("Day: "+ day +
                                        "\nTeacher: " + teacher +
                                        "\nNote: " + description)
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    if(dateOfWeek.contains(WDAY)) {
                                        long newRowId = dbHelper.insertYogaClassSchedule(day, teacher, yogaClassID, description);
                                        if (newRowId != -1) {
                                            Toast.makeText(getContext(), "Added new Class to schedule", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Failed to add new Class to schedule", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(getContext(), "This class is not available on this day", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .setNegativeButton("No", ((dialog, which) -> {
                                    dialog.dismiss();
                                }))
                                .show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Invalid input! Please enter valid data.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(),e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
