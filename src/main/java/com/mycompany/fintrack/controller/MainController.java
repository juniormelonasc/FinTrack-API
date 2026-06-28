package com.mycompany.fintrack.controller;

import com.mycompany.fintrack.dao.TransacaoDAO;
import com.mycompany.fintrack.model.Transacao;
import com.mycompany.fintrack.utils.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;

public class MainController {

    @FXML
    private TableView<Transacao> tabelaTransacoes;
    @FXML
    private TableColumn<Transacao, String> colData;
    @FXML
    private TableColumn<Transacao, String> colDescricao;
    @FXML
    private TableColumn<Transacao, BigDecimal> colValor;
    @FXML
    private TableColumn<Transacao, String> colTipo;

    private final TransacaoDAO dao = new TransacaoDAO();
    private final ObservableList<Transacao> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        atualizarTabela();
    }

    @FXML
    public void atualizarTabela() {
        try {
            lista.setAll(dao.listarTodas());
            tabelaTransacoes.setItems(lista);
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtil.mostrarErro("Erro ao carregar transações: " + e.getMessage());
        }
    }

    @FXML
    public void abrirNovaTransacao() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/novaTransacao.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nova Transação");
            stage.setScene(scene);
            stage.showAndWait();
            atualizarTabela();
        } catch (Exception e) {
            AlertUtil.mostrarErro("Erro ao abrir tela: " + e.getMessage());
        }
    }

    @FXML
    public void abrirRelatorio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/relatorio.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Relatório e Saldo");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            AlertUtil.mostrarErro("Erro ao abrir relatório: " + e.getMessage());
        }
    }

    @FXML
    public void removerTransacao() {
        Transacao selecionada = tabelaTransacoes.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            AlertUtil.mostrarErro("Selecione uma transação para remover.");
            return;
        }
        try {
            dao.deletar(selecionada.getId());
            atualizarTabela();
            AlertUtil.mostrarInfo("Transação removida com sucesso!");
        } catch (SQLException e) {
            AlertUtil.mostrarErro("Erro ao remover: " + e.getMessage());
        }
    }
}