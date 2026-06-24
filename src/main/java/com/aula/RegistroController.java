package com.aula;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.aula.model.Usuario;
import com.aula.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class RegistroController {

    @FXML
    private Label linkLogin;

    @FXML
    private void irParaLogin() {
        try {
            Stage stage = (Stage) linkLogin.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/main/login.fxml"));

            Scene scene = new Scene(loader.load());

            // Carrega o CSS
            scene.getStylesheets().add(
                    getClass().getResource("/main/login.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private TextField usuarioField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField senhaField;

    @FXML
    private TextField confirmaField;

    @FXML
    public void registrar() {

        if (!senhaField.getText().equals(confirmaField.getText())) {
            System.out.println("Senhas não conferem");
            return;
        }

        Usuario u = new Usuario();
        u.setNome(usuarioField.getText());
        u.setEmail(emailField.getText());
        u.setSenha(senhaField.getText());

        EntityManager em = JPAUtil.getEntityManager();

        em.getTransaction().begin();
        em.persist(u);
        em.getTransaction().commit();

        em.close();

        System.out.println("Usuário salvo!");
    }
}