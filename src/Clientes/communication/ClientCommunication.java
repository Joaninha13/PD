package Clientes.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import share.login.login;
import share.registo.registo;

public class ClientCommunication {

    private InetAddress serverAddr;
    private int serverPort;

    public ClientCommunication(String serverAddress, int serverPort) throws UnknownHostException {
        this.serverAddr = InetAddress.getByName(serverAddress);
        this.serverPort = serverPort;
    }

    public login authenticateUser(login userLogin) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(serverAddr, serverPort);
             ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream oin = new ObjectInputStream(socket.getInputStream())) {

            oout.writeObject(userLogin);
            oout.flush();

            return (login) oin.readObject();
        }
    }

    public registo editUserData(registo userData) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(serverAddr, serverPort);
             ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream oin = new ObjectInputStream(socket.getInputStream())) {

            userData.setMsg("edit");

            oout.writeObject(userData);
            oout.flush();

            return (registo) oin.readObject();
        }
    }

    public registo registerUser(registo userRegister) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(serverAddr, serverPort);
             ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream oin = new ObjectInputStream(socket.getInputStream())) {

            oout.writeObject(userRegister);
            oout.flush();

            return (registo) oin.readObject();
        }
    }

    public String submitCode(String code, String email) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(serverAddr, serverPort);
             ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream oin = new ObjectInputStream(socket.getInputStream())) {

            String message = "sub " + code + " " + email;
            oout.writeObject(message);
            oout.flush();

            String response = (String) oin.readObject();
            return response;
        }
    }

    public String consultAttendance(String email, String filter) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(serverAddr, serverPort);
             ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream oin = new ObjectInputStream(socket.getInputStream())) {

            String message = "ConsultPresenca " + email;
            if (filter != null && !filter.isEmpty()) {
                message += " " + filter;
            }

            oout.writeObject(message);
            oout.flush();

            return (String) oin.readObject();
        }
    }

}
