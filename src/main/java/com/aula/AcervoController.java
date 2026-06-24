package com.aula;

import com.aula.model.BibliotecaDados;
import com.aula.model.Livro;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AcervoController {

    @FXML
    private TextField buscaField;

    @FXML
    private ListView<Livro> listaLivros;

    @FXML
    public void initialize() {
        listaLivros.getItems().setAll(BibliotecaDados.listarLivros());
    }

    @FXML
    private void buscarLivro() {
        listaLivros.getItems().setAll(BibliotecaDados.buscarLivros(buscaField.getText()));
    }

    @FXML
    public void selecionarLivro(ActionEvent actionEvent) {
        Livro livro = listaLivros.getSelectionModel().getSelectedItem();

        if (livro == null) {
            mostrarAlerta("Selecione um livro no acervo.");
            return;
        }

        BibliotecaDados.setLivroSelecionado(livro);
        abrirTelaEmprestimo(actionEvent);
    }

    private void abrirTelaEmprestimo(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/emprestimo.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/main/emprestimo.css").toExternalForm());

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Nao foi possivel abrir a tela de emprestimo.");
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acervo");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
