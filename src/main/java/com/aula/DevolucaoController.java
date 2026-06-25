package com.aula;

import com.aula.dao.AcervoDao;
import com.aula.dao.EmprestimoDao;
import com.aula.model.Acervo;
import com.aula.model.Emprestimo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DevolucaoController {

    @FXML
    private ListView<Emprestimo> listaEmprestados;

    private final EmprestimoDao emprestimoDao = new EmprestimoDao();
    private final AcervoDao acervoDao = new AcervoDao();

    @FXML
    public void initialize() {
        listaEmprestados.getItems().setAll(emprestimoDao.buscarTodos());
    }

    @FXML
    public void devolverLivro(ActionEvent actionEvent) {
        Emprestimo emprestimo = listaEmprestados.getSelectionModel().getSelectedItem();
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

        listaEmprestados.getItems().setAll(emprestimoDao.buscarTodos());
        mostrarAlerta("Devolucao realizada com sucesso!");
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Devolucao");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
