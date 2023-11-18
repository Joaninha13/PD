package Servidores.ServidorPrincipal.BDConection;

import share.events.events;
import share.registo.registo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class conectionBD {

    private static final String NOME_BD = "dataBaseTP.db";

    private static int IDUtilizador = 2;


    // URL de conexão com o banco de dados
    private static final String URL_CONEXAO = "jdbc:sqlite:" + NOME_BD;

    Connection conn;


    // Start/create functions
    public conectionBD(){
        //connect a BD

        criarBD();

        try {
            conn = java.sql.DriverManager.getConnection(URL_CONEXAO);
            System.out.println("Connection to SQLite has been established.");
            setIDUtilizador(getLastIDUtilizador() + 1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    } // testar -> OK

    public static void criarBD() {
        try {
            Connection conexao = DriverManager.getConnection("jdbc:sqlite:" + NOME_BD);
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

    //Exemplo de uma query
    /*public String getResults() {
        String selectQuery = "SELECT * FROM Utilizadores";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);
            String results = "";
            while (rs.next()) {
                results += "USER '" + rs.getInt("Numero_Indentificacao") + "' --> name: " + rs.getString("Nome") + ", Email: " + rs.getString("Email") + "\n";
            }
            return results;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }*/


    //private functions
    private int getID(String email){
        try {
            String selectQuery = "SELECT * FROM Utilizadores WHERE Email = '" + email + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            // Verificar se há algum resultado
            int id = rs.getInt("Numero_Indentificacao");

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


    //Variavel statica
    private int getIDUtilizador() {return IDUtilizador;}
    private void incrementIDUtilizador() {IDUtilizador++;}
    private void setIDUtilizador(int ID) {ID = ID;}
    private int getLastIDUtilizador(){

        try{

            String selectQuery = "SELECT * FROM Utilizadores ORDER BY Numero_Indentificacao DESC LIMIT 1";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            // Verificar se há algum resultado
            int id = rs.getInt("Numero_Indentificacao");

            // Fechar recursos
            rs.close();
            stmt.close();

            return id;

        } catch (SQLException e) {
            System.err.println("Erro ao buscar id, utilizador inexistente: " + e.getMessage());
            return -1;

        }

    }   // vai a base de dados buscar o ultimo utilizador registado e retorna o seu id



    //Utilizadores

    public boolean autenticaCliente(String email, String password){
        System.out.println("entrei no autenticaCliente");

        try {
            String selectQuery = "SELECT * FROM Utilizadores WHERE Email = '" + email + "' AND Password = '" + password + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            // Verificar se há algum resultado
            boolean autenticado = rs.next();

            // Fechar recursos
            rs.close();
            stmt.close();

            return autenticado;
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar utilizador: " + e.getMessage());
            return false;
        }
    } // testar - >

    public boolean registaCliente(registo reg){

        if (autenticaCliente(reg.getEmail(), reg.getPassword()))
            return false;

        try {
            String insertQuery = "INSERT INTO Utilizadores (Numero_Indentificacao,Nome, Email, Password) VALUES ('" + getIDUtilizador() + "','" + reg.getName() + "', '" + reg.getEmail() + "', '" + reg.getPassword() + "')";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(insertQuery);

            // Fechar recursos
            rs.close();
            stmt.close();

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

        try{
            String updateQuery = "UPDATE Utilizadores SET Nome = '" + editReg.getName() + "', Email = '" + editReg.getEmail() + "', Password = '" + editReg.getPassword() + "' WHERE Numero_Indentificacao = '" + editReg.getIdentificationNumber() + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(updateQuery);

            // Fechar recursos
            rs.close();
            stmt.close();

            return editReg;

        } catch (SQLException e) {
            System.err.println("Erro ao editar utilizador: " + e.getMessage());
            return null;
        }
    } // testar ->


    //Eventos

    //ver este ainda, nao sei se esta bem
    public String criaEvento(String designacaoEvent, String local, String data, String horaInicio, String horaFim) {

        //testar primeiro mas em principio mudar a data e as horas para o seu tipo de dados (data, Time)

        try {
            if (existEvento(designacaoEvent))
                return "Evento ja existe";

            String insertQuery = "INSERT INTO Eventos (Designacao,Localidade, Data, Hora_Inicio, Hora_Fim) VALUES ('" + designacaoEvent + "', '" + local + "', '" + data + "', '" + horaInicio + "', '" + horaFim + "')";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(insertQuery);

            // Fechar recursos
            rs.close();
            stmt.close();

            return "Evento criado com sucesso";

        } catch (SQLException e) {
            System.err.println("Erro ao registar evento: " + e.getMessage());
            return "Erro ao criar um evento";
        }

    } // testar ->

    public events editEvento(events editEvent){

        // editar dados do evento se nao tiver presenças registadas

        if (editEvent.getMsg() != null)
            if (!existPresencasEvent(editEvent.getMsg())) {
                editEvent.setMsg("Evento ja tem presenças registadas");
                return editEvent;
            }

        try{
            String updateQuery = "UPDATE Eventos SET Designacao_Evento = '" + editEvent.getDescricao() + "', Local = '" + editEvent.getLocal() + "', Data = '" + editEvent.getData() + "', Hora_Inicio = '" + editEvent.getHoraIncio() + "', Hora_Fim = '" + editEvent.getHoraFim() + "' WHERE Designacao = '" + editEvent.getMsg() + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(updateQuery);

            // Fechar recursos
            rs.close();
            stmt.close();

            editEvent.setMsg("Evento editado com sucesso");

            return editEvent;

        } catch (SQLException e) {
            System.err.println("Erro ao editar evento: " + e.getMessage());
            return null;
        }
    } // testar ->

    public String eliminaEvento(String designacaoEvent){

        // eliminar um evento desde que nao tenha presenças associadas

        if (!existEvento(designacaoEvent))
            return "Evento ja tem presenças registadas";

        try {
            if (!existEvento(designacaoEvent))
                return "Event ja tem presencas associadas";

            String deleteQuery = "DELETE FROM Eventos WHERE Designacao_Evento = '" + designacaoEvent + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(deleteQuery);

            // Fechar recursos
            rs.close();
            stmt.close();

            return "Evento eliminado com sucesso";

        } catch (SQLException e) {
            System.err.println("Erro ao eliminar evento: " + e.getMessage());
            return "Erro ao eliminar evento";
        }

    } // testar ->

    public String eliminaPresenca(String email, String designacaoEvent){

        try{
            String deleteQuery = "DELETE FROM Presencas WHERE Evento_Designacao = '" + designacaoEvent + "' AND Utilizador_ID = '" + getID(email) + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(deleteQuery);

            // Fechar recursos
            rs.close();
            stmt.close();

            return "Presenca eliminada com sucesso";

        } catch (SQLException e) {
            System.err.println("Erro ao eliminar presenca: " + e.getMessage());
            return "Erro ao eliminar presenca";
        }

    } // testar ->

    public String inserePresenca(String email, String designacaoEvent){

        try {

            if (!existEvento(designacaoEvent))
                return "Evento nao existe";

            if (existPresencas(email, designacaoEvent))
                return "Presenca ja registada";


            String insertQuery = "INSERT INTO Presencas (Evento_Designacao,Utilizador_ID) VALUES ('" + designacaoEvent + "','" + getID(email) + "')";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(insertQuery);

            // Fechar recursos
            rs.close();
            stmt.close();

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
    public void consultaPresencasUtilizador(String email, String filtro){}
    public void consultaPresencasEvento(String designacaoEvent, String filtro){}
    public void consultaEventos(String filtro){}


    //Codigos
    public String geraCodigo(String designacaoEvent, String tempoLimite){

        if (!existEvento(designacaoEvent))
            return "Evento nao existe";

        int codigo = (int)(Math.random() * 100000);

        try {
            String insertQuery = "INSERT INTO Codigos (Codigo,Tempo_Limite,Evento_Designacao) VALUES ('" + codigo + "','" + tempoLimite + "', '" + designacaoEvent + "')";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(insertQuery);

            // Fechar recursos
            rs.close();
            stmt.close();

            return "Codigo gerado com sucesso -> " + codigo;

        } catch (SQLException e) {
            System.err.println("Erro ao gerar codigo: " + e.getMessage());
            return "Erro ao gerar codigo";
        }

    } // testar ->

}
