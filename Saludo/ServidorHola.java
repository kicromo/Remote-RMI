package Saludo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

// Clase Servidor que extiende UnicastRemoteObject para manejar las llamadas remotas automáticamente.
public class ServidorHola extends UnicastRemoteObject implements Hola {
    private static final long serialVersionUID = 1L; // Para evitar problemas de serialización

    // Constructor que lanza RemoteException
    protected ServidorHola() throws RemoteException {
        super();
    }

    // Implementación del método remoto
    @Override
    public String di_hola() throws RemoteException {
        return "Hola, Mundo!";
    }

    public static void main(String[] args) {
        try {
            // Instancia del servidor
            ServidorHola server = new ServidorHola();

            // Crear y obtener el registro RMI en el puerto 1099 (por defecto)
            Registry registry = LocateRegistry.createRegistry(1099);

            // Registrar el servidor bajo el nombre "Servidor_Hola"
            registry.rebind("Servidor_Hola", server);

            System.out.println("ServidorHola instalado correctamente.");
        } catch (Exception e) {
            System.err.println("Excepción del servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}
