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
Descripción:   Pojo que representa la entidad Cabecera Pedido
*/
public class CabeceraPedido {

    public String idCabeceraPedido;

    public String fecha;

    public String idCliente;

    public String idFormaPago;

    public String nombreCliente;
    public String nombreFormaPago;

    public CabeceraPedido(String idCabeceraPedido, String fecha,
                          String idCliente, String idFormaPago) {
        this.idCabeceraPedido = idCabeceraPedido;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idFormaPago = idFormaPago;
    }

    public CabeceraPedido(String fecha,
                          String idCliente, String idFormaPago) {
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idFormaPago = idFormaPago;
    }

    public CabeceraPedido(String idCabeceraPedido,String fecha, String idCliente, String idFormaPago, String nombreCliente, String nombreFormaPago) {
        this.idCabeceraPedido = idCabeceraPedido;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idFormaPago = idFormaPago;
        this.nombreCliente = nombreCliente;
        this.nombreFormaPago = nombreFormaPago;
    }

    public CabeceraPedido(String idCabeceraPedido,String fecha, String nombreCliente, String nombreFormaPago, int modo) {
        this.idCabeceraPedido = idCabeceraPedido;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idFormaPago = idFormaPago;
        this.nombreCliente = nombreCliente;
        this.nombreFormaPago = nombreFormaPago;
    }
    public CabeceraPedido(String fecha, String nombreCliente, String nombreFormaPago, int op) {

        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idFormaPago = idFormaPago;
        this.nombreCliente = nombreCliente;
        this.nombreFormaPago = nombreFormaPago;
    }
    @Override
    public String toString() {
        return nombreCliente+" "+nombreFormaPago+ " "+fecha;
    }
}
