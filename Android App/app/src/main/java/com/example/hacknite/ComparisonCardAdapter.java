package com.example.hacknite;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ComparisonCardAdapter extends RecyclerView.Adapter<ComparisonCardAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<ComparisonCardModel> courseModelArrayList;

    // Constructor
    public ComparisonCardAdapter(Context context, ArrayList<ComparisonCardModel> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public ComparisonCardAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comparison_card, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComparisonCardAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        ComparisonCardModel model = courseModelArrayList.get(position);
        holder.nameMed.setText(model.getMedName());
        holder.price.setText(model.getMedPrice());
        holder.nameOfProvider.setText(model.getNameOfPRovider());
        holder.time.setText(model.getTime());
        holder.rating.setText(model.getRating());

        CompareOptionsAdapter adapter = new CompareOptionsAdapter(model.getActivityContext(), model.getOptions());
        holder.options.setAdapter(adapter);

        new DownloadImageFromInternet(holder.medImage).execute(model.getMedImage());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return courseModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView medImage;
        private final TextView nameMed;
        private final TextView price;
        private final TextView nameOfProvider;
        private final TextView time;
        private final TextView rating;
        private final ListView options;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            medImage = itemView.findViewById(R.id.medImage);
            nameMed = itemView.findViewById(R.id.nameMed);
            price = itemView.findViewById(R.id.price);
            nameOfProvider = itemView.findViewById(R.id.nameOfProvider);
            time = itemView.findViewById(R.id.time);
            rating = itemView.findViewById(R.id.rating);
            options = itemView.findViewById(R.id.pricingOptions);
        }
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
//            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...",Toast.LENGTH_SHORT).show();
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage=BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}