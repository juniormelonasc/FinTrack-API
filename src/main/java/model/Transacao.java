/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Júnior Melo
 */

import java.time.LocalDate;

public class Transacao {
    private String descricao;
    private double valor;
    private boolean ehReceita; // true = receita, false = despesa
    private LocalDate data;

    // Construtor
    public Transacao(String descricao, double valor, boolean ehReceita, LocalDate data) {
        this.descricao = descricao;
        this.valor = valor;
        this.ehReceita = ehReceita;
        this.data = data;
    }

    // Getters e Setters
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public boolean isEhReceita() {
        return ehReceita;
    }

    public void setEhReceita(boolean ehReceita) {
        this.ehReceita = ehReceita;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    // Metodo para exibir resumo 
    public String getResumo() {
        String tipo = ehReceita ? "RECEITA" : "DESPESA";
        return String.format("%s | %s | %s | %s",
                tipo,
                descricao,
                utils.Formatador.formatarValor(valor),
                utils.Formatador.formatarData(data));
    }
}