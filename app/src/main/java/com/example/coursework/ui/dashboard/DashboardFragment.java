package com.example.coursework.ui.dashboard;

import static com.example.coursework.ui.TimePicker.GetDayOfTheWeek;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.coursework.R;
import com.example.coursework.ui.DataBaseHelper;

import java.util.Calendar;


public class DashboardFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.adding_data, container, false);

        Button buttonToYogaClassAdd = rootView.findViewById(R.id.buttonToYogaClassAdd);
        Button buttonToScheduleAdd = rootView.findViewById(R.id.buttonToScheduleAdd);

        buttonToYogaClassAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(DashboardFragment.this);
                navController.navigate(R.id.navigation_yoga_course_add);
            }
        });
        buttonToScheduleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(DashboardFragment.this);
                navController.navigate(R.id.navigation_schedule_add);
            }
        });


        return rootView;
    }
}