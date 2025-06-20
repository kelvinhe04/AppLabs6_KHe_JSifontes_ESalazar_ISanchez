package com.example.applabs6_khe_jsifontes_esalazar_isanchez;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class RegistroActivity extends AppCompatActivity {

    EditText etNombre, etCorreo, etContrasena;
    Spinner spinnerTipoUsuario;
    Button btnRegistrar, btnVolverLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        spinnerTipoUsuario = findViewById(R.id.spinnerTipoUsuario);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolverLogin = findViewById(R.id.btnVolverLogin);

        // Cargar opciones del spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"1 - Administrador", "2 - Usuario", "3 - Registrador"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoUsuario.setAdapter(adapter);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = etNombre.getText().toString().trim();
                String correo = etCorreo.getText().toString().trim();
                String contrasena = etContrasena.getText().toString().trim();
                String tipo = spinnerTipoUsuario.getSelectedItem().toString().split(" ")[0];

                if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(RegistroActivity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!esCorreoValido(correo)) {
                    Toast.makeText(RegistroActivity.this, "Correo no válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    FileInputStream fis = openFileInput("usuarios.txt");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                    String linea;
                    boolean correoExistente = false;

                    while ((linea = reader.readLine()) != null) {
                        String[] datos = linea.split("\\|");
                        if (datos.length >= 2 && datos[1].equalsIgnoreCase(correo)) {
                            correoExistente = true;
                            break;
                        }
                    }

                    reader.close();
                    fis.close();

                    if (correoExistente) {
                        Toast.makeText(RegistroActivity.this, "El correo ya está registrado. Inicie sesión.", Toast.LENGTH_LONG).show();

                        // Redirigir al login
                        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        return; // Detener registro
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }






                // Guardar en archivo (usuarios.txt)
                String registro = nombre + "|" + correo + "|" + contrasena + "|" + tipo + "\n";
                try {
                    FileOutputStream fos = openFileOutput("usuarios.txt", MODE_APPEND); // Agrega al archivo
                    fos.write(registro.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(RegistroActivity.this, "Error al guardar el usuario", Toast.LENGTH_SHORT).show();
                    return;
                }


                Toast.makeText(RegistroActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();

                // Ir a pantalla de bienvenida
                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                finish();
            }
        });


        btnVolverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }

    public boolean esCorreoValido(String correo) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches();
    }
}