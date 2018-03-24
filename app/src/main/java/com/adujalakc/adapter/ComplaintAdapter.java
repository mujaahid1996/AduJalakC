package com.adujalakc.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.adujalakc.R;

import java.util.List;

import com.adujalakc.app. AppController;
import com.adujalakc.model.Complaint;

/**
 * Created by Anadara on 12/06/2017.
 */

public class ComplaintAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Complaint> complaints;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ComplaintAdapter(Activity activity, List<Complaint> complaints){
        this.activity = activity;
        this.complaints = complaints;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return complaints.size();
    }

    @Override
    public Object getItem(int location) {
        return complaints.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView show = (NetworkImageView) convertView.findViewById(R.id.showImage);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView location = (TextView) convertView.findViewById(R.id.location);

        Complaint c = complaints.get(position);
        show.setImageUrl( c.getImg(), imageLoader );
        name.setText(c.getName());
        location.setText("Location : \n" + c.getLocation());
        date.setText(c.getComp_date());

        return convertView;
    }
}