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
import android.widget.TextView;
import android.widget.Toast;

import com.herprogramacion.pedidos.R;
import com.herprogramacion.pedidos.modelo.Producto;
import com.herprogramacion.pedidos.sqlite.OperacionesBaseDatos;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class ActividadProductos extends Fragment {
    public static ActividadProductos newInstance() {
        Bundle args = new Bundle();
        ActividadProductos exampleFragment = new ActividadProductos();
        exampleFragment.setArguments(args);
        return exampleFragment;
    }

    public ActividadProductos() {
    }

    //Creacción de variables de instancia de clase

    ArrayAdapter<Producto> arrayAdapterProducto; // Adaptador para enviar a una List y presentarlo en un ListView
    EditText producto; //Caja de texto

    EditText precio;
    EditText existencias;
    ListView listaProductoVista; //componente android para mostrar una lista de cosas
    Producto ProductoSeleccionado; // objeto FormaPago que esta enlazado a bd
    OperacionesBaseDatos datos; // Objeto que hace referencia a la clase que maneja la bd
    CheckBox estado;

    private final String SCREAR = "Crear";
    private final String SACTUALIZAR = "Actualizar";
    Button crear;

    //Se ejecuta cuando inicia la app
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_actividad_productos, container, false);

        producto = (EditText) view.findViewById(R.id.txt_nombre); //enlazamos el objeto formaPago con el componente EditText de la vista
        precio = (EditText) view.findViewById(R.id.txt_precio);
        existencias = (EditText) view.findViewById(R.id.txt_existencias);
        listaProductoVista = (ListView) view.findViewById(R.id.lista_productos); //enlazamos la listaFormaPago con el componente ListView de la vista
        crear = (Button) view.findViewById(R.id.id_btnCrearProd);
        estado = (CheckBox) view.findViewById(R.id.id_CheckP); //enlazamos al check del estado

        datos = OperacionesBaseDatos.obtenerInstancia(getActivity()); //se consigue una insntancia de conexión con la bd

        listarProductos(); //llamamos metodo para listar formas de pago, apenas inicie la app

        //Este evento nos permite obtener un elemento de la lista que el usuario seleccione en la vista
        listaProductoVista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                estado.setVisibility(View.VISIBLE); //hacemos visible el check
                ProductoSeleccionado = (Producto) adapterView.getItemAtPosition(i); //guardamos el objeto seleccionado
                producto.setText(ProductoSeleccionado.nombre); // le enviamos a nuestra caja de texto lo que selecciono el usuario
                precio.setText(String.valueOf(ProductoSeleccionado.precio));
                existencias.setText(String.valueOf(ProductoSeleccionado.existencias));
                estado.setChecked(ProductoSeleccionado.estado == 1 ? true : false);
                crear.setText(SACTUALIZAR); //se le indica al usuario en que modo esta trabajando
            }
        });
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (crear.getText().toString().equals(SCREAR))
                    insertar();
                else {
                    actualizar();
                    estado.setVisibility(View.INVISIBLE); //perdemos el estado del check/
                }
            }
        });
        return view;
    }


    //Método que lista las formas de pago
    public void listarProductos() {
        List<Producto> listaProducto = datos.obtenerProductos();

        ////preparamos arrayadapter para enviarle nuestra arraylist de formas de pago
        if (!listaProducto.isEmpty()) {
            arrayAdapterProducto = new ArrayAdapter<Producto>(getActivity(), android.R.layout.simple_list_item_1, listaProducto);
            listaProductoVista.setAdapter(arrayAdapterProducto);
        }
    }

    //para limpiar la caja de texto despues de cada actualización o inserción
    public void limpiarTexto() {
        producto.setText("");
        precio.setText("");
        existencias.setText("");
    }

    //Método para insertar registros en la tabla formas de pago
    public void insertar() {
        String producto = this.producto.getText().toString(); //obtenemos lo que el usuario digita en la caja de texto
        float precio = Float.valueOf(this.precio.getText().toString());
        int existencias = Integer.valueOf(this.existencias.getText().toString());
        if (!producto.isEmpty()) { //verificamos que no este vacio la caja de texto
            datos.getDb().beginTransaction(); //iniciamos transacción

            datos.insertarProducto(new Producto(producto, precio, existencias,1)); //insertamos en bd la forma de pago
            datos.getDb().setTransactionSuccessful(); //indicamos que ha sido un éxito la inserción
            datos.getDb().endTransaction(); //cerramos la transacción
            limpiarTexto();
            listarProductos(); //una vez que se inserte actualizamos la lista
            Toast.makeText(getActivity(), "Insertado Correctamente", Toast.LENGTH_LONG).show(); //mensaje para el usuario
        } else {
            Toast.makeText(getActivity(), "Texto vacio", Toast.LENGTH_LONG).show();
        }
    }

    //Método para actualizar registros en la tabla formas de pago
    public void actualizar() {
        String txtFormaPago = this.producto.getText().toString(); //obtenemos lo que el usuario digite
        int estado=this.estado.isChecked()?1:0;
        float precio = Float.valueOf(this.precio.getText().toString());
        int existencias = Integer.valueOf(this.existencias.getText().toString());
        try {
            if (!txtFormaPago.isEmpty() && !ProductoSeleccionado.idProducto.isEmpty()) { //validamos que el texto no este vacio y que si se trata de una actualización en base al id que no sea nulo
                Producto producto = new Producto(ProductoSeleccionado.idProducto, txtFormaPago, precio, existencias,estado); //almacenamos los cambios de la forma de pago con su respectivo id

                datos.getDb().beginTransaction(); //iniciamos la transacción
                datos.actualizarProducto(producto); //actualizamos el registro
                datos.getDb().setTransactionSuccessful(); //en pocas un commit
                datos.getDb().endTransaction(); //cerramos la transacción
                limpiarTexto();
                listarProductos();
                Toast.makeText(getActivity(), "Actualizado Correctamente", Toast.LENGTH_LONG).show();//mensaje para el usuario
            } else {
                Toast.makeText(getActivity(), "Algo salió mal", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Algo salió mal", Toast.LENGTH_LONG).show(); //por si algo sale mal
        } finally {
            crear.setText(SCREAR); //indicamos al usuario que ha vuelto al modo insertar
        }

    }
}
