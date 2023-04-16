package com.example.hacknite;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MedsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> name;
    private final ArrayList<String> sub;
//    private final ArrayList<String> enabled;

    public MedsListAdapter(Activity context, ArrayList<String> name, ArrayList<String> sub) {
        super(context, R.layout.meds_list, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.name=name;
        this.sub=sub;
//        this.chemicals=chemicals;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.meds_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.name);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subs);

        titleText.setText(name.get(position));
        subtitleText.setText(sub.get(position));

        return rowView;
    }
}