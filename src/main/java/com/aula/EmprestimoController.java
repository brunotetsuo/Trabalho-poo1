package com.aula;

import com.aula.model.BibliotecaDados;
import com.aula.model.Emprestimo;
import com.aula.model.Livro;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.time.temporal.ChronoUnit;

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
    private ListView<Livro> listaLivros;

    @FXML
    public void initialize() {
        Livro livroSelecionado = BibliotecaDados.getLivroSelecionado();
        String nomeUsuarioLogado = BibliotecaDados.getNomeUsuarioLogado();

        if (!nomeUsuarioLogado.isEmpty()) {
            usuarioField.setText(nomeUsuarioLogado);
        }

        if (livroSelecionado != null) {
            livroField.setText(livroSelecionado.getTitulo());
            listaLivros.getItems().setAll(livroSelecionado);
            listaLivros.getSelectionModel().select(livroSelecionado);
        } else {
            listaLivros.getItems().setAll(BibliotecaDados.listarLivros());
        }

        listaLivros.getSelectionModel().selectedItemProperty().addListener((observable, livroAntigo, livro) -> {
            if (livro != null) {
                livroField.setText(livro.getTitulo());
            }
        });
    }

    @FXML
    public void handleEmprestimo(ActionEvent actionEvent) {
        Livro livro = listaLivros.getSelectionModel().getSelectedItem();

        if (livro == null) {
            mostrarAlerta("Selecione um livro em Livros Encontrados.");
            return;
        }

        if (!livro.isDisponivel()) {
            mostrarAlerta("Este livro ja esta indisponivel.");
            return;
        }

        if (usuarioField.getText() == null || usuarioField.getText().trim().isEmpty()) {
            mostrarAlerta("Informe o nome do usuario.");
            return;
        }

        if (dataEmprestimo.getValue() == null || dataDevolucao.getValue() == null) {
            mostrarAlerta("Informe a data de emprestimo e a data de devolucao.");
            return;
        }

        long diasEmprestimo = ChronoUnit.DAYS.between(dataEmprestimo.getValue(), dataDevolucao.getValue());
        String tipoMembro = BibliotecaDados.getTipoMembroUsuarioLogado();
        int limiteDias = "ESPECIAL".equalsIgnoreCase(tipoMembro) ? 30 : 15;

        if (diasEmprestimo < 1) {
            mostrarAlerta("A data de devolucao deve ser depois da data de emprestimo.");
            return;
        }

        if (diasEmprestimo > limiteDias) {
            mostrarAlerta("Membro " + tipoMembro + " pode pegar livro por no maximo " + limiteDias + " dias.");
            return;
        }

        Emprestimo emprestimo = new Emprestimo(
                livro,
                usuarioField.getText().trim(),
                tipoMembro,
                dataEmprestimo.getValue(),
                dataDevolucao.getValue()
        );

        BibliotecaDados.registrarEmprestimo(emprestimo);
        listaLivros.refresh();
        mostrarAlerta("Emprestimo registrado. O livro ficou indisponivel.");
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

    @FXML
    public void buscarLivro(KeyEvent keyEvent) {
        listaLivros.getItems().setAll(BibliotecaDados.buscarLivros(livroField.getText()));
    }

    private void limparCampos() {
        BibliotecaDados.setLivroSelecionado(null);
        livroField.clear();
        usuarioField.clear();
        dataEmprestimo.setValue(null);
        dataDevolucao.setValue(null);
        listaLivros.getSelectionModel().clearSelection();
        listaLivros.getItems().setAll(BibliotecaDados.listarLivros());
        livroField.requestFocus();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Emprestimo");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
