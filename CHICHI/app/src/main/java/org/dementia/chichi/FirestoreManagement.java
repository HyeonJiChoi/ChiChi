package org.dementia.chichi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.sql.DriverManager.println;

public class FirestoreManagement {
    FirebaseFirestore db;
    public Map<String, Object> user;
    public Set<String> todayProblems;
    public Map<String, Object> picture_number;
    public ArrayList<String> userAddresses = new ArrayList<String>();
    public ArrayList<String> userIds = new ArrayList<String>();

    public FirestoreManagement() {
        db = FirebaseFirestore.getInstance();
        read_user(MainActivity.name + "_" + MainActivity.password);
        read_orientation_picture();
        read_users();
    }

    public void add(String name, String password, Map<String, Object> newUser) {
        // Create a new user with a first and last name

// Add a new document with a generated ID
        db.collection("Inspector")
                .document(name + "_" + password)
                .set(newUser);
    }

    public void addScore(int day, int score, String name, String password) {
        // Create a new user with a first and last name
        user.put(day + "_day_score", score);

// Add a new document with a generated ID
        db.collection("Inspector")
                .document(name + "_" + password)
                .set(user);
    }

    public void fixedDay(int day, String name, String password) {
        // Create a new user with a first and last name
        user.put("day", day);
        System.out.println(user);
// Add a new document with a generated ID
        db.collection("Inspector")
                .document(name + "_" + password)
                .set(user);
        read_day_problems(Integer.toString(day));
    }

    public void read_user(String id) {
        db.collection("Inspector")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                user = document.getData();
                                read_day_problems(user.get("day").toString());

                            } else {
                                System.out.println("User_sorry1");
                                read_day_problems("0");
                            }
                        } else {
                            System.out.println("User_sorry2");
                            read_day_problems("0");
                        }
                    }
                });
    }

    public void read_day_problems(String day) {
        db.collection("PerdayProblem")
                .document(day)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> problems = document.getData();
                                todayProblems = problems.keySet();

                            } else {
                                System.out.println("PerdayProblem_sorry1");
                            }
                        } else {
                            System.out.println("PerdayProblem_sorry2");
                        }
                    }
                });
    }

    public void read_orientation_picture() {
        db.collection("Problem")
                .document("Orientation_picture")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                picture_number = document.getData();
                                ;

                            } else {
                                System.out.println("Problem_sorry1");
                            }
                        } else {
                            System.out.println("Problem_sorry2");
                        }
                    }
                });
    }

    public void read_users() {
        db.collection("Inspector")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userIds.add(document.getId());
                                userAddresses.add(document.getData().get("home").toString());
                            }
                        } else {
                            System.out.println("Inspector_sorry1");
                        }

                    }
                });
    }
}