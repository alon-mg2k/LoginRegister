package com.example.loginregister.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregister.R;

public class HolderMensaje extends RecyclerView.ViewHolder {

    private TextView nombre;
    private TextView mensaje;
    private TextView hora;
    private ImageView fotoPerfil;
    private ImageView fotoMensajeEnviar;

    public HolderMensaje(@NonNull View itemView) {
        super(itemView);
        nombre= (TextView) itemView.findViewById(R.id.nombreMensaje);
        mensaje= (TextView) itemView.findViewById(R.id.mensajeMensaje);
        hora = (TextView) itemView.findViewById(R.id.horaMensaje);
        fotoPerfil = (ImageView) itemView.findViewById(R.id.fotoPerfilMensaje);
        fotoMensajeEnviar = (ImageView) itemView.findViewById(R.id.mensajeFoto);

    }

    public ImageView getFotoMensajeEnviar() {
        return fotoMensajeEnviar;
    }

    public void setFotoMensajeEnviar(ImageView fotoMensajeEnviar) {
        this.fotoMensajeEnviar = fotoMensajeEnviar;
    }

    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public TextView getMensaje() {
        return mensaje;
    }

    public void setMensaje(TextView mensaje) {
        this.mensaje = mensaje;
    }

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public ImageView getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(ImageView fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

}
