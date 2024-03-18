package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {//connects to database Assignment3 in pgAdmin4
        String url = "jdbc:postgresql://localhost:5432/Assignment3";
        //username and password to  access database
        String user = "postgres";
        String password = "password";

    try{
        Class.forName("org.postgresql.Driver");//loads driver class into memory
        Connection connection = DriverManager.getConnection(url, user, password);//establishes connection to database with the given url,user and password


        if (connection != null){//if connected output successful message
            System.out.println("Connected to Database\n");
            
            Scanner scanner = new Scanner(System.in);
                int choice;
                do {//menu to allow for user input. tests all four functions
                    System.out.println("Menu:");
                    System.out.println("1. View all students");
                    System.out.println("2. Add a new student");
                    System.out.println("3. Update a student's email");
                    System.out.println("4. Delete a student");
                    System.out.println("0. Exit");

                    System.out.print("Enter your choice: ");
                    choice = scanner.nextInt();
                    scanner.nextLine(); 
                    
                    switch (choice) {
                        case 1://getAllStudents()
                        System.out.println("All Students:\n");
                        getAllStudents(connection);
            
                        break;
                        case 2://addStudent()
                        System.out.print("Enter new student's first name: ");
                        String firstName = scanner.nextLine();
                        System.out.print("Enter new student's last name: ");
                        String lastName = scanner.nextLine();
                        System.out.print("Enter new student's email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter new student's enrollment date (YYYY-MM-DD): ");
                        String enrollmentDate = scanner.nextLine();
                        addStudent(connection, firstName, lastName, email, enrollmentDate);
                        System.out.println("Updated table of students:\n");
                        getAllStudents(connection);
                        break;
                        case 3://updateStudentEmail()
                        System.out.print("Enter the student's ID to update their email: ");
                        int student_Id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter new email: ");
                        String new_Email = scanner.nextLine();
                        updateStudentEmail(connection, student_Id, new_Email);
                        System.out.println("Updated table of students:\n");
                        getAllStudents(connection);
                        break;
                        case 4://deleteStudent()
                        System.out.print("Enter the student's ID to delete student: ");
                        int deletestudent_Id = scanner.nextInt();
                        deleteStudent(connection, deletestudent_Id);
                        System.out.println("Updated table of students:\n");
                        getAllStudents(connection);
                        break;

                        case 0:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 0);

            scanner.close();
            connection.close();
        } else {
            System.out.println("Failed to connect to the database");
        }
 } catch(Exception e){
    System.out.println(e);
}     
}

 public static void getAllStudents(Connection connection) {
    try{
         Statement statement = connection.createStatement();
        statement.executeQuery( "SELECT * FROM students ORDER BY student_id");//select everything from students table and order by student_id
        ResultSet resultSet = statement.getResultSet();
        System.out.printf("%-15s %-15s %-15s %-25s %-15s%n", "Student ID", "First Name", "Last Name", "Email", "Enrollment Date");//help with formatting to make it more organized
        while (resultSet.next()){

            int studentId = resultSet.getInt("student_id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String enrollmentDate = resultSet.getString("enrollment_date");
  
            System.out.printf("%-15d %-15s %-15s %-25s %-15s%n", studentId, firstName, lastName, email, enrollmentDate);//prints out all columns in the correct format
        
          
        }
    }catch(Exception e){
        System.out.println(e);
    }
}
public static void addStudent(Connection connection, String firstName, String lastName, String email, String enrollment_Date) {
    String query = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";//?-represents the values the user will input

    
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        //parameters for the prepared statement
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, email);
        //enrollment data is converted to java.sql.Date object
        java.sql.Date dateType = java.sql.Date.valueOf(enrollment_Date);
        statement.setDate(4, dateType);
   

        int newInfo = statement.executeUpdate();
        if (newInfo > 0) {
            System.out.println("New student added successfully.");//if insertion successful...print success message
        } else {
            System.out.println("ERROR: Unable to add new student");
        }
    } catch (SQLException e) {
        System.out.println(e);
    }
}
public static void updateStudentEmail (Connection connection, int student_id, String new_Email){
    String query = "UPDATE students SET email = ? WHERE student_id = ?";//update student email based on student id
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, new_Email);
        statement.setInt(2, student_id); 

    int newInfo = statement.executeUpdate();
    if (newInfo > 0) {
        System.out.println("Email updated successfully.");
    } else {
        System.out.println("ERROR: Unable to update email");
    }
    } catch (SQLException e) {
        System.out.println(e);
    }
}
public static void deleteStudent(Connection connection, int student_id){
    String query = "DELETE FROM students WHERE student_id = ?";//delete student based on student id
   
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, student_id);
        int newInfo = statement.executeUpdate();

        if (newInfo > 0) {
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("ERROR: Unable to delete student");
        }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    



}

    


