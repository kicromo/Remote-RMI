package Saludo;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteHola {
    public ClienteHola() {} // Constructor

    public static void main(String[] args) {
        String elHost = "localhost"; // Por defecto al localhost
        if (args.length >= 1) {
            elHost = args[0]; // Si hay un host en los argumentos, lo usa
        }

        try {
            // Conectar con el registro del servidor
            Registry registry = LocateRegistry.getRegistry(elHost);

            // Buscar el servidor registrado bajo el nombre "Servidor_Hola"
            Hola elServidor = (Hola) registry.lookup("Servidor_Hola");

            // Llamar al método remoto
            String respuesta = elServidor.di_hola();
            System.out.println("Respuesta del servidor: " + respuesta);
        } catch (Exception e) {
            System.err.println("Excepción del cliente: " + e.toString());
            e.printStackTrace();
        }
    }
}
