package ServicioCalculadora;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteCalculadora {

    public static void main(String[] args) {
        String host = "localhost"; // Por defecto, localhost
        try {
            // Obtener el registro del servidor en localhost
            Registry registry = LocateRegistry.getRegistry(host);

            // Buscar la referencia remota del servidor "Calculadora"
            Calculadora calculadora = (Calculadora) registry.lookup("Calculadora");

            // Invocar los métodos de la calculadora remota
            float a = 5.0f, b = 3.0f;
            int exponent = 2;

            System.out.println("Suma: " + calculadora.addition(a, b));
            System.out.println("Resta: " + calculadora.substraction(a, b));
            System.out.println("Multiplicación: " + calculadora.multiplication(a, b));
            System.out.println("División: " + calculadora.division(a, b));
            System.out.println("Potencia: " + calculadora.power(a, exponent));
        } catch (Exception e) {
            System.err.println("Excepción del cliente: " + e.toString());
            e.printStackTrace();
        }
    }
}
