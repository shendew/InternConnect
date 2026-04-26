
## 🚀 Project Overview
**InternConnect** is a specialized mobile application designed to easily find internship oportunities for fresher in the filed. The app allows students to discover, filter, and apply for internships while enabling recruiters to post and manage job opportunities in real-time,and providing a centralized platform where users can view detailed job descriptions, filter by work setting (Remote/Hybrid/On-site).

### Key Features
* **Job Feed:** Dynamically fetched from MockAPI.
* **Advanced Filtering:** filtering by Paid/Unpaid, Work Type (Remote/Hybrid/On-site), and Employment Type (Full-time/Part-time).
* **External Integration:** "Apply" functionality via external web intents with URL validation.
* **Recruiter Suite:** Interactive "Add Job" form with input validation and real-time POST requests.

---

## 🛠️ Technology Stack
* **Platform:** Android (Native)
* **Language:** Java
* **UI Approach:** **XML Layouts**
    * Utilizes `CoordinatorLayout` and `NestedScrollView` for modern, responsive detail views.
    * Custom `AlertDialog` components for a clean, non-intrusive filtering experience.
* **Networking:** Retrofit 2 for high-performance REST API communication.
* **Serialization:** GSON for JSON-to-Java object mapping.
* **Backend:** MockAPI.

---

## ⚙️ Setup and Run Instructions

### Prerequisites
* **Android Studio** (Ladybug | 2024.2.1 or newer recommended).
* **JDK 17** or higher.
* An Android Device or Emulator running **API 26 (Oreo)** or higher.

### Steps
1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/yourusername/InternConnect.git
    ```
2.  **Open in Android Studio:**
    * Launch Android Studio and select **Open**.
    * Navigate to the cloned folder and wait for the Gradle sync and indexing to finish.
3.  **Network:**
    * Ensure your testing device has an active internet connection to reach the MockAPI server.
4.  **Build and Run:**
    * Click the **Run** icon (Green play button) in the toolbar.
    * Select your target device/emulator.
5.  **Accounts and Permissions:**
    * Create account using **@internconnect.com** to access **Add Post, View Posted Jobs** features.
    * Create account using any email to access common features.

---

## 📡 API Endpoints
The app interacts with a MockAPI backend to manage users and job postings. The following endpoints are implemented in the `ApiService` interface:
**Base Url:**
```bash
     https://69eb64e797482ad5c527a543.mockapi.io/api/v1
```

### User Management
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **POST** | `/user` | Registers a new user account. |
| **GET** | `/user` | Retrieves all registered users. |
| **GET** | `/user?email={email}` | Searches for a user by their email address. |

### Job Management
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **POST** | `/jobs` | Creates a new job posting. |
| **GET** | `/jobs` | Retrieves all available job postings. |
| **PUT** | `/jobs/{id}` | Updates an existing job posting by ID. |
| **GET** | `/jobs?title={query}` | Searches for job postings by title. |
| **GET** | `/jobs?addedBy={email}` | Retrieves all jobs posted by a specific recruiter. |
| **GET** | `/jobs?{queryMap}` | Applies multiple filters (paid status, work type, etc.) simultaneously. |
| **DELETE** | `/jobs/{id}` | Deletes a job posting from the system. |

---

**Developer:** Shehara Dewanagala 
**Date:** April 2026
