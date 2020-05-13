package com.example.spring2020_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements iOnItemListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "demo";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Button btn_add_schedule;
    ArrayList<Schedule> scheduleArrayList = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        getMeetings();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getMeetings();
        mRecyclerView = findViewById(R.id.mRecyclerView);

        mRecyclerView.setHasFixedSize(true);
        setTitle("Basic Scheduler");
        btn_add_schedule = findViewById(R.id.btn_addSchedule);
        btn_add_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CreateScheduleActivity.class);
                startActivity(i);

            }
        });
    }

        public void getMeetings(){
            db.collection("Schedule").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        scheduleArrayList.removeAll(scheduleArrayList);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Schedule tr = new Schedule(document.getId(), document.getData().get("name").toString(), document.getData().get("place").toString(),document.getData().get("date").toString(), document.getData().get("time").toString() );
                            scheduleArrayList.add(tr);
                        }

                        scheduleArrayList.sort(new Comparator<Schedule>() {
                            @Override
                            public int compare(Schedule o1, Schedule o2) {
                                return o1.date.compareTo(o2.date);
                            }
                        });

                        if(scheduleArrayList.size() > 0){
                            mLayoutManager = new LinearLayoutManager(MainActivity.this);
                            mRecyclerView.setLayoutManager(mLayoutManager);

                            mAdapter = new ScheduleAdapter(scheduleArrayList, MainActivity.this);

                            mRecyclerView.setAdapter(mAdapter);
                        }


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }

    @Override
    public void onItemLongPress(final int pos) {
        Log.d(TAG, "onItemLongPress: " + scheduleArrayList.get(pos));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("are you sure?")
        .setTitle("Delete");

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.collection("Schedule").document(scheduleArrayList.get(pos).id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Log.d(TAG, "Successfully deleted!");
                                getMeetings();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error occurred while deleting");

                            }
                        });
            }
        });


        AlertDialog dialog = builder.create();

        dialog.show();

    }
}
