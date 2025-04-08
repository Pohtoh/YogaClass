package com.example.coursework.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.R;
import com.example.coursework.YogaClassAdapter2;
import com.example.coursework.YogaClassData;
import com.example.coursework.ui.DataBaseHelper;
import com.example.coursework.ui.TimePicker;
import com.example.coursework.YogaClassScheduleData;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private YogaClassAdapter2 yogaClassAdapter2;
    private DataBaseHelper dbHelper;
    private ArrayList<YogaClassScheduleData> yogaScheduleList = new ArrayList<>();
    private ArrayList<YogaClassData> yogaClassList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewYogaClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DataBaseHelper(view.getContext());

        yogaScheduleList = dbHelper.getAllYogaSchedules();
        yogaClassList = dbHelper.getAllYogaClasses();

        yogaClassAdapter2 = new YogaClassAdapter2(getContext(), yogaScheduleList, this, dbHelper);
        recyclerView.setAdapter(yogaClassAdapter2);

        return view;
    }

    public void EditYogaClass(final boolean isUpdated, final YogaClassScheduleData scheduleData) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.item_update_yoga_schedule, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setView(view);

        final EditText editTextDate = view.findViewById(R.id.editTextDate);
        final EditText editTextTeacher = view.findViewById(R.id.autoCompleteTextViewTeacher);
        final EditText editTextDescription = view.findViewById(R.id.editTextTextMultiLine);
        final AutoCompleteTextView classDropdown = view.findViewById(R.id.autoCompleteTextViewClassType);

        TimePicker.GetDateLayout(view);

        // Setup dropdown for class type selection
        ArrayList<String> classTypeList = new ArrayList<>();
        for (YogaClassData yogaClass : yogaClassList) {
            classTypeList.add(yogaClass.getClassType());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, classTypeList);
        classDropdown.setAdapter(adapter);

        if (isUpdated && scheduleData != null) {
            editTextDate.setText(scheduleData.getDate());
            editTextTeacher.setText(scheduleData.getTeacher());
            editTextDescription.setText(scheduleData.getDescription());

            YogaClassData linkedClass = dbHelper.getYogaClassById(scheduleData.getYogaClassID());
            if (linkedClass != null) {
                classDropdown.setText(linkedClass.getClassType(), false);
            }
        }

        dialogBuilder.setCancelable(true)
                .setPositiveButton(isUpdated ? "Update" : "Save", (dialog, which) -> {
                    if (validateInputs(editTextDate, editTextTeacher, classDropdown)) {

                        String selectedClassType = classDropdown.getText().toString();
                        int selectedClassId = -1;
                        for (YogaClassData yogaClass : yogaClassList) {
                            if (yogaClass.getClassType().equals(selectedClassType)) {
                                selectedClassId = yogaClass.getId();
                                break;
                            }
                        }

                        if (selectedClassId == -1) {
                            Toast.makeText(getContext(), "Invalid class type selected", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        scheduleData.setDate(editTextDate.getText().toString());
                        scheduleData.setTeacher(editTextTeacher.getText().toString());
                        scheduleData.setDescription(editTextDescription.getText().toString());
                        scheduleData.setYogaClassID(selectedClassId);

                        dbHelper.updateYogaSchedule(scheduleData);

                        int updatedPosition = yogaScheduleList.indexOf(scheduleData);
                        if (updatedPosition != -1) {
                            yogaScheduleList.set(updatedPosition, scheduleData);
                            yogaClassAdapter2.notifyItemChanged(updatedPosition);
                        }
                    }
                })
                .setNegativeButton(isUpdated ? "Delete" : "Cancel", (dialog, which) -> {
                    if (isUpdated) {
                        dbHelper.deleteYogaSchedule(scheduleData);
                        int deletedPosition = yogaScheduleList.indexOf(scheduleData);
                        if (deletedPosition != -1) {
                            yogaScheduleList.remove(deletedPosition);
                            yogaClassAdapter2.notifyItemRemoved(deletedPosition);
                        }
                    } else {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private boolean validateInputs(EditText... inputs) {
        for (EditText input : inputs) {
            if (TextUtils.isEmpty(input.getText().toString())) {
                input.setError("Required");
                return false;
            }
        }
        return true;
    }
}