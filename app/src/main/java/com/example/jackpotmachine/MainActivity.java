package com.example.jackpotmachine;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int pulsaciones = 0;
    private int puntuacion = 0;

    // Class variables to store the current Ores
    private int idOre1, idOre2, idOre3;

    private TextView tvPulsaciones, tvPuntuacion;
    private ImageView img1, img2, img3;
    private AnimationDrawable anim1, anim2, anim3;

    private MediaPlayer mpFondo;
    private MediaPlayer mpGiro;

    private final int[] oresMinecraft = {
            R.drawable.amethyst, R.drawable.coal, R.drawable.copper,
            R.drawable.diamond, R.drawable.emerald, R.drawable.glowstone,
            R.drawable.gold, R.drawable.iron, R.drawable.lapis_lazuli,
            R.drawable.quartz, R.drawable.redstone, R.drawable.ender_pearl
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Musica
        mpFondo = MediaPlayer.create(this, R.raw.kasino_cantgetover);
        mpFondo.setLooping(true);
        mpFondo.start();

        mpGiro = MediaPlayer.create(this, R.raw.case_opening);
        mpGiro.setLooping(true);

        // Initialize Views
        tvPulsaciones = findViewById(R.id.tvPulsaciones);
        tvPuntuacion = findViewById(R.id.tvPuntuacion);
        img1 = findViewById(R.id.imgMineral1);
        img2 = findViewById(R.id.imgMineral2);
        img3 = findViewById(R.id.imgMineral3);
        Button btnJugar = findViewById(R.id.btnJugar);
        Button btnParar = findViewById(R.id.btnParar);

        // 1. Requirement: Start with random ores
        generarOresAleatorios();

        btnJugar.setOnClickListener(v -> {
            //Sonido de la ruleta
            if (!mpGiro.isPlaying()) {
                mpGiro.start();
            }
            pulsaciones++;
            actualizarTextoPulsaciones();

            // Set animation resource and start spinning
            img1.setImageResource(R.drawable.animacion_ore);
            img2.setImageResource(R.drawable.animacion_ore);
            img3.setImageResource(R.drawable.animacion_ore);

            anim1 = (AnimationDrawable) img1.getDrawable();
            anim2 = (AnimationDrawable) img2.getDrawable();
            anim3 = (AnimationDrawable) img3.getDrawable();

            anim1.start();
            anim2.start();
            anim3.start();
        });

        btnParar.setOnClickListener(v -> {
            if (mpGiro.isPlaying()) {
                mpGiro.pause();
                mpGiro.seekTo(0);
            }
            // Stop animations if they are running
            if (anim1 != null) {
                anim1.stop();
                anim2.stop();
                anim3.stop();
            }

            // Generate the final random result
            generarOresAleatorios();

            // 2. Requirement: Use a separate function for points
            procesarResultadoPuntos();
        });
    }

    private void generarOresAleatorios() {
        Random random = new Random();

        // Select 3 random indices
        idOre1 = oresMinecraft[random.nextInt(oresMinecraft.length)];
        idOre2 = oresMinecraft[random.nextInt(oresMinecraft.length)];
        idOre3 = oresMinecraft[random.nextInt(oresMinecraft.length)];

        // Display them in the UI
        img1.setImageResource(idOre1);
        img2.setImageResource(idOre2);
        img3.setImageResource(idOre3);
    }

    private void procesarResultadoPuntos() {
        // Rule: 3 equal = 10 points; 2 equal = 3 points
        if (idOre1 == idOre2 && idOre2 == idOre3) {
            puntuacion += 10;
        } else if (idOre1 == idOre2 || idOre2 == idOre3 || idOre1 == idOre3) {
            puntuacion += 3;
        }

        actualizarTextoPuntuacion();
    }

    private void actualizarTextoPulsaciones() {
        String texto = getResources().getQuantityString(R.plurals.contador_pulsaciones, pulsaciones, pulsaciones);
        tvPulsaciones.setText(texto);
    }

    private void actualizarTextoPuntuacion() {
        String texto = getResources().getQuantityString(R.plurals.contador_puntuacion, puntuacion, puntuacion);
        tvPuntuacion.setText(texto);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mpFondo != null && mpFondo.isPlaying()) mpFondo.pause();
        if (mpGiro != null && mpGiro.isPlaying()) mpGiro.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mpFondo != null) mpFondo.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar memoria
        if (mpFondo != null) {
            mpFondo.release();
            mpFondo = null;
        }
        if (mpGiro != null) {
            mpGiro.release();
            mpGiro = null;
        }

    }
}