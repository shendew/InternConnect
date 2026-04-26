package com.kingdew.internconnect.models;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Job implements Serializable {
        @SerializedName("id")
        private String id;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("addedBy")
        private String addedBy;

        @SerializedName("title")
        private String title;

        @SerializedName("comp_name")
        private String compName;

        @SerializedName("comp_location")
        private String compLocation;

        @SerializedName("due_date")
        private Date dueDate;

        @SerializedName("apply_link")
        private String applyLink;

        @SerializedName("job_description")
        private String jobDescription;
        @SerializedName("salary")
        private double salary;

        @SerializedName("type")
        private boolean type;

        @SerializedName("paid")
        private boolean paid;

        @SerializedName("work_type")
        private int workType;

        // Default Constructor
        public Job() {}


        public Job(String addedBy, String title, String compName, String compLocation, Date dueDate, String applyLink, String jobDescription, double salary, boolean type, boolean paid, int workType) {
                this.addedBy = addedBy;
                this.title = title;
                this.compName = compName;
                this.compLocation = compLocation;
                this.dueDate = dueDate;
                this.applyLink = applyLink;
                this.jobDescription = jobDescription;
                this.salary = salary;
                this.type = type;
                this.paid = paid;
                this.workType = workType;
        }

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
        }

        public String getAddedBy() {
                return addedBy;
        }

        public void setAddedBy(String addedBy) {
                this.addedBy = addedBy;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getCompName() {
                return compName;
        }

        public void setCompName(String compName) {
                this.compName = compName;
        }

        public String getCompLocation() {
                return compLocation;
        }

        public void setCompLocation(String compLocation) {
                this.compLocation = compLocation;
        }

        public Date getDueDate() {
                return dueDate;
        }

        public void setDueDate(Date dueDate) {
                this.dueDate = dueDate;
        }

        public String getApplyLink() {
                return applyLink;
        }

        public void setApplyLink(String applyLink) {
                this.applyLink = applyLink;
        }

        public String getJobDescription() {
                return jobDescription;
        }

        public void setJobDescription(String jobDescription) {
                this.jobDescription = jobDescription;
        }

        public double getSalary() {
                return salary;
        }

        public void setSalary(double salary) {
                this.salary = salary;
        }

        public boolean isType() {
                return type;
        }

        public void setType(boolean type) {
                this.type = type;
        }

        public boolean isPaid() {
                return paid;
        }

        public void setPaid(boolean paid) {
                this.paid = paid;
        }

        public int getWorkType() {
                return workType;
        }

        public void setWorkType(int workType) {
                this.workType = workType;
        }
}
