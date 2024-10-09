package Pruebas;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class MainClient {
    //public Pruebas.MainClient() {} // Constructor

    public static void main(String args[]) {
        String elHost = null;
        // El primer parámetro es el host de Registry
        // Si no hay parámetro, usa el local
        if (args.length >= 1) elHost = args[0];
        try {
            // Se localiza el servidor en el registro por su nombre “Servidor_Hola” ·
            Registry registry = LocateRegistry.getRegistry(elHost);
            CalOp elServidor = (CalOp) registry.lookup("Servidor_Hola");
            int respuesta = elServidor.suma( 4, 5); // Se invoca el servicio remoto
            System.out.println("Respuesta: " + respuesta);
        } catch (Exception e) {
            System.err.println("Excepción del cliente: " + e.getMessage());
        }
    }
}

