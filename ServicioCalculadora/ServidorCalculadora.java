package ServicioCalculadora;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

// Implementación del servidor que extiende UnicastRemoteObject
public class ServidorCalculadora extends UnicastRemoteObject implements Calculadora {

    private static final long serialVersionUID = 1L;

    // Constructor que lanza RemoteException
    protected ServidorCalculadora() throws RemoteException {
        super();
    }

    // Implementación de los métodos de la calculadora
    @Override
    public float addition(float a, float b) throws RemoteException {
        return a + b;
    }

    @Override
    public float substraction(float a, float b) throws RemoteException {
        return a - b;
    }

    @Override
    public float multiplication(float a, float b) throws RemoteException {
        return a * b;
    }

    @Override
    public float division(float a, float b) throws RemoteException {
        if (b == 0) throw new RemoteException("División por cero no permitida");
        return a / b;
    }

    @Override
    public float power(float a, int b) throws RemoteException {
        return (float) Math.pow(a, b);
    }

    public static void main(String[] args) {
        try {
            // Instancia del servidor
            ServidorCalculadora server = new ServidorCalculadora();

            // Crear el registro RMI en el puerto 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // Registrar el servidor con el nombre "Calculadora"
            registry.rebind("Calculadora", server);

            System.out.println("Servidor de la Calculadora listo.");
        } catch (Exception e) {
            System.err.println("Excepción del servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}
