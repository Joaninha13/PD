package Clientes.main;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import Clientes.communication.ClientCommunication;
import share.login.login;
import share.registo.registo;

public class ClienteApp {

    private static boolean isLoggedIn = false;
    private static boolean isAdmin = false;
    private static registo currentUser;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ClientCommunication communication;


        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);

        try {
            communication = new ClientCommunication(serverAddress, serverPort);
            if(communication.isConnected()){
                System.out.println("Conexão bem sucedida");
            }
            else{
                System.out.println("Conexão falhada");
            }

            int escolha;
            do {
                System.out.println("Bem-vindo! Escolha uma opção:");
                System.out.println("1. Registrar");
                System.out.println("2. Fazer Login");
                System.out.println("3. Sair");

                escolha = sc.nextInt();
                sc.nextLine();

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
                            isAdmin = responseLogin.isAdmin();
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
                    if (isAdmin) {
                        System.out.println("Menu de Administrador:");
                        System.out.println("1. Criar Evento");
                        System.out.println("2. Editar Evento");
                        System.out.println("3. Eliminar Evento");
                        System.out.println("4. Consultar Eventos");
                        System.out.println("5. Gerar código de registo da presença");
                        System.out.println("6. Consultar presenças registadas");
                        System.out.println("7. Obter ficheiro csv");
                        System.out.println("8. Consultar Eventos de um Utilizador");
                        System.out.println("9. Eliminar Presenças Registadas");
                        System.out.println("10. Inserção de Presenças");
                        System.out.println("11. Logout");
                    }
                    else{
                        System.out.println("Menu de Utilizador:");
                        System.out.println("1. Editar dados de Registo");
                        System.out.println("2. Submeter código de registo da presença");
                        System.out.println("3. Consultar presenças registadas");
                        System.out.println("4. Obter ficheiro csv");
                        System.out.println("9. Logout");
                    }

                    int opcao = sc.nextInt();
                    sc.nextLine();

                    if(isAdmin){
                        switch (opcao) {
                            case 1:
                                System.out.print("Nome do evento: ");
                                String nomeEvento = sc.nextLine();
                                System.out.print("Local: ");
                                String localEvento = sc.nextLine();
                                System.out.print("Data (DD/MM/AAAA): ");
                                String dataEvento = sc.nextLine();
                                System.out.print("Hora de início (HH:MM): ");
                                String horaInicio = sc.nextLine();
                                System.out.print("Hora de fim (HH:MM): ");
                                String horaFim = sc.nextLine();

                                String message = "create " + nomeEvento + " " + localEvento + " " +
                                        dataEvento + " " + horaInicio + " " + horaFim;

                                try {
                                    String response = communication.sendEventDetails(message);
                                    System.out.println("Resposta do servidor: " + response);
                                } catch (IOException | ClassNotFoundException e) {
                                    System.out.println("Erro ao enviar dados do evento: " + e.getMessage());
                                }
                                break;
                            case 2:
                                // Lógica para editar eventos
                                break;
                            case 3:
                                // Lógica para eliminar eventos
                                break;
                            case 11:
                                isLoggedIn = false;
                                isAdmin = false;
                                currentUser = null;
                                System.out.println("Logout...");
                                break;
                            default:
                                System.out.println("Opção inválida. Escolha novamente.");
                        }
                    }
                    else{
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

                            case 3:
                                System.out.println("Consulta de presenças. Se quiser aplicar um filtro, introduza-o agora, caso contrário, deixe em branco:");
                                String filter = sc.nextLine();

                                try {
                                    String attendanceResponse = communication.consultAttendance(currentUser.getEmail(), filter);
                                    System.out.println("Resposta do servidor: " + attendanceResponse);
                                } catch (IOException | ClassNotFoundException e) {
                                    System.out.println("Erro ao consultar presenças: " + e.getMessage());
                                }
                                break;

                            case 9:
                                isLoggedIn = false;
                                currentUser = null;
                                System.out.println("Logout...");
                                break;

                            default:
                                System.out.println("Opção inválida. Escolha novamente.");
                        }
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