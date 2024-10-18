package TextoCompartidoDistribuido;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TextClient1 {
    public static void main(String[] args) {
        String host = "localhost";

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            TextEditorService editor = (TextEditorService) registry.lookup("TextoCompartido");
            NodoCliente cliente = new NodoCliente(editor, "Cliente 1");
            cliente.start();

            cliente.join();
        } catch (Exception e) {
            System.err.println("Error en Cliente 1: " + e.toString());
            e.printStackTrace();
        }
    }
}
