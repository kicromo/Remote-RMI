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

    protected TextCompartidoServidor() throws RemoteException {
        registroMensajes = new StringBuilder();
    }

    @Override
    public synchronized String getDocument() throws RemoteException {
        return registroMensajes.toString();
    }

    @Override
    public synchronized void insertText(int position, String text) throws RemoteException {
        if (position >= 0 && position <= registroMensajes.length()) {
            registroMensajes.insert(position, text);
            System.out.println("Texto insertado en la posición " + position);
        } else {
            System.out.println("Posición fuera de los límites.");
        }
    }

    @Override
    public synchronized void deleteText(int position, int length) throws RemoteException {
        if (position >= 0 && position + length <= registroMensajes.length()) {
            registroMensajes.delete(position, position + length);
            System.out.println("Texto eliminado.");
        } else {
            System.out.println("Posición fuera de los límites.");
        }
    }

    @Override
    public synchronized void saveDocument(String content) throws RemoteException {
        registroMensajes = new StringBuilder(content);
        System.out.println("Documento guardado.");
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


