package com.example.coursework.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.coursework.R;
import com.example.coursework.YogaClassData;
import com.example.coursework.YogaClassScheduleData;
import com.example.coursework.ui.DataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardFragment extends Fragment {
    private DataBaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.adding_data, container, false);
        dbHelper = new DataBaseHelper(requireContext());

        Button buttonToYogaClassAdd = rootView.findViewById(R.id.buttonToYogaClassAdd);
        Button buttonToScheduleAdd = rootView.findViewById(R.id.buttonToScheduleAdd);
        Button buttonSynchData = rootView.findViewById(R.id.buttonSynchData);

        buttonToYogaClassAdd.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(DashboardFragment.this);
            navController.navigate(R.id.navigation_yoga_course_add);
        });
        buttonToScheduleAdd.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(DashboardFragment.this);
            navController.navigate(R.id.navigation_schedule_add);
        });

        buttonSynchData.setOnClickListener(v -> {
            try {
                ArrayList<YogaClassScheduleData> scheduleList = dbHelper.getAllYogaSchedules();
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                for (YogaClassScheduleData schedule : scheduleList) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", schedule.getId());
                    data.put("date", schedule.getDate());
                    data.put("teacher", schedule.getTeacher());
                    data.put("yogaClassID", schedule.getYogaClassID());
                    data.put("description", schedule.getDescription());

                    firestore.collection("YogaSchedules")
                            .document(String.valueOf(schedule.getId())) // or .add(data) for auto-ID
                            .set(data)
                            .addOnSuccessListener(aVoid -> Log.d("Firebase", "Uploaded: " + schedule.getId()))
                            .addOnFailureListener(e -> Log.e("Firebase", "Failed: ", e));
                }
                ArrayList<YogaClassData> classList = dbHelper.getAllYogaClasses();
                for (YogaClassData yogaClass : classList) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", yogaClass.getId());
                    data.put("classType", yogaClass.getClassType());
                    data.put("day", yogaClass.getDay());
                    data.put("time", yogaClass.getTime());
                    data.put("duration", yogaClass.getDuration());
                    data.put("numberOfPeople", yogaClass.getNumberOfPeople());
                    data.put("price", yogaClass.getPrice());
                    data.put("description", yogaClass.getDescription());

                    firestore.collection("YogaClasses")
                            .document(String.valueOf(yogaClass.getId()))
                            .set(data);
                }
                Toast.makeText(getContext(), "Synchronized data completed.", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.e("Firebase", "Failed: ", e);
            }
        });


        return rootView;
    }
}