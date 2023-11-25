package Servidores.ServidorPrincipal.BDConection;

import share.consultas.ConsultPresence;
import share.events.events;
import share.registo.registo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class conectionBD {

    private static final String NOME_BD = "dataBaseTP.db";
    private int IDUtilizador;
    private static String dbDir;

    private static volatile conectionBD instance = null;

    // URL de conexão com o banco de dados
    private static String URL_CONEXAO;

    private static Connection conn;

    public static conectionBD getInstance() {
        if (instance == null) {
            synchronized(conectionBD.class) {
                if (instance == null) {
                    instance = new conectionBD(dbDir);
                }
            }
        }
        return instance;
    }


    // Start/create functions
    public conectionBD(String dbDir) {
        //connect a BD

        URL_CONEXAO = "jdbc:sqlite:" + dbDir + "/" + NOME_BD;

        criarBD();

        try {
            conn = java.sql.DriverManager.getConnection(URL_CONEXAO);
            System.out.println("Connection to SQLite has been established.");
            setIDUtilizador(getLastIDUtilizador() + 1);
            instance = this;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    } // testar -> OK
    public static void criarBD() {
        try {
            Connection conexao = DriverManager.getConnection(URL_CONEXAO);
            Statement statement = conexao.createStatement();

            // Lê o script SQL do arquivo "bdScript.sql" e executa-o
            StringBuilder scriptSQL = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader("bdScript.sql"))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    scriptSQL.append(linha).append("\n");
                }
            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo de script SQL: " + e.getMessage());
                return;
            }

            statement.executeUpdate(scriptSQL.toString());

            statement.close();
            conexao.close();

            System.out.println("Base de dados criada com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar a base de dados: " + e.getMessage());
        }
    } // testar -> OK


    //private functions
    private int getID(String email){
        try {
            String selectQuery = "SELECT * FROM Utilizadores WHERE Email = '" + email + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            // Verificar se há algum resultado
            int id = rs.getInt("Numero_Indentificacao");

            System.out.println("ID: " + id);


            // Fechar recursos
            rs.close();
            stmt.close();

            return id;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar id, utilizador inexistente: " + e.getMessage());
            return -1;
        }
    }

    private boolean existEvento(String designacaoEvent){
        try {
            String selectQuery = "SELECT * FROM Eventos WHERE Designacao = '" + designacaoEvent + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            // Verificar se há algum resultado
            boolean existe = rs.next();

            // Fechar recursos
            rs.close();
            stmt.close();

            return existe;
        } catch (SQLException e) {
            System.err.println("Erro ao verificar se evento existe: " + e.getMessage());
            return false;
        }
    }

    private boolean existPresencasUtilizador(String email) {

        try {
            String selectQuery = "SELECT * FROM Presencas WHERE Utilizador_ID = '" + getID(email) + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            // Verificar se há algum resultado
            boolean existe = rs.next();

            // Fechar recursos
            rs.close();
            stmt.close();

            return existe;
        } catch (SQLException e) {
            System.err.println("Erro ao verificar se presenca existe: " + e.getMessage());
            return false;
        }
    }

    private boolean existPresencas(String email, String designacaoEvent) {

        try {
            String selectQuery = "SELECT * FROM Presencas WHERE Evento_Designacao = '" + designacaoEvent + "' OR Utilizador_ID = '" + getID(email) + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            // Verificar se há algum resultado
            boolean existe = rs.next();

            // Fechar recursos
            rs.close();
            stmt.close();

            return existe;
        } catch (SQLException e) {
            System.err.println("Erro ao verificar se presenca existe: " + e.getMessage());
            return false;
        }
    }

    private boolean existPresencasEvent(String designacaoEvent) {

        try {
            String selectQuery = "SELECT * FROM Presencas WHERE Evento_Designacao = '" + designacaoEvent + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            // Verificar se há algum resultado
            boolean existe = rs.next();

            // Fechar recursos
            rs.close();
            stmt.close();

            return existe;
        } catch (SQLException e) {
            System.err.println("Erro ao verificar se presenca existe: " + e.getMessage());
            return false;
        }
    }

    private boolean verificaCodigo(String designacaoEvent) {

        //verificar se existe codigo e se o tempo de limite ainda esta dentro do tempo limite

        try {
            String selectQuery = "SELECT * FROM Codigos WHERE Evento_Designacao = '" + designacaoEvent + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            // Verificar se há algum resultado
            boolean existe = rs.next();

            if (existe){
                //verificar se o tempo limite ainda esta dentro do tempo limite
                if(rs.getInt("Tempo") == 0){
                    //codigo expirou
                    existe = false;
                }
            }

            // Fechar recursos
            rs.close();
            stmt.close();

            return existe;
        } catch (SQLException e) {
            System.err.println("Erro ao verificar se codigo existe: " + e.getMessage());
            return false;
        }

    }

    private events getEvento(String designacaoEvent) {

        try(Statement stmt = conn.createStatement()) {
            String selectQuery = "SELECT * FROM Eventos WHERE Designacao = '" + designacaoEvent + "'";
            ResultSet rs = stmt.executeQuery(selectQuery);

            // Verificar se há algum resultado
            events event = new events(rs.getString("Designacao"), rs.getString("Localidade"), rs.getString("Data"), rs.getString("Hora_Inicio"), rs.getString("Hora_Fim"));

            // Fechar recursos
            rs.close();

            return event;
        } catch (SQLException e) {
            System.err.println("Erro ao verificar se evento existe: " + e.getMessage());
            return null;
        }
    }


    //Variavel statica
    private int getIDUtilizador() {return IDUtilizador;}
    private void incrementIDUtilizador() {IDUtilizador++;}
    private void setIDUtilizador(int ID) {IDUtilizador = ID;}
    private int getLastIDUtilizador(){

        try(Statement stmt = conn.createStatement()){

            String selectQuery = "SELECT Numero_Indentificacao FROM Utilizadores ORDER BY Numero_Indentificacao DESC LIMIT 1";
            ResultSet rs = stmt.executeQuery(selectQuery);

            // Verificar se há algum resultado
            int id = rs.getInt("Numero_Indentificacao");

            // Fechar recursos
            rs.close();

            return id;

        } catch (SQLException e) {
            System.err.println("Erro ao buscar id, utilizador inexistente: " + e.getMessage());
            return -1;

        }

    }   // vai a base de dados buscar o ultimo utilizador registado e retorna o seu id



    //Utilizadores

    public boolean autenticaCliente(String email, String password){
        System.out.println("entrei no autenticaCliente");

        try(Statement stmt = conn.createStatement()) {
            String selectQuery = "SELECT * FROM Utilizadores WHERE Email = '" + email + "' AND Password = '" + password + "'";
            ResultSet rs = stmt.executeQuery(selectQuery);

            // Verificar se há algum resultado
            boolean autenticado = rs.next();

            // Fechar recursos
            rs.close();

            return autenticado;
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar utilizador: " + e.getMessage());
            return false;
        }
    } // testar - > FEITO

    public boolean registaCliente(registo reg){

        if (autenticaCliente(reg.getEmail(), reg.getPassword()))
            return false;

        try(Statement stmt = conn.createStatement()) {
            String insertQuery = "INSERT INTO Utilizadores (Numero_Indentificacao,Nome, Email, Password) VALUES ('" + getIDUtilizador() + "','" + reg.getName() + "', '" + reg.getEmail() + "', '" + reg.getPassword() + "')";
            stmt.executeUpdate(insertQuery);

            incrementIDUtilizador();

            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao registar utilizador: " + e.getMessage());
            return false;
        }
    } //testar ->

    public void consultaCliente(String email){}

    public registo editCliente(registo editReg){

        // editar dados do utilizador

        try(Statement stmt = conn.createStatement()){
            String updateQuery = "UPDATE Utilizadores SET Nome = '" + editReg.getName() + "', Email = '" + editReg.getEmail() + "', Password = '" + editReg.getPassword() + "' WHERE Numero_Indentificacao = '" + editReg.getIdentificationNumber() + "'";
            stmt.executeUpdate(updateQuery);

            return editReg;

        } catch (SQLException e) {
            System.err.println("Erro ao editar utilizador: " + e.getMessage());
            return null;
        }
    } // testar ->


    //Eventos

    public String criaEvento(String designacaoEvent, String local, String data, String horaInicio, String horaFim) {

        //testar primeiro mas em principio mudar a data e as horas para o seu tipo de dados (data, Time)

        try(Statement stmt = conn.createStatement()) {
            if (existEvento(designacaoEvent))
                return "Evento ja existe";

            String insertQuery = "INSERT INTO Eventos (Designacao,Localidade, Data, Hora_Inicio, Hora_Fim) VALUES ('" + designacaoEvent + "', '" + local + "', '" + data + "', '" + horaInicio + "', '" + horaFim + "')";
            stmt.executeUpdate(insertQuery);

            return "Evento criado com sucesso";

        } catch (SQLException e) {
            System.err.println("Erro ao registar evento: " + e.getMessage());
            return "Erro ao criar um evento";
        }

    } // testar -> FEITO

    public String editEvento(events editEvent){
        // editar dados do evento se nao tiver presenças registadas

        if (editEvent.getMsg() == null || editEvent.getMsg().equals(""))
            return "Nome do Evento a alterar nao pode ser Null";

        if (!existEvento(editEvent.getMsg()))
            return "Evento não existe";

        if (existPresencasEvent(editEvent.getMsg()))
            return "Evento ja tem presencas registadas";

        events auxEvent = getEvento(editEvent.getMsg());

        if (editEvent.getDescricao() == null)
            editEvent.setDescricao(auxEvent.getDescricao());
        if (editEvent.getLocal() == null)
            editEvent.setLocal(auxEvent.getLocal());
        if (editEvent.getData() == null)
            editEvent.setData(auxEvent.getData());
        if (editEvent.getHoraIncio() == null)
            editEvent.setHoraIncio(auxEvent.getHoraIncio());
        if (editEvent.getHoraFim() == null)
            editEvent.setHoraFim(auxEvent.getHoraFim());



        try(Statement stmt = conn.createStatement()) {
            String updateQuery = "UPDATE Eventos SET Designacao = '" + editEvent.getDescricao() + "', Localidade = '" + editEvent.getLocal() + "', Data = '" + editEvent.getData() + "', Hora_Inicio = '" + editEvent.getHoraIncio() + "', Hora_Fim = '" + editEvent.getHoraFim() + "' WHERE Designacao = '" + editEvent.getMsg() + "'";
            stmt.executeUpdate(updateQuery);

            return "Evento editado com sucesso";

        } catch (SQLException e) {
            System.err.println("Erro ao editar evento: " + e.getMessage());
            return "Erro ao editar evento";
        }
    } // testar ->

    public String eliminaEvento(String designacaoEvent){

        // eliminar um evento desde que nao tenha presenças associadas

        if (!existEvento(designacaoEvent))
            return "Evento não existe";
        else if (existPresencasEvent(designacaoEvent))
            return "Event ja tem presencas associadas";

        try(Statement stmt = conn.createStatement()) {

            String deleteQuery = "DELETE FROM Eventos WHERE Designacao = '" + designacaoEvent + "'";
            stmt.executeUpdate(deleteQuery);

            return "Evento eliminado com sucesso";

        } catch (SQLException e) {
            System.err.println("Erro ao eliminar evento: " + e.getMessage());
            return "Erro ao eliminar evento";
        }

    } // testar -> FEITO

    public String eliminaPresenca(String email, String designacaoEvent){

        try(Statement stmt = conn.createStatement()){
            String deleteQuery = "DELETE FROM Presencas WHERE Evento_Designacao = '" + designacaoEvent + "' AND Utilizador_ID = '" + getID(email) + "'";
            stmt.executeUpdate(deleteQuery);

            return "Presenca eliminada com sucesso";

        } catch (SQLException e) {
            System.err.println("Erro ao eliminar presenca: " + e.getMessage());
            return "Erro ao eliminar presenca";
        }

    } // testar ->

    public String inserePresenca(String email, String designacaoEvent){

            if (!existEvento(designacaoEvent))
                return "Evento nao existe";

            if (existPresencas(email, designacaoEvent))
                return "Presenca ja registada";

            try(Statement stmt = conn.createStatement()) {

            String insertQuery = "INSERT INTO Presencas (Evento_Designacao,Utilizador_ID) VALUES ('" + designacaoEvent + "','" + getID(email) + "')";
            stmt.executeUpdate(insertQuery);

            return "Presenca registada com sucesso";

        } catch (SQLException e) {
            System.err.println("Erro ao registar presenca: " + e.getMessage());
            return "Erro ao registar presenca";
        }
    } // testar ->

    public String registaPresenca(String email, String designacaoEvent) {

            //verificar se codigo existe e se ainda esta dentro do tempo limite

            if (!verificaCodigo(designacaoEvent))
                return "Codigo nao existe ou ja expirou";

            return inserePresenca(email, designacaoEvent);

            /*if (verificaPresenca(email, designacaoEvent))
                return "Presenca ja registada";

            String insertQuery = "INSERT INTO Presencas (Evento_ID,Utilizador_ID) VALUES ('" + getIDEvent(designacaoEvent) + "','" + getID(email) + "')";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(insertQuery);

            // Fechar recursos
            rs.close();
            stmt.close();

            return "Presenca registada com sucesso";*/

    } // testar ->


    //Consultas
    public ConsultPresence consultaPresencasUtilizador(String email, String filtro){
        ConsultPresence consulta = new ConsultPresence();

        try (Statement stmt = conn.createStatement()) {
            String selectQuery = "SELECT P.Evento_Designacao, E.Localidade, E.Data, E.Hora_Inicio, E.Hora_Fim " +
                    "FROM Presencas P " +
                    "JOIN Eventos E ON P.Evento_Designacao = E.Designacao " +
                    "JOIN Utilizadores U ON P.Utilizador_ID = U.Numero_Indentificacao " +
                    "WHERE U.Email = '" + email + "' AND (E.Data LIKE '%" + filtro + "%' OR E.Localidade LIKE '%" + filtro + "%' OR E.Hora_Inicio LIKE '%" + filtro + "%' OR E.Hora_Fim LIKE '%" + filtro + "%')";

            try (ResultSet rs = stmt.executeQuery(selectQuery)) {
                while (rs.next()) {
                    // Aqui você pode processar os resultados, por exemplo, imprimindo no console
                    System.out.println("Evento: " + rs.getString("Evento_Designacao"));
                    System.out.println("Localidade: " + rs.getString("Localidade"));
                    System.out.println("Data: " + rs.getDate("Data"));
                    System.out.println("Hora Início: " + rs.getTime("Hora_Inicio"));
                    System.out.println("Hora Fim: " + rs.getTime("Hora_Fim"));
                    System.out.println("-----------------------");

                    consulta.getEvent().add(new events(rs.getString("Evento_Designacao"), rs.getString("Localidade"), rs.getString("Data"), rs.getString("Hora_Inicio"), rs.getString("Hora_Fim")));

                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar presenças do utilizador: " + e.getMessage());
        }
    }

    //Consulta de presencas de um evento
    public void consultaPresencasEvento(String designacaoEvent, String filtro){}

    //consulta de eventos criados
    public void consultaEventos(String filtro){}


    //Codigos
    public String geraCodigo(String designacaoEvent, String tempoLimite){

        if (!existEvento(designacaoEvent))
            return "Evento nao existe";

        int codigo = (int)(Math.random() * 100000);

        try(Statement stmt = conn.createStatement()) {
            String insertQuery = "INSERT INTO Codigos (Codigo,Tempo_Limite,Evento_Designacao) VALUES ('" + codigo + "','" + tempoLimite + "', '" + designacaoEvent + "')";
            stmt.executeUpdate(insertQuery);

            return "Codigo gerado com sucesso -> " + codigo;

        } catch (SQLException e) {
            System.err.println("Erro ao gerar codigo: " + e.getMessage());
            return "Erro ao gerar codigo";
        }

    } // testar ->

    //Versao

    public static int getVersion() {

        try(Statement stmt = conn.createStatement()){

            String selectQuery = "SELECT * FROM Versao";
            ResultSet rs = stmt.executeQuery(selectQuery);

            // Verificar se há algum resultado
            int versao = rs.getInt("numero_versao");

            // Fechar recursos
            rs.close();

            return versao;

        } catch (SQLException e) {
            System.err.println("Erro ao buscar Numero da versao : " + e.getMessage());
            return -1;

        }


    } // testar ->

    public void updateVersion() {

        try (Statement stmt = conn.createStatement()) {

            String updateQuery = "UPDATE Versao SET numero_versao = '" + (getVersion() + 1) + "'";
            stmt.executeUpdate(updateQuery);

        } catch (SQLException e) {
            System.err.println("Erro ao dar Update do numero da versao : " + e.getMessage());
        }
    } // testar ->

}
