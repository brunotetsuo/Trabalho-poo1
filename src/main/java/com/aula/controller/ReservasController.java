package com.aula.controller;

import com.aula.model.Membro;
import com.aula.model.Reserva;
import com.aula.service.ReservaService;
import com.aula.util.Sessao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ReservasController {

    @FXML
    private Label statusMembroLabel;

    @FXML
    private ListView<Reserva> listaReservados;

    private final ReservaService reservaService = new ReservaService();

    @FXML
    public void initialize() {
        Membro membro = Sessao.getMembroLogado();
        if (membro != null) {
            statusMembroLabel.setText("Status: " + ("E".equals(membro.getTipoMembro()) ? "Especial" : "Comum"));
        }
        atualizarLista();
    }

    @FXML
    public void cancelarReserva(ActionEvent actionEvent) {
        Reserva reserva = listaReservados.getSelectionModel().getSelectedItem();
        if (reserva == null) {
            mostrarAlerta("Selecione uma reserva para cancelar.");
            return;
        }

        try {
            reservaService.cancelarReserva(reserva);
            atualizarLista();
            mostrarAlerta("Reserva cancelada com sucesso!");
        } catch (IllegalStateException e) {
            mostrarAlerta(e.getMessage());
        }
    }

    private void atualizarLista() {
        Membro membro = Sessao.getMembroLogado();
        if (membro != null) {
            listaReservados.getItems().setAll(reservaService.buscarAtivosPorMembro(membro.getId()));
        }
    }

    @FXML
    private void irParaMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
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
        alert.setTitle("Reservas");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
