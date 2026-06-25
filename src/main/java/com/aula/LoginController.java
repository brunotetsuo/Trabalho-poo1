package com.aula;

import com.aula.dao.MembroDao;
import com.aula.model.Membro;
import com.aula.util.Sessao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginController {

    @FXML
    private TextField usuarioField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private Label mensagem;

    private final MembroDao membroDao = new MembroDao();

    @FXML
    public void fazerLogin() {
        String login = usuarioField.getText();
        String senha = senhaField.getText();

        if (login.isEmpty() || senha.isEmpty()) {
            mensagem.setText("Preencha todos os campos!");
            return;
        }

        Optional<Membro> membro = membroDao.buscarPorLoginESenha(login, senha);

        if (membro.isPresent()) {
            Sessao.setMembroLogado(membro.get());
            mensagem.setText("Login OK!");
            abrirMenu();
        } else {
            mensagem.setText("Login ou senha invalidos!");
        }
    }

    private void abrirMenu() {
        try {
            Stage stage = (Stage) usuarioField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/menu.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/main/menu.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void linkCadastro() {
        try {
            Stage stage = (Stage) usuarioField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/registro.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/main/registro.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
