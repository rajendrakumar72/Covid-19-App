package com.example.cornonalivetrackerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cornonalivetrackerapp.model.DataAdapter;
import com.example.cornonalivetrackerapp.model.DataClass;
import com.example.cornonalivetrackerapp.network.ApiInterface;
import com.example.cornonalivetrackerapp.network.RetrofitClass;
import com.hbb20.CountryCodePicker;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    CountryCodePicker countryCodePicker;
    TextView mTodayTotal,mTotal,mTodayActive,mActive,mTodayRecovered,mRecovered,mTodayDeaths,mDeaths;

    String country;
    TextView mFilter;
    Spinner spinner;
    String[] types ={"Cases","Deaths","Recovered","Active"};
    private List<DataClass> dataList1;
    private List<DataClass> dataList2;
    PieChart pieChart;
    private RecyclerView recyclerView;
    DataAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        countryCodePicker=findViewById(R.id.ccp);
        mTotal=findViewById(R.id.totalCase);
        mTodayTotal=findViewById(R.id.todayTotal);
        mRecovered=findViewById(R.id.recoveredCase);
        mTodayRecovered=findViewById(R.id.todayRecovered);
        mDeaths=findViewById(R.id.totalDeath);
        mTodayDeaths=findViewById(R.id.todayDeath);
        mActive=findViewById(R.id.activeCase);
        mTodayActive=findViewById(R.id.todayActive);

        recyclerView=findViewById(R.id.recyclerView);
        pieChart=findViewById(R.id.pieChart);
        spinner=findViewById(R.id.spinner);
        mFilter=findViewById(R.id.filter);

        dataList1=new ArrayList<>();
        dataList2=new ArrayList<>();

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,types);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);
        //spinner.setSelection(0,true);

        RetrofitClass.getRetrofitInstance().getCountryData().enqueue(new Callback<List<DataClass>>() {
            @Override
            public void onResponse(Call<List<DataClass>> call, Response<List<DataClass>> response) {
                dataList2.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<DataClass>> call, Throwable t) {

            }
        });

        adapter=new DataAdapter(dataList2,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        countryCodePicker.setAutoDetectedCountry(true);
        country=countryCodePicker.getSelectedCountryName();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country=countryCodePicker.getSelectedCountryName();
                fetchData();
            }
        });
        fetchData();

    }

    private void fetchData() {
        RetrofitClass.getRetrofitInstance().getCountryData().enqueue(new Callback<List<DataClass>>() {
            @Override
            public void onResponse(Call<List<DataClass>> call, Response<List<DataClass>> response) {
                dataList1.addAll(response.body());

                for (int i=0;i<dataList1.size();i++){
                    if (dataList1.get(i).getCountry().equals(country)){

                        mActive.setText(dataList1.get(i).getActive());
                        mTodayDeaths.setText(dataList1.get(i).getTodayDeaths());
                        mTodayRecovered.setText(dataList1.get(i).getTodayRecovered());
                        mTodayTotal.setText(dataList1.get(i).getTodayCases());
                        mTotal.setText(dataList1.get(i).getCases());
                        mDeaths.setText(dataList1.get(i).getDeaths());
                        mRecovered.setText(dataList1.get(i).getRecovered());

                        int active,cases,recovered,deaths;
                        active=Integer.parseInt(dataList1.get(i).getActive());
                        cases=Integer.parseInt(dataList1.get(i).getCases());
                        recovered=Integer.parseInt(dataList1.get(i).getRecovered());
                        deaths=Integer.parseInt(dataList1.get(i).getDeaths());

                        updateGraph(active,cases,recovered,deaths);

                    }
                }
            }

            @Override
            public void onFailure(Call<List<DataClass>> call, Throwable t) {

            }
        });
    }

    private void updateGraph(int active, int total, int recovered, int deaths) {
        pieChart.clearChart();
        pieChart.addPieSlice(new PieModel("Confirm",total, Color.parseColor("#FFB701")));
        pieChart.addPieSlice(new PieModel("Active",active, Color.parseColor("#FF4CAF50")));
        pieChart.addPieSlice(new PieModel("Recovered",recovered, Color.parseColor("#38ACCD")));
        pieChart.addPieSlice(new PieModel("Deaths",deaths, Color.parseColor("#F55c47")));
        pieChart.startAnimation();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item=types[position];
        mFilter.setText(item);
        adapter.filter(item);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}