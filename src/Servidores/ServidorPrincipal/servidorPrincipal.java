package Servidores.ServidorPrincipal;

import Servidores.ServidorPrincipal.ConectionClientThread.conectionClientThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class servidorPrincipal extends Thread {


    public static void main(String[] args) {

        if(args.length != 4){
            System.out.println("Sintaxe: java servidorPrincipal listeningPort localDirectoryBD serviceNameRMI listeningPortRMI");
            return;
        }


        try(ServerSocket socket = new ServerSocket(Integer.parseInt(args[0]))){

            System.out.println("TCP Server iniciado no porto " + socket.getLocalPort() + " ...");

            while(true){

                Socket toClientSocket = socket.accept();
                new conectionClientThread(toClientSocket).start();

            }

        }catch(NumberFormatException e){
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        }catch(IOException e){
            System.out.println("Ocorreu um erro ao nivel do socket de escuta:\n\t"+e);
        }


    }
}
