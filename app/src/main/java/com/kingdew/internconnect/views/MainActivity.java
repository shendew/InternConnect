package com.kingdew.internconnect.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kingdew.internconnect.R;
import com.kingdew.internconnect.adapters.JobAdapter;
import com.kingdew.internconnect.api.RetrofitClient;
import com.kingdew.internconnect.models.Job;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    RecyclerView jobView;
    ArrayList<Job> jobArrayList;
    JobAdapter adapter;
    private Handler searchHandler = new Handler();
    private Runnable searchRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        ImageView profileImage=findViewById(R.id.prof_img);
        jobView =findViewById(R.id.job_rview);
        FloatingActionButton addJobBtn= findViewById(R.id.addJobBtn);
        TextInputEditText searchTextField = findViewById(R.id.search_input);



        jobView.setLayoutManager(new LinearLayoutManager(this));
        jobView.setHasFixedSize(true);
        jobArrayList=new ArrayList<>();
        adapter=new JobAdapter(this,jobArrayList);
        jobView.setAdapter(adapter);


        loadAllJobs();

        searchTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                final String query = s.toString().trim();

                searchRunnable = ()-> {
                    if (!query.isEmpty()){
                        performSearch(query);
                    }else{
                        loadAllJobs();
                    }
                };
                searchHandler.postDelayed(searchRunnable,500);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
            }
        });


        profileImage.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });

        addJobBtn.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, AddJobActivity.class));
        });

    }

    private void performSearch(String query){
        RetrofitClient.getApiService().searchJobs(query).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                if (response.isSuccessful() && response.body() != null){
                    jobArrayList.clear();
                    jobArrayList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else if (response.body() == null){
                    jobArrayList.clear();
                    adapter.notifyDataSetChanged();

                    Toast.makeText(MainActivity.this, "No jobs found", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Server error :"+response.code(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong, please try again."+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    };

    protected void loadAllJobs(){
        RetrofitClient.getApiService().getAllJobs().enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                if (response.isSuccessful() && response.body() != null){
                    jobArrayList.clear();
                    jobArrayList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(MainActivity.this, "Server error :"+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong, please try again."+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (searchHandler != null && searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
    }
}