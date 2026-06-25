package com.aula;

import com.aula.dao.MembroDao;
import com.aula.model.Membro;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

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

    private final MembroDao membroDao = new MembroDao();

    @FXML
    private void irParaLogin() {
        try {
            Stage stage = (Stage) linkLogin.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/login.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/main/login.css").toExternalForm());
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

        Optional<Membro> existente = membroDao.buscarPorLogin(loginField.getText());
        if (existente.isPresent()) {
            mostrarErro("Login ja cadastrado!");
            return;
        }

        Membro membro = new Membro();
        membro.setNomeCompleto(nomeField.getText());
        membro.setLogin(loginField.getText());
        membro.setSenha(senhaField.getText());
        membro.setPunido(false);

        boolean especial = membroEspecialRadio.isSelected();
        membro.setTipoMembro(especial ? "E" : "C");
        membro.setLimiteEmprestimos(especial ? 5 : 3);

        try {
            membroDao.salvar(membro);
            mostrarSucesso("Membro cadastrado com sucesso!");
            limparCampos();
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
