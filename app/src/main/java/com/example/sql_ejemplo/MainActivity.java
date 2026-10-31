package com.example.sql_ejemplo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText input_nombre, input_telefono;
    Button boton_crear ;
    ListView lista_contactos ;

    List<Integer> id_contactos  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //buscamos nuestros inputs en el XML
        input_nombre = findViewById(R.id.input_nombre);
        input_telefono = findViewById(R.id.input_telefono);
        boton_crear = findViewById(R.id.boton_crear);
        lista_contactos = findViewById(R.id.lista_contactos);

        //iniciar carga de informacion de la base de datos a la tabla
        cargarListaContactos();

        boton_crear.setOnClickListener(v -> {
            crearContacto();
        });

        //detectar el click de listview y cargar el contacto asociado
        lista_contactos.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int posicion, long id){
                int id_actual = id_contactos.get(posicion);

                Intent intent_detalle = new Intent(MainActivity.this, DetalleActivity.class);
                intent_detalle.putExtra("id_actual", id_actual);
                startActivity(intent_detalle);
            }
        });
    }

    public void crearContacto()
    {
        String nombre_actual = String.valueOf(input_nombre.getText());
        String telefono_actual = String.valueOf(input_telefono.getText());

        ContactosSQLOpenHelper admin = new ContactosSQLOpenHelper(this);
        SQLiteDatabase db = admin.getWritableDatabase(); //como necesitamos escribir es writable

        if (nombre_actual.isEmpty() == false & telefono_actual.isEmpty() == false)
        {
            //escribieron algo en telefono o nombre entonces agregamos
            ContentValues registro = new ContentValues();
            registro.put("nombre", nombre_actual);
            registro.put("telefono", telefono_actual);

            //insertar este registro en la base de datos
            db.insert("contactos", null, registro);
            db.close();

            //restablecer inputs
            input_telefono.setText("");
            input_nombre.setText("");

            //volver a cargar la lista de datos desde la base de datos
            cargarListaContactos();
        }
        else
        {
            Toast.makeText(this, "Ingrese un nombre o telefono para continuar", Toast.LENGTH_LONG).show();
        }
    }

    public void cargarListaContactos()
    {
        ContactosSQLOpenHelper admin = new ContactosSQLOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        ArrayList<String> listaContactos = new ArrayList<>();
        id_contactos = new ArrayList<>();
        //Preparar consulta para el Select
        Cursor fila = db.rawQuery("SELECT nombre,telefono, id FROM contactos", null);

        if (fila.moveToFirst())
        {
            do
            {
                //se va a ejecutar del primer elemento hasta el ultimo que encuentro con move to next
                String contacto_texto = "Nombre : "+fila.getString(0)+" //// Telefono : "+fila.getString(1);
                listaContactos.add(contacto_texto);
                id_contactos.add(fila.getInt(2));
            } while(fila.moveToNext() );
        }
        else{
            listaContactos.add("No se encontraron contactos en la base de datos");
        }
        //cerramos la conexion a la base de datos
        fila.close(); //cerramos el cursos en si
        db.close(); //cerramos la base de datos

        //cargar informacion a ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaContactos);
        lista_contactos.setAdapter(adapter);
    }
}