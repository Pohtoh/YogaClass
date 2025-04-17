package com.example.coursework.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private YogaClassAdapter2 yogaClassAdapter2;
    private DataBaseHelper dbHelper;
    private ArrayList<YogaClassScheduleData> yogaScheduleList = new ArrayList<>();
    private ArrayList<YogaClassData> yogaClassList = new ArrayList<>();
    private String dateOfWeek;
    private String WDAY;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewYogaClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DataBaseHelper(view.getContext());

        yogaScheduleList = dbHelper.getAllYogaSchedules();
        yogaClassList = dbHelper.getAllYogaClasses();

        yogaClassAdapter2 = new YogaClassAdapter2(getContext(), yogaScheduleList, HomeFragment.this, dbHelper);
        recyclerView.setAdapter(yogaClassAdapter2);


        SearchView searchView = view.findViewById(R.id.searchView);
//filter
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                yogaClassAdapter2.filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                yogaClassAdapter2.filter(newText);
                return false;
            }
        });

        return view;
    }

    public void EditYogaClass(final boolean isUpdated, final YogaClassScheduleData scheduleData) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.item_update_yoga_schedule, null);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setView(view);

        final EditText editTextDate = view.findViewById(R.id.editTextDate);
        TimePicker.showDatePicker(view, new TimePicker.DatePickerCallback() {
            @Override
            public void onDatePicked(String formattedDate, String dayOfWeek) {
                editTextDate.setText(formattedDate);
                WDAY = dayOfWeek;
            }
        });
        final EditText editTextTeacher = view.findViewById(R.id.autoCompleteTextViewTeacher);
        final EditText editTextDescription = view.findViewById(R.id.editTextTextMultiLine);
        Spinner spinner = view.findViewById(R.id.spinner);

        //TimePicker.GetDateLayout(view);
        //drop down

        ArrayList<YogaClassData> yogaClassDataList = dbHelper.getAllYogaClasses();
        ArrayList<String> yogaClassNames = new ArrayList<>();
        ArrayAdapter<String> yogaClassAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, yogaClassNames);
        for (YogaClassData yogaClassData : yogaClassDataList) {
            yogaClassNames.add(yogaClassData.getClassType() + ": " + yogaClassData.getDay() + " " + yogaClassData.getPrice() + "£");
        }
        spinner.setAdapter(yogaClassAdapter);



        if (isUpdated && scheduleData != null) {
            editTextDate.setText(scheduleData.getDate());
            editTextTeacher.setText(scheduleData.getTeacher());
            editTextDescription.setText(scheduleData.getDescription());

            String classLabel = dbHelper.getYogaClassTypeById(scheduleData.getYogaClassID());
            int index = yogaClassNames.indexOf(classLabel);
            if (index >= 0) spinner.setSelection(index);
        }

        dialogBuilder.setCancelable(true)
                .setPositiveButton(isUpdated ? "Update" : "Save", (dialog, which) -> {
                    int selectedPosition = spinner.getSelectedItemPosition();
                    YogaClassData selectedYogaClass = yogaClassDataList.get(selectedPosition);
                    int yogaClassID = selectedYogaClass.getId();
                    dateOfWeek = dbHelper.getYogaClassTypeById(yogaClassID);

                    if (validateInputs(editTextDate, editTextTeacher)) {
                        if(dateOfWeek.contains(WDAY)) {
                            scheduleData.setDate(editTextDate.getText().toString());
                            scheduleData.setTeacher(editTextTeacher.getText().toString());
                            scheduleData.setDescription(editTextDescription.getText().toString());
                            scheduleData.setYogaClassID(yogaClassID);

                            dbHelper.updateYogaSchedule(scheduleData);
                            firestore.collection("YogaSchedules")
                                    .document(String.valueOf(scheduleData.getId()))
                                    .set(scheduleData);

                            int updatedPosition = yogaScheduleList.indexOf(scheduleData);
                            if (updatedPosition != -1) {
                                yogaScheduleList.set(updatedPosition, scheduleData);
                                yogaClassAdapter2.notifyItemChanged(updatedPosition);
                            }
                        } else{
                            Toast.makeText(getContext(), "This class is not available on this day", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(isUpdated ? "Delete" : "Cancel", (dialogInterface, i) -> {
                    if (isUpdated) {
                        dbHelper.deleteYogaSchedule(scheduleData);
                        firestore.collection("YogaSchedules")
                                .document(String.valueOf(scheduleData.getId()))
                                .delete();

                        int deletedPosition = yogaScheduleList.indexOf(scheduleData);
                        if (deletedPosition != -1) {
                            yogaScheduleList.remove(deletedPosition);
                            yogaClassAdapter2.removeItem(scheduleData);
                        }
                    } else {
                        dialogInterface.cancel();
                    }
                });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private boolean validateInputs(EditText date, EditText teacher) {
        if (date.getText().toString().isEmpty()) {
            date.setError("Date is required");
            return false;
        }
        if (teacher.getText().toString().isEmpty()) {
            teacher.setError("Teacher name is required");
            return false;
        }

        return true;
    }
}