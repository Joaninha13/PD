package Servidores.ServidorPrincipal.ConectionClientThread;

import Servidores.ServidorPrincipal.BDConection.conectionBD;
import share.login.login;
import share.registo.registo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
public class conectionClientThread extends Thread{


    private Socket toClientSocket, auxSocket;
    private conectionBD bd;

    public conectionClientThread(Socket toClientSocket){
        this.toClientSocket = toClientSocket;
    }

    @Override
    public void run(){

        login log = null;
        registo reg = null;
        String msg;

        try (ObjectOutputStream oout = new ObjectOutputStream(toClientSocket.getOutputStream());
             ObjectInputStream oin = new ObjectInputStream(toClientSocket.getInputStream())){


            //assim da?

            if (oin.readObject() instanceof login) {
                System.out.println("Recebi class login");
                log = (login) oin.readObject();
            }
            else if (oin.readObject() instanceof registo) {
                System.out.println("Recebi registo");
                reg = (registo) oin.readObject();
            }
            else if (oin.readObject() instanceof String) {
                System.out.println("Recebi string");
                msg = (String) oin.readObject();
            }


            //ou assim?
            /*try{

                log = (login) oin.readObject();

            }catch(IOException e){
                System.out.println("Nao é login, é registo");
                reg = (registo) oin.readObject();
            }*/

            //ou assim?

            /*if ((log = (login) oin.readObject()) == null)//EOF
                return;
            else if((reg = (registo) oin.readObject()) == null)//EOF
                return;*/

            // para teste

            //System.out.println("[" + /*Thread.currentThread().*/getName() + "]Recebido \"" + log.getEmail() + "\" de " +
            //       toClientSocket.getInetAddress().getHostAddress() + ":" +
            //       toClientSocket.getPort());
            
            /*if (reg != null){
                registo auxReg = new registo(reg.getName(), reg.getIdentificationNumber(), reg.getEmail(), reg.getPassword());

                if (!bd.registaCliente()) {
                    auxReg.setRegistered(false);
                    auxReg.setMsg("Email ja se encontra registrado");

                }
                else {
                    auxReg.setRegistered(true);
                    auxReg.setMsg("Registo efetuado com sucesso");
                }

                oout.writeObject(auxReg);
                oout.flush();
                
            }
            else{

                login auxLog = new login(log.getEmail(), log.getPass());

                //Verificar se o mail e pass estao na BD
                if (!bd.autenticaCliente()) {
                    auxLog.setValid(false);
                    auxLog.setMsg("Email ou password errados");
                }
                else {
                    auxLog.setValid(true);
                    auxLog.setMsg("Bem vindo");
                }

                oout.writeObject(auxLog);
                oout.flush();

            }*/

            auxSocket = toClientSocket;
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
