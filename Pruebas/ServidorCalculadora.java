package Pruebas;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServidorCalculadora implements CalOp {
    @Override
    public int suma(int a, int b) {
        return a + b;
    }

    @Override
    public int resta(int a, int b) {
    return a - b;
    }

    @Override
    public int multiplicacion(int a, int b) {
    return a * b;
    }

    @Override
    public double division(int a, int b) {
        double resultado = 0;
        if (b == 0) {
            throw new ArithmeticException();
        }else {
            resultado = (double) a / b;
        }
        return resultado;
    }


    public static void main(String args[]) {
        try {
            ServidorCalculadora server = new ServidorCalculadora(); //Instancia del servidor
            // El servidor se registra en el RMI como un objeto remoto y se obtiene su referencia remota
            CalOp ServidorHolaRef = (CalOp) UnicastRemoteObject.exportObject(server, 0);
            // El servidor se registra en el registry con un nombre (“Servidor_Hola”)
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind ("Servidor_Hola", ServidorHolaRef );
                    System.err.println("ServidorHola instalado"); // Servidor instalado con éxito
            } catch (Exception e) {
                System.err.println("Server exception: " + e.getMessage());
            }
    }

}
