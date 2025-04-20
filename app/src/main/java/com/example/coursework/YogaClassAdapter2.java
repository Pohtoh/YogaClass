package com.example.coursework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.ui.DataBaseHelper;
import com.example.coursework.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class YogaClassAdapter2 extends RecyclerView.Adapter<YogaClassAdapter2.YogaClassViewHolder> {

    private List<YogaClassScheduleData> yogaClassScheduleList;
    private final Context context;
    private final HomeFragment homeFragment;
    private final DataBaseHelper dbHelper;
    private final List<YogaClassScheduleData> fullList;
    private List<YogaClassScheduleData> filteredList;

    public YogaClassAdapter2(Context context, List<YogaClassScheduleData> list, HomeFragment homeFragment, DataBaseHelper dbHelper) {
        this.context = context;
        this.yogaClassScheduleList = list;
        this.fullList = new ArrayList<>(list);
        this.filteredList = new ArrayList<>(list);
        this.homeFragment = homeFragment;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public YogaClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_yoga_class, parent, false);
        return new YogaClassViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull YogaClassViewHolder holder, int position) {
        final YogaClassScheduleData scheduleData = filteredList.get(position);

        YogaClassData yogaClass = dbHelper.getYogaClassById(scheduleData.getYogaClassID());
        String classType = (yogaClass != null) ? yogaClass.getClassType() : "Unknown Class";
        String classStartAt = (yogaClass != null) ? yogaClass.getTime() : "Unknown Class";

        holder.classType.setText(classType);
        String details = "Class id" + scheduleData.getId()
                + "\nTeacher: " + scheduleData.getTeacher()
                + "\nDate: " + scheduleData.getDate()
                + "\nStart Time: " + classStartAt
                + "\nNote: " + scheduleData.getDescription();
        holder.details.setText(details);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeFragment != null){
                    homeFragment.EditYogaClass(true, scheduleData);
                }
            }
    });
        }
    public void filter(String query) {
        filteredList = new ArrayList<>();
        for (YogaClassScheduleData item : fullList) {
            boolean match1 = false;
            boolean match2 = false;
            if (item.getTeacher().toLowerCase().contains(query.toLowerCase()) ||
                    item.getDate().toLowerCase().contains(query.toLowerCase())
                     ) {
                match1 = true;
            }
            YogaClassData yogaClass = dbHelper.getYogaClassById(item.getYogaClassID());
            if(yogaClass != null && yogaClass.getClassType().toLowerCase().contains(query.toLowerCase())){
                match2 = true;
            }
            if (match1 || match2) {
                filteredList.add(item);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredList.size() ;
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
    public void removeItem(YogaClassScheduleData item) {
        int index = filteredList.indexOf(item);
        filteredList.remove(item);
        fullList.remove(item);

        if (index != -1) {
            notifyItemRemoved(index);
        } else {// ensure item actually removed
            notifyDataSetChanged();
        }
    }
}