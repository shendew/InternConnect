package com.kingdew.internconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.kingdew.internconnect.R;
import com.kingdew.internconnect.models.Job;
import com.kingdew.internconnect.views.JobViewActivity;
import com.kingdew.internconnect.views.UpdateJobActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private Context context;
    private ArrayList<Job> jobList;

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

        if (job.isPaid()){
            holder.itemJobPaid.setText("Paid");
        }else{
            holder.itemJobPaid.setText("Non-Paid");
        }

        if (job.isType()){
            holder.itemJobType.setText("Full-time");
        }else{
            holder.itemJobType.setText("Part-time");
        }

        if (job.getWorkType()==0){
            holder.itemWorkType.setText("On-Site");
        } else if (job.getWorkType()==1) {
            holder.itemWorkType.setText("Remote");
        }else if(job.getWorkType()==2){
            holder.itemWorkType.setText("Hybrid");
        }

        holder.itemJobApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!job.getApplyLink().startsWith("http://") && !job.getApplyLink().startsWith("https://"))
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+job.getApplyLink())));

            }
        });

        holder.itemCard.setOnClickListener(v->{
            Intent intent = new Intent(context, JobViewActivity.class);
            intent.putExtra("JOB",job);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder{

        TextView itemCompanyName,itemJobTitle,itemJobSalary,itemJobDueDate,itemJobType,itemJobPaid,itemWorkType;
        Button itemJobApply;
        MaterialCardView itemCard;
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCompanyName=itemView.findViewById(R.id.item_comp_name);
            itemJobTitle=itemView.findViewById(R.id.item_job_title);
            itemJobSalary=itemView.findViewById(R.id.item_job_salary);
            itemJobDueDate=itemView.findViewById(R.id.item_job_date);
            itemJobApply=itemView.findViewById(R.id.item_job_apply);
            itemJobType=itemView.findViewById(R.id.item_job_type);
            itemJobPaid=itemView.findViewById(R.id.item_job_paid);
            itemWorkType=itemView.findViewById(R.id.item_work_type);
            itemCard=itemView.findViewById(R.id.item_card);
        }
    }

}
