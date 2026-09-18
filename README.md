# Hostel Maintenance & Complaint Management System

## Overview
A pure Java, command-line based application developed for VIT college project evaluation. It allows students to report hostel maintenance issues (plumbing, electrical, etc.) and allows administrators to assign these complaints to specific maintenance staff members. 

## Features
- **Role-based Access Control**: Distinct menus for Students, Admins, and Maintenance Staff.
- **Complaint Lifecycle**: Complaints move through distinct statuses (OPEN -> ASSIGNED -> IN_PROGRESS -> RESOLVED).
- **File-based Persistence**: Data is saved locally using Java `BufferedWriter`/`BufferedReader` in standard `.txt` files.
- **Input Validation**: Prevents invalid status transitions and handles incorrect enum inputs using try-catch blocks.

## Project Structure (Layered Architecture)
- `model/`: Data entities (User, Student, Complaint) and Enums.
- `repository/`: Handles read/write operations to `data/users.txt` and `data/complaints.txt`.
- `service/`: Contains core business logic (validating status updates, searching records).
- `Main.java`: The Controller/CLI that interacts with the user.

## How to Run
1. Ensure you have Java 8+ installed.
2. Create a folder named `data` in the project root directory.
3. Compile all files: `javac -d bin src/**/*.java src/*.java`
4. Run the main class: `java -cp bin Main`
5. Default login: ID `A01` / Password `admin123` (Admin).
