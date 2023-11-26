package Servidores.ServidorBackup;

import Servidores.ServidorBackup.Cluster.MulticastChat;

import javax.management.InstanceNotFoundException;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class servidorBackup {

    private static final String ADDRESS = "230.44.44.44";
    private static final int PORT = 4444;
    static String FILENAME = "presences.db";

    public static void main(String[] args) throws IOException {
        File DBRDirectory;
        String objectUrl;
        String filepath;

        boolean isEmptyDirectory;

        if(args.length != 1){
            System.out.println("Sintaxe: java servidorBackup <dir_replica_bd>");
            return;
        }

        //System.setProperty("java.rmi.server.hostname", "192.168.56.1");

        //trocar para args[0]...
        Path dirRepBd = Paths.get(args[1]);
        DBRDirectory = new File(args[1].trim());
        objectUrl = "rmi//localhost//servidor-backup-database";


        if(!DBRDirectory.exists()){
            System.out.println("A directoria " + DBRDirectory + " nao existe!");
            return;
        }
        if(!DBRDirectory.isDirectory()){
            System.out.println("O caminho " + DBRDirectory + " nao se refere a uma diretoria!");
            return;
        }
        if(!DBRDirectory.canWrite()){
            System.out.println("Sem permissoes de escrita na diretoria " + DBRDirectory);
            return;
        }
        if(!(isEmptyDirectory = Files.list(dirRepBd).findAny().isPresent())) {
            System.out.println("Diretoria não está vazia!\n");
        }

        NetworkInterface nif;
        MulticastSocket mskt = null;
        MulticastChat mchat = null;

        try {

            try{
                filepath = new File(DBRDirectory.getPath()+File.separator+FILENAME).getCanonicalPath();
            } catch (IOException ex) {
                System.out.println("Erro E/S - " + ex);
                return;
            }

            try {
                nif = NetworkInterface.getByInetAddress(InetAddress.getByName(args[0]));
            } catch (SocketException | NullPointerException | UnknownHostException | SecurityException ex) {
                nif = NetworkInterface.getByName(args[0]);
            }

            mskt = new MulticastSocket(PORT);
            mskt.joinGroup(new InetSocketAddress(ADDRESS, PORT),nif);
            mchat = new MulticastChat("S", mskt);
            mchat.start();

            new RmiServiceCli("sv", PORT, DBRDirectory, filepath, objectUrl).start();

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