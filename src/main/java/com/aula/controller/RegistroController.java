package com.aula.controller;

import com.aula.model.Membro;
import com.aula.service.MembroService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegistroController {

    @FXML
    private Label linkLogin;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private PasswordField confirmaField;

    @FXML
    private RadioButton membroComumRadio;

    @FXML
    private RadioButton membroEspecialRadio;

    private final MembroService membroService = new MembroService();

    @FXML
    private void irParaLogin() {
        try {
            Stage stage = (Stage) linkLogin.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void registroButton() {
        if (nomeField.getText().isEmpty() || loginField.getText().isEmpty()
                || senhaField.getText().isEmpty()) {
            mostrarErro("Preencha todos os campos!");
            return;
        }

        if (!senhaField.getText().equals(confirmaField.getText())) {
            mostrarErro("Senhas nao conferem!");
            return;
        }

        try {
            String tipoMembro = membroEspecialRadio.isSelected() ? "E" : "C";
            membroService.cadastrar(nomeField.getText(), loginField.getText(),
                    senhaField.getText(), tipoMembro);
            mostrarSucesso("Membro cadastrado com sucesso!");
            limparCampos();
        } catch (IllegalStateException e) {
            mostrarErro(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao cadastrar membro!");
        }
    }

    private void limparCampos() {
        nomeField.clear();
        loginField.clear();
        senhaField.clear();
        confirmaField.clear();
        membroComumRadio.setSelected(true);
        membroEspecialRadio.setSelected(false);
        nomeField.requestFocus();
    }

    private void mostrarErro(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarSucesso(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
