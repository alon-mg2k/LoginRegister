package com.example.loginregister.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.loginregister.Model.DeviceInformation;
import com.example.loginregister.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterDataRD extends RecyclerView.Adapter<AdapterDataRD.ViewHolder> {
    private int resource;
    private ArrayList<DeviceInformation> arrayInfo;
    private ArrayList<Integer> distArray;
    private ArrayList<Float> CO2Array;

    public AdapterDataRD(ArrayList<DeviceInformation> arrayInfo, int resource, ArrayList<Integer> distArray, ArrayList<Float> CO2Array) {
        this.arrayInfo = arrayInfo;
        this.resource = resource;
        this.distArray = distArray;
        this.CO2Array = CO2Array;

    }

    @NonNull
    @Override
    public AdapterDataRD.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new AdapterDataRD.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDataRD.ViewHolder holder, int position) {
        DeviceInformation rs = arrayInfo.get(position);
        holder.tvCO2.setText("CO2: "+ rs.getCo2() + " ppm. STATUS: " + rs.getState() + ".");
        holder.tvDist.setText("DIST: "  + rs.getDistance() +"cm.");
        holder.tvSC.setText("SC: " + rs.getStepCounter() + ".");
    }

    @Override
    public int getItemCount() {
        return arrayInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TableLayout tLayout;
        private TextView tvCO2;
        private TextView tvDist;
        private TextView tvSC;
        private View rowView;

        public ViewHolder(View rowView){
            super(rowView);
            this.tvCO2 = (TextView) rowView.findViewById(R.id.table_co2);
            this.tvDist = (TextView) rowView.findViewById(R.id.table_dist);
            this.tvSC = (TextView) rowView.findViewById(R.id.table_stepcounter);
            this.tLayout = (TableLayout) rowView.findViewById(R.id.DataReadingLayout);
        }
    }

}
