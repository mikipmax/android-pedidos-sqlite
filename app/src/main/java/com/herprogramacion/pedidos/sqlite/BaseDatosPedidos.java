package com.herprogramacion.pedidos.sqlite;
/*
***UNIVERSIDAD CENTRAL DEL ECUADOR***
***FACULTAD DE INGENIERIA Y CIENCIAS APLICADAS***
***CARRERAS: INGENIERÍA EN COMPUTACIÓN GRÁFICA​E INGENIERÍA INFORMÁTICA***
***MATERIA: DISPOSITIVOS MÓVILES***

Grupo N: 5
Integrantes:  -Nicolalde Estefanía ​Correo: jenicolaldep@uce.edu.ec
              -Ponce Michael​​Correo: mfponce@uce.edu.ec
              -Sánchez Jonathan​​Correo: jjsanchezl1@uce.edu.ec
              -Tituaña Mayra​Correo: mrtituana@uce.edu.ec
Descripción:   Clase que administra la conexión de la base de datos y su estructuración
*/
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import com.herprogramacion.pedidos.sqlite.ContratoPedidos.CabecerasPedido;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos.Clientes;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos.DetallesPedido;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos.FormasPago;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos.Productos;


public class BaseDatosPedidos extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DATOS = "pedidos.db";

    private static final int VERSION_ACTUAL = 1;

    private final Context contexto;

    interface Tablas {
        String CABECERA_PEDIDO = "cabecera_pedido";
        String DETALLE_PEDIDO = "detalle_pedido";
        String PRODUCTO = "producto";
        String CLIENTE = "cliente";
        String FORMA_PAGO = "forma_pago";
    }

    interface Referencias {

        String ID_CABECERA_PEDIDO = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                Tablas.CABECERA_PEDIDO, CabecerasPedido.ID);

        String ID_PRODUCTO = String.format("REFERENCES %s(%s)",
                Tablas.PRODUCTO, Productos.ID);

        String ID_CLIENTE = String.format("REFERENCES %s(%s)",
                Tablas.CLIENTE, Clientes.ID);

        String ID_FORMA_PAGO = String.format("REFERENCES %s(%s)",
                Tablas.FORMA_PAGO, FormasPago.ID);
    }

    public BaseDatosPedidos(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s DATETIME NOT NULL,%s TEXT NOT NULL %s," +
                        "%s TEXT NOT NULL %s)",
                Tablas.CABECERA_PEDIDO, BaseColumns._ID,
                CabecerasPedido.ID, CabecerasPedido.FECHA,
                CabecerasPedido.ID_CLIENTE, Referencias.ID_CLIENTE,
                CabecerasPedido.ID_FORMA_PAGO, Referencias.ID_FORMA_PAGO));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,"+
                        "%s TEXT NOT NULL %s," +
                        "%s TEXT NOT NULL %s, " +
                        "%s INTEGER NOT NULL," +
                        "%s REAL NOT NULL)",
                Tablas.DETALLE_PEDIDO, BaseColumns._ID,
                DetallesPedido.ID,
                DetallesPedido.ID_CABECERA, Referencias.ID_CABECERA_PEDIDO,
                DetallesPedido.ID_PRODUCTO, Referencias.ID_PRODUCTO,
                DetallesPedido.CANTIDAD,
                DetallesPedido.PRECIO
        ));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s REAL NOT NULL," +
                        "%s INTEGER NOT NULL CHECK(%s>=0),%s INTEGER NOT NULL)",
                Tablas.PRODUCTO, BaseColumns._ID,
                Productos.ID, Productos.NOMBRE, Productos.PRECIO,
                Productos.EXISTENCIAS, Productos.EXISTENCIAS, Productos.ESTADO));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL,%s INTEGER NOT NULL)",
                Tablas.CLIENTE, BaseColumns._ID,
                Clientes.ID, Clientes.NOMBRES, Clientes.APELLIDOS, Clientes.TELEFONO, Clientes.ESTADO));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s INTEGER NOT NULL)",
                Tablas.FORMA_PAGO, BaseColumns._ID,
                FormasPago.ID, FormasPago.NOMBRE, FormasPago.ESTADO));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CABECERA_PEDIDO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.DETALLE_PEDIDO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.PRODUCTO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CLIENTE);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.FORMA_PAGO);

        onCreate(db);
    }


}