package com.example.coursework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.ui.yogaCourse.YogaCourse;

import java.util.ArrayList;
import java.util.List;

public class YogaClassAdapter extends RecyclerView.Adapter<YogaClassAdapter.YogaClassViewHolder> {

    private List<YogaClassData> yogaClassList;
    private Context context;
    private YogaCourse yogaCourse;
    public YogaClassAdapter(Context context, ArrayList<YogaClassData> classList, YogaCourse yogaCourse) {
        this.context = context;
        this.yogaClassList = classList;
        this.yogaCourse = yogaCourse;
    }

    @NonNull
    @Override
    public YogaClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_yoga_class, parent, false);
        return new YogaClassViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull YogaClassViewHolder holder, int position) {
        //final YogaClassData yogaClassData = yogaClassList.get(position);
        YogaClassData yogaClass = yogaClassList.get(position);

        holder.classType.setText(yogaClass.getClassType());
        String details = "ID: "+yogaClass.getId()
                + " | Day of the Week: " + yogaClass.getDay()
                + "\nTimeStart: " + yogaClass.getTime()
                + " | Duration: " + yogaClass.getDuration()
                + " | People: " + yogaClass.getNumberOfPeople()
                + " | Price: " + yogaClass.getPrice() + " £"
                + "\nNote: " + yogaClass.getDescription();
        holder.details.setText(details);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(yogaCourse != null){
                    yogaCourse.EditYogaClass(true, yogaClass);
                }
            }
    });
        }

    @Override
    public int getItemCount() {
        return yogaClassList.size();
    }

    public static class YogaClassViewHolder extends RecyclerView.ViewHolder {
        TextView classType;
        TextView details;

        public YogaClassViewHolder(@NonNull View itemView) {
            super(itemView);
            classType = itemView.findViewById(R.id.textViewClassType);
            details = itemView.findViewById(R.id.textViewDetails);
        }
    }
}