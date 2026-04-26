package com.kingdew.internconnect.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kingdew.internconnect.R;
import com.kingdew.internconnect.api.RetrofitClient;
import com.kingdew.internconnect.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    TextView profNameField,profEmailField;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressBar=findViewById(R.id.loader);

        profNameField= findViewById(R.id.prof_name);
        profEmailField=findViewById(R.id.prof_email);

        CardView postedJobBtn=findViewById(R.id.prof_postedjobs_btn);
        Button logOutBtn=findViewById(R.id.logout_btn);

        SharedPreferences sp= getSharedPreferences("UserSession",MODE_PRIVATE);
        String email=sp.getString("userEmail","");

        getProfileData(email);


        postedJobBtn.setOnClickListener(view->{
            startActivity(new Intent(ProfileActivity.this,PostedJobsActivity.class));
        });
        logOutBtn.setOnClickListener(view->{
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        logOutBtn.setEnabled(false);
                        logOutBtn.setText("Logging out");
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();

        });

    }

    private void getProfileData(String email) {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getApiService().searchUser(email).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful() && response.body() != null){
                    User user=response.body().get(0);
                    profEmailField.setText(user.getEmail());
                    profNameField.setText(user.getName());
                }else {
                    Toast.makeText(ProfileActivity.this, "Server error:"+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ProfileActivity.this, "Something went wrong,please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}