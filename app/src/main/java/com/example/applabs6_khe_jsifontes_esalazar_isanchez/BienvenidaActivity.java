package com.example.applabs6_khe_jsifontes_esalazar_isanchez;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
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

                // Crear un LinearLayout con buen espaciado
                LinearLayout layout = new LinearLayout(BienvenidaActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(48, 24, 48, 24);

                // Campo Nombre
                final EditText etNombre = new EditText(BienvenidaActivity.this);
                etNombre.setHint("Nombre");
                etNombre.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                etNombre.setPadding(16, 24, 16, 24);
                layout.addView(etNombre);

                // Campo Correo
                final EditText etCorreo = new EditText(BienvenidaActivity.this);
                etCorreo.setHint("Correo electrónico");
                etCorreo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                etCorreo.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                etCorreo.setPadding(16, 24, 16, 24);
                layout.addView(etCorreo);

                // Campo Contraseña
                final EditText etContrasena = new EditText(BienvenidaActivity.this);
                etContrasena.setHint("Contraseña");
                etContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etContrasena.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                etContrasena.setPadding(16, 24, 16, 24);
                layout.addView(etContrasena);

                builder.setView(layout);

                builder.setPositiveButton("Guardar", (dialogInterface, i) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String correo = etCorreo.getText().toString().trim();
                    String contrasena = etContrasena.getText().toString().trim();
                    String tipo = "4"; // Usuario especial

                    if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                        Toast.makeText(BienvenidaActivity.this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!esCorreoValido(correo)) {
                        Toast.makeText(BienvenidaActivity.this, "Correo no válido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String registro = nombre + "|" + correo + "|" + contrasena + "|" + tipo + "\n";

                    try {
                        FileOutputStream fos = openFileOutput("usuarios.txt", MODE_APPEND);
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



        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mostrar alerta de confirmación
                new AlertDialog.Builder(BienvenidaActivity.this)
                        .setTitle("Cerrar sesión")
                        .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
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
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });






    }

    public boolean esCorreoValido(String correo) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches();
    }
}