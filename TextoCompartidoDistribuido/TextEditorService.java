package TextoCompartidoDistribuido;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface TextEditorService extends Remote {
    String getDocument() throws RemoteException;
    void insertText(int position, String text) throws RemoteException;
    void deleteText(int position, int length) throws RemoteException;
    void saveDocument(String content) throws RemoteException;

}