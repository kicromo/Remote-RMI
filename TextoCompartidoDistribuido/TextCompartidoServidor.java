package TextoCompartidoDistribuido;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class TextCompartidoServidor extends UnicastRemoteObject implements TextEditorService {
    private StringBuilder registroMensajes;
    private StringBuilder copiaTemporal;
    private final Semaphore semaforo;
    private boolean isEditing;
    private Queue<String> colaClientes;  // Cola para manejar los turnos de los clientes.

    protected TextCompartidoServidor() throws RemoteException {
        registroMensajes = new StringBuilder();
        semaforo = new Semaphore(1, true); // Semáforo binario para controlar la edición.
        isEditing = false;
        colaClientes = new LinkedList<>();  // Inicializamos la cola.
    }

    @Override
    public synchronized String getDocument() throws RemoteException {
        return registroMensajes.toString();
    }

    @Override
    public void insertText(int position, String text) throws RemoteException {
        try {
            // El cliente tiene que esperar su turno en la cola para modificar.
            String clienteActual = Thread.currentThread().getName();
            esperarTurno(clienteActual);

            semaforo.acquire(); // El cliente actual adquiere el semáforo.
            
            if (!isEditing) {
                isEditing = true;
                copiaTemporal = new StringBuilder(registroMensajes);
            }

            if (position >= 0 && position <= copiaTemporal.length()) {
                copiaTemporal.insert(position, text);
                System.out.println(clienteActual + " ha insertado texto en la posición " + position);
            } else {
                System.out.println("Posición fuera de los límites.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // No liberamos el semáforo aquí, el cliente seguirá modificando hasta que llame a saveDocument.
        }
    }

    @Override
    public void deleteText(int position, int length) throws RemoteException {
        try {
            String clienteActual = Thread.currentThread().getName();
            esperarTurno(clienteActual);

            semaforo.acquire(); // El cliente actual adquiere el semáforo.

            if (!isEditing) {
                isEditing = true;
                copiaTemporal = new StringBuilder(registroMensajes);
            }

            if (position >= 0 && position + length <= copiaTemporal.length()) {
                copiaTemporal.delete(position, position + length);
                System.out.println(clienteActual + " ha eliminado texto.");
            } else {
                System.out.println("Posición fuera de los límites.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // No liberamos el semáforo aquí.
        }
    }

    @Override
    public synchronized void saveDocument(String content) throws RemoteException {
        try {
            if (isEditing) {
                registroMensajes = new StringBuilder(copiaTemporal);
                isEditing = false;
                System.out.println("Documento guardado y actualizado.");
            } else {
                System.out.println("No se ha hecho ninguna modificación para guardar.");
            }
        } finally {
            semaforo.release(); // Liberamos el semáforo cuando se guarda el documento.
            liberarTurno();     // Liberamos el turno para el siguiente cliente.
        }
    }

    /**
     * Este método encola al cliente y lo mantiene esperando hasta que sea su turno.
     */
    private synchronized void esperarTurno(String cliente) {
        if (!colaClientes.contains(cliente)) {
            colaClientes.add(cliente);  // Añadimos al cliente a la cola si no está.
        }
        
        if (colaClientes.peek().contains(cliente)) {
        	semaforo.release();		// el primer cliente quiere seguir editando
        }
        
        // Esperamos hasta que sea el turno del cliente.
        while (!colaClientes.peek().equals(cliente)) {
            try {
                System.out.println(cliente + " está esperando su turno...");
                wait();  // Esperar hasta que sea notificado.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Este método libera el turno del cliente actual y notifica al siguiente.
     */
    private synchronized void liberarTurno() {
        colaClientes.poll(); // Eliminamos al cliente actual de la cola.
        notifyAll(); // Notificamos a todos para que el siguiente cliente pueda tomar el turno.
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


