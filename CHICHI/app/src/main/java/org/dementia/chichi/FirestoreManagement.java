package org.dementia.chichi;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.sql.DriverManager.println;

public class FirestoreManagement {
    FirebaseFirestore db;
    public Map<String, Object> user;
    public Set<String> todayProblems;
    public Map<String, Object> picture_number;
    public int userCount;

    public FirestoreManagement() {
        db = FirebaseFirestore.getInstance();
        read_user("최현지chj159258357");
        read_orientation_picture();
    }

    public void add(String name, String password, int age, String home, String number) {
        // Create a new user with a first and last name
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("age", age);
        newUser.put("home", home);
        newUser.put("number", number);

// Add a new document with a generated ID
        db.collection("Inspector")
                .document(name + password)
                .set(newUser);
    }

    public void read_user(String name) {
        db.collection("Inspector")
                .document(name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                user = document.getData();
                                read_day_problems(user.get("day").toString());
                                read_users_count();
                            } else {
                                System.out.println("sorry1");
                            }
                        } else {
                            System.out.println("sorry2");
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
                                System.out.println("sorry1");
                            }
                        } else {
                            System.out.println("sorry2");
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
                                System.out.println("sorry1");
                            }
                        } else {
                            System.out.println("sorry2");
                        }
                    }
                });
    }

    public void read_users_count() {
        db.collection("Inspector").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot document = task.getResult();
                            if (!document.isEmpty()) {
                                userCount = document.getDocuments().size();
                            } else {
                                System.out.println("sorry1");
                            }
                        } else {
                            System.out.println("sorry2");
                        }
                    }
                });
    }
}