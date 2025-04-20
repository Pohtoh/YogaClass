package com.example.coursework.ui.yogaCourse;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.R;
import com.example.coursework.YogaClassData;
import com.example.coursework.YogaClassScheduleData;
import com.example.coursework.ui.DataBaseHelper;
import com.example.coursework.YogaClassAdapter;
import com.example.coursework.ui.TimePicker;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class YogaCourse extends Fragment {
    private RecyclerView recyclerView;
    private YogaClassAdapter yogaClassAdapter;
    private DataBaseHelper dbHelper;
    private ArrayList<YogaClassData> yogaClassArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_yoga_course, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewYogaClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        dbHelper = new DataBaseHelper(view.getContext());

        yogaClassArrayList = dbHelper.getAllYogaClasses();
        yogaClassAdapter = new YogaClassAdapter(getContext(), yogaClassArrayList, YogaCourse.this);

        recyclerView.setAdapter(yogaClassAdapter);
        return view;
    }

    public void EditYogaClass(final boolean isUpdated, final YogaClassData yogaClass) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.item_update_yoga_class, null);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(getContext());
        alerDialogBuilder.setView(view);

        final EditText editTextDay = view.findViewById(R.id.editTextDate);
        TimePicker.GetDayOfTheWeek(view);

        final EditText editTextTimeStart = view.findViewById(R.id.editTextTimeStart);
        TimePicker.GetTimeLayout(view);

        final EditText editTextDuration = view.findViewById(R.id.editTextDuration);
        final EditText editTextNumberOfPeople = view.findViewById(R.id.editTextNumberOfPeople);
        final EditText editTextPrice = view.findViewById(R.id.editTextPrice);
        final EditText editTextClassType = view.findViewById(R.id.autoCompleteTextView);
        final EditText editTextDescription = view.findViewById(R.id.editTextTextMultiLine);


        if (isUpdated && yogaClass != null) {
            editTextDay.setText(String.valueOf(yogaClass.getDay()));
            editTextTimeStart.setText(String.valueOf(yogaClass.getTime()));
            editTextDuration.setText(String.valueOf(yogaClass.getDuration()));
            editTextNumberOfPeople.setText(String.valueOf(yogaClass.getNumberOfPeople()));
            editTextPrice.setText(String.valueOf(yogaClass.getPrice()));
            editTextClassType.setText(yogaClass.getClassType());
            editTextDescription.setText(yogaClass.getDescription());
        }
        new AlertDialog.Builder(getContext())
                .setTitle("Yoga Course")
                .setNegativeButton("view schedule Class", (dialog, which) -> {
                    viewAllClassSchedule(yogaClass);
                })
                .setPositiveButton("Edit Yoga Course", (dialog, which) -> {
                    alerDialogBuilder.setCancelable(true)
                            .setPositiveButton(isUpdated ? "Update" : "Save", (dialogInterface, i) -> {
                                if (validateInputs(editTextDay, editTextDuration, editTextNumberOfPeople, editTextPrice, editTextClassType)) {
                                    yogaClass.setDay(editTextDay.getText().toString());
                                    yogaClass.setTime(editTextTimeStart.getText().toString());
                                    yogaClass.setDuration(Integer.parseInt(editTextDuration.getText().toString()));
                                    yogaClass.setNumberOfPeople(Integer.parseInt(editTextNumberOfPeople.getText().toString()));
                                    yogaClass.setPrice(Integer.parseInt(editTextPrice.getText().toString()));
                                    yogaClass.setClassType(editTextClassType.getText().toString());
                                    yogaClass.setDescription(editTextDescription.getText().toString());

                                    dbHelper.updateYogaClass(yogaClass);

                                    firestore.collection("YogaClasses")
                                            .document(String.valueOf(yogaClass.getId()))
                                            .set(yogaClass);
                                    int updatedPosition = yogaClassArrayList.indexOf(yogaClass);
                                    if (updatedPosition != -1) {
                                        yogaClassArrayList.set(updatedPosition, yogaClass);
                                        yogaClassAdapter.notifyItemChanged(updatedPosition);
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Please fill in all required fields!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(isUpdated ? "Delete" : "Cancel", (dialogInterface, i) -> {
                                if (isUpdated) {
                                    dbHelper.deleteYogaClass(yogaClass);

                                    int deletePosition = yogaClassArrayList.indexOf(yogaClass);
                                    firestore.collection("YogaClasses")
                                            .document(String.valueOf(yogaClass.getId()))
                                            .delete();
                                    if (deletePosition != -1) {
                                        yogaClassArrayList.remove(deletePosition);
                                        yogaClassAdapter.notifyItemRemoved(deletePosition);
                                    }
                                } else {
                                    dialogInterface.cancel();
                                }
                            });

                    final AlertDialog alertDialog = alerDialogBuilder.create();
                    alertDialog.show();
                })
                .show();
    }

    private void viewAllClassSchedule(YogaClassData yogaClassData) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.popup_schedule_list, null);
        LinearLayout scheduleListContainer = view.findViewById(R.id.scheduleListContainer);

        ArrayList<YogaClassScheduleData> allSchedules = dbHelper.getAllYogaSchedules();
        for (YogaClassScheduleData schedule : allSchedules) {
            if (schedule.getYogaClassID() == yogaClassData.getId()) {
                TextView scheduleItem = new TextView(getContext());
                scheduleItem.setText(
                        String.format("Date: %s\nTeacher: %s\nDescription: %s", schedule.getDate(), schedule.getTeacher(), schedule.getDescription())
                );
                scheduleListContainer.addView(scheduleItem);
            }
        }
        if (scheduleListContainer.getChildCount() == 0) {
            TextView noSchedules = new TextView(getContext());
            noSchedules.setText("No schedules found");
            scheduleListContainer.addView(noSchedules);
        }
        new AlertDialog.Builder(getContext())
                .setTitle("Schedules")
                .setView(view)
                .setPositiveButton("OK", null)
                .show();

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