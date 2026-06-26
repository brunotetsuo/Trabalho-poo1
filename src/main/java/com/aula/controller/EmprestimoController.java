package com.aula.controller;

import com.aula.model.Acervo;
import com.aula.model.Membro;
import com.aula.service.EmprestimoService;
import com.aula.service.ReservaService;
import com.aula.util.Sessao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EmprestimoController {

    @FXML
    private TextField livroField;

    @FXML
    private TextField usuarioField;

    @FXML
    private DatePicker dataEmprestimo;

    @FXML
    private DatePicker dataDevolucao;

    @FXML
    private ListView<Acervo> listaAcervos;

    private final EmprestimoService emprestimoService = new EmprestimoService();
    private final ReservaService reservaService = new ReservaService();

    @FXML
    public void initialize() {
        Membro membroLogado = Sessao.getMembroLogado();
        if (membroLogado != null) {
            usuarioField.setText(membroLogado.getNomeCompleto());
            usuarioField.setEditable(false);
        }
        listaAcervos.getItems().setAll(emprestimoService.buscarItensDisponiveis());

        listaAcervos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                livroField.setText(newVal.getTitulo());
            }
        });
    }

    @FXML
    public void handleEmprestimo(ActionEvent actionEvent) {
        Acervo acervo = listaAcervos.getSelectionModel().getSelectedItem();
        if (acervo == null) {
            mostrarAlerta("Selecione um item no acervo.");
            return;
        }

        Membro membro = Sessao.getMembroLogado();
        if (membro == null) {
            mostrarAlerta("Nenhum membro logado.");
            return;
        }

        if (dataEmprestimo.getValue() == null || dataDevolucao.getValue() == null) {
            mostrarAlerta("Informe as datas de emprestimo e devolucao.");
            return;
        }

        try {
            emprestimoService.realizarEmprestimo(membro, acervo,
                    dataEmprestimo.getValue(), dataDevolucao.getValue());
            mostrarAlerta("Emprestimo registrado com sucesso!");
            limparCampos();
        } catch (IllegalStateException e) {
            if (e.getMessage().contains("ja esta emprestado") && !reservaService.temReservaAtiva(acervo)) {
                Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacao.setTitle("Item Indisponivel");
                confirmacao.setHeaderText(null);
                confirmacao.setContentText("Este item ja esta emprestado. Deseja fazer uma reserva?");
                if (confirmacao.showAndWait().get() == ButtonType.OK) {
                    try {
                        reservaService.criarReserva(membro, acervo);
                        mostrarAlerta("Reserva realizada com sucesso!");
                    } catch (Exception ex) {
                        mostrarAlerta(ex.getMessage());
                    }
                }
            } else {
                mostrarAlerta(e.getMessage());
            }
        }
    }

    @FXML
    public void irParaDevolucao(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/devolucao.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/devolucao.css").toExternalForm());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Nao foi possivel abrir a tela de devolucao.");
        }
    }

    @FXML
    private void irParaMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        livroField.clear();
        dataEmprestimo.setValue(null);
        dataDevolucao.setValue(null);
        listaAcervos.getSelectionModel().clearSelection();
        listaAcervos.getItems().setAll(emprestimoService.buscarItensDisponiveis());
        livroField.requestFocus();
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Emprestimo");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
