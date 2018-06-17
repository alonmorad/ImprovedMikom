package com.example.alon.mikommeorer;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alonm on 05/03/2018.
 */

public class ListBuilderServices {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "Document:";

    public void getStations(final Callback<List<Station>> callback, String userPick) { //takes all the stations from database with the same line number the user chose
        db.collection("stations").whereEqualTo("linenumber",userPick)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Station> stations = new ArrayList<>(); //new list
                            for (DocumentSnapshot document : task.getResult()) {
                                stations.add(document.toObject(Station.class)); //each document that on the requiremnts are into station object and the added to the list
                            }
                            callback.onCallback(stations);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
