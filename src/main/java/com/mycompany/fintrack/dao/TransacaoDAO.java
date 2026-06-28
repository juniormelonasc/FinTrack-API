package com.mycompany.fintrack.dao;

import com.mycompany.fintrack.model.TipoTransacao;
import com.mycompany.fintrack.model.Transacao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransacaoDAO {

    public void inserir(Transacao t) throws SQLException {
        String sql = "INSERT INTO transacoes (descricao, valor, tipo, data) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getDescricao());
            ps.setBigDecimal(2, t.getValor());
            ps.setString(3, t.getTipo().name());
            ps.setDate(4, Date.valueOf(t.getData()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                t.setId(rs.getInt(1));
            }
        }
    }

    public void atualizar(Transacao t) throws SQLException {
        String sql = "UPDATE transacoes SET descricao=?, valor=?, tipo=?, data=? WHERE id=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getDescricao());
            ps.setBigDecimal(2, t.getValor());
            ps.setString(3, t.getTipo().name());
            ps.setDate(4, Date.valueOf(t.getData()));
            ps.setInt(5, t.getId());
            ps.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM transacoes WHERE id=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Optional<Transacao> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM transacoes WHERE id=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public List<Transacao> listarTodas() throws SQLException {
        List<Transacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM transacoes ORDER BY data DESC, id DESC";
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public BigDecimal calcularSaldo() throws SQLException {
        String sql = "SELECT SUM(CASE WHEN tipo='RECEITA' THEN valor ELSE -valor END) as saldo FROM transacoes";
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getBigDecimal("saldo") != null ? rs.getBigDecimal("saldo") : BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    private Transacao mapear(ResultSet rs) throws SQLException {
        Transacao t = new Transacao();
        t.setId(rs.getInt("id"));
        t.setDescricao(rs.getString("descricao"));
        t.setValor(rs.getBigDecimal("valor"));
        t.setTipo(TipoTransacao.valueOf(rs.getString("tipo")));
        t.setData(rs.getDate("data").toLocalDate());
        return t;
    }
}