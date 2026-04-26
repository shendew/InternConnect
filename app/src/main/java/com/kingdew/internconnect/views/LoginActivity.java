package com.kingdew.internconnect.views;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kingdew.internconnect.R;
import com.kingdew.internconnect.api.RetrofitClient;
import com.kingdew.internconnect.models.User;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs=getSharedPreferences("UserSession",MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn",false);
        if (isLoggedIn){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView loginReg = findViewById(R.id.login_regbtn);
        loginBtn = findViewById(R.id.login_btn);
        TextInputEditText emailField =findViewById(R.id.login_email_input);
        TextInputLayout emailLay =findViewById(R.id.login_email_lay);
        TextInputEditText passField= findViewById(R.id.login_pass_input);
        TextInputLayout passLay =findViewById(R.id.login_pass_lay);

        loginBtn.setOnClickListener(v -> {
            String email=emailField.getText().toString().trim();
            String password=passField.getText().toString().trim();
            boolean isValid = true;

            if (email.isEmpty()){
                emailLay.setError("Email cannot be empty");
                isValid=false;
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailLay.setError("Invalid email format");
                isValid=false;
            }else{
                emailLay.setError(null);
            }
            if (password.isEmpty()){
                passLay.setError("Password cannot be empty");
                isValid=false;
            }else if(password.length()<8){
                passLay.setError("Password must be at least 8 character long");
                isValid=false;
            }else{
                emailLay.setError(null);
            }


            if (isValid){
                loginBtn.setEnabled(false);
                loginBtn.setText("Logging in...");

                RetrofitClient.getApiService().getAllUsers().enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful() && response.body() != null){
                            boolean found = false;
                            for (User user : response.body()){
                                if (user.getEmail().toLowerCase().equals(email.toLowerCase()) && BCrypt.checkpw(password,user.getPassword())){
                                    found = true;
                                    SharedPreferences sp=getSharedPreferences("UserSession",MODE_PRIVATE);
                                    SharedPreferences.Editor editor= sp.edit();
                                    editor.putBoolean("isLoggedIn",true);
                                    editor.putString("userEmail",email);
                                    editor.putString("userId",user.getId());
                                    editor.apply();

                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    finish();
                                }
                            }
                            if (!found){
                                loginBtn.setEnabled(true);
                                loginBtn.setText("Login");
                                Toast.makeText(LoginActivity.this, "Credentials are wrong!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show();
                        loginBtn.setEnabled(true);
                        loginBtn.setText("Login");
                        if (!call.isCanceled()) {
                            Toast.makeText(LoginActivity.this, "Network error. Check your connection.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
        loginReg.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        });


    }
}