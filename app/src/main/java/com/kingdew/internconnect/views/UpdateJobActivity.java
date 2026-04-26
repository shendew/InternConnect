package com.kingdew.internconnect.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kingdew.internconnect.R;
import com.kingdew.internconnect.api.RetrofitClient;
import com.kingdew.internconnect.models.Job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateJobActivity extends AppCompatActivity {

    private Date selectedGDate;
    private TextInputEditText jobTitleField, compNameField, compLocationField, salaryField, applyLinkField, dueDateField,jobDescriptionField;
    private TextInputLayout jobTitleLay, comNameLay, comLocationLay, jobSalaryLay, jobLinkLay, dueDateLay,jobDescLay;
    private Spinner workTypeSpinner;
    private RadioGroup rgJobType;
    private RadioButton rbFullTime;
    private CheckBox cbIsPaid;
    private Button btnUpdate;
    private String email;
    private ProgressBar progressBar;
    private Job job;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_job);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        get job object form intent
        job =(Job) getIntent().getSerializableExtra("JOB");
        if (job==null){
            Toast.makeText(this, "Data hasn't passed correctly.", Toast.LENGTH_SHORT).show();
            finish();
        }

        initView();


//        setup work type spinner
        String[] workTypes = {"Onsite", "Remote", "Hybrid"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, workTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workTypeSpinner.setAdapter(adapter);

        setData(job);


        initListeners();




    }



    private boolean validateForm() {
        boolean isValid = true;

        // Title validation
        if (jobTitleField.getText().toString().trim().isEmpty()) {
            jobTitleLay.setError("Title is required");
            isValid = false;
        } else { jobTitleLay.setError(null); }

        // Company Name
        if (compNameField.getText().toString().trim().isEmpty()) {
            comNameLay.setError("Company name is required");
            isValid = false;
        } else { comNameLay.setError(null); }

        if (compLocationField.getText().toString().trim().isEmpty()){
            comLocationLay.setError("Location is required");
            isValid=false;
        }else {comLocationLay.setError(null);}

        if (jobDescriptionField.getText().toString().trim().isEmpty()){
            jobDescLay.setError("Description is required");
            isValid=false;
        }

        // Salary validation (Only if paid is checked)
        if (cbIsPaid.isChecked()) {
            String salary = salaryField.getText().toString().trim();
            if (salary.isEmpty()) {
                jobSalaryLay.setError("Salary is required for paid jobs");
                isValid = false;
            } else { jobSalaryLay.setError(null); }
        }

        // URL validation
        String link = applyLinkField.getText().toString().trim();
        if (link.isEmpty()) {
            jobLinkLay.setError("Link is required");
            isValid = false;
        } else if (!android.util.Patterns.WEB_URL.matcher(link).matches()) {
            jobLinkLay.setError("Enter a valid URL");
            isValid = false;
        } else { jobLinkLay.setError(null); }

        // Date validation
        if (selectedGDate == null) {
            dueDateLay.setError("Select a due date");
            isValid = false;
        } else { dueDateLay.setError(null); }

        return isValid;
    }

    private void initView(){
        jobTitleField = findViewById(R.id.add_job_title);
        compNameField = findViewById(R.id.add_comp_name);
        compLocationField = findViewById(R.id.add_comp_location);
        salaryField = findViewById(R.id.add_salary);
        applyLinkField = findViewById(R.id.add_apply_link);
        dueDateField = findViewById(R.id.add_due_date);
        jobDescriptionField=findViewById(R.id.add_job_description);


        workTypeSpinner = findViewById(R.id.add_work_type_spinner);
        rgJobType = findViewById(R.id.rg_job_type);
        rbFullTime = findViewById(R.id.rb_full_time);
        cbIsPaid = findViewById(R.id.cb_is_paid);
        btnUpdate = findViewById(R.id.btn_update_job);

        jobTitleLay = findViewById(R.id.job_title_lay);
        comNameLay = findViewById(R.id.com_name_lay);
        comLocationLay = findViewById(R.id.com_location_name);
        jobSalaryLay = findViewById(R.id.job_salary_lay);
        jobLinkLay = findViewById(R.id.job_link_lay);
        dueDateLay = findViewById(R.id.due_date_lay);
        jobDescLay=findViewById(R.id.add_job_description_lay);


        progressBar=findViewById(R.id.loader);
    }

    private void setData(Job job){
        jobTitleField.setText(job.getTitle());
        compNameField.setText(job.getCompName());
        compLocationField.setText(job.getCompLocation());
        salaryField.setText(String.valueOf(job.getSalary()));
        applyLinkField.setText(job.getApplyLink());
        jobDescriptionField.setText(job.getJobDescription());
        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDisplay = displayFormat.format(job.getDueDate().getTime());
        dueDateField.setText(formattedDisplay);

        rbFullTime.setSelected(job.isType());
        workTypeSpinner.setSelection(job.getWorkType());
        workTypeSpinner.setSelection(job.getWorkType());
        selectedGDate=new Date(job.getDueDate().toString());
        cbIsPaid.setChecked(job.isPaid());
        if (job.isPaid()){
            salaryField.setEnabled(true);
        }
    }
    private void updateJobs(Job job, Job newJob) {
        Call<Job> call= RetrofitClient.getApiService().updateJob(job.getId(),newJob);
        call.enqueue(new Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, Response<Job> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()){
                    Toast.makeText(UpdateJobActivity.this, "Job saved.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    btnUpdate.setEnabled(true);
                    btnUpdate.setText("Update Job Posting");
                    Toast.makeText(UpdateJobActivity.this, "Job saving failed.please try again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Job> call, Throwable t) {
                btnUpdate.setEnabled(true);
                btnUpdate.setText("Update Job Posting");
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(UpdateJobActivity.this, "Something went wrong,please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initListeners(){
        dueDateField.setOnClickListener(v -> {
            Calendar calendar=Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);

            DatePickerDialog dialog=new DatePickerDialog(UpdateJobActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);

                    SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDisplay = displayFormat.format(selectedDate.getTime());
                    dueDateField.setText(formattedDisplay);

                    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                    isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String dateForDb = isoFormat.format(selectedDate.getTime());
                    selectedGDate=new Date(selectedDate.getTime().toString());

                }
            },year,month,day);
            dialog.show();
        });
        cbIsPaid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                jobSalaryLay.setEnabled(isChecked);
                if (!isChecked) {
                    salaryField.setText("0");
                    jobSalaryLay.setError(null);
                }
            }
        });

        btnUpdate.setOnClickListener(v -> {
            if (validateForm()) {
                btnUpdate.setEnabled(false);
                btnUpdate.setText("Saving");
                progressBar.setVisibility(View.VISIBLE);

                String title = jobTitleField.getText().toString().trim();
                String company = compNameField.getText().toString().trim();
                String location = compLocationField.getText().toString().trim();
                String desc=jobDescriptionField.getText().toString().trim();
                String link=applyLinkField.getText().toString().trim();
                double salary = cbIsPaid.isChecked() ? Double.parseDouble(salaryField.getText().toString()) : 0.0;
                int workType = workTypeSpinner.getSelectedItemPosition(); // 0, 1, 2
                boolean isFullTime = rbFullTime.isChecked();
                boolean isPaid = cbIsPaid.isChecked();

                Job newJob= new Job(email,title,company,location,selectedGDate,link,desc,salary,isFullTime,isPaid,workType);

                updateJobs(job,newJob);

            }
        });
    }
}