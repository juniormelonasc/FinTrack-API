package com.mycompany.fintrack.repository;

import com.mycompany.fintrack.model.TipoTransacao;
import com.mycompany.fintrack.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByUsuarioId(Long usuarioId);

    List<Transacao> findByUsuarioIdAndDataBetween(Long usuarioId, LocalDate inicio, LocalDate fim);

    List<Transacao> findByUsuarioIdAndCategoriaId(Long usuarioId, Long categoriaId);

    @Query("SELECT COALESCE(SUM(CASE WHEN t.tipo = 'RECEITA' THEN t.valor ELSE -t.valor END), 0) " +
            "FROM Transacao t WHERE t.usuario.id = :usuarioId")
    BigDecimal calcularSaldo(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t " +
            "WHERE t.usuario.id = :usuarioId AND t.tipo = :tipo")
    BigDecimal somarPorTipo(@Param("usuarioId") Long usuarioId, @Param("tipo") TipoTransacao tipo);
}