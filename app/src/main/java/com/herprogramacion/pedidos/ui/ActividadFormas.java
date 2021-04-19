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
import android.view.MenuItem;

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
public class ActividadFormas extends Fragment {
    public static ActividadFormas newInstance() {
        Bundle args = new Bundle();
        ActividadFormas exampleFragment = new ActividadFormas();
        exampleFragment.setArguments(args);
        return exampleFragment;
    }

    public ActividadFormas() {
    }

    //Creacción de variables de instancia de clase

    ArrayAdapter<FormaPago> arrayAdapterForma; // Adaptador para enviar a una List y presentarlo en un ListView
    EditText formaPago; //Caja de texto
    ListView listaFormaPago; //componente android para mostrar una lista de cosas
    FormaPago formaPagoSeleccionado; // objeto FormaPago que esta enlazado a bd
    OperacionesBaseDatos datos; // Objeto que hace referencia a la clase que maneja la bd

    private final String SCREAR = "Crear";
    private final String SACTUALIZAR = "Actualizar";
    Button crear;
    CheckBox estado;

    //Se ejecuta cuando inicia la app
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_actividad_formas, container, false);

        formaPago = (EditText) view.findViewById(R.id.txt_forma); //enlazamos el objeto formaPago con el componente EditText de la vista
        listaFormaPago = (ListView) view.findViewById(R.id.lista_formas); //enlazamos la listaFormaPago con el componente ListView de la vista

        estado=(CheckBox) view.findViewById(R.id.id_checkFP); //enlazamos al check del estado
        //getActivity().deleteDatabase("pedidos.db"); //Se elimina los datos de la bd
        datos = OperacionesBaseDatos.obtenerInstancia(getActivity()); //se consigue una insntancia de conexión con la bd
        crear = (Button) view.findViewById(R.id.id_btnCrearFormas);
        listarFormaPago(); //llamamos metodo para listar formas de pago, apenas inicie la app

        //Este evento nos permite obtener un elemento de la lista que el usuario seleccione en la vista
        listaFormaPago.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {






                formaPagoSeleccionado = (FormaPago) adapterView.getItemAtPosition(i); //guardamos el objeto seleccionado
                estado.setVisibility(View.VISIBLE); //hacemos visible el check
                estado.setChecked(formaPagoSeleccionado.estado==1?true:false);
                formaPago.setText(formaPagoSeleccionado.nombre); // le enviamos a nuestra caja de texto lo que selecciono el usuario
                crear.setText(SACTUALIZAR);
            }
        });
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (crear.getText().toString().equals(SCREAR))
                    insertar();
                else{
                    actualizar();
                    estado.setVisibility(View.INVISIBLE); //perdemos el estado del check
                }

            }
        });
        return view;
    }


    //Método que lista las formas de pago
    public void listarFormaPago() {

         List<FormaPago> listaForma = datos.obtenerFormaPago2();


        ////preparamos arrayadapter para enviarle nuestra arraylist de formas de pago
        arrayAdapterForma = new ArrayAdapter<FormaPago>(getActivity(), android.R.layout.simple_list_item_1, listaForma);
        listaFormaPago.setAdapter(arrayAdapterForma);
    }

    //para limpiar la caja de texto despues de cada actualización o inserción
    public void limpiarTexto() {
        formaPago.setText("");
    }

    //Método para insertar registros en la tabla formas de pago
    public void insertar() {
        String formaPago = this.formaPago.getText().toString(); //obtenemos lo que el usuario digita en la caja de texto
        if (!formaPago.isEmpty()) { //verificamos que no este vacio la caja de texto
            datos.getDb().beginTransaction(); //iniciamos transacción

            datos.insertarFormaPago(new FormaPago(formaPago,1)); //insertamos en bd la forma de pago
            datos.getDb().setTransactionSuccessful(); //indicamos que ha sido un éxito la inserción
            datos.getDb().endTransaction(); //cerramos la transacción
            limpiarTexto();
            listarFormaPago(); //una vez que se inserte actualizamos la lista
            Toast.makeText(getActivity(), "Insertado Correctamente", Toast.LENGTH_LONG).show(); //mensaje para el usuario
        }else{
            Toast.makeText(getActivity(), "Texto vacio", Toast.LENGTH_LONG).show();
        }
    }
 //Método para actualizar registros en la tabla formas de pago
    public void actualizar() {
        String txtFormaPago = this.formaPago.getText().toString(); //obtenemos lo que el usuario digite
        int estadoAux=estado.isChecked()?1:0;
        try {
            if (!txtFormaPago.isEmpty() &&!formaPagoSeleccionado.idFormaPago.isEmpty()) { //validamos que el texto no este vacio y que si se trata de una actualización en base al id que no sea nulo
                FormaPago formaPago = new FormaPago(formaPagoSeleccionado.idFormaPago, txtFormaPago, estadoAux ); //almacenamos los cambios de la forma de pago con su respectivo id

                datos.getDb().beginTransaction(); //iniciamos la transacción
                datos.actualizarFormaPago(formaPago); //actualizamos el registro
                datos.getDb().setTransactionSuccessful(); //en pocas un commit
                datos.getDb().endTransaction(); //cerramos la transacción
                limpiarTexto();
                listarFormaPago();
                Toast.makeText(getActivity(), "Actualizado Correctamente", Toast.LENGTH_LONG).show();//mensaje para el usuario
            } else {
                Toast.makeText(getActivity(), "Algo salió mal", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Algo salió mal", Toast.LENGTH_LONG).show(); //por si algo sale mal
        }finally {
            crear.setText(SCREAR); //indicamos al usuario que ha vuelto al modo insertar
        }

    }
}
