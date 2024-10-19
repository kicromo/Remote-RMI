package TextoCompartidoDistribuido;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

public class TextCompartidoServidor extends UnicastRemoteObject implements TextEditorService {
    private StringBuilder registroMensajes;
    private StringBuilder copiaTemporal; // Copia temporal del documento.
    private final Semaphore semaforo;    // Semáforo para gestionar la edición.
    private boolean isEditing = false;   // Indica si el documento está siendo editado.

    protected TextCompartidoServidor() throws RemoteException {
        registroMensajes = new StringBuilder();
        semaforo = new Semaphore(1, true); // Semáforo binario para controlar la edición.
    }

    @Override
    public synchronized String getDocument() throws RemoteException {
        // Si el documento está siendo editado, informar del estado.
        if (isEditing) {
            return "El documento está siendo editado por otro cliente.";
        }
        return registroMensajes.toString(); // Si no está editado, devolver el contenido original.
    }

    @Override
    public void insertText(int position, String text) throws RemoteException {
        try {
            semaforo.acquire(); // Intentar adquirir el semáforo para la edición.
            
            // Si no se está editando, empezar la edición con una copia temporal.
            if (!isEditing) {
                isEditing = true; // Marcar que el documento está en modo edición.
                copiaTemporal = new StringBuilder(registroMensajes); // Crear una copia temporal del documento.
            }
            
            // Inserción en la copia temporal.
            if (position >= 0 && position <= copiaTemporal.length()) {
                copiaTemporal.insert(position, text);
                System.out.println("Texto insertado temporalmente en la posición " + position);
            } else {
                System.out.println("Posición fuera de los límites.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // No se libera el semáforo aquí, se mantiene hasta que se guarde el documento.
            // Esto garantiza que solo el mismo cliente siga editando hasta que llame a saveDocument.
        }
    }

    @Override
    public void deleteText(int position, int length) throws RemoteException {
        try {
            semaforo.acquire(); // Intentar adquirir el semáforo para la edición.

            // Si es la primera vez que se está editando, crear la copia temporal.
            if (!isEditing) {
                isEditing = true;
                copiaTemporal = new StringBuilder(registroMensajes); // Crear la copia temporal.
            }

            // Eliminación en la copia temporal.
            if (position >= 0 && position + length <= copiaTemporal.length()) {
                copiaTemporal.delete(position, position + length);
                System.out.println("Texto eliminado temporalmente.");
            } else {
                System.out.println("Posición fuera de los límites.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Igual que en `insertText`, no se libera el semáforo hasta que se haga un save.
        }
    }

    @Override
    public void saveDocument(String content) throws RemoteException {
        try {
            
            if (isEditing) {
                registroMensajes = new StringBuilder(copiaTemporal);
                isEditing = false; 
                System.out.println("Documento guardado y actualizado.");
            } else {
                System.out.println("No se ha hecho ninguna modificación para guardar.");
            }
        } finally {
            semaforo.release(); 
        }
    }

    public static void main(String[] args) {
        try {
            TextCompartidoServidor server = new TextCompartidoServidor();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("TextoCompartido", server);
            System.out.println("Servidor listo...");
        } catch (Exception e) {
            System.err.println("Excepción del servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}


