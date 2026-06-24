package com.aula;

import com.aula.model.BibliotecaDados;
import com.aula.model.Emprestimo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ReservasController {
    @FXML
    private Label statusMembroLabel;

    @FXML
    private ListView<Emprestimo> listaReservados;

    @FXML
    public void initialize() {
        statusMembroLabel.setText("Status de membro: " + BibliotecaDados.getTipoMembroUsuarioLogado());
        atualizarLista();
    }

    @FXML
    public void devolverLivro(ActionEvent actionEvent) {
        Emprestimo emprestimo = listaReservados.getSelectionModel().getSelectedItem();

        if (emprestimo == null) {
            mostrarAlerta("Selecione um livro emprestado para devolver.");
            return;
        }

        BibliotecaDados.devolver(emprestimo);
        atualizarLista();
        mostrarAlerta("Livro devolvido com sucesso.");
    }

    private void atualizarLista() {
        listaReservados.getItems().setAll(BibliotecaDados.listarEmprestimosDoUsuarioLogado());
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reservas");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
