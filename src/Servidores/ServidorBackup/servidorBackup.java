package Servidores.ServidorBackup;

import Servidores.ServidorBackup.Cluster.MulticastChat;

import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.net.*;

public class servidorBackup {

    private static final String ADDRESS = "230.44.44.44";
    private static final int PORT = 4444;


    public static void main(String[] args) throws IOException {
        /*boolean isEmptyDirectory;

        if(args.length != 1){
            System.out.println("Sintaxe: java servidorBackup <dir_replica_bd>");
            return;
        }

        Path dirRepBd = Paths.get(args[1]);

        //verifica se a diretoria não está vazia
        if((isEmptyDirectory = Files.list(dirRepBd).findAny().isPresent()) == false){
            System.out.println("Diretoria não está vazia!\n");
            return;
        }*/

        NetworkInterface nif;
        MulticastSocket mskt = null;
        MulticastChat mchat = null;

        try {

            mskt = new MulticastSocket(PORT);
            mskt.joinGroup(new InetSocketAddress(ADDRESS, PORT).getAddress());

            mchat = new MulticastChat("S", mskt);

            mchat.start();

            while (true){}

        } finally {
            if (mchat != null) {
                mchat.terminate();
            }
            if (mskt != null){
                mskt.close();
            }

        }
    }
}