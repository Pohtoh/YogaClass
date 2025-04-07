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
import com.example.coursework.R;
import com.example.coursework.ui.DataBaseHelper;

import java.util.Calendar;


public class DashboardFragment extends Fragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button myButton = findViewById(R.id.my_button);  // Assuming the button has this ID

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of the fragment
                MyFragment myFragment = new MyFragment();

                // Get the FragmentTransaction
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace the current fragment with the new fragment
                transaction.replace(R.id.fragment_container, myFragment);  // fragment_container is your container's ID
                transaction.addToBackStack(null);  // Optional: Add to back stack to handle navigation back
                transaction.commit();  // Commit the transaction
            }
        });
    }
}