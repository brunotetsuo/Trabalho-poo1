package com.aula;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuController {

    private void trocarTela(String fxml) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxml));
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirAcervo() {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/acervo.fxml"));

            Scene scene = new Scene(loader.load());

            // Carrega o arquivo CSS
            scene.getStylesheets().add(
                    getClass().getResource("/main/acervo.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirReservas(){
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/reservas.fxml"));

            Scene scene = new Scene(loader.load());

            // Carrega o arquivo CSS
            scene.getStylesheets().add(
                    getClass().getResource("/main/reservas.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirEmprestimo(){
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/emprestimo.fxml"));

            Scene scene = new Scene(loader.load());

            // Carrega o arquivo CSS
            scene.getStylesheets().add(
                    getClass().getResource("/main/emprestimo.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirDevolucao() {
        trocarTela("main/devolucao.fxml");
    }

    @FXML
    private void abrirRegistro() {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/devolucao.fxml"));

            Scene scene = new Scene(loader.load());

            // Carrega o arquivo CSS
            scene.getStylesheets().add(
                    getClass().getResource("/main/devolucao.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}