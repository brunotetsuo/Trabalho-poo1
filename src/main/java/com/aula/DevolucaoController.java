package com.aula;

import com.aula.model.BibliotecaDados;
import com.aula.model.Emprestimo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

public class DevolucaoController {
    @FXML
    private ListView<Emprestimo> listaEmprestados;

    @FXML
    public void initialize() {
        listaEmprestados.getItems().setAll(BibliotecaDados.listarEmprestimos());
    }

    @FXML
    public void devolverLivro(ActionEvent actionEvent) {
        Emprestimo emprestimo = listaEmprestados.getSelectionModel().getSelectedItem();

        if (emprestimo == null) {
            mostrarAlerta("Selecione um emprestimo para devolver.");
            return;
        }

        BibliotecaDados.devolver(emprestimo);
        listaEmprestados.getItems().setAll(BibliotecaDados.listarEmprestimos());
        mostrarAlerta("Livro devolvido. Ele voltou a ficar disponivel no acervo.");
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Devolucao");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
