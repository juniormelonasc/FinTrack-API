package com.mycompany.fintrack.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transacao {
    private int id;
    private String descricao;
    private BigDecimal valor;
    private TipoTransacao tipo;
    private LocalDate data;

    // construtores getters e setters
    public Transacao() {}

    public Transacao(String descricao, BigDecimal valor, TipoTransacao tipo, LocalDate data) {
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.data = data;
    }

    // getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public TipoTransacao getTipo() { return tipo; }
    public void setTipo(TipoTransacao tipo) { this.tipo = tipo; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public boolean isReceita() {
        return tipo == TipoTransacao.RECEITA;
    }
}