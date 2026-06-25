package com.aula;

import com.aula.dao.AcervoDao;
import com.aula.model.Acervo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class AcervoController {

    @FXML
    private TextField buscaField;

    @FXML
    private ListView<Acervo> listaAcervos;

    private final AcervoDao acervoDao = new AcervoDao();

    @FXML
    public void initialize() {
        listaAcervos.getItems().setAll(acervoDao.buscarTodos());
    }

    @FXML
    private void buscarLivro() {
        String termo = buscaField.getText();
        if (termo.isEmpty()) {
            listaAcervos.getItems().setAll(acervoDao.buscarTodos());
        } else {
            listaAcervos.getItems().setAll(acervoDao.buscarPorTitulo(termo));
        }
    }

    @FXML
    public void selecionarLivro(ActionEvent actionEvent) {
        Acervo acervo = listaAcervos.getSelectionModel().getSelectedItem();
        if (acervo == null) {
            mostrarAlerta("Selecione um item no acervo.");
            return;
        }
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

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acervo");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
