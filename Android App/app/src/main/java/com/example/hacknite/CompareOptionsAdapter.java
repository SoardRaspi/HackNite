package com.example.hacknite;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CompareOptionsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> codes;
//    private final ArrayList<String> sub;
//    private final ArrayList<String> enabled;

    public CompareOptionsAdapter(Activity context, ArrayList<String> codes) {
        super(context, R.layout.compare_options, codes);
        // TODO Auto-generated constructor stub

        this.context=context;
//        this.name=name;
        this.codes=codes;
//        this.chemicals=chemicals;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.compare_options, null,true);

//        TextView titleText = (TextView) rowView.findViewById(R.id.name);
//        TextView subtitleText = (TextView) rowView.findViewById(R.id.subs);

        WebView web_view = (WebView) rowView.findViewById(R.id.wview);
//        TextView tv = (TextView) rowView.findViewById(R.id.tv);

//        web_view.requestFocus();
//        web_view.getSettings().setLightTouchEnabled(true);
//        web_view.getSettings().setJavaScriptEnabled(true);
//        web_view.getSettings().setGeolocationEnabled(true);
//        web_view.setSoundEffectsEnabled(true);
//        web_view.loadData("<html><body>Hello, world!</body></html>",
//                "text/html", "UTF-8");

//        web_view.loadData("<html><body>" + "<div><span class=\"PriceBoxPlanOption__offer-price___3v9x8 PriceBoxPlanOption__offer-price-cp___2QPU_\">₹<!-- -->107</span><span class=\"PriceBoxPlanOption__margin-right-4___2aqFt PriceBoxPlanOption__stike___pDQVN\">₹<!-- -->120</span><span class=\"PriceBoxPlanOption__discount___iN_jm\">11% off</span></div>" + "</body></html>",
//                "text/html", "UTF-8");

        web_view.loadData(codes.get(position), "text/html", "UTF-8");

//        tv.setText(codes.get(position));
//        subtitleText.setText(sub.get(position));

        return rowView;
    }
}