package com.aula;

import com.aula.model.Usuario;
import com.aula.util.JPAUtil;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistroController {

    @FXML
    private Label linkLogin;

    @FXML
    private TextField usuarioField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField senhaField;

    @FXML
    private TextField confirmaField;

    @FXML
    private RadioButton membroComumRadio;

    @FXML
    private RadioButton membroEspecialRadio;

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
        if (usuarioField.getText().isEmpty()
                || emailField.getText().isEmpty()
                || senhaField.getText().isEmpty()) {
            mostrarErro("Preencha todos os campos!");
            return;
        }

        if (!senhaField.getText().equals(confirmaField.getText())) {
            mostrarErro("Senhas nao conferem!");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();

        try {
            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM Usuario u WHERE u.email = :email",
                            Long.class
                    )
                    .setParameter("email", emailField.getText())
                    .getSingleResult();

            if (count > 0) {
                mostrarErro("Email ja cadastrado!");
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setNome(usuarioField.getText());
            usuario.setEmail(emailField.getText());
            usuario.setSenha(senhaField.getText());
            usuario.setTipoMembro(membroEspecialRadio.isSelected() ? "ESPECIAL" : "COMUM");

            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();

            mostrarSucesso("Usuario criado com sucesso!");
            limparCampos();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao cadastrar usuario!");
        } finally {
            em.close();
        }
    }

    private void limparCampos() {
        usuarioField.clear();
        emailField.clear();
        senhaField.clear();
        confirmaField.clear();
        membroComumRadio.setSelected(true);
        membroEspecialRadio.setSelected(false);
        usuarioField.requestFocus();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
