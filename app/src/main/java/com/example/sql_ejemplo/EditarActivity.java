package com.example.sql_ejemplo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditarActivity extends AppCompatActivity {
    EditText input_nombre, input_telefono;
    Button boton_editar ;

    int id_actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        input_nombre = findViewById(R.id.input_nombre_editar);
        input_telefono = findViewById(R.id.input_telefono_editar);
        boton_editar = findViewById(R.id.boton_enviar_editar);

        Intent intent_actual = getIntent();
        id_actual = intent_actual.getIntExtra("id_actual", -1);

        cargarContacto(id_actual);

        boton_editar.setOnClickListener(v -> {
            editarContacto();
        });
    }

    public void editarContacto()
    {
        String nombre_valor = String.valueOf(input_nombre.getText());
        String telefono_valor = String.valueOf(input_telefono.getText());

        if (nombre_valor.isEmpty() == false & telefono_valor.isEmpty() == false)
        {
            //tiene valor tanto telefono como nombre
            ContactosSQLOpenHelper admin = new ContactosSQLOpenHelper(this);
            SQLiteDatabase db = admin.getWritableDatabase();

            ContentValues dato_update = new ContentValues();
            dato_update.put("nombre", nombre_valor);
            dato_update.put("telefono", telefono_valor);

            db.update("contactos", dato_update, "id="+id_actual, null);
            db.close();
            Toast.makeText(this, "Contacto Editado", Toast.LENGTH_SHORT).show();
            finish(); //cerramos el activity actual y nos devolvemos a detalle
        }
        else
        {
            Toast.makeText(this, "Ingrese un nombre o telefono", Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarContacto(int id)
    {
        ContactosSQLOpenHelper admin = new ContactosSQLOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor fila = db.rawQuery("SELECT id, nombre, telefono FROM contactos Where id="+id_actual, null);

        if (fila.moveToFirst())
        {
            //se encontro el elemento
            input_nombre.setText(fila.getString(1));
            input_telefono.setText(fila.getString(2));
        }
        else
        {
            //no se encontro elemento con los datos en la base de datos
            Toast.makeText(this, "No se encontro el contacto con ID "+id, Toast.LENGTH_SHORT).show();
        }

        fila.close();
        db.close();
    }
}