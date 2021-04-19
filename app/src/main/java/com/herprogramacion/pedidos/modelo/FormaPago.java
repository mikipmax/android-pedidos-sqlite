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
Descripción:   Pojo que representa la entidad Forma de Pago
*/
public class FormaPago {

    public String idFormaPago;

    public String nombre;
    public int estado;
    //constructor que se llama cuando se va a actualizar y se requiere el id
    public FormaPago(String idFormaPago, String nombre, int estado) {
        this.idFormaPago = idFormaPago;
        this.nombre = nombre;
        this.estado=estado;
    }


    public FormaPago(String nombre,int estado) {

        this.nombre = nombre;
this.estado=estado;
    }
    @Override
    public String toString() {
        return this.nombre;
    }
}
