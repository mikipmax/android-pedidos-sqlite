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
Descripción: * Clase auxiliar que implementa a {@link BaseDatosPedidos} para llevar a cabo el CRUD
*              sobre las entidades existentes.
*/
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.herprogramacion.pedidos.modelo.CabeceraPedido;
import com.herprogramacion.pedidos.modelo.Cliente;
import com.herprogramacion.pedidos.modelo.DetallePedido;
import com.herprogramacion.pedidos.modelo.FormaPago;
import com.herprogramacion.pedidos.modelo.Producto;
import com.herprogramacion.pedidos.sqlite.BaseDatosPedidos.Tablas;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos.CabecerasPedido;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos.Clientes;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos.DetallesPedido;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos.FormasPago;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos.Productos;

import java.util.ArrayList;
import java.util.List;
public final class OperacionesBaseDatos {

    private static BaseDatosPedidos baseDatos;

    private static OperacionesBaseDatos instancia = new OperacionesBaseDatos();


    private OperacionesBaseDatos() {
    }

    public static OperacionesBaseDatos obtenerInstancia(Context contexto) {
        if (baseDatos == null) {
            baseDatos = new BaseDatosPedidos(contexto);
        }
        return instancia;
    }

    // [OPERACIONES_CABECERA_PEDIDO]
    public Cursor obtenerCabecerasPedidos() {


        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.CABECERA_PEDIDO);

        return db.rawQuery(sql, null);

    }


    public List<CabeceraPedido> obtenerCabecerasPedidos2() {
        Cursor cursorCabecera = obtenerCabecerasPedidos();
        String id_cabecera;
        String idClien;
        String idFrmP;
        String fecha;
        List<CabeceraPedido> lista = new ArrayList<>();
        cursorCabecera.moveToFirst(); //apunta el primer registro de la tabla
        while (!cursorCabecera.isAfterLast()) { //permite recorrer todos los registros
            id_cabecera = cursorCabecera.getString(cursorCabecera.getColumnIndex("id")); //almacena el id de cliente
            fecha = cursorCabecera.getString(cursorCabecera.getColumnIndex("fecha"));

            idClien = cursorCabecera.getString(cursorCabecera.getColumnIndex("id_cliente"));
            idFrmP = cursorCabecera.getString(cursorCabecera.getColumnIndex("id_forma_pago"));
            lista.add(new CabeceraPedido(id_cabecera, fecha, idClien, idFrmP));
            cursorCabecera.moveToNext(); //mueve al siguiente registro
        }
        return lista;
    }

    //Sirve para mostrar en el spinner, mediante join con la tabla cliente y forma de pago
    public CabeceraPedido obtenerCabeceraID(String id) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.CABECERA_PEDIDO, CabecerasPedido.ID);

        String[] selectionArgs = {id};

        Cursor cursor = db.rawQuery(sql, selectionArgs);

        String nombreFormaPago;
        String nombreCliente;
        String fecha;
        CabeceraPedido cabecera = null;
        cursor.moveToFirst(); //apunta el primer registro de la tabla


        while (!cursor.isAfterLast()) { //permite recorrer todos los registros


            fecha = cursor.getString(cursor.getColumnIndex("fecha")); //almacenamos valores de la columna
            nombreCliente = obtenerClientespoID2(cursor.getString(cursor.getColumnIndex("id_cliente"))).nombres;
            nombreFormaPago = obtenerFormaID(cursor.getString(cursor.getColumnIndex("id_forma_pago"))).nombre;

                cabecera = new CabeceraPedido(id, fecha, nombreCliente, nombreFormaPago,0);


            cursor.moveToNext(); //mueve al siguiente registro
        }

        return cabecera;

    }


    public String insertarCabeceraPedido(CabeceraPedido pedido) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Generar Pk
        String idCabeceraPedido = CabecerasPedido.generarIdCabeceraPedido();

        ContentValues valores = new ContentValues();
        valores.put(CabecerasPedido.ID, idCabeceraPedido);
        valores.put(CabecerasPedido.FECHA, pedido.fecha);
        valores.put(CabecerasPedido.ID_CLIENTE, pedido.idCliente);
        valores.put(CabecerasPedido.ID_FORMA_PAGO, pedido.idFormaPago);

        // Insertar cabecera
        db.insertOrThrow(Tablas.CABECERA_PEDIDO, null, valores);

        return idCabeceraPedido;
    }

    public boolean actualizarCabeceraPedido(CabeceraPedido pedidoNuevo) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(CabecerasPedido.FECHA, pedidoNuevo.fecha);
        valores.put(CabecerasPedido.ID_CLIENTE, pedidoNuevo.idCliente);
        valores.put(CabecerasPedido.ID_FORMA_PAGO, pedidoNuevo.idFormaPago);

        String whereClause = String.format("%s=?", CabecerasPedido.ID);
        String[] whereArgs = {pedidoNuevo.idCabeceraPedido};

        int resultado = db.update(Tablas.CABECERA_PEDIDO, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarCabeceraPedido(String idCabeceraPedido) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = CabecerasPedido.ID + "=?";
        String[] whereArgs = {idCabeceraPedido};

        int resultado = db.delete(Tablas.CABECERA_PEDIDO, whereClause, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_CABECERA_PEDIDO]

    // [OPERACIONES_DETALLE_PEDIDO]
    public List<DetallePedido> obtenerDetallesPedido() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.DETALLE_PEDIDO);

        Cursor cursor = db.rawQuery(sql, null);

        String idDetalle;
        String idCabecera;
        String idProducto;
        int cantidad;
        float precio;
        DetallePedido detallePedido = null;

        List<DetallePedido> lista = new ArrayList<>();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) { //permite recorrer todos los registros
            idDetalle = cursor.getString(cursor.getColumnIndex("id")); //almacenamos valores de la columna
            idCabecera = cursor.getString(cursor.getColumnIndex("id_cabecera"));
            idProducto = cursor.getString(cursor.getColumnIndex("id_producto"));
            cantidad = cursor.getInt(cursor.getColumnIndex("cantidad"));
            precio = cursor.getFloat(cursor.getColumnIndex("precio"));
            detallePedido = new DetallePedido(idDetalle, idCabecera, idProducto, cantidad, precio);
            lista.add(detallePedido);
            cursor.moveToNext(); //mueve al siguiente registro
        }
        return lista;
    }


    public String insertarDetallePedido(DetallePedido detalle) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Generar Pk
        String idDetallePedido = DetallesPedido.generarIdDetallePedido();

        ContentValues valores = new ContentValues();
        valores.put(DetallesPedido.ID, idDetallePedido);
        valores.put(DetallesPedido.ID_CABECERA, detalle.idCabeceraPedido);
        valores.put(DetallesPedido.ID_PRODUCTO, detalle.idProducto);
        valores.put(DetallesPedido.CANTIDAD, detalle.cantidad);
        valores.put(DetallesPedido.PRECIO, detalle.precio);

        db.insertOrThrow(Tablas.DETALLE_PEDIDO, null, valores);

        //return String.format("%s#%d", detalle.idCabeceraPedido, detalle.secuencia);
        return idDetallePedido;
    }

    public boolean actualizarDetallePedido(DetallePedido detalle) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DetallesPedido.ID_CABECERA, detalle.idCabeceraPedido);
        valores.put(DetallesPedido.ID_PRODUCTO, detalle.idProducto);
        valores.put(DetallesPedido.CANTIDAD, detalle.cantidad);
        valores.put(DetallesPedido.PRECIO, detalle.precio);

        String selection = String.format("%s=?", DetallesPedido.ID);
        String[] whereArgs = {detalle.idDetallePedido};

        int resultado = db.update(Tablas.DETALLE_PEDIDO, valores, selection, whereArgs);

        return resultado > 0;


    }

    public boolean eliminarDetallePedido(String idDetallePedido) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = DetallesPedido.ID + "=?";
        String[] whereArgs = {idDetallePedido};

        int resultado = db.delete(Tablas.DETALLE_PEDIDO, whereClause, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_DETALLE_PEDIDO]

    // [OPERACIONES_PRODUCTO]
    public List<Producto> obtenerProductos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.PRODUCTO);

        Cursor cursor = db.rawQuery(sql, null);
        List<Producto> lista = new ArrayList<>();
        String id;
        String nombre;
        int existencias;
        Float precio;
        int estado;
        Producto producto = null;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            id = cursor.getString(cursor.getColumnIndex("id"));
            nombre = cursor.getString(cursor.getColumnIndex("nombre"));
            precio = cursor.getFloat(cursor.getColumnIndex("precio"));
            existencias = cursor.getInt(cursor.getColumnIndex("existencias"));
            estado=cursor.getInt(cursor.getColumnIndex("estado"));
            producto = new Producto(id, nombre, precio, existencias,estado);
            lista.add(producto);
            cursor.moveToNext();
        }
      return lista;
    }


    public List<Producto> obtenerProductosFiltroEstado() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE ESTADO=1", Tablas.PRODUCTO);

        Cursor cursor = db.rawQuery(sql, null);
        List<Producto> lista = new ArrayList<>();
        String id;
        String nombre;
        int existencias;
        Float precio;
        int estado;
        Producto producto = null;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            id = cursor.getString(cursor.getColumnIndex("id"));
            nombre = cursor.getString(cursor.getColumnIndex("nombre"));
            precio = cursor.getFloat(cursor.getColumnIndex("precio"));
            existencias = cursor.getInt(cursor.getColumnIndex("existencias"));
            estado=cursor.getInt(cursor.getColumnIndex("estado"));
            producto = new Producto(id, nombre, precio, existencias,estado);
            lista.add(producto);
            cursor.moveToNext();
        }
        return lista;
    }

    public Producto obtenerProductoID(String id) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.PRODUCTO, Productos.ID);

        String[] selectionArgs = {id};

        Cursor cursor = db.rawQuery(sql, selectionArgs);
        String nombre;
        Float precio;
        int existencias;
        int estado;
        Producto producto = null;
        cursor.moveToFirst(); //apunta el primer registro de la tabla


        while (!cursor.isAfterLast()) { //permite recorrer todos los registros
            nombre = cursor.getString(cursor.getColumnIndex("nombre")); //almacenamos valores de la columna Nombres de Clientes
            precio=cursor.getFloat(cursor.getColumnIndex("precio"));
            existencias=cursor.getInt(cursor.getColumnIndex("existencias"));
            estado=cursor.getInt(cursor.getColumnIndex("estado"));
            producto = new Producto(id,nombre,precio,existencias,estado);
            cursor.moveToNext(); //mueve al siguiente registro
        }

        return producto;

    }


    public String insertarProducto(Producto producto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        // Generar Pk
        String idProducto = Productos.generarIdProducto();
        valores.put(Productos.ID, idProducto);
        valores.put(Productos.NOMBRE, producto.nombre);
        valores.put(Productos.PRECIO, producto.precio);
        valores.put(Productos.EXISTENCIAS, producto.existencias);
        valores.put(Productos.ESTADO, producto.estado);

        db.insertOrThrow(Tablas.PRODUCTO, null, valores);

        return idProducto;

    }

    public boolean actualizarProducto(Producto producto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Productos.NOMBRE, producto.nombre);
        valores.put(Productos.PRECIO, producto.precio);
        valores.put(Productos.EXISTENCIAS, producto.existencias);
        valores.put(Productos.ESTADO, producto.estado);
        String whereClause = String.format("%s=?", Productos.ID);
        String[] whereArgs = {producto.idProducto};

        int resultado = db.update(Tablas.PRODUCTO, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarProducto(String idProducto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Productos.ID);
        String[] whereArgs = {idProducto};

        int resultado = db.delete(Tablas.PRODUCTO, whereClause, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_PRODUCTO]

    // [OPERACIONES_CLIENTE]
    public Cursor obtenerClientes() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.CLIENTE);

        return db.rawQuery(sql, null);
    }

    public List<Cliente> obtenerClientes2() {
        Cursor cursorClientes = obtenerClientes();
        String idCliente;
        String nombre;
        String apellido;
        String telefono;
        int estado;
        List<Cliente> listaClientes = new ArrayList<>();

        cursorClientes.moveToFirst(); //apunta el primer registro de la tabla

        while (!cursorClientes.isAfterLast()) { //permite recorrer todos los registros
            telefono = cursorClientes.getString(cursorClientes.getColumnIndex("telefono")); //almacenamos valores de la columna Nombres de Clientes
            nombre = cursorClientes.getString(cursorClientes.getColumnIndex("nombres")); //almacenamos valores de la columna Nombres de Clientes
            apellido = cursorClientes.getString(cursorClientes.getColumnIndex("apellidos"));
            idCliente = cursorClientes.getString(cursorClientes.getColumnIndex("id")); //almacena el id de cliente
            estado=cursorClientes.getInt(cursorClientes.getColumnIndex("estado"));
            listaClientes.add(new Cliente(idCliente, nombre, apellido, telefono,estado));
            cursorClientes.moveToNext(); //mueve al siguiente registro
        }

        return listaClientes;
    }

    public List<Cliente> obtenerClientesFiltroEstado() {

        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE ESTADO=1", Tablas.CLIENTE);


        Cursor cursorClientes =  db.rawQuery(sql, null);
        String idCliente;
        String nombre;
        String apellido;
        String telefono;
        int estado;
        List<Cliente> listaClientes = new ArrayList<>();

        cursorClientes.moveToFirst(); //apunta el primer registro de la tabla

        while (!cursorClientes.isAfterLast()) { //permite recorrer todos los registros
            telefono = cursorClientes.getString(cursorClientes.getColumnIndex("telefono")); //almacenamos valores de la columna Nombres de Clientes
            nombre = cursorClientes.getString(cursorClientes.getColumnIndex("nombres")); //almacenamos valores de la columna Nombres de Clientes
            apellido = cursorClientes.getString(cursorClientes.getColumnIndex("apellidos"));
            idCliente = cursorClientes.getString(cursorClientes.getColumnIndex("id")); //almacena el id de cliente
            estado=cursorClientes.getInt(cursorClientes.getColumnIndex("estado"));
            listaClientes.add(new Cliente(idCliente, nombre, apellido, telefono,estado));
            cursorClientes.moveToNext(); //mueve al siguiente registro
        }

        return listaClientes;
    }




    public Cursor obtenerClienteporID(String id) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.CLIENTE, Clientes.ID);

        String[] selectionArgs = {id};

        return db.rawQuery(sql, selectionArgs);

    }


    public Cliente obtenerClientespoID2(String id) {
        Cursor cursorClientes = obtenerClienteporID(id);
        String idCliente;
        String nombre;
        String apellido;
        String telefono;
        int estado;
        // List<Cliente> listaClientes=new ArrayList<>();
        Cliente miCliente = new Cliente();

        cursorClientes.moveToFirst(); //apunta el primer registro de la tabla


        while (!cursorClientes.isAfterLast()) { //permite recorrer todos los registros
            nombre = cursorClientes.getString(2); //almacenamos valores de la columna Nombres de Clientes
            apellido = cursorClientes.getString(3);
            telefono = cursorClientes.getString(4);
            estado=cursorClientes.getInt(cursorClientes.getColumnIndex("estado"));
            miCliente = new Cliente(nombre, apellido, telefono,estado);
            cursorClientes.moveToNext(); //mueve al siguiente registro
        }

        return miCliente;
    }

    public String insertarCliente(Cliente cliente) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Generar Pk
        String idCliente = Clientes.generarIdCliente();

        ContentValues valores = new ContentValues();
        valores.put(Clientes.ID, idCliente);
        valores.put(Clientes.NOMBRES, cliente.nombres);
        valores.put(Clientes.APELLIDOS, cliente.apellidos);
        valores.put(Clientes.TELEFONO, cliente.telefono);
        valores.put(Clientes.ESTADO, cliente.estado);

        return db.insertOrThrow(Tablas.CLIENTE, null, valores) > 0 ? idCliente : null;
    }

    public boolean actualizarCliente(Cliente cliente) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Clientes.NOMBRES, cliente.nombres);
        valores.put(Clientes.APELLIDOS, cliente.apellidos);
        valores.put(Clientes.TELEFONO, cliente.telefono);
        valores.put(Clientes.ESTADO, cliente.estado);
        String whereClause = String.format("%s=?", Clientes.ID);
        final String[] whereArgs = {cliente.idCliente};

        int resultado = db.update(Tablas.CLIENTE, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarCliente(String idCliente) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Clientes.ID);
        final String[] whereArgs = {idCliente};

        int resultado = db.delete(Tablas.CLIENTE, whereClause, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_CLIENTE]

    // [OPERACIONES_FORMA_PAGO]
    public Cursor obtenerFormasPago() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.FORMA_PAGO);

        return db.rawQuery(sql, null);
    }

    public List<FormaPago> obtenerFormaPago2() {
        Cursor cursorFormaPago = obtenerFormasPago();
        String idFormaPago;
        String nombre;
        int estado;
        List<FormaPago> listaFormasPago = new ArrayList<>();
        cursorFormaPago.moveToFirst(); //apunta el primer registro de la tabla
        while (!cursorFormaPago.isAfterLast()) { //permite recorrer todos los registros
            idFormaPago = cursorFormaPago.getString(cursorFormaPago.getColumnIndex("id"));
            nombre = cursorFormaPago.getString(cursorFormaPago.getColumnIndex("nombre")); //almacenamos valores de la columna Nombres de Forma de Pago
            estado=cursorFormaPago.getInt(cursorFormaPago.getColumnIndex("estado"));
            listaFormasPago.add(new FormaPago(idFormaPago, nombre, estado));
            cursorFormaPago.moveToNext(); //mueve al siguiente registro
        }
        return listaFormasPago;
    }

    public List<FormaPago> obtenerFormaPagoFiltroEstado(){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE ESTADO=1", Tablas.FORMA_PAGO);

        Cursor cursor= db.rawQuery(sql, null);
        String idFormaPago;
        String nombre;
        int estado;
        List<FormaPago> listaFormasPago = new ArrayList<>();
        cursor.moveToFirst(); //apunta el primer registro de la tabla
        while (!cursor.isAfterLast()) { //permite recorrer todos los registros
            idFormaPago = cursor.getString(cursor.getColumnIndex("id"));
            nombre = cursor.getString(cursor.getColumnIndex("nombre")); //almacenamos valores de la columna Nombres de Forma de Pago
            estado=cursor.getInt(cursor.getColumnIndex("estado"));
            listaFormasPago.add(new FormaPago(idFormaPago, nombre, estado));
            cursor.moveToNext(); //mueve al siguiente registro
        }
        return listaFormasPago;
    }

    public FormaPago obtenerFormaID(String id) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.FORMA_PAGO, FormasPago.ID);

        String[] selectionArgs = {id};

        Cursor cursor = db.rawQuery(sql, selectionArgs);
        String nombreFormaPago;
        int estado;
        FormaPago formaPago = null;
        cursor.moveToFirst(); //apunta el primer registro de la tabla


        while (!cursor.isAfterLast()) { //permite recorrer todos los registros
            nombreFormaPago = cursor.getString(cursor.getColumnIndex("nombre")); //almacenamos valores de la columna Nombres de Clientes
            estado=cursor.getInt(cursor.getColumnIndex("estado"));
            formaPago = new FormaPago(nombreFormaPago,estado);
            cursor.moveToNext(); //mueve al siguiente registro
        }

        return formaPago;

    }

    public String insertarFormaPago(FormaPago formaPago) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Generar Pk
        String idFormaPago = FormasPago.generarIdFormaPago();

        ContentValues valores = new ContentValues();
        valores.put(FormasPago.ESTADO, formaPago.estado);
        valores.put(FormasPago.ID, idFormaPago);
        valores.put(FormasPago.NOMBRE, formaPago.nombre);

        return db.insertOrThrow(Tablas.FORMA_PAGO, null, valores) > 0 ? idFormaPago : null;
    }

    public boolean actualizarFormaPago(FormaPago formaPago) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(FormasPago.NOMBRE, formaPago.nombre);
        valores.put(FormasPago.ESTADO, formaPago.estado);
        String whereClause = String.format("%s=?", FormasPago.ID);
        String[] whereArgs = {formaPago.idFormaPago};

        int resultado = db.update(Tablas.FORMA_PAGO, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarFormaPago(String idFormaPago) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", FormasPago.ID);
        String[] whereArgs = {idFormaPago};

        int resultado = db.delete(Tablas.FORMA_PAGO, whereClause, whereArgs);

        return resultado > 0;
    }

    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }


    // [/OPERACIONES_FORMA_PAGO]


    private final String[] proyCabeceraPedido = new String[]{
            Tablas.CABECERA_PEDIDO + "." + CabecerasPedido.ID,
            CabecerasPedido.FECHA,
            Clientes.NOMBRES,
            Clientes.APELLIDOS,
            FormasPago.NOMBRE};

}
