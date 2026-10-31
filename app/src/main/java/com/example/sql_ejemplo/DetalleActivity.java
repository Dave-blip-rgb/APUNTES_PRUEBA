package com.example.sql_ejemplo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetalleActivity extends AppCompatActivity {

    private int id_actual ;
    TextView txt_id, txt_nombre, txt_telefono;
    Button boton_editar , boton_eliminar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //inicialiar XML
        txt_id = findViewById(R.id.txt_id);
        txt_nombre = findViewById(R.id.txt_nombre);
        txt_telefono = findViewById(R.id.txt_telefono);
        boton_editar = findViewById(R.id.boton_editar);
        boton_eliminar = findViewById(R.id.boton_eliminar);

        Intent intent_actual = getIntent();
        int id_actual = intent_actual.getIntExtra("id_actual", -1);
        cargarContacto(id_actual);

        boton_eliminar.setOnClickListener(v -> {
            eliminarContacto(id_actual);
        });

        boton_editar.setOnClickListener(v -> {
            Intent intent_editar = new Intent(DetalleActivity.this, EditarActivity.class);
            intent_editar.putExtra("id_actual", id_actual);
            startActivity(intent_editar);
        });
    }

    public void cargarContacto(int id)
    {
        ContactosSQLOpenHelper admin = new ContactosSQLOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor fila = db.rawQuery("SELECT id, nombre, telefono FROM contactos Where id="+id_actual, null);

        if (fila.moveToFirst())
        {
            //se encontro el elemento
            txt_id.setText("# "+fila.getInt(0));
            txt_nombre.setText("Nombre : "+fila.getString(1));
            txt_telefono.setText("Telefono : "+fila.getString(2));
        }
        else
        {
            //no se encontro elemento con los datos en la base de datos
            Toast.makeText(this, "No se encontro el contacto con ID "+id, Toast.LENGTH_SHORT).show();
        }

        fila.close();
        db.close();
    }

    public void eliminarContacto(int id)
    {
        ContactosSQLOpenHelper admin = new ContactosSQLOpenHelper(this);
        SQLiteDatabase db = admin.getWritableDatabase();

        db.delete("contactos", "id="+id_actual, null);
        db.close();

        Toast.makeText(this, "El contacto fue eliminado", Toast.LENGTH_SHORT).show();
        finish(); //finaliza el activity actual
    }
}