package com.xcheko51x.adminbibliotecaapp.Vistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.xcheko51x.adminbibliotecaapp.Adaptadores.AdaptadorMenu;
import com.xcheko51x.adminbibliotecaapp.Clases.Usuario;
import com.xcheko51x.adminbibliotecaapp.LoginScreen;
import com.xcheko51x.adminbibliotecaapp.R;
import com.xcheko51x.adminbibliotecaapp.databinding.ActivityVistaNavegacionBinding;

import java.util.ArrayList;

public class VistaNavegacion extends AppCompatActivity {

    private ActivityVistaNavegacionBinding binding;

    Usuario usuario = new Usuario();
    String[] listaMenu;
    AdaptadorMenu adaptadorMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaNavegacionBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);

        leerShared();
        binding.tvUsuario.setText(usuario.getNomUsuario());

        binding.rvMenu.setLayoutManager(new GridLayoutManager(VistaNavegacion.this, 2));

        listaMenu = getResources().getStringArray(R.array.lista_menu);

        binding.rvMenu.setAdapter(new AdaptadorMenu(VistaNavegacion.this, listaMenu));

        binding.ibtnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borrarShared();
            }
        });

    }

    public void borrarShared() {
        SharedPreferences preferences = getSharedPreferences("data_usuario", MODE_PRIVATE);
        preferences.edit().clear().commit();

        Intent intent = new Intent(VistaNavegacion.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }

    public void leerShared() {
        SharedPreferences preferences = getSharedPreferences("data_usuario", MODE_PRIVATE);
        usuario.setIdUsuario(preferences.getString("idUsuario", ""));
        usuario.setNomUsuario(preferences.getString("nomUsuario", ""));
    }
}