package com.kingdew.internconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kingdew.internconnect.R;
import com.kingdew.internconnect.models.Job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    Context context;
    ArrayList<Job> jobList;

    public JobAdapter(Context contect, ArrayList<Job> jobList) {
        this.context = contect;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v= inflater.inflate(R.layout.job_item,parent,false);
        return new JobViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job= jobList.get(position);
        holder.itemCompanyName.setText(job.getCompName()+" - "+job.getCompLocation());
        holder.itemJobTitle.setText(job.getTitle());
        holder.itemJobSalary.setText("LKR:"+ (job.getSalary() == -1 ?"-" : job.getSalary() +"/mo"));
        holder.itemJobDueDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(job.getDueDate()));

        holder.itemJobApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!job.getApplyLink().startsWith("http://") && !job.getApplyLink().startsWith("https://"))
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+job.getApplyLink())));

                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(job.getApplyLink())));


            }
        });

    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder{

        TextView itemCompanyName,itemJobTitle,itemJobSalary,itemJobDueDate;
        Button itemJobApply;
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCompanyName=itemView.findViewById(R.id.item_comp_name);
            itemJobTitle=itemView.findViewById(R.id.item_job_title);
            itemJobSalary=itemView.findViewById(R.id.item_job_salary);
            itemJobDueDate=itemView.findViewById(R.id.item_job_date);
            itemJobApply=itemView.findViewById(R.id.item_job_apply);

        }
    }

    public void filterList(ArrayList<Job> filteredList) {
        this.jobList = filteredList;
        notifyDataSetChanged();
    }
}
