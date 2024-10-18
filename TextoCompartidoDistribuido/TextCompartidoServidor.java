package TextoCompartidoDistribuido;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TextCompartidoServidor extends UnicastRemoteObject implements TextEditorService {
    private StringBuilder registroMensajes;
    private final ReentrantLock lock;  // Bloqueo para modificar el texto


    protected TextCompartidoServidor() throws RemoteException {
        registroMensajes = new StringBuilder();
        lock = new ReentrantLock();
    }

    // Devuelve el documento actual para que el cliente pueda verlo
    @Override
    public synchronized String getDocument() throws RemoteException {
        return registroMensajes.toString();
    }

    // Inserta un texto nuevo en la posición que se indica
    @Override
    public void insertText(int position, String text) throws RemoteException {
        try {
            lock.lock(); // Bloqueamos el acceso para modificar
            if (position >= 0 && position <= registroMensajes.length()) {
                registroMensajes.insert(position, text);
            } else {
                System.out.println("Posición fuera de los límites.");
            }
        } finally {
            lock.unlock(); // Desbloqueamos el acceso tras la modificación
        }
    }

    // Elimina el texto que viene dado por una posición dada y un tamaño
    @Override
    public void deleteText(int position, int length) throws RemoteException {
        try {
            lock.lock(); // Bloqueamos el acceso
            if (position >= 0 && position + length <= registroMensajes.length()) {
                registroMensajes.delete(position, position + length);
            } else {
                System.out.println("Posición fuera de los límites.");
            }
        } finally {
            lock.unlock(); // Desbloqueamos el acceso tras la eliminación
        }
    }

    // Actualiza el texto en el servidor para que se pueda trabajar por otro cliente
    @Override
    public void saveDocument(String content) throws RemoteException {
        try {
            lock.lock(); // Bloqueamos el acceso
            registroMensajes = new StringBuilder(content);
        } finally {
            lock.unlock(); // Desbloqueamos el acceso tras guardar
        }
    }

    public static void main(String[] args) {
        try {
            TextCompartidoServidor server = new TextCompartidoServidor();
            Registry registry = LocateRegistry.createRegistry(1099); // Usamos el puerto 1099
            registry.rebind("TextoCompartido", server); // Nos registramos al servidor con "TextoCompartido"

            System.out.println("El servidor está listo...");

        } catch (Exception e) {
            System.err.println("Excepción del servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}
