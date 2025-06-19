package com.example.applabs6_khe_jsifontes_esalazar_isanchez;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;

public class BienvenidaActivity extends AppCompatActivity {

    TextView tvBienvenida, tvCorreo, tvTipo;
    Button btnAdmin, btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        tvBienvenida = findViewById(R.id.tvBienvenida);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvTipo = findViewById(R.id.tvTipo);
        btnAdmin = findViewById(R.id.btnAdmin);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        SharedPreferences session = getSharedPreferences("sesion", MODE_PRIVATE);
        String nombre = session.getString("nombre", "Usuario");
        String correo = session.getString("correo", "No definido");
        String tipo = session.getString("tipo", "0");

        tvBienvenida.setText("¡Bienvenido, " + nombre + "!");
        tvCorreo.setText("Correo: " + correo);

        String textoTipo = "";
        switch (tipo) {
            case "1":
                textoTipo = "Administrador";
                btnAdmin.setVisibility(View.VISIBLE); // Mostrar botón solo si es admin
                break;
            case "2":
                textoTipo = "Usuario normal";
                break;
            case "3":
                textoTipo = "Registrador";
                break;
            case "4":
                textoTipo = "Usuario especial";
                break;
            default:
                textoTipo = "Desconocido";
        }

        tvTipo.setText("Tipo de usuario: " + textoTipo);


        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BienvenidaActivity.this);
                builder.setTitle("Crear Usuario Especial");

                // Layout del formulario
                LinearLayout layout = new LinearLayout(BienvenidaActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(20, 20, 20, 20);

                final EditText etNombre = new EditText(BienvenidaActivity.this);
                etNombre.setHint("Nombre");
                layout.addView(etNombre);

                final EditText etCorreo = new EditText(BienvenidaActivity.this);
                etCorreo.setHint("Correo");
                layout.addView(etCorreo);

                final EditText etContrasena = new EditText(BienvenidaActivity.this);
                etContrasena.setHint("Contraseña");
                layout.addView(etContrasena);

                builder.setView(layout);

                builder.setPositiveButton("Guardar", (dialogInterface, i) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String correo = etCorreo.getText().toString().trim();
                    String contrasena = etContrasena.getText().toString().trim();
                    String tipo = "4";  // Usuario especial

                    if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                        Toast.makeText(BienvenidaActivity.this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String registro = nombre + "," + correo + "," + contrasena + "," + tipo + "\n";

                    try {
                        FileOutputStream fos = openFileOutput("usuarios.txt", MODE_APPEND); // Agregar sin borrar
                        fos.write(registro.getBytes());
                        fos.close();
                        Toast.makeText(BienvenidaActivity.this, "Usuario especial guardado", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(BienvenidaActivity.this, "Error al guardar el usuario", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancelar", null);
                builder.show();
            }
        });

        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Eliminar datos de la sesión
                SharedPreferences session = getSharedPreferences("sesion", MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.clear();
                editor.apply();

                // Volver al LoginActivity
                Intent intent = new Intent(BienvenidaActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });





    }
}