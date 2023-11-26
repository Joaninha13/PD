package Servidores.ServidorPrincipal;

import Servidores.ServidorPrincipal.BDConection.conectionBD;
import Servidores.ServidorPrincipal.Cluster.HeartBeat.DBUpdate;
import Servidores.ServidorPrincipal.Cluster.HeartBeat.HeartBeat;
import Servidores.ServidorPrincipal.ConectionClientThread.conectionClientThread;
import Servidores.ServidorPrincipal.RMI.RmiService;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class servidorPrincipal{

    private static final String ADDRESS = "230.44.44.44";
    private static final int PORT = 4444;

    public static void main(String[] args) {
        File DBDirectory;

        if(args.length != 4){
            System.out.println("Sintaxe: java servidorPrincipal <listeningPort> <localDirectoryBD> <serviceNameRMI> <listeningPortRMI>");
            return;
        }

        //System.setProperty("java.rmi.server.hostname", "193.137.78.17");

        DBDirectory = new File(args[1].trim());

        if(!DBDirectory.exists()){
            System.out.println("A directoria " + DBDirectory + " nao existe!");
            return;
        }

        if(!DBDirectory.isDirectory()){
            System.out.println("O caminho " + DBDirectory + " nao se refere a uma diretoria!");
            return;
        }

        if(!DBDirectory.canRead()){
            System.out.println("Sem permissoes de leitura na diretoria " + DBDirectory + "!");
            return;
        }


        try(ServerSocket socket = new ServerSocket(Integer.parseInt(args[0]))){

            MulticastSocket ms = new MulticastSocket(PORT);
            InetAddress ipGroup = InetAddress.getByName(ADDRESS);

            new DBUpdate(args[2], Integer.parseInt(args[3]), ms, ipGroup, PORT);

            conectionBD bd = new conectionBD(args[1].trim());

            new RmiService(args[2], Integer.parseInt(args[3]), DBDirectory).start();

            new HeartBeat(args[2], Integer.parseInt(args[3]), ms, ipGroup, PORT).start();

            System.out.println("TCP Server iniciado no porto " + socket.getLocalPort() + " ...");

            while(true){

                Socket toClientSocket = socket.accept();
                new conectionClientThread(toClientSocket, bd).start();

            }

        }catch(NumberFormatException e){
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        }catch(IOException e){
            System.out.println("Ocorreu um erro ao nivel do socket de escuta:\n\t"+e);
        }
    }
}
