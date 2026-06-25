package com.aula;

import com.aula.dao.AcervoDao;
import com.aula.dao.EmprestimoDao;
import com.aula.model.Acervo;
import com.aula.model.Emprestimo;
import com.aula.model.Membro;
import com.aula.util.Sessao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ReservasController {

    @FXML
    private Label statusMembroLabel;

    @FXML
    private ListView<Emprestimo> listaReservados;

    private final EmprestimoDao emprestimoDao = new EmprestimoDao();
    private final AcervoDao acervoDao = new AcervoDao();

    @FXML
    public void initialize() {
        Membro membro = Sessao.getMembroLogado();
        if (membro != null) {
            statusMembroLabel.setText("Status: " + ("E".equals(membro.getTipoMembro()) ? "Especial" : "Comum"));
        }
        atualizarLista();
    }

    @FXML
    public void devolverLivro(ActionEvent actionEvent) {
        Emprestimo emprestimo = listaReservados.getSelectionModel().getSelectedItem();
        if (emprestimo == null) {
            mostrarAlerta("Selecione um emprestimo para devolver.");
            return;
        }

        emprestimo.setStatusAtivo(0);
        emprestimoDao.salvar(emprestimo);

        Acervo acervo = emprestimo.getItemEmprestado();
        if (acervo != null) {
            acervo.setStatusEmprestimo(0);
            acervoDao.salvar(acervo);
        }

        atualizarLista();
        mostrarAlerta("Devolucao realizada com sucesso!");
    }

    private void atualizarLista() {
        Membro membro = Sessao.getMembroLogado();
        if (membro != null) {
            listaReservados.getItems().setAll(emprestimoDao.buscarAtivosPorMembro(membro.getId()));
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
