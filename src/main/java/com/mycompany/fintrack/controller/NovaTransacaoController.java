package com.mycompany.fintrack.controller;

import com.mycompany.fintrack.dao.TransacaoDAO;
import com.mycompany.fintrack.model.TipoTransacao;
import com.mycompany.fintrack.model.Transacao;
import com.mycompany.fintrack.utils.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NovaTransacaoController {

    @FXML
    private TextField campoDescricao;
    @FXML
    private TextField campoValor;
    @FXML
    private DatePicker campoData;
    @FXML
    private RadioButton radioReceita;
    @FXML
    private RadioButton radioDespesa;

    private final TransacaoDAO dao = new TransacaoDAO();

    @FXML
    public void salvar() {
        try {
            // Validação da descrição
            String descricao = campoDescricao.getText().trim();
            if (descricao.isEmpty()) {
                AlertUtil.mostrarErro("A descrição é obrigatória.");
                return;
            }

            // Validação do valor
            BigDecimal valor;
            try {
                String valorStr = campoValor.getText().trim().replace(",", ".");
                valor = new BigDecimal(valorStr);
                if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                    AlertUtil.mostrarErro("O valor deve ser positivo.");
                    return;
                }
            } catch (NumberFormatException e) {
                AlertUtil.mostrarErro("Valor inválido. Use o formato 99.90.");
                return;
            }

            // Data
            LocalDate data = campoData.getValue();
            if (data == null) {
                data = LocalDate.now();
            }

            // Tipo receita ou despesa
            TipoTransacao tipo;
            if (radioReceita.isSelected()) {
                tipo = TipoTransacao.RECEITA;
            } else if (radioDespesa.isSelected()) {
                tipo = TipoTransacao.DESPESA;
            } else {
                AlertUtil.mostrarErro("Selecione se é receita ou despesa.");
                return;
            }

            // Cria e insere a transação
            Transacao transacao = new Transacao(descricao, valor, tipo, data);
            dao.inserir(transacao);

            AlertUtil.mostrarInfo("Transação adicionada com sucesso!");
            fecharJanela();

        } catch (Exception e) {
            AlertUtil.mostrarErro("Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void cancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) campoDescricao.getScene().getWindow();
        stage.close();
    }
}