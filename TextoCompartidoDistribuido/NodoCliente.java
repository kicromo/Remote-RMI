package TextoCompartidoDistribuido;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class NodoCliente extends Thread {
    private TextEditorService service;
    private String nombreCliente;
    private static final Semaphore semaforo = new Semaphore(1, true); // Semáforo para gestionar modificaciones.

    public NodoCliente(TextEditorService service, String nombreCliente) {
        this.service = service;
        this.nombreCliente = nombreCliente;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        System.out.println(nombreCliente + " conectado al servidor\n");

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
                        // Ver documento
                        System.out.println("Documento actual:");
                        System.out.println(service.getDocument());
                        break;
                    case 2:
                        // Insertar texto
                        if (acquireLock()) {
                            System.out.print("Introduce el texto a insertar: ");
                            String textToInsert = scanner.nextLine();
                            System.out.print("Introduce la posición: ");
                            int positionInsert = Integer.parseInt(scanner.nextLine());
                            service.insertText(positionInsert, textToInsert);
                        }
                        break;
                    case 3:
                        // Eliminar texto
                        if (acquireLock()) {
                            System.out.print("Introduce la posición desde donde eliminar: ");
                            int positionDelete = Integer.parseInt(scanner.nextLine());
                            System.out.print("Introduce la longitud del texto a eliminar: ");
                            int lengthDelete = Integer.parseInt(scanner.nextLine());
                            service.deleteText(positionDelete, lengthDelete);
                        }
                        break;
                    case 4:
                        // Guardar documento
                        releaseLock();
                        String currentDocument = service.getDocument();
                        service.saveDocument(currentDocument);
                        System.out.println("Documento guardado correctamente.");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (RemoteException e) {
                System.err.println("Error en la operación: " + e.getMessage());
            }

            System.out.print("\nEscribe 'fin' para salir o presiona Enter para continuar: ");
            input = scanner.nextLine();
        }

        System.out.println(nombreCliente + " ha terminado.");
    }

    private boolean acquireLock() {
        try {
            // Intentar adquirir el semáforo para modificar el documento
            if (!semaforo.tryAcquire()) {
                System.out.println("El documento está siendo modificado por otro cliente. Espera a que se guarde.");
                return false;
            }
            System.out.println("Has adquirido el semáforo. Puedes modificar el documento.");
            return true;
        } catch (Exception e) {
            System.err.println("Error al intentar adquirir el semáforo: " + e.getMessage());
            return false;
        }
    }

    private void releaseLock() {
        // Liberar el semáforo cuando el cliente guarda el documento
        semaforo.release();
        System.out.println("El semáforo ha sido liberado. Otros clientes pueden modificar ahora.");
    }
}
