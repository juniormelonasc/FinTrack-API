package com.mycompany.fintrack.service;

import com.mycompany.fintrack.dto.SaldoDTO;
import com.mycompany.fintrack.dto.TransacaoDTO;
import com.mycompany.fintrack.exception.ResourceNotFoundException;
import com.mycompany.fintrack.exception.SaldoInsuficienteException;
import com.mycompany.fintrack.model.Categoria;
import com.mycompany.fintrack.model.TipoTransacao;
import com.mycompany.fintrack.model.Transacao;
import com.mycompany.fintrack.model.Usuario;
import com.mycompany.fintrack.repository.CategoriaRepository;
import com.mycompany.fintrack.repository.TransacaoRepository;
import com.mycompany.fintrack.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public TransacaoService(TransacaoRepository transacaoRepository,
                            UsuarioRepository usuarioRepository,
                            CategoriaRepository categoriaRepository) {
        this.transacaoRepository = transacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<TransacaoDTO> listarPorUsuario(Long usuarioId) {
        return transacaoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // NOVO: filtro por período
    public List<TransacaoDTO> listarPorPeriodo(Long usuarioId, LocalDate inicio, LocalDate fim) {
        return transacaoRepository.findByUsuarioIdAndDataBetween(usuarioId, inicio, fim)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // NOVO: filtro por categoria
    public List<TransacaoDTO> listarPorCategoria(Long usuarioId, Long categoriaId) {
        return transacaoRepository.findByUsuarioIdAndCategoriaId(usuarioId, categoriaId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public TransacaoDTO criar(Long usuarioId, TransacaoDTO dto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        TipoTransacao tipo = TipoTransacao.valueOf(dto.getTipo().toUpperCase());

        if (tipo == TipoTransacao.DESPESA) {
            BigDecimal saldo = transacaoRepository.calcularSaldo(usuarioId);
            if (saldo.compareTo(dto.getValor()) < 0) {
                throw new SaldoInsuficienteException("Saldo insuficiente para esta despesa");
            }
        }

        Transacao transacao = new Transacao();
        transacao.setDescricao(dto.getDescricao());
        transacao.setValor(dto.getValor());
        transacao.setTipo(tipo);
        transacao.setData(dto.getData());
        transacao.setUsuario(usuario);

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
            transacao.setCategoria(categoria);
        }

        return toDTO(transacaoRepository.save(transacao));
    }

    // NOVO: atualizar transação
    @Transactional
    public TransacaoDTO atualizar(Long usuarioId, Long transacaoId, TransacaoDTO dto) {
        Transacao transacao = transacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));

        if (!transacao.getUsuario().getId().equals(usuarioId)) {
            throw new ResourceNotFoundException("Transação não pertence a este usuário");
        }

        TipoTransacao novoTipo = TipoTransacao.valueOf(dto.getTipo().toUpperCase());

        // Se for despesa, valida o saldo (somando o valor antigo de volta)
        if (novoTipo == TipoTransacao.DESPESA) {
            BigDecimal saldoAtual = transacaoRepository.calcularSaldo(usuarioId);
            // devolve o valor antigo se a transação original era despesa
            if (transacao.getTipo() == TipoTransacao.DESPESA) {
                saldoAtual = saldoAtual.add(transacao.getValor());
            }
            if (saldoAtual.compareTo(dto.getValor()) < 0) {
                throw new SaldoInsuficienteException("Saldo insuficiente para esta despesa");
            }
        }

        transacao.setDescricao(dto.getDescricao());
        transacao.setValor(dto.getValor());
        transacao.setTipo(novoTipo);
        transacao.setData(dto.getData());

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
            transacao.setCategoria(categoria);
        } else {
            transacao.setCategoria(null);
        }

        return toDTO(transacaoRepository.save(transacao));
    }

    @Transactional
    public void deletar(Long usuarioId, Long transacaoId) {
        Transacao transacao = transacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));

        if (!transacao.getUsuario().getId().equals(usuarioId)) {
            throw new ResourceNotFoundException("Transação não pertence a este usuário");
        }

        transacaoRepository.delete(transacao);
    }

    public SaldoDTO calcularSaldo(Long usuarioId) {
        BigDecimal receitas = transacaoRepository.somarPorTipo(usuarioId, TipoTransacao.RECEITA);
        BigDecimal despesas = transacaoRepository.somarPorTipo(usuarioId, TipoTransacao.DESPESA);
        BigDecimal saldo = receitas.subtract(despesas);
        return new SaldoDTO(saldo, receitas, despesas);
    }

    private TransacaoDTO toDTO(Transacao t) {
        return new TransacaoDTO(
                t.getId(),
                t.getDescricao(),
                t.getValor(),
                t.getTipo().name(),
                t.getData(),
                t.getCategoria() != null ? t.getCategoria().getId() : null
        );
    }
}