package Saludo;

// Ejemplo de interfaz remota
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface Hola extends Remote {
    String di_hola() throws RemoteException;
}