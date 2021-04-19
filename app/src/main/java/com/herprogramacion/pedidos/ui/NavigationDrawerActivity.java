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
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.herprogramacion.pedidos.R;

import static com.herprogramacion.pedidos.ui.ActividadListaPedidos.newInstance;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.avigation_drawer_open, R.string.avigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Elegimos el fragmento de Cliente por default al iniciar la app
        if (savedInstanceState == null) {
            ActividadListaPedidos fragment = ActividadListaPedidos.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit();
        }
    }

    //Para ocultar o presentar un menú
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


    //Cuando doy click en un item del menu, voy mostrando los layouts de acuerdo al item seleccionado.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


//Aquí podemos cambiar el texto que se muestra en el background
        switch (item.getItemId()) {
            case R.id.nav_cab_pedido:
                obtenerFragmento(ActividadCabecera.newInstance());
                break;
            case R.id.nav_cliente:
                obtenerFragmento(ActividadListaPedidos.newInstance());
                break;
            case R.id.nav_det_pedido:
                obtenerFragmento(ActividadDetalles.newInstance());
                break;
            case R.id.nav_forma_pago:
                obtenerFragmento(ActividadFormas.newInstance());
                break;
            case R.id.nav_producto:
                obtenerFragmento(ActividadProductos.newInstance());
                break;
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void obtenerFragmento(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main, fragment)
                .addToBackStack("")
                .commit();
    }

}
