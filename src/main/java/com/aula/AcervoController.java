package com.aula;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AcervoController {

    @FXML
    private TextField buscaField;

    @FXML
    private ListView<String> listaLivros;

    @FXML
    public void initialize() {
        listaLivros.getItems().addAll(
                "Java Básico",
                "Banco de Dados",
                "Estrutura de Dados",
                "Algoritmos"
        );
    }

    @FXML
    private void buscarLivro() {
        String busca = buscaField.getText().toLowerCase();

        listaLivros.getItems().clear();

        if (busca.contains("java")) {
            listaLivros.getItems().add("Java Básico");
        }
        if (busca.contains("dados")) {
            listaLivros.getItems().add("Banco de Dados");
        }
    }

    public void selecionarLivro(ActionEvent actionEvent) {
    }
}