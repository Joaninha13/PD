package Clientes.main;

import Clientes.communication.ClientCommunication;
import share.login.login;

public class ClienteApp {
    public static void main(String[] args) {
        try {
            ClientCommunication communication = new ClientCommunication("localhost", 1234);

            // Enviar uma String
            communication.sendMessage("Hello Server!");

            // Enviar loginb
            login userLogin = new login("example@email.com", "password123");
            communication.authenticateUser(userLogin);

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}

