package com.herprogramacion.pedidos.ui;
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
Descripción: Estas es una aplicación android que permite gestionar la venta de productos.
*            Orientada únicamente hacia administradores de una tienda, los cuales serán capaces
*            de ingresar nuevos clientes, modificar las formas de pago que tendrá dicha tienda,
*            así como agregar nuevas facturas, con sus respectivo detalle.
*            Todo lo anterior mencionado será persistente mediante sqlite.
*            Finalmente está aplicación maneja estados para no eliminar información histórica,
*            estamos hablando de una aplicación con crud completo, usando fragmentos los cuales
*            se integraran con un menú, el cual irá mostrandolos según se los llame.
*/
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.herprogramacion.pedidos.R;
import com.herprogramacion.pedidos.modelo.CabeceraPedido;
import com.herprogramacion.pedidos.modelo.Cliente;
import com.herprogramacion.pedidos.modelo.DetallePedido;
import com.herprogramacion.pedidos.modelo.FormaPago;
import com.herprogramacion.pedidos.modelo.Producto;
import com.herprogramacion.pedidos.sqlite.OperacionesBaseDatos;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class ActividadDetalles extends Fragment {
    public static ActividadDetalles newInstance() {
        Bundle args = new Bundle();
        ActividadDetalles exampleFragment = new ActividadDetalles();
        exampleFragment.setArguments(args);
        return exampleFragment;
    }

    public ActividadDetalles() {
    }


    ArrayAdapter<DetallePedido> arrayAdapterDetalles; //Adaptador para enviar a una list y presentarlo en un listview

    DetallePedido detalleSeleccionado;


    private final String SCREAR = "Crear";
    private final String SACTUALIZAR = "Actualizar";
    Button btnCrear;
    Button btnEliminar;
    EditText cantidad;
    EditText precio;
    ListView listaDetalleView;


    OperacionesBaseDatos datos;


    //Variables para cargar Spinner desde SQLite
    //Clientes
    Spinner sp_cabeceras;
    String idCabecera;
    ArrayAdapter<CabeceraPedido> comboAdapter;


    //forma de Pago
    Spinner spProducto;
    String idProducto;
    ArrayAdapter<Producto> comboAdapter2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_actividad_detalle, container, false);

        //getApplicationContext().deleteDatabase("pedidos.db"); //elimina la base de datos

        datos = OperacionesBaseDatos.obtenerInstancia(getActivity()); //una instancia de conexión con la BD


        /*para el spinner Cliente */
        /*..........................................*/

        /*.......................................................*/
        btnCrear = (Button) view.findViewById(R.id.id_btnCrearDetalle);
        btnEliminar=(Button) view.findViewById(R.id.id_btn_eliminar_detalle);
        cantidad = (EditText) view.findViewById(R.id.txt_cantidad);
        precio = (EditText) view.findViewById(R.id.txt_precio);
        listaDetalleView = (ListView) view.findViewById(R.id.lista_detalles);
        listarCabecera();
        addSpinner(view);

        listaDetalleView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                detalleSeleccionado = (DetallePedido) adapterView.getItemAtPosition(i); //para guardar el objeto seleccionado
                cantidad.setText(String.valueOf(detalleSeleccionado.cantidad));
                precio.setText(String.valueOf(detalleSeleccionado.precio));
                CabeceraPedido cabeceraAux = datos.obtenerCabeceraID(detalleSeleccionado.idCabeceraPedido);
                Producto productoAux=datos.obtenerProductoID(detalleSeleccionado.idProducto);
                sp_cabeceras.setSelection(obtenerPosicionItem(sp_cabeceras, detalleSeleccionado.cabeceraPedidoRef.nombreCliente+
                        " "+detalleSeleccionado.cabeceraPedidoRef.nombreFormaPago+ " "+detalleSeleccionado.cabeceraPedidoRef.fecha));

                spProducto.setSelection(obtenerPosicionItem(spProducto, detalleSeleccionado.productoRef.nombre+" con precio unitario: "+
                        detalleSeleccionado.productoRef.precio+" stock: "+detalleSeleccionado.productoRef.existencias));
                btnCrear.setText(SACTUALIZAR);
                btnEliminar.setVisibility(View.VISIBLE);
            }
        });

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btnCrear.getText().toString().equals(SCREAR))
                    insertar();
                else {
                    actualizar();
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar();
            }
        });

        return view;
    }


    public static int obtenerPosicionItem(Spinner spinner, String valor) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String valor`
        //que lo pasaremos posteriormente
        for (int i = 0; i < spinner.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(valor)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }


    public void actualizar() {


        /*........................................................*/
        String cantidad = this.cantidad.getText().toString(); //obtiene el texto digitado por el usuario
        String precio = this.precio.getText().toString();
        try {
            Toast.makeText(getActivity(), idProducto, Toast.LENGTH_LONG).show();//mensaje para el usuario
            if (!cantidad.isEmpty() && !precio.isEmpty() && !detalleSeleccionado.idCabeceraPedido.isEmpty()) { //validamos que el texto no este vaacio
                DetallePedido dp = new DetallePedido(detalleSeleccionado.idDetallePedido,
                        idCabecera, idProducto,
                        Integer.valueOf(cantidad), Float.valueOf(precio));
                datos.getDb().beginTransaction(); //inicia la trasaccion
                datos.actualizarDetallePedido(dp);
                datos.getDb().setTransactionSuccessful();
                datos.getDb().endTransaction();
                limpiarTexto();
                listarCabecera();
                Toast.makeText(getActivity(), "Actualizado Correctamente", Toast.LENGTH_LONG).show();//mensaje para el usuario
            } else {
                Toast.makeText(getActivity(), "Algo salió mal", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Algo salió mal" + ex.getMessage(), Toast.LENGTH_LONG).show(); //por si algo sale mal
        } finally {
            btnCrear.setText(SCREAR);
            btnEliminar.setVisibility(View.INVISIBLE);
        }

        /*.........................................................*/

    }

    public void limpiarTexto() {
        cantidad.setText("");
        precio.setText("");

    }

    public void insertar() {
        /*...........................................*/


        String cantidad = this.cantidad.getText().toString();
        String precio = this.precio.getText().toString();
        if (!cantidad.isEmpty()&&!precio.isEmpty()) { //verificamos que no este vacío
            datos.getDb().beginTransaction(); //iniciamos transaccion
            datos.insertarDetallePedido(new DetallePedido(idCabecera, idProducto,Integer.valueOf(cantidad),Float.valueOf(precio)));
            datos.getDb().setTransactionSuccessful(); //exito de la trasaccion
            datos.getDb().endTransaction();//cierra la transaccion
            limpiarTexto();
            listarCabecera(); //una vez insertado, actualiza la lista
            Toast.makeText(getActivity(), "Insertado Correctamente", Toast.LENGTH_LONG).show(); //mensaje para el ussuario
        } else {
            Toast.makeText(getActivity(), "Texto vacío", Toast.LENGTH_LONG).show();
        }
        /*............................................*/

    }

    public void eliminar(){

        try {
            if (!detalleSeleccionado.idDetallePedido.isEmpty()) {
                datos.getDb().beginTransaction();
                datos.eliminarDetallePedido(detalleSeleccionado.idDetallePedido);
                datos.getDb().setTransactionSuccessful(); //exito de la trasaccion
                datos.getDb().endTransaction();//cierra la transaccion
                limpiarTexto();
                listarCabecera(); //una vez insertado, actualiza la lista
                Toast.makeText(getActivity(), "Eliminado Correctamente", Toast.LENGTH_LONG).show(); //mensaje para el ussuario
            }else{
                Toast.makeText(getActivity(), "Algo salió mal", Toast.LENGTH_LONG).show(); //mensaje para el ussuario
            }
        }catch (Exception ex){
            Toast.makeText(getActivity(), "Algo salió mal "+ex.getMessage(), Toast.LENGTH_LONG).show(); //mensaje para el ussuario
        }finally {
        btnEliminar.setVisibility(View.INVISIBLE);
        btnCrear.setText(SCREAR);
        }


    }

    public void addSpinner(View view) {
        sp_cabeceras = (Spinner) view.findViewById(R.id.id_cabeceras);

        spProducto = (Spinner) view.findViewById(R.id.id_productos);

        //Implemento el setOnItemSelectedListener: para realizar acciones cuando se seleccionen los ítems
        sp_cabeceras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (adapterView.getId()) {
                    case R.id.id_cabeceras:
                        //Almaceno el id del cliente seleccionado
                        idCabecera = datos.obtenerCabecerasPedidos2().get(i).idCabeceraPedido;


                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (adapterView.getId()) {
                    case R.id.id_productos:
                        idProducto = datos.obtenerProductos().get(i).idProducto;

                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<CabeceraPedido> listaCabecera = datos.obtenerCabecerasPedidos2(); //lista de clientes que se mostrarán en listview
        if (!listaCabecera.isEmpty()) {
            List<CabeceraPedido> listaAuxiliar = new ArrayList<>();
            CabeceraPedido cpAux = null;
            for (CabeceraPedido cp : listaCabecera) {
                cpAux = new CabeceraPedido(cp.idCabeceraPedido, cp.fecha, cp.idCliente,
                        cp.idFormaPago, datos.obtenerClientespoID2(cp.idCliente).nombres,
                        datos.obtenerFormaID(cp.idFormaPago).nombre);
                listaAuxiliar.add(cpAux);
            }

            comboAdapter = new ArrayAdapter<CabeceraPedido>(getActivity(), android.R.layout.simple_spinner_item, listaAuxiliar);
            //Cargo el spinner con los datos
            sp_cabeceras.setAdapter(comboAdapter);

        }



        /*FORMA DE PAGO..................................................................*/
        List<Producto> listaProducto = datos.obtenerProductosFiltroEstado();


        comboAdapter2 = new ArrayAdapter<Producto>(getActivity(), android.R.layout.simple_spinner_item, listaProducto);
        //Cargo el spinner con los datos
        spProducto.setAdapter(comboAdapter2);

    }

    //para listar todos los datos de la cabecera
    public void listarCabecera() {
        List<DetallePedido> listaDetalleP = datos.obtenerDetallesPedido(); //lista de detalles de Pedidos que se mostrarán en listview
        if (!listaDetalleP.isEmpty()) {
            List<DetallePedido> listaAuxiliar = new ArrayList<>();
            DetallePedido dpAux = null;

            for (DetallePedido dp : listaDetalleP) {
                dpAux = new DetallePedido(dp.idDetallePedido, datos.obtenerCabeceraID(dp.idCabeceraPedido),
                       datos.obtenerProductoID(dp.idProducto),
                        dp.cantidad,dp.precio);
                listaAuxiliar.add(dpAux);
            }

            arrayAdapterDetalles = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listaAuxiliar);

            listaDetalleView.setAdapter(arrayAdapterDetalles);

        }
    }


}
