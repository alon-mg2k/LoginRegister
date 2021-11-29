package com.example.loginregister.Adaptadores;

import android.bluetooth.BluetoothClass;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.content.res.Resources;

import com.example.loginregister.Model.DeviceInformation;
import com.example.loginregister.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterDataRS extends RecyclerView.Adapter<AdapterDataRS.ViewHolder> {

    private int resource;
    private ArrayList<DeviceInformation> arrayInfo;

    public AdapterDataRS(ArrayList<DeviceInformation> arrayInfo, int resource) {
        this.arrayInfo = arrayInfo;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceInformation rs = arrayInfo.get(position);
        holder.tvCO2.setText("CO2: "+ rs.getCo2() + " ppm. STATUS: " + rs.getState() + ".");
        holder.tvDist.setText("DIST: " + rs.getDistance() + "cm.");
        holder.tvSC.setText("PC: " + rs.getPersonCounter() + ".");
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
