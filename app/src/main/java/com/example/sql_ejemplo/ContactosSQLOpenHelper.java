package com.example.sql_ejemplo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactosSQLOpenHelper extends SQLiteOpenHelper
{
    //DEFINIR LA INFORMACION DE SU BASE DE DATOS
    static final String NOMBRE_BASE_DATOS = "contactos.db";
    static final int VERSION  = 1;

    //contructor
    public ContactosSQLOpenHelper(Context contexto)
    {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION);
    }

    //CREA SUS TABLAS PARA SU BASE DE DATOS
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String SQL_CREATE = "CREATE TABLE contactos (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, telefono TEXT)";
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int version_anterior, int nueva_version)
    {
        db.execSQL("DROP TABLE IF EXISTS contactos");
        onCreate(db);
    }
}
