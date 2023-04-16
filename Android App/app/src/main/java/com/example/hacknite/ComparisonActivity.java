package com.example.hacknite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ComparisonActivity extends AppCompatActivity {

    TextView name_tv;

    private String url = Constants.url;

    private String POST = "POST";
    private String GET = "GET";

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);

        name_tv = (TextView) findViewById(R.id.name);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        name_tv.setText(name);

        try {
            GPSTracker gps = new GPSTracker(ComparisonActivity.this);
            // check if GPS enabled
            if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                Toast.makeText(this, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                sendRequest(GET, "/details" + name + "/", null, null);
            } else {
                gps.showSettingsAlert();
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Error getting location...", Toast.LENGTH_SHORT).show();
        }
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

                ComparisonActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        tv.setText(responseData);

                        try {
                            reader[0] = new JSONObject(responseData);

                            if (reader[0].has("status")) {
                                try {
                                    flag = Integer.parseInt(reader[0].getJSONObject("status").toString());

                                    RecyclerView courseRV = findViewById(R.id.compareCard);

                                    ArrayList<String> options = new ArrayList<String>();
                                    options.add("<html><body>" + "<div><span class=\"PriceBoxPlanOption__offer-price___3v9x8 PriceBoxPlanOption__offer-price-cp___2QPU_\">₹<!-- -->107</span><span class=\"PriceBoxPlanOption__margin-right-4___2aqFt PriceBoxPlanOption__stike___pDQVN\">₹<!-- -->120</span><span class=\"PriceBoxPlanOption__discount___iN_jm\">11% off</span></div>" + "</body></html>");

                                    // Here, we have created new array list and added data to it
                                    ArrayList<ComparisonCardModel> courseModelArrayList = new ArrayList<ComparisonCardModel>();
                                    courseModelArrayList.add(new ComparisonCardModel("DSA in Java", "https://onemg.gumlet.io/q_auto,w_150,c_fit,h_150,f_auto/pa4gahjq58dvohzdnlke.jpg", "Rs. 400", "netmeds", "4 days", "4.4", options, ComparisonActivity.this));

                                    // we are initializing our adapter class and passing our arraylist to it.
                                    ComparisonCardAdapter courseAdapter = new ComparisonCardAdapter(ComparisonActivity.this, courseModelArrayList);

                                    // below line is for setting a layout manager for our recycler view.
                                    // here we are creating vertical list so we will provide orientation as vertical
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ComparisonActivity.this, LinearLayoutManager.VERTICAL, false);

                                    // in below two lines we are setting layoutmanager and adapter to our recycler view.
                                    courseRV.setLayoutManager(linearLayoutManager);
                                    courseRV.setAdapter(courseAdapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                        Toast.makeText(SelectionAndHistory.this, "flag: " + reader[0].getJSONObject("status")).toString(), Toast.LENGTH_SHORT).show();
                            }
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
}