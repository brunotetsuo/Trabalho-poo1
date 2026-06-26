    package com.aula;

    import javafx.application.Application;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Scene;
    import javafx.stage.Stage;
    import com.aula.util.JPAUtil;

    public class Main extends Application {
        public static void main(String[] args) {
            launch();
        }
        @Override
        public void start(Stage stage) throws Exception {
            FXMLLoader fxmlLoader = new
                    FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
            scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());
            stage.setTitle("App Login");
            stage.setScene(scene);
            stage.setWidth(1000);
            stage.setHeight(800);
            stage.setMinWidth(550);
            stage.setMinHeight(650);
            stage.setMaxWidth(1000);
            stage.setMaxHeight(800);
            stage.show();
        }

        @Override
        public void stop() {
            JPAUtil.close();
        }
    }
