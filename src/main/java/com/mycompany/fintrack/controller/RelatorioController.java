package com.mycompany.fintrack.controller;

import com.mycompany.fintrack.dao.TransacaoDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;

public class RelatorioController {

    @FXML
    private Label labelSaldo;

    private final TransacaoDAO dao = new TransacaoDAO();

    @FXML
    public void initialize() {
        try {
            BigDecimal saldo = dao.calcularSaldo();
            if (saldo == null) {
                saldo = BigDecimal.ZERO;
            }
            labelSaldo.setText(String.format("R$ %.2f", saldo));
        } catch (SQLException e) {
            labelSaldo.setText("Erro ao calcular saldo");
            e.printStackTrace();
        }
    }

    @FXML
    public void fechar() {
        Stage stage = (Stage) labelSaldo.getScene().getWindow();
        stage.close();
    }
}