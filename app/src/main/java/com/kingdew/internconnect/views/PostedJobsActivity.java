package com.kingdew.internconnect.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kingdew.internconnect.R;
import com.kingdew.internconnect.adapters.PostedJobAdapter;
import com.kingdew.internconnect.api.RetrofitClient;
import com.kingdew.internconnect.models.Job;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostedJobsActivity extends AppCompatActivity {

    private RecyclerView ownerRecView;
    private PostedJobAdapter adapter;
    private ArrayList<Job> jobArrayList;
    private String email;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_posted_jobs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sp= getSharedPreferences("UserSession",MODE_PRIVATE);
        email=sp.getString("userEmail","");

        initViews();
//        setup recyclerview
        ownerRecView.setHasFixedSize(true);
        ownerRecView.setLayoutManager(new LinearLayoutManager(this));
        jobArrayList=new ArrayList<>();
        adapter=new PostedJobAdapter(this,jobArrayList);
        ownerRecView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    private void getPostedJobs(String email) {
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getApiService().getPostedJobs(email).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                progressBar.setVisibility(View.INVISIBLE);

                if (response.isSuccessful() && response.body() != null){
                    jobArrayList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }else if(response.body() ==null){
                    jobArrayList.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(PostedJobsActivity.this, "No posted jobs", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(PostedJobsActivity.this, "Server error: "+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(PostedJobsActivity.this, "Something went wrong,please try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        jobArrayList.clear();
        getPostedJobs(email);
    }

    private void initViews(){
        progressBar=findViewById(R.id.loader);
        ownerRecView=findViewById(R.id.posted_rec_view);
    }

}