package Clientes.main;

import java.io.IOException;
import java.util.Scanner;
import Clientes.communication.ClientCommunication;
import share.login.login;
import share.registo.registo;

public class ClienteApp {

    private static boolean isLoggedIn = false;
    private static registo currentUser; // Dados do utilizador atualmente autenticado

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ClientCommunication communication;

        try {
            communication = new ClientCommunication("localhost", 1234); // Substitua com o endereço e porta corretos

            int escolha;
            do {
                System.out.println("Bem-vindo! Escolha uma opção:\n");
                System.out.println("1. Registrar");
                System.out.println("2. Fazer Login");
                System.out.println("3. Sair");

                escolha = sc.nextInt();
                sc.nextLine(); // Limpar o buffer

                switch (escolha) {
                    case 1:
                        System.out.println("Introduza o seu nome:");
                        String nome = sc.nextLine();
                        System.out.println("Introduza o seu numero de identificação:");
                        String numID = sc.nextLine();
                        System.out.println("Introduza o seu email:");
                        String email = sc.nextLine();
                        System.out.println("Introduza a sua password:");
                        String password = sc.nextLine();

                        registo userRegister = new registo(nome, numID, email, password);
                        registo responseRegister = communication.registerUser(userRegister);

                        if (responseRegister.isRegistered()) {
                            System.out.println(responseRegister.getMsg());
                            isLoggedIn = true;
                        } else {
                            System.out.println("Erro no registo: " + responseRegister.getMsg());
                        }
                        break;

                    case 2:
                        System.out.println("Introduza o seu email:");
                        email = sc.nextLine();
                        System.out.println("Introduza a sua password:");
                        password = sc.nextLine();

                        login userLogin = new login(email, password);
                        login responseLogin = communication.authenticateUser(userLogin);

                        if (responseLogin.isValid()) {
                            System.out.println("Autenticação bem-sucedida!");
                            isLoggedIn = true;
                        } else {
                            System.out.println("Erro de autenticação: " + responseLogin.getMsg());
                        }
                        break;

                    case 3:
                        System.out.println("A sair do programa...");
                        isLoggedIn = false;
                        break;

                    default:
                        System.out.println("Opção inválida. Escolha novamente.");
                }

                while (isLoggedIn) {
                    System.out.println("1. Editar dados de Registo");
                    System.out.println("9. Logout");

                    int opcao = sc.nextInt();
                    sc.nextLine(); // Limpar o buffer

                    switch (opcao) {
                        case 1:
                            System.out.println("Editar dados de registro. Deixe em branco se não deseja mudar o valor atual:");

                            System.out.println("Nome atual: " + currentUser.getName());
                            System.out.print("Novo nome: ");
                            String newName = sc.nextLine();
                            if (!newName.isEmpty()) {
                                currentUser.setName(newName);
                            }

                            System.out.println("Número de identificação atual: " + currentUser.getIdentificationNumber());
                            System.out.print("Novo número de identificação: ");
                            String newIdNumber = sc.nextLine();
                            if (!newIdNumber.isEmpty()) {
                                currentUser.setIdentificationNumber(newIdNumber);
                            }

                            System.out.println("Email atual: " + currentUser.getEmail());
                            System.out.print("Novo email: ");
                            String newEmail = sc.nextLine();
                            if (!newEmail.isEmpty()) {
                                currentUser.setEmail(newEmail);
                            }

                            System.out.println("Password atual: ********");
                            System.out.print("Nova password: ");
                            String newPassword = sc.nextLine();
                            if (!newPassword.isEmpty()) {
                                currentUser.setPassword(newPassword);
                            }

                            try {
                                registo responseEdit = communication.editUserData(currentUser);
                                if (responseEdit.isRegistered()) {
                                    System.out.println("Dados atualizados com sucesso.");
                                    currentUser = responseEdit;
                                } else {
                                    System.out.println("Erro na atualização dos dados: " + responseEdit.getMsg());
                                }
                            } catch (IOException | ClassNotFoundException e) {
                                System.out.println("Erro ao atualizar dados: " + e.getMessage());
                            }
                            break;

                        case 2:
                            System.out.println("Por favor, insira o código que deseja submeter:");
                            String codigo = sc.nextLine();
                            try {
                                String responseSubmit = communication.submitCode(codigo, currentUser.getEmail());
                                System.out.println("Resposta do servidor: " + responseSubmit);
                            } catch (IOException | ClassNotFoundException e) {
                                System.out.println("Erro ao submeter código: " + e.getMessage());
                            }
                            break;

                        case 9:
                            isLoggedIn = false;
                            System.out.println("Logout...");
                            break;

                        default:
                            System.out.println("Opção inválida. Escolha novamente.");
                    }
                }

            } while (escolha != 3);

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
