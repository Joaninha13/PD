package Clientes.communication;

import share.login.login;
import share.registo.registo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientCommunication {

    private InetAddress serverAddr;
    private int serverPort;

    public ClientCommunication(String serverAddress, int serverPort) throws UnknownHostException {
        this.serverAddr = InetAddress.getByName(serverAddress);
        this.serverPort = serverPort;
    }

    public login authenticateUser(login userLogin) throws IOException, ClassNotFoundException {
        return sendObject(userLogin);
    }

    public registo registerUser(registo userRegister) throws IOException, ClassNotFoundException {
        return sendObject(userRegister);
    }

    public String sendMessage(String message) throws IOException, ClassNotFoundException {
        return sendObject(message);
    }

    private <T> T sendObject(Object obj) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(serverAddr, serverPort);
             ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream oin = new ObjectInputStream(socket.getInputStream())) {

            oout.writeObject(obj);
            oout.flush();

            return (T) oin.readObject();
        }
    }
}
