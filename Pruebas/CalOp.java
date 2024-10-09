package Pruebas;

import java.rmi.Remote;

public interface CalOp extends Remote {
    public int suma(int a, int b);
    public int resta(int a, int b);
    public int multiplicacion(int a, int b);
    public double division(int a, int b);
}
