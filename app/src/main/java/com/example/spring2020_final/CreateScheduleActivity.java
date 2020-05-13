package com.example.spring2020_final;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateScheduleActivity extends AppCompatActivity {

    Button btn_add_place;
    Button btn_add_date;
    Button btn_add_time;
    Button btn_save;

    TextView tv_place;
    TextView tv_date;
    TextView tv_time;

    EditText et_title;

    TimePickerDialog pickerTime;
    DatePickerDialog pickerDate;

    Calendar calendar = Calendar.getInstance();

    int REQ_CODE = 200;
    final static String PLACE = "place";
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "demo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        setTitle("Schedule A Meeting");

        btn_add_date = findViewById(R.id.btn_add_date);
        tv_date = findViewById(R.id.tv_date);

        btn_add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // date picker dialog
                pickerDate = new DatePickerDialog(CreateScheduleActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                tv_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                pickerDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                pickerDate.show();

                btn_add_date.setVisibility(View.INVISIBLE);
            }
        });

        btn_add_time = findViewById(R.id.btn_add_time);
        tv_time = findViewById(R.id.tv_time);

        btn_add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                pickerTime = new TimePickerDialog(CreateScheduleActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                tv_time.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                pickerTime.show();

                btn_add_time.setVisibility(View.INVISIBLE);
            }
        });

        btn_add_place = findViewById(R.id.btn_add_place);
//        tv_time = findViewById(R.id.tv_time);
        btn_add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(CreateScheduleActivity.this, AddPlaceActivity.class);
                i.putExtra(PLACE, "");
                startActivityForResult(i, REQ_CODE);
            }
        });

        tv_place = findViewById(R.id.tv_place);
        et_title = findViewById(R.id.et_title);
        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_title.getText().toString() == ""){
                    et_title.setError("Please add title");
                }else if(tv_place.getText().toString()== "") {
                    Toast.makeText(getApplicationContext(), "Select Place", Toast.LENGTH_SHORT).show();
                }else if(tv_date.getText().toString() =="") {
                    Toast.makeText(getApplicationContext(), "Select Date", Toast.LENGTH_SHORT).show();
                }
                else if(tv_time.getText().toString() == "") {
                    Toast.makeText(getApplicationContext(), "Select time", Toast.LENGTH_SHORT).show();
                }else {
                    Map<String, Object> createSchedule = new HashMap<>();
                    createSchedule.put("name", et_title.getText().toString());
                    createSchedule.put("place", tv_place.getText().toString());
                    createSchedule.put("date", tv_date.getText().toString());
                    createSchedule.put("time", tv_time.getText().toString());

                    db.collection("Schedule").document().set(createSchedule).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            Intent i = new Intent();
                            setResult(RESULT_OK, i);
                            finish();
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE){
            if(resultCode == RESULT_OK){
//                Toast.makeText(this, data.getExtras().getString(COLOR_KEY), Toast.LENGTH_LONG);
                Log.d("datagot", data.getExtras().getString(PLACE));
                String pl = data.getExtras().getString(PLACE);
                tv_place.setText(pl);

                btn_add_place.setVisibility(View.INVISIBLE);
            }

        }
    }
}
