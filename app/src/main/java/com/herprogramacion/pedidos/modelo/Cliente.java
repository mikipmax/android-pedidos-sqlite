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
Descripción:   Pojo que representa la entidad Cliente
*/
public class Cliente {

    public String idCliente;

    public String nombres;

    public String apellidos;

    public String telefono;

    public int estado;

    public Cliente(String idCliente, String nombres, String apellidos, String telefono, int estado) {
        this.idCliente = idCliente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.estado=estado;
    }


    public Cliente(String nombres, String apellidos) {

        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public Cliente(String nombres, String apellidos, String telefono,int estado) {

        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.estado=estado;
    }

    public Cliente() {

    }


    @Override
    public String toString() {
        return "Nombres: " + nombres + " " + apellidos + ". " + "Teléfono: " + telefono;
    }


}
