package com.herprogramacion.pedidos.modelo;

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
Descripción:   Pojo que representa la entidad Producto
*/
public class Producto {

    public String idProducto;

    public String nombre;

    public float precio;

    public int existencias;
    public int estado;
    public Producto(String idProducto, String nombre, float precio, int existencias, int estado) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.existencias = existencias;
        this.estado=estado;
    }
    public Producto( String nombre, float precio, int existencias,int estado) {

        this.nombre = nombre;
        this.precio = precio;
        this.existencias = existencias;
        this.estado=estado;
    }

    @Override
    public String toString() {
        return nombre+" con precio unitario: "+precio+" stock: "+existencias;
    }
}
