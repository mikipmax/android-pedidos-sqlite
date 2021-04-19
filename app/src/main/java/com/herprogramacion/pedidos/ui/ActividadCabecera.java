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
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import java.util.Calendar;
import java.util.List;

@SuppressLint("NewApi")
public class ActividadCabecera extends Fragment {
    public static ActividadCabecera newInstance() {
        Bundle args = new Bundle();
        ActividadCabecera exampleFragment = new ActividadCabecera();
        exampleFragment.setArguments(args);
        return exampleFragment;
    }

    public ActividadCabecera() {
    }


    ArrayAdapter<CabeceraPedido> arrayAdapterCabPedido; //Adaptador para enviar a una list y presentarlo en un listview

    CabeceraPedido cabeceraSeleccionado;


    private final String SCREAR = "Crear";
    private final String SACTUALIZAR = "Actualizar";
    Button btnCrear;
    EditText txtDate;
    ListView listaCabeceraView;


    OperacionesBaseDatos datos;


    //Variables para cargar Spinner desde SQLite
    //Clientes
    Spinner sp_cliente;
    String idCliente, nombreCliente, apellidoCliente;
    ArrayAdapter<Cliente> comboAdapter;


    //forma de Pago
    Spinner spFormaPago;
    String idFormpg, formpg;
    ArrayAdapter<FormaPago> comboAdapter2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_actividad_cabecera, container, false);

        //getApplicationContext().deleteDatabase("pedidos.db"); //elimina la base de datos

        datos = OperacionesBaseDatos.obtenerInstancia(getActivity()); //una instancia de conexión con la BD


        /*para el spinner Cliente */
        /*..........................................*/

        /*.......................................................*/
        btnCrear = (Button) view.findViewById(R.id.id_buttonCrear);
        txtDate = (EditText) view.findViewById(R.id.txt_fecha);
        listaCabeceraView = (ListView) view.findViewById(R.id.lista_resultados);
        listarCabecera();
        addSpinner(view);

        listaCabeceraView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cabeceraSeleccionado = (CabeceraPedido) adapterView.getItemAtPosition(i); //para guardar el objeto seleccionado
                txtDate.setText(cabeceraSeleccionado.fecha);
                Cliente clienteAux = datos.obtenerClientespoID2(cabeceraSeleccionado.idCliente);
                sp_cliente.setSelection(obtenerPosicionItem(sp_cliente, "Nombres: " +
                        clienteAux.nombres +
                        " " + clienteAux.apellidos + ". " +
                        "Teléfono: " + clienteAux.telefono));

                spFormaPago.setSelection(obtenerPosicionItem(spFormaPago, cabeceraSeleccionado.nombreFormaPago));
                btnCrear.setText(SACTUALIZAR);
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
        String fecha = this.txtDate.getText().toString(); //obtiene el texto digitado por el usuario
        try {
            if (!fecha.isEmpty() && !cabeceraSeleccionado.idCabeceraPedido.isEmpty()) { //validamos que el texto no este vaacio
                CabeceraPedido cp = new CabeceraPedido(cabeceraSeleccionado.idCabeceraPedido, fecha, idCliente, idFormpg);
                datos.getDb().beginTransaction(); //inicia la trasaccion
                datos.actualizarCabeceraPedido(cp);
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
        }

        /*.........................................................*/

    }

    public void limpiarTexto() {
        txtDate.setText("");

    }

    public void insertar() {
        /*...........................................*/


        // Toast.makeText(this, "Texto vacío", Toast.LENGTH_LONG).show();
        String fecha = this.txtDate.getText().toString();

        if (!fecha.isEmpty()) { //verificamos que no este vacío
            datos.getDb().beginTransaction(); //iniciamos transaccion
            datos.insertarCabeceraPedido(new CabeceraPedido(fecha, idCliente, idFormpg));
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

    public void addSpinner(View view) {
        sp_cliente = (Spinner) view.findViewById(R.id.id_clientes);
        /*para el spinner Forma de Pago*/
        spFormaPago = (Spinner) view.findViewById(R.id.id_formaPago);

        //Implemento el setOnItemSelectedListener: para realizar acciones cuando se seleccionen los ítems
        sp_cliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (adapterView.getId()) {
                    case R.id.id_clientes:
                        //Almaceno el id del cliente seleccionado
                        idCliente = datos.obtenerClientes2().get(i).idCliente;

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spFormaPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (adapterView.getId()) {
                    case R.id.id_formaPago:
                        idFormpg = datos.obtenerFormaPago2().get(i).idFormaPago;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<Cliente> listaClientes = datos.obtenerClientesFiltroEstado(); //lista de clientes que se mostrarán en listview


        comboAdapter = new ArrayAdapter<Cliente>(getActivity(), android.R.layout.simple_spinner_item, listaClientes);
        //Cargo el spinner con los datos
        sp_cliente.setAdapter(comboAdapter);

        /*FORMA DE PAGO..................................................................*/
        List<FormaPago> listaFormaPago = datos.obtenerFormaPagoFiltroEstado();


        comboAdapter2 = new ArrayAdapter<FormaPago>(getActivity(), android.R.layout.simple_spinner_item, listaFormaPago);
        //Cargo el spinner con los datos
        spFormaPago.setAdapter(comboAdapter2);

    }

    //para listar todos los datos de la cabecera
    public void listarCabecera() {
        List<CabeceraPedido> listaCabeceraF = datos.obtenerCabecerasPedidos2(); //lista de detalles de Pedidos que se mostrarán en listview
        if (!listaCabeceraF.isEmpty()) {
            List<CabeceraPedido> listaAuxiliar = new ArrayList<>();
            CabeceraPedido cpAux = null;
            for (CabeceraPedido cp : listaCabeceraF) {
                cpAux = new CabeceraPedido(cp.idCabeceraPedido, cp.fecha, cp.idCliente,
                        cp.idFormaPago, datos.obtenerClientespoID2(cp.idCliente).nombres,
                        datos.obtenerFormaID(cp.idFormaPago).nombre);
                listaAuxiliar.add(cpAux);
            }

            arrayAdapterCabPedido = new ArrayAdapter<CabeceraPedido>(getActivity(), android.R.layout.simple_list_item_1, listaAuxiliar);

            listaCabeceraView.setAdapter(arrayAdapterCabPedido);

        }
    }


}
