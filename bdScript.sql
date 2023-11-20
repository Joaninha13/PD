-- Path: bdScript.sql

CREATE TABLE IF NOT EXISTS Versao (numero_versao INTEGER NOT NULL);

CREATE TABLE IF NOT EXISTS Eventos (Designacao Varchar(100) PRIMARY KEY, Localidade Varchar(100), Data Date, Hora_Inicio Time, Hora_Fim Time);

CREATE TABLE IF NOT EXISTS Codigo_Registo (codigo Varchar(100),Tempo INTEGER,Evento_Designacao Varchar(100),FOREIGN KEY (Evento_Designacao) REFERENCES Evento (Designacao));

CREATE TABLE IF NOT EXISTS Utilizadores (Numero_Indentificacao INTEGER PRIMARY KEY, Nome Varchar(100), Email Varchar(100), Password Varchar(100));

-- Inserir um utilizador na tabela Utilizadores
INSERT INTO Utilizadores (Numero_Indentificacao, Nome, Email, Password) VALUES ('1', 'admin', 'admin@isec.pt', 'admin');

CREATE TABLE IF NOT EXISTS Presencas (Evento_Designacao Varchar(100),Utilizador_ID INTEGER,FOREIGN KEY (Evento_Designacao) REFERENCES Evento (Designacao), FOREIGN KEY (Utilizador_ID) REFERENCES Utilizadores (Numero_Indentificacao));

-- para teste

--INSERT INTO EVENTO (Designacao, Localidade, Data, Hora_Inicio, Hora_Fim) VALUES ('Evento1', 'Leiria', '1, '12:00:00', '13:00:00');
--INSERT INTO EVENTO (Designacao, Localidade, Data, Hora_Inicio, Hora_Fim) VALUES ('Evento2', 'COIMBRA', '2020-12-12', '12:00:00', '13:00:00');
--INSERT INTO EVENTO (Designacao, Localidade, Data, Hora_Inicio, Hora_Fim) VALUES ('Evento3', 'Lisboa', '2020-12-12', '12:00:00', '13:00:00');

--INSERT INTO Utilizadores (Numero_Indentificacao, Nome, Email, Password) VALUES ('2', 'luis', 'luis@isec.com', 'luis');
--INSERT INTO Utilizadores (Numero_Indentificacao, Nome, Email, Password) VALUES ('3', 'joao', 'joao@isec.com', 'joao');

--INSERT INTO Presencas (Evento_Designacao, Utilizador_ID) VALUES ('Evento1', '2');
--INSERT INTO Presencas (Evento_Designacao, Utilizador_ID) VALUES ('Evento1', '3');

--INSERT INTO CODIGO_REGISTO (codigo, Tempo, Evento_Designacao) VALUES ('123456', '10', 'Evento1');