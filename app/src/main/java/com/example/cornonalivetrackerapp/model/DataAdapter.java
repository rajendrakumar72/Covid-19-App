package com.example.cornonalivetrackerapp.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cornonalivetrackerapp.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    int m=1;
    private List<DataClass> dataClassesList;
    private Context context;

    public DataAdapter(List<DataClass> dataClassesList, Context context) {
        this.dataClassesList = dataClassesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataClass dataClass=dataClassesList.get(position);
        if (m==1){
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(dataClass.getCases())));
        }else if (m==2){
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(dataClass.getRecovered())));
        }else if (m==3){
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(dataClass.getDeaths())));
        }else {
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(dataClass.getActive())));
        }

        holder.country.setText(dataClass.getCountry());
    }

    @Override
    public int getItemCount() {
        return dataClassesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cases,country;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cases=itemView.findViewById(R.id.countryCase);
            country=itemView.findViewById(R.id.countryName);
        }
    }

    public void filter(String charText){
        if (charText.equals("Cases")){
            m=1;
        }else if (charText.equals("Recovered")){
            m=2;
        }else if (charText.equals("Deaths")){
            m=3;
        }else {
            m=4;
        }
        notifyDataSetChanged();
    }
}
