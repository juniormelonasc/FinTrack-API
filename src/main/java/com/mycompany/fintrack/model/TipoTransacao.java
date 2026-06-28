package com.mycompany.fintrack.model;

public enum TipoTransacao {
    RECEITA, DESPESA;

    @Override
    public String toString() {
        return this == RECEITA ? "Receita" : "Despesa";
    }
}