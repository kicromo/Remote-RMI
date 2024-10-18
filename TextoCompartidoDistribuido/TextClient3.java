package TextoCompartidoDistribuido;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TextClient3 {
    public static void main(String[] args) {
        String host = "localhost";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            TextEditorService editor = (TextEditorService) registry.lookup("TextoCompartido");

            // Crear un nuevo nodo cliente y pasarle el servicio de editor y el nombre del cliente
            NodoCliente cliente = new NodoCliente(editor, "Cliente 1");
            cliente.start();

            cliente.join();
        } catch (Exception e) {
            System.err.println("Error en Cliente 1: " + e.toString());
            e.printStackTrace();
        }
    }
}
