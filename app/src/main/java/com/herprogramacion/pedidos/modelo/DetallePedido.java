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
Descripción:   Pojo que representa la entidad Detalle Pedido
*/
public class DetallePedido {

    public String idDetallePedido; //secuencia

    public String idCabeceraPedido;

    public String idProducto;

    public int cantidad;

    public float precio;

    public CabeceraPedido cabeceraPedidoRef;

    public Producto productoRef;

    public DetallePedido(String idDetallePedido, String idCabeceraPedido,
                         String idProducto, int cantidad, float precio) {
        this.idDetallePedido=idDetallePedido;
        this.idCabeceraPedido = idCabeceraPedido;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
    }
    public DetallePedido( String idCabeceraPedido,
                         String idProducto, int cantidad, float precio) {

        this.idCabeceraPedido = idCabeceraPedido;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
    }
    public DetallePedido() {
    }

    public DetallePedido(String idDetallePedido, CabeceraPedido cabeceraPedidoRef, Producto producto, int cantidad, float precio) {
        this.idDetallePedido = idDetallePedido;
        this.productoRef = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.cabeceraPedidoRef = cabeceraPedidoRef;
        this.idCabeceraPedido=cabeceraPedidoRef.idCabeceraPedido;
        this.idProducto=producto.idProducto;
    }

    @Override
    public String toString() {
        return  "La Cabecera: "+cabeceraPedidoRef+" compró: "+cantidad+" "+productoRef +" con subtotal de: "+precio;
    }
}
