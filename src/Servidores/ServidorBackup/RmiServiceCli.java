package Servidores.ServidorBackup;

import share.RMI.IRmiClient;
import share.RMI.IRmiService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiServiceCli extends UnicastRemoteObject implements IRmiClient {

    private static final String REGISTRY_IP = "127.0.0.1";

    private static String SERVICE_NAME;
    private static int RMI_PORT ;
    public static final int MAX_CHUNCK_SIZE = 10000; //bytes
    private static String DIR;
    private static String RMILOC;

    protected File DBRDirectory;

    FileOutputStream fout = null;

    public RmiServiceCli(String name, int port, File DBRDirectory, String dir, String objectUrl) throws RemoteException {
        SERVICE_NAME = name;
        RMI_PORT = port;
        DBRDirectory = this.DBRDirectory;
        DIR = dir;
        RMILOC = objectUrl;
    }

    public void start(){
        try{
            RmiServiceCli myRemoteService = null;
            IRmiService remoteFileService;

            try(FileOutputStream localFileOutputStream = new FileOutputStream(DIR)){
                System.out.println("Ficheiro " + DIR + "criado.");

                remoteFileService = (IRmiService) Naming.lookup(RMILOC);

                myRemoteService = new RmiServiceCli(SERVICE_NAME,RMI_PORT,DBRDirectory,DIR, RMILOC);

                myRemoteService.setFout(localFileOutputStream);

                remoteFileService.getDb(SERVICE_NAME,myRemoteService);

            }

        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        }catch(Exception e){
            System.out.println("Erro - " + e);
            System.exit(1);
        }
    }

    public synchronized void setFout(FileOutputStream fout) {this.fout = fout;}


    @Override
    public void writeFileChunk(byte[] fileChunk, int nbytes) throws IOException {

        if (fout == null) {
            System.out.println("Nao existe qualquer ficheiro aberto para escrita!");
            throw new IOException("<CLI> Nao existe qualquer ficheiro aberto para escrita!");
        }
        try {
            fout.write(fileChunk, 0, nbytes);
        } catch (IOException e) {
            System.out.println("Erro ao escrever no ficheiro!" + e);
            throw new IOException("<CLI> Erro ao escrever no ficheiro!", e.getCause());
        }
    }
}
