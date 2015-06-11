package com.mauriciohincapie.rancherito;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Alejandro on 24/05/2015.
 */
public class DataBaseManager {
    public static final String TABLE_NAME = "restaurantes";
    public static final String CN_ID = "_id";  // Nombre columna
    public static final String CN_NAME = "nombre";
    public static final String CN_LONGITUD = "longitud";
    public static final String CN_LATITUD = "latitud";
    // create table contactos(
    //                          _id integer primary key autoincrement,
    //                          nombre text not null,
    //                          telefono text);
    public static final String CREATE_TABLE = "create table "+ TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement, "
            + CN_NAME + " TEXT NOT NULL, "
            + CN_LONGITUD + " DOUBLE, "
            + CN_LATITUD + " DOUBLE "
            + ")";

    DBhelper helper;
    SQLiteDatabase db;
    public DataBaseManager(Context context) {
        helper = new DBhelper(context);
        db = helper.getWritableDatabase();
    }

    public ContentValues generarContentValues (String Nombre, String longitud,String latitud){
        ContentValues valores = new ContentValues();
        valores.put(CN_NAME,Nombre);
        valores.put(CN_LONGITUD,longitud);
        valores.put(CN_LATITUD, latitud);
        return valores;
    }

    public void insertar(String Nombre, String longitud,String latitud){
        db.insert(TABLE_NAME,null,generarContentValues(Nombre,longitud,latitud));
    }

    public Cursor cargarCursorContactos(){
        String [] columnas = new String[]{CN_ID,CN_NAME,CN_LONGITUD,CN_LATITUD};
        return db.query(TABLE_NAME,columnas,null,null,null,null,null);
    }

    public Cursor buscarContacto(String Nombre){
        String [] columnas = new String[]{CN_ID,CN_NAME,CN_LONGITUD,CN_LATITUD};
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return db.query(TABLE_NAME,columnas,CN_NAME + "=?",new String[]{Nombre},null,null,null);
    }

    public void eliminar(String nombre){
        db.delete(TABLE_NAME, CN_NAME + "=?", new String[]{nombre});
    }

    public void Modificardatos(String nombre, String nuevalongitud,String nuevalatitud){
        db.update(TABLE_NAME,generarContentValues(nombre,nuevalongitud,nuevalatitud),CN_NAME+"=?",new String[]{nombre});
    }


}
