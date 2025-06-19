package com.example.applabs6_khe_jsifontes_esalazar_isanchez;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    EditText etNombre, etCorreo, etContrasena;
    Spinner spinnerTipoUsuario;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        spinnerTipoUsuario = findViewById(R.id.spinnerTipoUsuario);
        btnRegistrar = findViewById(R.id.btnRegistrar);

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

                // Guardar en SharedPreferences
                SharedPreferences prefs = getSharedPreferences("usuarios", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nombre", nombre);
                editor.putString("correo", correo);
                editor.putString("contrasena", contrasena);
                editor.putString("tipo", tipo);
                editor.apply();

                Toast.makeText(RegistroActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();

                // Ir a Login
                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}