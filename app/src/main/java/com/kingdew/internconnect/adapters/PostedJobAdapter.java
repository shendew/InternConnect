package com.kingdew.internconnect.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kingdew.internconnect.R;
import com.kingdew.internconnect.api.RetrofitClient;
import com.kingdew.internconnect.models.Job;
import com.kingdew.internconnect.views.UpdateJobActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostedJobAdapter extends RecyclerView.Adapter<PostedJobAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Job> jobList;

    public PostedJobAdapter(Context context, ArrayList<Job> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.posted_job_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
        }else{
            holder.itemWorkType.setText("Hybrid");
        }


        holder.ownerJobCard.setOnClickListener(v->{
            Intent intent = new Intent(context, UpdateJobActivity.class);
            intent.putExtra("JOB",job);
            context.startActivity(intent);
        });

        holder.itemJobDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Delete Confirmation")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            deleteJob(job.getId(), holder.getAbsoluteAdapterPosition());
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemCompanyName,itemJobTitle,itemJobSalary,itemJobDueDate,itemJobType,itemJobPaid,itemWorkType;
        Button itemJobDelete;
        CardView ownerJobCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCompanyName=itemView.findViewById(R.id.item_comp_name);
            itemJobTitle=itemView.findViewById(R.id.item_job_title);
            itemJobSalary=itemView.findViewById(R.id.item_job_salary);
            itemJobDueDate=itemView.findViewById(R.id.item_job_date);
            itemJobDelete=itemView.findViewById(R.id.item_job_delete);
            ownerJobCard=itemView.findViewById(R.id.owner_job_card);
            itemJobType=itemView.findViewById(R.id.item_job_type);
            itemJobPaid=itemView.findViewById(R.id.item_job_paid);
            itemWorkType=itemView.findViewById(R.id.item_work_type);
        }
    }

    private void deleteJob(String id,int pos){
        RetrofitClient.getApiService().deleteJob(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    jobList.remove(pos);
                    notifyItemRemoved(pos);
                    Toast.makeText(context, "Job deleted Successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Job deleted failed: "+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Something went wrong,please try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
