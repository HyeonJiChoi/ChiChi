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

import static java.sql.DriverManager.println;

public class FirestoreManagement {
    String age;
    FirebaseFirestore db;

    public FirestoreManagement() {
        db = FirebaseFirestore.getInstance();
    }

    public void add(String name, String password, int birth) {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        int age = 2020 - birth / 10000;
        user.put("age", age);

// Add a new document with a generated ID
        db.collection("Inspector")
                .document(name + password)
                .set(user);
    }

    public void read_data(String name) {
        db.collection("Inspector")
                .document(name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println(document.getData());
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