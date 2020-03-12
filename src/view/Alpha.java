/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.DBConnection;
import controller.IndexController;
import controller.VoicePrompt;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Optional;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.persistence.Query;
import model.Document;
import model.Student;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Jake
 */
public class Alpha extends Application {

    static ScreensController mainContainer = new ScreensController();
    public static SimpleDateFormat sfm = new SimpleDateFormat("M/d/yyyy");
    public Dimension screenSize;
    public static Student currentUser;
    public static String index = "Login";
    public static String indexFile = "Index.fxml";

    public static String user = "user";
    public static String userFile = "User.fxml";

    public static String settings = "settings";
    public static String settingsFile = "Settings.fxml";

    public static String courses = "courses";
    public static String coursesFile = "Courses.fxml";

    public static String scheduler = "scheduler";
    public static String schedulerFile = "Scheduler.fxml";

    public static String academics = "academics";
    public static String academicsFile = "Academics.fxml";

    public static String wallet = "wallet";
    public static String walletFile = "Wallet.fxml";

    public static String documents = "documents";
    public static String documentsFile = "Documents.fxml";

    public static String grading = "grading";
    public static String gradingFile = "Grading.fxml";

    public static String performance = "performance";
    public static String performanceFile = "Performance.fxml";

    public static String src = "src";
    public static String srcFile = "SRC.fxml";

    javax.persistence.EntityManager entitymanager = DBConnection.getEntityManger();
    public static Stage stage;
    private static File infofile = null;

    @Override
    public void start(Stage stage) throws Exception {

        screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        retrieveSystemFiles();
        stage.setHeight(screenSize.height);
        stage.setWidth(screenSize.width);
        stage.setTitle("Students' Profile Partner");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.getIcons().clear();

        stage.getIcons().add(new Image(new File("lib/logo.png").toURI().toString()));
        mainContainer.loadScreen(Alpha.index, Alpha.indexFile);
        mainContainer.loadScreen(Alpha.user, Alpha.userFile);
        mainContainer.loadScreen(Alpha.settings, Alpha.settingsFile);
        mainContainer.loadScreen(Alpha.courses, Alpha.coursesFile);
        mainContainer.loadScreen(Alpha.scheduler, Alpha.schedulerFile);
        mainContainer.loadScreen(Alpha.academics, Alpha.academicsFile);
        mainContainer.loadScreen(Alpha.wallet, Alpha.walletFile);
        mainContainer.loadScreen(Alpha.documents, Alpha.documentsFile);
        mainContainer.loadScreen(Alpha.grading, Alpha.gradingFile);
        mainContainer.loadScreen(Alpha.performance, Alpha.performanceFile);
        mainContainer.loadScreen(Alpha.src, Alpha.srcFile);

        mainContainer.setScreen(Alpha.index);

        Group root = new Group();
        root.autosize();
        root.setAutoSizeChildren(true);
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root, screenSize.width, screenSize.height);
        stage.setScene(scene);
        Alpha.stage = stage;
        VoicePrompt vp = new VoicePrompt();
        stage.setOnCloseRequest((source) -> {

//            Dialogs.create().title("Students' Profile Partner").masthead("Students Profile Partner. \nVersion 1.0.1 September 2015.\nCopyright © JCyberSolutions(JCS) 2015.").message("jcybersolutions@gmail.com  Tel. 0242525727.").graphic(new ImageView(stage.getIcons().get(0))).owner( stage.isShowing() ? stage : null).showInformation();
            try {
                if (currentUser.getAudio().equals("true")) {
                    VoicePrompt.goodbyeClip.play(1.0);
                }
            } catch (Exception e) {
                VoicePrompt.goodbyeClip.play(1.0);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("JCyber Solutions");
            alert.setHeaderText("Students' Profile Partner. \nVersion 1.5.0 May 2015.\nCopyright © JCyberSolutions(JCS) 2016.");
            alert.setContentText("jcybersolutions@gmail.com  Tel. 0242525727.");
            alert.setGraphic(new ImageView(stage.getIcons().get(0)));
            alert.initOwner(Alpha.stage);

            FadeTransition fadeTransition = FadeTransitionBuilder.create()
                    .duration(Duration.seconds(2))
                    .node(alert.getDialogPane())
                    .fromValue(0)
                    .toValue(1)
                    .cycleCount(1)
                    .autoReverse(false)
                    .build();
            fadeTransition.play();
            alert.showAndWait();
        });
        stage.show();
    }

    /**
     * this is a method to switch the current screen
     *
     * @param node
     */
    private static void animateScreenLoader(Node node) {
        ScaleTransition scaleTransition1 = ScaleTransitionBuilder.create()
                .node(node)
                .duration(Duration.seconds(3))
                .toX(-5)
                .toY(-5)
                .cycleCount(2)
                .autoReverse(true)
                .build();
        scaleTransition1.play();
    }

    public static void startUser(Node node) {
        playStarter();
        animateScreenLoader(node);
        mainContainer.setScreen(Alpha.user);
    }

    public static void startIndex(Node node) {
        playStarter();
        animateScreenLoader(node);
        mainContainer.setScreen(Alpha.index);
    }

    public static void startSettings(Node node) {
        playStarter();
        animateScreenLoader(node);
        mainContainer.setScreen(Alpha.settings);
    }

    public static void startCourses(Node node) {
        playStarter();
        animateScreenLoader(node);
        mainContainer.setScreen(Alpha.courses);
    }

    public static void startScheduler(Node node) {
        playStarter();
        animateScreenLoader(node);
        mainContainer.setScreen(Alpha.scheduler);
    }

    public static void startAcademics(Node node) {
        playStarter();
        animateScreenLoader(node);
        mainContainer.setScreen(Alpha.academics);
    }

    public static void startWallet(Node node) {
        playStarter();
        animateScreenLoader(node);
        mainContainer.setScreen(Alpha.wallet);
    }

    public static void startSRC(Node node) {
        playStarter();
        animateScreenLoader(node);
        mainContainer.setScreen(Alpha.src);
    }

    public static void startDocuments(Node node) {
        playStarter();
        animateScreenLoader(node);
        mainContainer.setScreen(Alpha.documents);
    }

    public static void startGrading(Node node) {
        playStarter();
        animateScreenLoader(node);
        mainContainer.setScreen(Alpha.grading);
    }

    public static void startPerformance(Node node) {
        playStarter();
        animateScreenLoader(node);
        mainContainer.setScreen(Alpha.performance);
    }
    public static void startInfo(){
        try {
             Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+infofile.getAbsolutePath());
        } catch (Exception e) {
            Notifications.create().title("Information File Error").text(e.getLocalizedMessage()).showError();
        }
    }
 private static void playStarter() {
        try {
            if (currentUser.getAudio().equals("true")) {
                VoicePrompt.actionClip.play(1.0);
            }
        } catch (Exception e) {
        }
    }
    public static void startAbout(Stage stage) {
        playStarter();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("JCyber Solutions");
        alert.setHeaderText("Students' Profile Partner. \nVersion 1.5.0 May 2016.\nCopyright © JCyberSolutions(JCS) 2016.");
        alert.setContentText("jcybersolutions@gmail.com  Tel. 0242525727.");
        alert.setGraphic(new ImageView(stage.getIcons().get(0)));
        alert.initOwner(stage);

        FadeTransition fadeTransition = FadeTransitionBuilder.create()
                .duration(Duration.seconds(4))
                .node(alert.getDialogPane())
                .fromValue(0)
                .toValue(1)
                .cycleCount(1)
                .autoReverse(false)
                .build();
        fadeTransition.play();
        alert.initOwner(Alpha.stage);
        alert.show();
//       Dialogs.create().title("Students Profile Partner").masthead("Students Profile Partner. \nVersion 1.0.1 September 2015.\nCopyright © JCyberSolutions(JCS) 2015.").message("JCyberSolutions@gmail.com  Tel. 0242525727.").graphic(new ImageView(stage.getIcons().get(0))).owner(stage).showInformation();
    }

    public static void closeApp(Node node) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirm application closure");
        alert.setContentText("You are about to close this application");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            ((Stage) node.getScene().getWindow()).close();
        }
    }

    public static void logout(Node node) {
         try {
            if (currentUser.getAudio().equals("true")) {
                VoicePrompt.standbyClip.play(1.0);
            }
        } catch (Exception e) {
        }
        IndexController.isSessionActive = false;
        Alpha.currentUser = null;
        animateScreenLoader(node);
        mainContainer.setScreen(Alpha.index);
    }

    public void getBanner(String name) {
        try {
            Query query = this.entitymanager.createQuery("SELECT d FROM Document d WHERE d.reference LIKE 'System' AND d.title LIKE " + "'" + name + "'");
            Document currentDoc = (Document) query.getSingleResult();
            File thisFile = new File("lib/banner.png");
            thisFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream("lib/banner.png");
            fos.write(currentDoc.getFile());
            fos.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error retrieving report banner");
            alert.setContentText(e.getLocalizedMessage());
            alert.initOwner(Alpha.stage);
            alert.show();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void retrieveSystemFiles() {
        try {

            Query query = this.entitymanager.createQuery("SELECT d FROM Document d WHERE d.title LIKE 'logo' AND d.reference LIKE 'System'");
            if (query.getResultList().size() > 0) {
                Document doc = (Document) query.getSingleResult();
                File file = new File("lib/logo.png");
                file.deleteOnExit();
                FileOutputStream fos = new FileOutputStream("lib/logo.png");
                fos.write(doc.getFile());
                fos.close();
            }
            query = this.entitymanager.createQuery("SELECT d FROM Document d WHERE d.title LIKE 'infofile'");
            if (query.getResultList().size() > 0) {
                Document doc = (Document) query.getSingleResult();
                infofile = new File("lib/infofile.pdf");
                infofile.deleteOnExit();
                FileOutputStream fos = new FileOutputStream("lib/infofile.pdf");
                fos.write(doc.getFile());
                fos.close();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error retrieving logo");
            alert.setContentText(e.getLocalizedMessage());
            alert.initOwner(Alpha.stage);
            alert.show();
        }
    }

}
