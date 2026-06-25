package com.aula;

import com.aula.dao.AcervoDao;
import com.aula.dao.EmprestimoDao;
import com.aula.model.Acervo;
import com.aula.model.Emprestimo;
import com.aula.model.Membro;
import com.aula.util.Sessao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.temporal.ChronoUnit;
import java.util.Date;

public class EmprestimoController {

    @FXML
    private TextField livroField;

    @FXML
    private TextField usuarioField;

    @FXML
    private DatePicker dataEmprestimo;

    @FXML
    private DatePicker dataDevolucao;

    @FXML
    private ListView<Acervo> listaAcervos;

    private final AcervoDao acervoDao = new AcervoDao();
    private final EmprestimoDao emprestimoDao = new EmprestimoDao();

    @FXML
    public void initialize() {
        Membro membroLogado = Sessao.getMembroLogado();
        if (membroLogado != null) {
            usuarioField.setText(membroLogado.getNomeCompleto());
            usuarioField.setEditable(false);
        }
        listaAcervos.getItems().setAll(acervoDao.buscarTodos());

        listaAcervos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                livroField.setText(newVal.getTitulo());
            }
        });
    }

    @FXML
    public void handleEmprestimo(ActionEvent actionEvent) {
        Acervo acervo = listaAcervos.getSelectionModel().getSelectedItem();
        if (acervo == null) {
            mostrarAlerta("Selecione um item no acervo.");
            return;
        }

        if (acervo.getStatusEmprestimo() != null && acervo.getStatusEmprestimo() == 1) {
            mostrarAlerta("Este item ja esta emprestado.");
            return;
        }

        Membro membro = Sessao.getMembroLogado();
        if (membro == null) {
            mostrarAlerta("Nenhum membro logado.");
            return;
        }

        if (membro.isPunido()) {
            mostrarAlerta("Membro esta punido e nao pode realizar emprestimos.");
            return;
        }

        long emprestimosAtivos = emprestimoDao.contarAtivosPorMembro(membro.getId());
        if (emprestimosAtivos >= membro.getLimiteEmprestimos()) {
            mostrarAlerta("Limite de emprestimos atingido (" + membro.getLimiteEmprestimos() + ").");
            return;
        }

        if (dataEmprestimo.getValue() == null || dataDevolucao.getValue() == null) {
            mostrarAlerta("Informe as datas de emprestimo e devolucao.");
            return;
        }

        long dias = ChronoUnit.DAYS.between(dataEmprestimo.getValue(), dataDevolucao.getValue());
        if (dias < 1) {
            mostrarAlerta("A data de devolucao deve ser posterior a data de emprestimo.");
            return;
        }

        int limiteDias = "E".equals(membro.getTipoMembro()) ? 30 : 15;
        if (dias > limiteDias) {
            mostrarAlerta("Membro " + membro.getTipoMembro() + " pode pegar por no maximo " + limiteDias + " dias.");
            return;
        }

        Emprestimo emp = new Emprestimo();
        emp.setUsuario(membro);
        emp.setItemEmprestado(acervo);
        emp.setDataExpiracao(Date.from(dataDevolucao.getValue().atStartOfDay().toInstant(java.time.ZoneOffset.UTC)));
        emp.registrar();

        acervo.setStatusEmprestimo(1);

        emprestimoDao.salvar(emp);
        acervoDao.salvar(acervo);

        mostrarAlerta("Emprestimo registrado com sucesso!");
        limparCampos();
    }

    @FXML
    public void irParaDevolucao(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/devolucao.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/main/devolucao.css").toExternalForm());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Nao foi possivel abrir a tela de devolucao.");
        }
    }

    private void limparCampos() {
        livroField.clear();
        dataEmprestimo.setValue(null);
        dataDevolucao.setValue(null);
        listaAcervos.getSelectionModel().clearSelection();
        listaAcervos.getItems().setAll(acervoDao.buscarTodos());
        livroField.requestFocus();
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Emprestimo");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
