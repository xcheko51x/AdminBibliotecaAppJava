package com.xcheko51x.adminbibliotecaapp.Vistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.xcheko51x.adminbibliotecaapp.Clases.Prestamo;
import com.xcheko51x.adminbibliotecaapp.databinding.ActivityVistaPrestamosBinding;

import java.util.List;

public class VistaPrestamos extends AppCompatActivity {

    private ActivityVistaPrestamosBinding binding;

    List<Prestamo> listaPrestamos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaPrestamosBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);

        binding.rvPrestamos.setLayoutManager(new GridLayoutManager(this, 1));

        obtenerPrestamos();
    }

    private void obtenerPrestamos() {

    }
}