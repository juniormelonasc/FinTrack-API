/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author Júnior Melo
 */

import model.Transacao;
import model.TransacaoMensal;
import exceptions.EntradaInvalidaException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinTracker {
    private List<Transacao> transacoes;

    public FinTracker() {
        transacoes = new ArrayList<>();
    }

    // Adicionar transação comum
    public void adicionarTransacao(String descricao, double valor, boolean ehReceita, LocalDate data) {
        Transacao t = new Transacao(descricao, valor, ehReceita, data);
        transacoes.add(t);
    }

    // Adicionar transação mensal (polimorfismo)
    public void adicionarTransacaoMensal(String descricao, double valor, boolean ehReceita, LocalDate data, int mesReferencia) {
        TransacaoMensal tm = new TransacaoMensal(descricao, valor, ehReceita, data, mesReferencia);
        transacoes.add(tm);
    }

    // Listar todas as transações
    public void listarTransacoes() {
        if (transacoes.isEmpty()) {
            System.out.println("Nenhuma transação cadastrada.");
            return;
        }
        System.out.println("\n----- LISTA DE TRANSAÇÕES -----");
        for (int i = 0; i < transacoes.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, transacoes.get(i).getResumo());
        }
    }

    // Remover transação por indice (1-based)
    public void removerTransacao(int indice) throws EntradaInvalidaException {
        if (indice < 1 || indice > transacoes.size()) {
            throw new EntradaInvalidaException("Índice inválido! Escolha um número entre 1 e " + transacoes.size());
        }
        Transacao removida = transacoes.remove(indice - 1);
        System.out.println("Transação removida: " + removida.getResumo());
    }

    // Calcular saldo total (receitas - despesas)
    public double calcularSaldoTotal() {
        double saldo = 0.0;
        for (Transacao t : transacoes) {
            if (t.isEhReceita()) {
                saldo += t.getValor();
            } else {
                saldo -= t.getValor();
            }
        }
        return saldo;
    }

    // Metodo auxiliar para verificar se há transações
    public boolean temTransacoes() {
        return !transacoes.isEmpty();
    }

    // Retorna o numero de transações 
    public int quantidadeTransacoes() {
        return transacoes.size();
    }
}