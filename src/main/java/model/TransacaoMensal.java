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

public class TransacaoMensal extends Transacao {
    private int mesReferencia; // 1 a 12

    public TransacaoMensal(String descricao, double valor, boolean ehReceita, LocalDate data, int mesReferencia) {
        super(descricao, valor, ehReceita, data);
        this.mesReferencia = mesReferencia;
    }

    public int getMesReferencia() {
        return mesReferencia;
    }

    public void setMesReferencia(int mesReferencia) {
        this.mesReferencia = mesReferencia;
    }

    @Override
    public String getResumo() {
        return super.getResumo() + String.format(" | Mês ref: %d", mesReferencia);
    }
}