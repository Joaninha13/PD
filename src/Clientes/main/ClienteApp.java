package Clientes.main;

import java.util.Scanner;
import Clientes.communication.ClientCommunication;
import share.login.login;
import share.registo.registo;

public class ClienteApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ClientCommunication communication;

        try {
            communication = new ClientCommunication("serverAddress", 1234); // Substitua com o endereço e porta corretos

            int escolha;
            do {
                System.out.println("Escolha uma opção:");
                System.out.println("1. Registrar");
                System.out.println("2. Fazer Login");
                System.out.println("3. Sair");

                escolha = sc.nextInt();
                sc.nextLine(); // Limpar o buffer

                switch (escolha) {
                    case 1:
                        System.out.println("Introduza o seu nome");
                        String nome = sc.nextLine();
                        System.out.println("Introduza o seu numero de identificação");
                        String numID = sc.nextLine();
                        System.out.println("Introduza o seu email:");
                        String email = sc.nextLine();
                        System.out.println("Introduza a sua password:");
                        String password = sc.nextLine();

                        registo userRegister = new registo(nome, numID, email, password);
                        registo responseRegister = communication.registerUser(userRegister);

                        if(responseRegister.isRegistered()) {
                            System.out.println(responseRegister.getMsg());
                        } else {
                            System.out.println("Erro no registo");
                        }
                        break;

                    case 2:
                        System.out.println("Introduza o seu email:");
                        email = sc.nextLine();
                        System.out.println("Introduza a sua password:");
                        password = sc.nextLine();

                        login userLogin = new login(email, password);
                        login responseLogin = communication.authenticateUser(userLogin);

                        if(responseLogin.isValid()) {
                            System.out.println("Autenticação bem-sucedida!");
                        } else {
                            System.out.println("Erro de autenticação: " + responseLogin.getMsg());
                        }
                        break;

                    case 3:
                        System.out.println("A sair do programa...");
                        break;
                    default:
                        System.out.println("Opção inválida. Escolha novamente.");
                }

            } while (escolha != 3);

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
