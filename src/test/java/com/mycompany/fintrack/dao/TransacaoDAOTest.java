package com.mycompany.fintrack.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

class TransacaoDAOTest {

    private static Connection connection;
    private TransacaoDAO dao;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        String createTable = "CREATE TABLE transacoes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao VARCHAR(100), " +
                "valor DECIMAL(10,2), " +
                "tipo VARCHAR(10), " +
                "data DATE)";
        connection.createStatement().execute(createTable);
    }

    @BeforeEach
    void setupDAO() throws SQLException {
        dao = new TransacaoDAO() {
            @Override
            public Connection getConnection() {
                return connection;
            }
        };
    }

    @AfterEach
    void clean() throws SQLException {
        connection.createStatement().execute("DELETE FROM transacoes");
    }

    @Test
    void testInserirEListar() throws SQLException {
        Transacao t = new Transacao("Teste", new BigDecimal("100.00"), TipoTransacao.RECEITA, LocalDate.now());
        dao.inserir(t);
        List<Transacao> lista = dao.listarTodas();
        assertEquals(1, lista.size());
        assertEquals("Teste", lista.get(0).getDescricao());
    }

    @Test
    void testCalcularSaldo() throws SQLException {
        dao.inserir(new Transacao("R1", new BigDecimal("200"), TipoTransacao.RECEITA, LocalDate.now()));
        dao.inserir(new Transacao("D1", new BigDecimal("50"), TipoTransacao.DESPESA, LocalDate.now()));
        BigDecimal saldo = dao.calcularSaldo();
        assertEquals(new BigDecimal("150.00"), saldo);
    }
}