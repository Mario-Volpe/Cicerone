package com.example.cicerone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cicerone.Attivita;

import java.util.ArrayList;

public class FoodAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Attivita> foodModelArrayList;

    public FoodAdapter(Context context, ArrayList<Attivita> foodModelArrayList) {

        this.context = context;
        this.foodModelArrayList = foodModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return foodModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.multiple_columns, null, true);

            holder.id = (TextView) convertView.findViewById(R.id.id);
            holder.citta = (TextView) convertView.findViewById(R.id.citta);
            holder.data = (TextView) convertView.findViewById(R.id.data);
            holder.partecipanti = (TextView) convertView.findViewById(R.id.partecipanti);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.id.setText(""+foodModelArrayList.get(position).getIdAttivita());
        holder.citta.setText(foodModelArrayList.get(position).getCitta());
        holder.data.setText(foodModelArrayList.get(position).getData());
        holder.partecipanti.setText(""+foodModelArrayList.get(position).getMaxPartecipanti());

        return convertView;
    }

    private class ViewHolder {

        protected TextView id, citta, data, partecipanti;

    }

}