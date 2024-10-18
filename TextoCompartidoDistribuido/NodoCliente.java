package TextoCompartidoDistribuido;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class NodoCliente extends Thread {
    private TextEditorService service;
    private String nombreCliente;
    private static Semaphore[] semaforos; // Array de semáforos para controlar acceso de clientes
    private static Semaphore semaforo = new Semaphore(1);
    private static int totalClientes = 0;  // Contador de clientes
    private final int indiceCliente;  // Índice del cliente en el array

    public NodoCliente(TextEditorService service, String nombreCliente) {
        this.nombreCliente = nombreCliente;
        this.service = service;

        synchronized (NodoCliente.class) {
            // Inicializamos el semáforo solo una vez
            if (semaforos == null) {
                semaforos = new Semaphore[2]; // Cambia a 2 si esperas 2 clientes
                for (int i = 0; i < semaforos.length; i++) {
                    semaforos[i] = new Semaphore(1); // Cada cliente tiene su propio semáforo
                }
            }
            indiceCliente = totalClientes++; // Asigna un índice al cliente
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        System.out.println(nombreCliente + " conectado al servidor.");
        System.out.println("Escribe 'fin' para salir.");

        while (!input.equalsIgnoreCase("fin")) {
            System.out.println("\nElige una operación: ");
            System.out.println("1. Ver documento");
            System.out.println("2. Insertar texto");
            System.out.println("3. Eliminar texto");
            System.out.println("4. Guardar documento");
            System.out.print("Operación: ");
            int option = Integer.parseInt(scanner.nextLine());

            try {
                switch (option) {
                    case 1:
                        // Ver documento (no requiere bloqueo)
                        System.out.println("Documento actual:");
                        System.out.println(service.getDocument());
                        break;

                    case 2:
                        // Insertar texto (requiere bloqueo)
                        semaforo.acquire(); // Intenta adquirir el semáforo del cliente
                        try {
                            System.out.print("Introduce el texto a insertar: ");
                            String textToInsert = scanner.nextLine();
                            System.out.print("Introduce la posición: ");
                            int positionInsert = Integer.parseInt(scanner.nextLine());
                            service.insertText(positionInsert, textToInsert);
                            System.out.println("Texto insertado correctamente.");
                        } finally {
                            // No liberamos el semáforo aquí, se liberará al guardar (opción 4)
                        }
                        break;

                    case 3:
                        // Eliminar texto (requiere bloqueo)
                        semaforo.acquire(); // Intenta adquirir el semáforo del cliente
                        try {
                            System.out.print("Introduce la posición desde donde eliminar: ");
                            int positionDelete = Integer.parseInt(scanner.nextLine());
                            System.out.print("Introduce la longitud del texto a eliminar: ");
                            int lengthDelete = Integer.parseInt(scanner.nextLine());
                            service.deleteText(positionDelete, lengthDelete);
                            System.out.println("Texto eliminado correctamente.");
                        } finally {
                            // No liberamos el semáforo aquí, se liberará al guardar (opción 4)
                        }
                        break;

                    case 4:
                        // Guardar documento (libera el bloqueo)
                        semaforo.release();
                        String currentDocument = service.getDocument();
                        service.saveDocument(currentDocument);
                        System.out.println("Documento guardado correctamente.");

                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (RemoteException e) {
                System.err.println("Error en la operación: " + e.getMessage());
            } catch (InterruptedException e) {
                System.err.println("Error con el semáforo: " + e.getMessage());
            }

            System.out.print("\nEscribe 'fin' para salir o presiona Enter para continuar: ");
            input = scanner.nextLine();
        }

        System.out.println(nombreCliente + " ha terminado.");
    }
}
