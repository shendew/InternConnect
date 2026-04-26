package com.kingdew.internconnect.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
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

import java.security.MessageDigest;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private Button regBtn;
    private TextView regLogin;
    private TextInputEditText emailField,passField,repassField,nameField;
    private TextInputLayout emailLay,passLay,repassLay,nameLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        initListeners();
    }

    private void initViews(){
        regLogin = findViewById(R.id.reg_loginbtn);
        regBtn = findViewById(R.id.reg_btn);
        emailField =findViewById(R.id.reg_email_input);
        emailLay =findViewById(R.id.reg_email_lay);
        passField= findViewById(R.id.reg_pass_input);
        passLay =findViewById(R.id.reg_pass_lay);
        repassField=findViewById(R.id.reg_repass_input);
        repassLay=findViewById(R.id.reg_repass_lay);
        nameField=findViewById(R.id.reg_name_input);
        nameLay=findViewById(R.id.reg_name_lay);
    }
    private void registerUser(String name,String email,String password){
        String hashedPass= BCrypt.hashpw(password,BCrypt.gensalt());
        User newUser=new User(name,email,hashedPass);

        Call<User> call =RetrofitClient.getApiService().registerUser(newUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    SharedPreferences sp=getSharedPreferences("UserSession",MODE_PRIVATE);
                    SharedPreferences.Editor editor= sp.edit();
                    editor.putBoolean("isLoggedIn",true);
                    editor.putString("userEmail",email);
                    assert response.body() != null;
                    editor.putString("userId",response.body().getId());

                    editor.apply();

                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this, "Registration failed.please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Something went wrong,please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Boolean validateInputs(String name,String email,String password,String repassword){
        boolean isValid = true;

        if (name.isEmpty()){
            nameLay.setError("Name cannot be empty");
            isValid=false;
        }
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
        }else if(!repassword.equals(password)){
            repassLay.setError("Re-Password is not matching");
            isValid=false;
        }else if(password.length()<8){
            passLay.setError("Password must be at least 8 character long");
            isValid=false;
        }else{
            emailLay.setError(null);
        }
        return isValid;
    }

    private void initListeners(){
        regBtn.setOnClickListener(v -> {
            String name=nameField.getText().toString().trim();
            String email=emailField.getText().toString().trim();
            String password=passField.getText().toString().trim();
            String repassword=repassField.getText().toString().trim();

            if (validateInputs(name,email,password,repassword)){
                regBtn.setEnabled(false);
                regBtn.setText("Sign in...");
                checkUserAvailability(name,email,password);
            }

        });
        regLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        });
    }

    private void checkUserAvailability(String name,String email,String password) {
        RetrofitClient.getApiService().searchUser(email).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null){
                    Toast.makeText(RegisterActivity.this, "Email has already registered", Toast.LENGTH_SHORT).show();

                } else if (response.body() == null) {
                    registerUser(name,email,password);
                } else {
                    Toast.makeText(RegisterActivity.this, "Server error:"+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Something went wrong,please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}