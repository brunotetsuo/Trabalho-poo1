package com.aula.controller;

import com.aula.model.Acervo;
import com.aula.model.Membro;
import com.aula.service.AcervoService;
import com.aula.service.ReservaService;
import com.aula.util.Sessao;
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
    private ListView<Acervo> listaAcervos;

    private final AcervoService acervoService = new AcervoService();
    private final ReservaService reservaService = new ReservaService();

    @FXML
    public void initialize() {
        listaAcervos.getItems().setAll(acervoService.buscarTodos());
    }

    @FXML
    private void buscarLivro() {
        String termo = buscaField.getText();
        if (termo.isEmpty()) {
            listaAcervos.getItems().setAll(acervoService.buscarTodos());
        } else {
            listaAcervos.getItems().setAll(acervoService.buscarPorTitulo(termo));
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

    @FXML
    public void reservarItem(ActionEvent actionEvent) {
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

        try {
            reservaService.criarReserva(membro, acervo);
            listaAcervos.getItems().setAll(acervoService.buscarTodos());
            mostrarAlerta("Reserva realizada com sucesso!");
        } catch (IllegalStateException e) {
            mostrarAlerta(e.getMessage());
        }
    }

    private void abrirTelaEmprestimo(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/emprestimo.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/emprestimo.css").toExternalForm());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Nao foi possivel abrir a tela de emprestimo.");
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
        alert.setTitle("Acervo");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
