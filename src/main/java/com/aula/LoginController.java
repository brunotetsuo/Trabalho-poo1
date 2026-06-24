package com.aula;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.aula.model.Usuario;
import com.aula.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class LoginController {

    @FXML
    private TextField usuarioField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private Label mensagem;

    // 🔹 Abre menu
    private void abrirMenu() {
        try {
            Stage stage = (Stage) usuarioField.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/main/menu.fxml")
            );

            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(
                    getClass().getResource("/main/menu.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 Vai para cadastro
    @FXML
    private void linkCadastro() {
        try {
            Stage stage = (Stage) usuarioField.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/main/registro.fxml")
            );

            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(
                    getClass().getResource("/main/registro.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 Login com banco
    @FXML
    public void fazerLogin() {

        EntityManager em = JPAUtil.getEntityManager();

        TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nome = :nome AND u.senha = :senha",
                Usuario.class
        );

        query.setParameter("nome", usuarioField.getText());
        query.setParameter("senha", senhaField.getText());

        try {
            Usuario u = query.getSingleResult();

            mensagem.setText("Login OK!");

            // 🔥 AGORA SIM abre o menu
            abrirMenu();

        } catch (Exception e) {
            mensagem.setText("Usuário ou senha inválidos");
        }

        em.close();
    }
}