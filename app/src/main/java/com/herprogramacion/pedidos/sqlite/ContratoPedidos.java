package com.herprogramacion.pedidos.sqlite;
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
Descripción:   Clase que establece los nombres a usar en la base de datos
*/
import java.util.UUID;

/**
 * Clase que establece los nombres a usar en la base de datos
 */
public class ContratoPedidos {

    interface ColumnasCabeceraPedido {
        String ID = "id";
        String FECHA = "fecha";
        String ID_CLIENTE = "id_cliente";
        String ID_FORMA_PAGO = "id_forma_pago";
    }

    interface ColumnasDetallePedido {
        String ID = "id";
        String ID_CABECERA = "id_cabecera";
        String ID_PRODUCTO = "id_producto";
        String CANTIDAD = "cantidad";
        String PRECIO = "precio";
    }

    interface ColumnasProducto {
        String ID = "id";
        String NOMBRE = "nombre";
        String PRECIO = "precio";
        String EXISTENCIAS = "existencias";
        String ESTADO="estado";
    }

    interface ColumnasCliente {
        String ID = "id";
        String NOMBRES = "nombres";
        String APELLIDOS = "apellidos";
        String TELEFONO = "telefono";
        String ESTADO="estado";
    }

    interface ColumnasFormaPago {
        String ID = "id";
        String NOMBRE = "nombre";
        String ESTADO="estado";
    }

    public static class CabecerasPedido implements ColumnasCabeceraPedido {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }
    }

    public static class DetallesPedido implements ColumnasDetallePedido {
        // Métodos auxiliares
        public static String generarIdDetallePedido() {
            return "DP-" + UUID.randomUUID().toString();
        }
    }

    public static class Productos implements ColumnasProducto{
        public static String generarIdProducto() {
            return "PRO-" + UUID.randomUUID().toString();
        }
    }

    public static class Clientes implements ColumnasCliente{
        public static String generarIdCliente() {
            return "CLI-" + UUID.randomUUID().toString();
        }
    }

    public static class FormasPago implements ColumnasFormaPago{
        public static String generarIdFormaPago() {
            return "FP-" + UUID.randomUUID().toString();
        }
    }


    private ContratoPedidos() {
    }

}
