package Servidores.ServidorBackup.Cluster;

import share.heartBeatMsg.HeartBeatMess;

import java.io.*;
import java.net.*;

public class MulticastChat extends Thread {
    private static final int MAX_SIZE = 1000;
    private static final int MAX_TIME = 3000;

    protected String username;
    private final MulticastSocket s;
    protected boolean running;

    public MulticastChat(String username, MulticastSocket s) {
        this.username = username;
        this.s = s;
        running = true;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {

        DatagramPacket pkt;
        HeartBeatMess hbm;
        Object obj;

        if (s == null || !running) {
            return;
        }

        try {

            while (running) {
                System.out.println("entrei no while");

                pkt = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                s.receive(pkt);

                System.out.println("recebi packet");

                try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(pkt.getData(), 0, pkt.getLength()))) {

                    // "Deserializa" o objecto transportado no datagrama acabado de ser recebido

                    obj = in.readObject();

                    System.out.println();
                    System.out.print("(" + pkt.getAddress().getHostAddress() + ":" + pkt.getPort() + ") ");

                    if (obj instanceof HeartBeatMess) {

                        hbm = (HeartBeatMess) obj;
                        System.out.println("Db :" + hbm.getDatabaseVersion());
                        System.out.println("Rmi :" + hbm.getServiceNameRMI());
                        //Caso o objecto recebido seja uma instancia de String...
                    } else if (obj instanceof String) {

                        //Mostra a String
                        System.out.println((String) obj + " (" + obj.getClass() + ")");
                    }

                    System.out.println();
                    System.out.print("> ");

                } catch (ClassNotFoundException e) {
                    System.out.println();
                    System.out.println("Mensagem recebida de tipo inesperado!");
                } catch (IOException e) {
                    System.out.println();
                    System.out.println("Impossibilidade de aceder ao conteudo da mensagem recebida!");
                }

            }

        } catch (IOException e) {
            if (running) {
                System.out.println(e);
            }

            if (!s.isClosed()) {
                s.close();
            }
        }

    }
}