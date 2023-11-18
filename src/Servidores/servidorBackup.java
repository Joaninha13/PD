package Servidores;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class servidorBackup {



    public static void main(String[] args) {
        boolean isEmptyDirectory;

        if(args.length != 1){
            System.out.println("Sintaxe: java servidorBackup <dir_replica_bd>");
            return;
        }

        Path dirRepBd = Paths.get(args[1]);

        //verifica se a diretoria não está vazia
        if((isEmptyDirectory = Files.list(dirRepBd).findAny().isPresent()) == false){
            System.out.println("Diretoria não está vazia!\n");
            return;
        }
    }
}