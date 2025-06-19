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
    Button btnIniciarSesion, btnIrARegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.etCorreoLogin);
        etContrasena = findViewById(R.id.etContrasenaLogin);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnIrARegistro  = findViewById(R.id.btnIrARegistro);


        // Verificar si hay sesión activa
        SharedPreferences session = getSharedPreferences("sesion", MODE_PRIVATE);
        String correoSesion = session.getString("correo", null);
        String tipoSesion = session.getString("tipo", null);

        if (correoSesion != null && tipoSesion != null) {
            // Ya hay una sesión activa, redirigir a Bienvenida
            Intent intent = new Intent(LoginActivity.this, BienvenidaActivity.class);
            startActivity(intent);
            finish();
            return; // Importante: detener aquí el resto de la ejecución
        }




        btnIrARegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });


        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = etCorreo.getText().toString().trim();
                String contrasena = etContrasena.getText().toString().trim();

                if (correo.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!esCorreoValido(correo)) {
                    Toast.makeText(LoginActivity.this, "Correo no válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar con archivo
                try {
                    FileInputStream fis = openFileInput("usuarios.txt");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                    String linea;
                    boolean encontrado = false;

                    while ((linea = reader.readLine()) != null) {
                        String[] datos = linea.split(",");
                        if (datos.length == 4) {
                            String nombre = datos[0];
                            String correoGuardado = datos[1];
                            String contraGuardada = datos[2];
                            String tipo = datos[3];

                            if (correo.equals(correoGuardado) && contrasena.equals(contraGuardada)) {
                                // Guardar sesión
                                SharedPreferences session = getSharedPreferences("sesion", MODE_PRIVATE);
                                SharedPreferences.Editor editor = session.edit();
                                editor.putString("nombre", nombre);
                                editor.putString("correo", correoGuardado);
                                editor.putString("tipo", tipo);
                                editor.apply();

                                encontrado = true;
                                startActivity(new Intent(LoginActivity.this, BienvenidaActivity.class));
                                finish();
                                break;
                            }
                        }
                    }
                    reader.close();
                    fis.close();

                    if (!encontrado) {
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Error al leer archivo", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    public boolean esCorreoValido(String correo) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches();
    }
}