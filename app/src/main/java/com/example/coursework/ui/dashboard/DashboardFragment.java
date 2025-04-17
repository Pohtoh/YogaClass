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
import java.util.List;
import java.util.Map;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
        Button buttonUploadData = rootView.findViewById(R.id.buttonUploadData);
        Button buttonRetrieveData = rootView.findViewById(R.id.buttonRetrieveData);

        buttonToYogaClassAdd.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(DashboardFragment.this);
            navController.navigate(R.id.navigation_yoga_course_add);
        });
        buttonToScheduleAdd.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(DashboardFragment.this);
            navController.navigate(R.id.navigation_schedule_add);
        });

        buttonUploadData.setOnClickListener(v -> {
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
                            .document(String.valueOf(schedule.getId()))
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
                Toast.makeText(getContext(), "Synchronized data failed.", Toast.LENGTH_SHORT).show();
                Log.e("Firebase", "Failed: ", e);
            }
        });

        buttonRetrieveData.setOnClickListener(v ->{
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("YogaSchedules")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<YogaClassScheduleData> scheduleList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            Long idLong = doc.getLong("id");
                            int id = (idLong != null) ? idLong.intValue() : 0;

                            String date = doc.getString("date");
                            String teacher = doc.getString("teacher");
                            String description = doc.getString("description");

                            Long classIdLong = doc.getLong("yogaClassID");
                            int yogaClassID = (classIdLong != null) ? classIdLong.intValue() : 0;

                            YogaClassScheduleData data = new YogaClassScheduleData(id, yogaClassID, teacher, date, description);
                            dbHelper.insertYogaClassSchedule2(id, date, teacher, yogaClassID, description);
                        }
                        Toast.makeText(getContext(), "Retrieve data completed.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->{
                        Log.e("Firebase", "Error getting document", e);
                    });
            firestore.collection("YogaClasses")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<YogaClassData> classList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Long idLong = doc.getLong("id");
                            int id = (idLong != null) ? idLong.intValue() : 0;
                            String classType = doc.getString("classType");
                            String day = doc.getString("day");
                            String time = doc.getString("time");
                            Long durationLong = doc.getLong("duration");
                            int duration = (durationLong != null) ? durationLong.intValue() : 0;
                            Long numberOfPeopleLong = doc.getLong("numberOfPeople");
                            int numberOfPeople = (numberOfPeopleLong != null) ? numberOfPeopleLong.intValue() : 0;
                            Long priceLong = doc.getLong("price");
                            int price = (priceLong != null) ? priceLong.intValue() : 0;
                            String description = doc.getString("description");

                            dbHelper.insertYogaClass2(id, day, time, duration, numberOfPeople, price, classType, description);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Retrieve data failed.", Toast.LENGTH_SHORT).show();
                        Log.e("Firebase", "Error getting documents", e);
                    });
        });
        return rootView;
    }
}