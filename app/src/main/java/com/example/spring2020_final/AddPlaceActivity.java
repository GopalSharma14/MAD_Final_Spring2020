package com.example.spring2020_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AddPlaceActivity extends AppCompatActivity {
    EditText et_search;
    Button btn_go;
    String apiurl = "";
    ArrayList<String> searchPlaces = new ArrayList<>();

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        et_search = findViewById(R.id.et_search);
        btn_go = findViewById(R.id.btn_go);
        listView = findViewById(R.id.listview);

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String placeEntered = et_search.getText().toString();
                if (placeEntered == null || placeEntered.equals("")) {
                    et_search.setError("Please Input data");
                } else {
                    apiurl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + placeEntered + "&key=AIzaSyDJ6tsJg_dc_iD04BoOrUmxq4tqkRU0_i0";
                    new GetAsyncData().execute(apiurl);
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra(CreateScheduleActivity.PLACE, searchPlaces.get(i).toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
    public class GetAsyncData extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            try {
                URL urlTrips = new URL(strings[0]);
                connection = (HttpURLConnection) urlTrips.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    Log.d("JSON", root.toString());
                    JSONArray predictions = root.getJSONArray("predictions");
                    if (predictions.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Nothing Found", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < predictions.length(); i++) {
                            JSONObject jsonObject = (JSONObject) predictions.get(i);
                            Log.d("locations",jsonObject.toString());
                            searchPlaces.add(jsonObject.getString("description"));
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("error",e.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("error",e.toString());
            }
            return searchPlaces;
        }

        @Override
        protected void onPostExecute(ArrayList<String> data) {
            super.onPostExecute(data);
            if(data.size() >0){

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddPlaceActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, data);
                listView.setAdapter(adapter);
            }

        }
    }
}
