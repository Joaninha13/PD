package Clientes.main;

import java.util.Scanner;

public class ClienteApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int escolha;

        do {

            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar Evento");
            System.out.println("2. Editar Evento.");
            System.out.println("3. Eliminar Evento.");
            System.out.println("4. Consultar Evento Criados.");
            System.out.println("5. Gera Codigo Evento.");
            System.out.println("6. Consulta Presenças."); // Obtenção de um ficheiro csv com a lista de presenças
            System.out.println("7. Consulta dos eventos de um utilizador."); // Obtenção de um ficheiro csv com a lista de presenças
            System.out.println("8. Eliminar presença registada.");
            System.out.println("9. Inserção de presença manual.");
            System.out.println("10. Logout");

            escolha = sc.nextInt();

            switch (escolha) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 7:

                    break;
                case 8:

                    break;
                case 9:
                    break;
                case 10:
                    System.out.println("Saindo do programa.");
                    break;
                default:
                    System.out.println("Opção inválida. Escolha novamente.");
            }

        }while (escolha != 10);
    }

}
