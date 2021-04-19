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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.herprogramacion.pedidos.R;
import com.herprogramacion.pedidos.modelo.Cliente;
import com.herprogramacion.pedidos.sqlite.OperacionesBaseDatos;

import java.util.List;


@SuppressLint("NewApi")
public class ActividadListaPedidos extends Fragment {

    public static ActividadListaPedidos newInstance() {
        Bundle args = new Bundle();
        ActividadListaPedidos exampleFragment = new ActividadListaPedidos();
        exampleFragment.setArguments(args);
        return exampleFragment;
    }

    public ActividadListaPedidos() {
    }

    /*...........................*/
    //Creación de variable de instancia de clase

    ArrayAdapter<Cliente> arrayAdapterCliente; //Adaptador para enviar a una list y presentarlo en un listview
    EditText nombre_cliente;
    EditText apellido_cliente;
    EditText telefono_cliente;
    ListView listaClientesVista; //componente android para mostrar la lista
    Cliente clienteSeleccionado; //objeto cliente que se enlaza a la BD
    OperacionesBaseDatos datos; //objeto que hace refencia a la clase que maneja la BD
    private final String SCREAR = "Crear";
    private final String SACTUALIZAR = "Actualizar";
    Button crear;
    CheckBox estado;
    /*.................................*/
    //Se ejecuta cuando inicia la app


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_actividad_lista_pedidos, container, false);
        nombre_cliente = (EditText) view.findViewById(R.id.txt_nombre);//enlazamos al editTex Nombre de la vista
        apellido_cliente = (EditText) view.findViewById(R.id.txt_apellidos); //enlazamos al editTex Apellido de la vista
        telefono_cliente = (EditText) view.findViewById(R.id.txt_telefono);// enlazamos al editTex Telefono de la vista
        listaClientesVista = (ListView) view.findViewById(R.id.lista_clientes); //enlazamos la lista de la vista
        crear = (Button) view.findViewById(R.id.btn_crear);

        estado = (CheckBox) view.findViewById(R.id.id_checkC); //enlazamos al check del estado

        // getActivity().deleteDatabase("pedidos.db"); //elimina la base de datos
        datos = OperacionesBaseDatos.obtenerInstancia(getActivity()); //una instancia de conexión con la BD
        listarClientes(); //metodo para listar clientes, al inicio de la aplicación
        //Evento para obtener un elemento de la lista que se seleccione en la vista
        listaClientesVista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                estado.setVisibility(View.VISIBLE); //hacemos visible el check

                clienteSeleccionado = (Cliente) adapterView.getItemAtPosition(i); //para guardar el objeto seleccionado
                nombre_cliente.setText(clienteSeleccionado.nombres); //envia a la caja de texto lo seleccionado con el usuario
                apellido_cliente.setText(clienteSeleccionado.apellidos);
                telefono_cliente.setText(String.valueOf(clienteSeleccionado.telefono));
                estado.setChecked(clienteSeleccionado.estado == 1 ? true : false);
                crear.setText(SACTUALIZAR);

            }
        });
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (crear.getText().toString().equals(SCREAR)) {
                    insertar();
                } else {
                    actualizar();
                    estado.setVisibility(View.INVISIBLE); //perdemos el estado del check
                }
            }
        });
        return view;
    }


    //Método para listar clientes
    public void listarClientes() {
        /*..................................................................*/
        List<Cliente> listaClientes = datos.obtenerClientes2(); //lista de clientes que se mostrarán en listview
        arrayAdapterCliente = new ArrayAdapter<Cliente>(getActivity(), android.R.layout.simple_list_item_1, listaClientes);
        listaClientesVista.setAdapter(arrayAdapterCliente);
        /*...................................................................*/

    }

    //para limpiar la caja de texto despues de cada actualización o inserción
    public void limpiarTexto() {
        nombre_cliente.setText("");
        apellido_cliente.setText("");
        telefono_cliente.setText("");
    }

    //Método para insertar registros en la tabla Clientes
    public void insertar() {
        /*...........................................*/

        String nombreCliente = this.nombre_cliente.getText().toString(); //obtiene el texto digitado por el usuario
        String apellidoCliente = this.apellido_cliente.getText().toString();
        String telefonoCliente = this.telefono_cliente.getText().toString();

        if (!nombreCliente.isEmpty()) { //verificamos que no este vacío
            datos.getDb().beginTransaction(); //iniciamos transaccion
            datos.insertarCliente(new Cliente(nombreCliente, apellidoCliente, telefonoCliente,1));
            datos.getDb().setTransactionSuccessful(); //exito de la trasaccion
            datos.getDb().endTransaction();//cierra la transaccion
            limpiarTexto();
            listarClientes(); //una vez insertado, actualiza la lista
            Toast.makeText(getActivity(), "Insertado Correctamente", Toast.LENGTH_LONG).show(); //mensaje para el ussuario
        } else {
            Toast.makeText(getActivity(), "Texto vacío", Toast.LENGTH_LONG).show();
        }
        /*............................................*/

    }

    //Método para actualizar registros en la tabla formas de pago
    public void actualizar() {


        /*........................................................*/
        String nombreCliente = this.nombre_cliente.getText().toString(); //obtiene el texto digitado por el usuario
        String apellidoCliente = this.apellido_cliente.getText().toString();
        String telefonoCliente = this.telefono_cliente.getText().toString();
        int estado=this.estado.isChecked()?1:0;
        try {
            if (!nombreCliente.isEmpty() && !clienteSeleccionado.idCliente.isEmpty()) { //validamos que el texto no este vaacio
                Cliente cliente = new Cliente(clienteSeleccionado.idCliente, nombreCliente, apellidoCliente, telefonoCliente,estado);
                datos.getDb().beginTransaction(); //inicia la trasaccion
                datos.actualizarCliente(cliente);
                datos.getDb().setTransactionSuccessful();
                datos.getDb().endTransaction();
                limpiarTexto();
                listarClientes();
                Toast.makeText(getActivity(), "Actualizado Correctamente", Toast.LENGTH_LONG).show();//mensaje para el usuario
            } else {
                Toast.makeText(getActivity(), "Algo salió mal", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Algo salió mal", Toast.LENGTH_LONG).show(); //por si algo sale mal
        } finally {
            crear.setText(SCREAR);
        }

        /*.........................................................*/

    }


}
