package com.kingdew.internconnect.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kingdew.internconnect.R;
import com.kingdew.internconnect.models.Job;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class JobViewActivity extends AppCompatActivity {
    private TextView titleField, companyField, workTypeField, paidField, descField, locationField, dateField,jobTypeField;
    private Button applyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        titleField = findViewById(R.id.view_job_title);
        companyField = findViewById(R.id.view_comp_name);
        workTypeField = findViewById(R.id.view_work_type);
        paidField = findViewById(R.id.view_paid_status);
        descField = findViewById(R.id.view_description);
        locationField = findViewById(R.id.view_location);
        dateField = findViewById(R.id.view_due_date);
        jobTypeField=findViewById(R.id.view_job_type);
        applyBtn = findViewById(R.id.btn_apply_now);

        Job job =(Job) getIntent().getSerializableExtra("JOB");
        if (job!=null){
            displayJobDetails(job);
        }
        applyBtn.setOnClickListener(v->{
            if (!job.getApplyLink().startsWith("http://") && !job.getApplyLink().startsWith("https://"))
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+job.getApplyLink())));

        });
    }
    private void displayJobDetails(Job job) {
        titleField.setText(job.getTitle());
        companyField.setText(job.getCompName());
        descField.setText(job.getJobDescription());
        locationField.setText(job.getCompLocation());
        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDisplay = displayFormat.format(job.getDueDate());
        dateField.setText(formattedDisplay);

        paidField.setText(job.isPaid() ? "Paid" : "Non-paid");

        String workLabel = (job.getWorkType() == 0) ? "On-Site" :
                (job.getWorkType() == 1 ? "Remote" : "Hybrid");
        workTypeField.setText(workLabel);
        
        jobTypeField.setText(job.isType()?"Full-Time":"Part-Time");
        
    }
}