package Servidores.ServidorPrincipal.ConectionClientThread;

import Servidores.ServidorPrincipal.BDConection.conectionBD;
import share.consultas.ConsultPresence;
import share.events.events;
import share.login.login;
import share.registo.registo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
public class conectionClientThread extends Thread{


    private Socket toClientSocket, auxSocket;
    private conectionBD bd;

    public conectionClientThread(Socket toClientSocket, conectionBD bd){
        this.bd = bd;
        this.toClientSocket = toClientSocket;
        auxSocket = toClientSocket;
    }

    @Override
    public void run(){

        login log = null;
        registo reg = null;
        events event = null;
        String msg, resp;

        try (ObjectOutputStream oout = new ObjectOutputStream(toClientSocket.getOutputStream());
             ObjectInputStream oin = new ObjectInputStream(toClientSocket.getInputStream())){

            Object obj = oin.readObject();

            // talvez mudar estes dois primeiros ifs para so mandar uma mensagem a dizer que o foi bem sucedido o não!!
            if (obj instanceof login) {

                System.out.println("Recebi class login");
                log = (login) obj;

                System.out.println("Depois do read -> " + log.getEmail() + " " + log.getPass());

                if (!bd.autenticaCliente(log.getEmail(), log.getPass())) {
                    log.setValid(false);
                    log.setMsg("Email ou password errados");
                }
                else {
                    System.out.println("entrei no else");
                    log.setValid(true);
                    log.setMsg("Bem vindo");
                }


                oout.writeObject(log);
                oout.flush();


            }
            else if (obj instanceof registo) {

                System.out.println("Recebi registo");
                reg = (registo) obj;


                if (reg.getMsg().equalsIgnoreCase("edit")){
                    //editar dados do utilizador
                    oout.writeObject(bd.editCliente(reg));
                    oout.flush();
                }
                else if (!bd.registaCliente(reg)) {
                    reg.setRegistered(false);
                    reg.setMsg("Email ja se encontra registrado");

                }
                else {
                    reg.setRegistered(true);
                    reg.setMsg("Registo efetuado com sucesso");
                }

                oout.writeObject(reg);
                oout.flush();

                System.out.println("Mandei class registo");

            }
            else if (obj instanceof events) {

                System.out.println("Recebi a classe events");
                event = (events) obj;

                oout.writeObject(bd.editEvento(event));
                oout.flush();

            }
            else if (obj instanceof ConsultPresence) {/*fazer coisas depois tenho de ver*/}
            else if (obj instanceof String) {
                System.out.println("Recebi string");
                msg = (String) obj;

                //divisao da msg por espaços

                String[] parts = msg.split(" ");

                //verificar se o primeiro elemento é um comando

                if (parts[0].equals("sub")){
                    //adicionar o utilizador a lista de presenças do evento

                    oout.writeObject(bd.registaPresenca(parts[1], parts[2]));
                    oout.flush();

                }
                else if (parts[0].equals("create")) {
                    //criar evento

                    oout.writeObject(bd.criaEvento(parts[1], parts[2], parts[3], parts[4], parts[5]));
                    oout.flush();

                }
                else if (parts[0].equals("delete")) {
                    //eliminar evento

                    oout.writeObject(bd.eliminaEvento(parts[1]));
                    oout.flush();

                }
                else if (parts[0].equals("checkin")) {
                    //adicionar o utilizador a lista de presenças do evento

                    oout.writeObject(bd.inserePresenca(parts[1], parts[2]));
                    oout.flush();

                }
                else if (parts[0].equals("checkout")) {
                    //remover o utilizador da lista de presenças do evento

                    oout.writeObject(bd.eliminaPresenca(parts[1], parts[2]));
                    oout.flush();

                }
                else if (parts[0].equals("gerar")) {
                    //gerar codigo

                    oout.writeObject(bd.geraCodigo(parts[1], parts[2]));
                    oout.flush();
                }
                else if (parts[0].equals("ConsultPresenca")) {
                    //gerar codigo

                    oout.writeObject(bd.geraCodigo(parts[1], parts[2]));
                    oout.flush();
                }

            }

        } catch (ClassNotFoundException e) {
            System.out.println();
            System.out.println("Mensagem recebida de tipo inesperado!");
        } catch (IOException e) {
            System.out.println();
            System.out.println("Impossibilidade de aceder ao conteudo da mensagem recebida!");
        } catch(Exception e){
            assert auxSocket != null;
            System.out.println("Problema na comunicacao com o cliente " +
                    auxSocket.getInetAddress().getHostAddress() + ":" +
                    auxSocket.getPort()+"\n\t" + e);
        }
    }

}
