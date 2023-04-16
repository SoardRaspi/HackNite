package com.example.hacknite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;

public class SelectionAndHistory extends AppCompatActivity {

    Button getResult, connect;
    //    TextView tv;
    EditText name;
    ListView meds_lv;
    ChipGroup medform;

    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;
    LocationManager locationManager;

    private String url = Constants.url;

    private String POST = "POST";
    private String GET = "GET";

    int flag = 0;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_and_history);

        getResult = (Button) findViewById(R.id.get_result);
        connect = (Button) findViewById(R.id.connect);
//        tv = (TextView) findViewById(R.id.tv);
        name = (EditText) findViewById(R.id.name);
        meds_lv = (ListView) findViewById(R.id.meds_lv);
        medform = (ChipGroup) findViewById(R.id.medform);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(GET, "/connect/", null, null);
            }
        });

        getResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GPSTracker gps = new GPSTracker(SelectionAndHistory.this);
//                // check if GPS enabled
//                if(gps.canGetLocation()){
//                    double latitude = gps.getLatitude();
//                    double longitude = gps.getLongitude();
//                     Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//                }else{
//                    gps.showSettingsAlert();
//                }

//                Toast.makeText(SelectionAndHistory.this, "something!!!", Toast.LENGTH_SHORT).show();
                sendRequest(GET, "/medicine/" + name.getText().toString() + "/", null, null);
//                sendRequest(GET, name.getText().toString(),null,null);
            }
        });
    }

    void sendRequest(@NonNull String type, String method, String paramname, String param) {

        /* if url is of our get request, it should not have parameters according to our implementation.
         * But our post request should have 'name' parameter. */
//        String fullURL=url+"/"+method+(param==null?"":"/"+param);
//        final String[] response = null;
//        HashMap<String, String> data = new HashMap<String, String>();
        final JSONObject[] reader = {null};

        final String[] fullURL = {url + method};
        Request request;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(3600, TimeUnit.SECONDS)
                .writeTimeout(3600, TimeUnit.SECONDS).build();

        /* If it is a post request, then we have to pass the parameters inside the request body*/
        if (type.equals(POST)) {
            RequestBody formBody = new FormBody.Builder()
                    .add(paramname, param)
                    .build();

            request = new Request.Builder()
                    .url(fullURL[0])
                    .post(formBody)
                    .build();
        } else {
            /*If it's our get request, it doen't require parameters, hence just sending with the url*/
            request = new Request.Builder()
                    .url(fullURL[0])
                    .build();
        }
        /* this is how the callback get handled */
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {

                // Read data on the worker thread
                final String responseData = Objects.requireNonNull(response.body()).string();

                // Run view-related code back on the main thread.
                // Here we display the response message in our text view
//                SelectionAndHistory.this.runOnUiThread(() -> tv.setText(responseData));

                SelectionAndHistory.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        tv.setText(responseData);

                        try {
                            reader[0] = new JSONObject(responseData);

                            if (reader[0].has("status")) {
                                try {
                                    flag = Integer.parseInt(reader[0].getJSONObject("status").toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                        Toast.makeText(SelectionAndHistory.this, "flag: " + reader[0].getJSONObject("status")).toString(), Toast.LENGTH_SHORT).show();
                            } else if (reader[0].has("meds")) {
//                    Log.d("meds", reader.getJSONObject("meds")));
//                    tv.setText(data.get("meds"));

                                ArrayList<String> selectedlabels = new ArrayList<String>();

                                JSONArray meds = null;
                                JSONArray labels = null;
                                JSONArray subs = null;

                                try {
                                    meds = reader[0].getJSONArray("meds");
                                    labels = reader[0].getJSONArray("labels");
                                    subs = reader[0].getJSONArray("sub");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                ArrayList<String> medsdata = new ArrayList<String>();
                                ArrayList<String> labelsdata = new ArrayList<String>();
                                ArrayList<String> subdata = new ArrayList<String>();

                                if (meds != null) {
                                    for (int i = 0; i < meds.length(); i++) {
                                        try {
                                            medsdata.add(meds.getString(i));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

//                                ArrayList<String> enabled = new ArrayList<String>();

                                if (labels != null) {
                                    for (int i = 0; i < labels.length(); i++) {
                                        try {
                                            labelsdata.add(labels.getString(i));
//                                            addChip(labels.getString(i), medform, enabled, medsdata, medsdata);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                if (subs != null) {
                                    for (int i = 0; i < subs.length(); i++) {
                                        try {
                                            subdata.add(subs.getString(i));
//                                            addChip(labels.getString(i), medform, enabled, medsdata, medsdata);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

//                                for (String subject : medsdata) {
//                                    if (labelsdata.contains(subject)) {
//                                        enabled.add(subject);
//                                    }
//                                }
//
                                for (String label : labelsdata) {
                                    addChip(label, medform, selectedlabels, subdata, medsdata);
                                }
//
//                                if (!enabled.isEmpty()) {
//                                    MedsListAdapter medsAdapter = new MedsListAdapter(SelectionAndHistory.this, enabled);
//                                    meds_lv.setAdapter(medsAdapter);
//                                } else {
//                                    MedsListAdapter medsAdapter = new MedsListAdapter(SelectionAndHistory.this, medsdata);
//                                    meds_lv.setAdapter(medsAdapter);
//                                }

                                MedsListAdapter medsAdapter = new MedsListAdapter(SelectionAndHistory.this, medsdata, subdata);
                                meds_lv.setAdapter(medsAdapter);

                                meds_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String selectedItem = (String) parent.getItemAtPosition(position);

                                        Intent intent = new Intent(SelectionAndHistory.this, ComparisonActivity.class);
                                        intent.putExtra("name", selectedItem);
                                        startActivity(intent);
//                                        Toast.makeText(SelectionAndHistory.this, "selectedItem:" + selectedItem, Toast.LENGTH_SHORT).show();
                                    }
                                });

//                                if (!labelsdata.isEmpty()) {
//                                    addChip(labelsdata, medform);
//                                } else {
//                                    Toast.makeText(SelectionAndHistory.this, "No forms found...", Toast.LENGTH_SHORT).show();
//                                }
                            }

//                            JSONObject stat  = reader.getJSONObject("status");
//                            String status = reader.getString("status");
//                            tv.setText(status.toString());

//                            Toast.makeText(SelectionAndHistory.this, "started", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(SelectionAndHistory.this, reader.toString(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(SelectionAndHistory.this, "ended", Toast.LENGTH_SHORT).show();

//                            JSONArray array = reader.getJSONArray("meds");
//                            Toast.makeText(SelectionAndHistory.this, array.toString(), Toast.LENGTH_SHORT).show();

//                            Iterator<String> keys = reader.keys();
//
//                            while (keys.hasNext()){
//                                String key = keys.next();
//                                data.put(key, reader.getString(key));
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        resp[0] = responseData;
//                        Toast.makeText(SelectionAndHistory.this, responseData, Toast.LENGTH_SHORT).show();
//                        tv.setText(responseData);
                    }
                });
            }
        });
    }

    private void addChip(String pItem, ChipGroup pChipGroup, ArrayList<String> selectedlabels, ArrayList<String> subs, ArrayList<String> medsdata) {
        Chip lChip = new Chip(this);
        lChip.setText(pItem);
        lChip.setTextColor(getResources().getColor(R.color.teal_700));
        lChip.setChipBackgroundColor(getResources().getColorStateList(R.color.teal_200));

        lChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedlabels.contains(pItem.toLowerCase(Locale.ROOT))) {
                    selectedlabels.remove(pItem.toLowerCase(Locale.ROOT));
                    lChip.setChecked(false);
                }
                else {
                    selectedlabels.add(pItem.toLowerCase(Locale.ROOT));
                    lChip.setChecked(true);
                }

                if (selectedlabels.isEmpty()) {
                    MedsListAdapter medsAdapter = new MedsListAdapter(SelectionAndHistory.this, medsdata, subs);
                    meds_lv.setAdapter(medsAdapter);
                }
                else {
                    ArrayList<String> temp = new ArrayList<String>();
                    ArrayList<String> temp_sub = new ArrayList<String>();

                    for (int i = 0; i < subs.size(); i++) {
                        if (containsItem(selectedlabels, subs.get(i))) {
                            temp.add(medsdata.get(i));
                            temp_sub.add(subs.get(i));
                        }
                    }

                    MedsListAdapter medsAdapter = new MedsListAdapter(SelectionAndHistory.this, temp, temp_sub);
                    meds_lv.setAdapter(medsAdapter);
                }
            }
        });

//        lChip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (enabled.contains(pItem)) {
//                    lChip.setChecked(false);
//                    enabled.remove(pItem);
//                }
//                else {
//                    lChip.setChecked(true);
//                    enabled.add(pItem);
//                }
////                if (lChip.isChecked()) {
////                    lChip.setChecked(false);
////                    enabled.remove(pItem);
////                }
////                else {
////                    lChip.setChecked(true);
////                    enabled.add(pItem);
////                }
//
////                Toast.makeText(SelectionAndHistory.this, enabled.toString(), Toast.LENGTH_SHORT).show();
//
//                if (!enabled.isEmpty()) {
//                    MedsListAdapter medsAdapter = new MedsListAdapter(SelectionAndHistory.this, enabled);
//                    meds_lv.setAdapter(medsAdapter);
//                } else {
//                    MedsListAdapter medsAdapter = new MedsListAdapter(SelectionAndHistory.this, medsdata);
//                    meds_lv.setAdapter(medsAdapter);
//                }
//
////                MedsListAdapter medsAdapter = new MedsListAdapter(SelectionAndHistory.this, enabled, enabled);
////                meds_lv.setAdapter(medsAdapter);
//            }
//        });

        pChipGroup.addView(lChip, pChipGroup.getChildCount() - 1);
    }

    public static boolean containsItem(ArrayList<String> items, String input) {
        for (String item : items) {
            if (input.contains(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}