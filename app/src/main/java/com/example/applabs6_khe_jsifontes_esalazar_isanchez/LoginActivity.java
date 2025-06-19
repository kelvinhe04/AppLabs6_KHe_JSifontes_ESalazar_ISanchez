package com.example.applabs6_khe_jsifontes_esalazar_isanchez;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;

public class LoginActivity extends AppCompatActivity {

    EditText etCorreo, etContrasena;
    Button btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.etCorreoLogin);
        etContrasena = findViewById(R.id.etContrasenaLogin);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = etCorreo.getText().toString().trim();
                String contrasena = etContrasena.getText().toString().trim();

                if (correo.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences prefs = getSharedPreferences("usuarios", MODE_PRIVATE);
                String correoGuardado = prefs.getString("correo", "");
                String contrasenaGuardada = prefs.getString("contrasena", "");

                if (correo.equals(correoGuardado) && contrasena.equals(contrasenaGuardada)) {
                    // Usuario normal
                    SharedPreferences session = getSharedPreferences("sesion", MODE_PRIVATE);
                    SharedPreferences.Editor editor = session.edit();
                    editor.putString("nombre", prefs.getString("nombre", ""));
                    editor.putString("correo", correoGuardado);
                    editor.putString("tipo", prefs.getString("tipo", ""));
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, BienvenidaActivity.class));
                    finish();
                    return;
                }

                // Validar con archivo
                try {
                    FileInputStream fis = openFileInput("usuarioEspecial.txt");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                    String linea = reader.readLine();
                    fis.close();

                    if (linea != null) {
                        String[] datos = linea.split(",");
                        if (datos.length == 4) {
                            String nombreArchivo = datos[0];
                            String correoArchivo = datos[1];
                            String contrasenaArchivo = datos[2];
                            String tipoArchivo = datos[3];

                            if (correo.equals(correoArchivo) && contrasena.equals(contrasenaArchivo)) {
                                SharedPreferences session = getSharedPreferences("sesion", MODE_PRIVATE);
                                SharedPreferences.Editor editor = session.edit();
                                editor.putString("nombre", nombreArchivo);
                                editor.putString("correo", correoArchivo);
                                editor.putString("tipo", tipoArchivo);
                                editor.apply();

                                startActivity(new Intent(LoginActivity.this, BienvenidaActivity.class));
                                finish();
                                return;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}