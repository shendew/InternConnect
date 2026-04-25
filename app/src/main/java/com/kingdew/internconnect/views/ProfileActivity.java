package com.kingdew.internconnect.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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

        profNameField= findViewById(R.id.prof_name);
        profEmailField=findViewById(R.id.prof_email);

        CardView postedJobBtn=findViewById(R.id.prof_postedjobs_btn);
        Button logOutBtn=findViewById(R.id.logout_btn);

        SharedPreferences sp= getSharedPreferences("UserSession",MODE_PRIVATE);
        String email=sp.getString("email","");

        getProfileData(email);


        postedJobBtn.setOnClickListener(view->{
            startActivity(new Intent(ProfileActivity.this,PostedJobsActivity.class));
        });
        logOutBtn.setOnClickListener(view->{
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {

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
        RetrofitClient.getApiService().searchUser(email).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null){
                    User user=response.body().get(0);
                    profEmailField.setText(user.getEmail());
                    profNameField.setText(user.getName());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }
}