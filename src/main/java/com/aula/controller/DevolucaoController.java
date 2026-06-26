package com.aula.controller;

import com.aula.model.Emprestimo;
import com.aula.service.EmprestimoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DevolucaoController {

    @FXML
    private ListView<Emprestimo> listaEmprestados;

    private final EmprestimoService emprestimoService = new EmprestimoService();

    @FXML
    public void initialize() {
        listaEmprestados.getItems().setAll(emprestimoService.buscarAtivos());
    }

    @FXML
    public void devolverLivro(ActionEvent actionEvent) {
        Emprestimo emprestimo = listaEmprestados.getSelectionModel().getSelectedItem();
        if (emprestimo == null) {
            mostrarAlerta("Selecione um emprestimo para devolver.");
            return;
        }

        try {
            emprestimoService.devolverLivro(emprestimo);
            listaEmprestados.getItems().setAll(emprestimoService.buscarAtivos());
            mostrarAlerta("Devolucao realizada com sucesso!");
        } catch (IllegalStateException e) {
            mostrarAlerta(e.getMessage());
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

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Devolucao");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
