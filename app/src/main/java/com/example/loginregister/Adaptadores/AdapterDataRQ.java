package com.example.loginregister.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.loginregister.Model.DeviceInformation;
import com.example.loginregister.R;
import com.example.loginregister.data.RQActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterDataRQ extends RecyclerView.Adapter<AdapterDataRQ.ViewHolder> {
    private int resource;
    private ArrayList<DeviceInformation> arrayInfo;

    public AdapterDataRQ(int resource, ArrayList<DeviceInformation> arrayInfo) {
        this.arrayInfo = arrayInfo;
        this.resource = resource;
    }

    @NonNull
    @Override
    public AdapterDataRQ.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new AdapterDataRQ.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDataRQ.ViewHolder holder, int position) {
        DeviceInformation rs = arrayInfo.get(position);
        holder.tvCO2.setText("CO2: "+ rs.getCo2() + " ppm. STATUS: " + rs.getState() + ".");
        holder.tvDist.setText("DIST: "  + rs.getDistance() +"cm.");
        holder.tvSC.setText("SC: " + rs.getStepCounter() + ". PC: " + rs.getPersonCounter() + ".");
        holder.tvDevice.setText(rs.getName());
        holder.tvDatetime.setText(rs.getDatetime());
        holder.tvMode.setText(rs.getUsername());
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
        private TextView tvDevice;
        private TextView tvMode;
        private TextView tvDatetime;
        private View rowView;

        public ViewHolder(View rowView){
            super(rowView);
            this.tvCO2 = (TextView) rowView.findViewById(R.id.table_co2);
            this.tvDist = (TextView) rowView.findViewById(R.id.table_dist);
            this.tvSC = (TextView) rowView.findViewById(R.id.table_stepcounter);
            this.tvMode = (TextView) rowView.findViewById(R.id.table_mode);
            this.tvDevice = (TextView) rowView.findViewById(R.id.table_device);
            this.tvDatetime = (TextView) rowView.findViewById(R.id.table_datetime);
            this.tLayout = (TableLayout) rowView.findViewById(R.id.TableReport);
        }
    }

    public void setArrayInfo(ArrayList<DeviceInformation> arrayInfo) {
        this.arrayInfo = arrayInfo;
    }
}
