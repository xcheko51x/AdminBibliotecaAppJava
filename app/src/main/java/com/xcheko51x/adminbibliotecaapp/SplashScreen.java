package com.xcheko51x.adminbibliotecaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.xcheko51x.adminbibliotecaapp.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {

    int DURACION_PANTALLA = 4000;
    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);

        Glide.with(this).load(R.drawable.gif_libro).centerInside().into(binding.ivSplashScreen);

        cambiarPantalla();
    }

    public void cambiarPantalla() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        }, DURACION_PANTALLA);
    }
}