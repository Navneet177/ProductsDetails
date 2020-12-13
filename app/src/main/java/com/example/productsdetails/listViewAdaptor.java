package com.example.productsdetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class listViewAdaptor extends BaseAdapter {
    Context context;
    ArrayList<Product> productsArrayList;
    LayoutInflater inflator;

    public listViewAdaptor(Context context, ArrayList<Product> productsArrayList) {
        this.context = context;
        this.productsArrayList = productsArrayList;
    }


    @Override
    public int getCount() {
        return productsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return productsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflator == null){
            inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        if(convertView == null){
            convertView = inflator.inflate(R.layout.customlay,parent,false);

        }
        ImageView imageView = convertView.findViewById(R.id.imageView2);
        TextView textView1 = convertView.findViewById(R.id.txtName);
        TextView textView2 = convertView.findViewById(R.id.txtPrice);

        Picasso.get().load(productsArrayList.get(position).getImage()).into(imageView);
        textView1.setText(productsArrayList.get(position).getPname());
        textView2.setText(productsArrayList.get(position).getPrice());
        return convertView;
    }
}
