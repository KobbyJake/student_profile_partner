/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import view.ScreensController;
import java.awt.Dimension;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.animation.SequentialTransition;
import javafx.animation.SequentialTransitionBuilder;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Query;
import model.Student;
import org.controlsfx.control.Notifications;
import view.Alpha;

/**
 *
 * @author Jake
 */
public class IndexController implements Initializable, ControlledScreen {

    public Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    public double width = screenSize.getWidth(), height = screenSize.getHeight();
    ScreensController myController;
    @FXML
    private Text lblDescriptionHeader1;
    @FXML
    private Hyperlink hptSRC;
    @FXML
    private Hyperlink hptUEW;
    @FXML
    private Hyperlink htpOSIS;
    @FXML
    private Hyperlink htpLibrary;
    @FXML
    private ComboBox<String> cboResource;
    @FXML
    private Label lblCurrentUser;
    @FXML
    private ImageView logo2;
    @FXML
    private Hyperlink resetPassHyperlink;
    @FXML
    private Hyperlink htpInfo;

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        baseContainer.setPrefSize(width - (width * 0.5 / 100), height - (height * 4 / 100));
    }

    @FXML
    private Color x4;
    @FXML
    private Font x3;
    @FXML
    private Button btnLogin;
    @FXML
    private TextField txtUName;
    @FXML
    private PasswordField txtPword;

    @FXML
    private TextArea txaDescription;

    javax.persistence.EntityManager entitymanager;
    public static Student currentUser;
    @FXML
    private Pane mainMenu;
    @FXML
    private Text lblDescriptionHeader;
    @FXML
    private Hyperlink signOutHyperlink;
    @FXML
    private Label lblUserName;
    @FXML
    private Label lblPassword;
    @FXML
    private ImageView logo, logo1;
    @FXML
    private Hyperlink newUserHyperlink;
    @FXML
    private FlowPane newUserPane;

    @FXML
    private BorderPane baseContainer;
    public static boolean isSessionActive = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        entitymanager = DBConnection.getEntityManger();
        loadResourceCombo();
        animateSponsor();
//        session = false;   
    }

    private void animate(Node node) {
        try {
            if (currentUser.getAudio().equals("true")) {
                VoicePrompt.menuClip.play(1.0);
            }
        } catch (Exception e) {
        }
        node.requestFocus();
        ScaleTransition scaleTransition = ScaleTransitionBuilder.create()
                .node(node)
                .duration(Duration.seconds(0.5))
                .toX(1.2)
                .toY(1.2)
                .cycleCount(1)
                .autoReverse(false)
                .build();

        scaleTransition.play();

        ScaleTransition scaleTransition2 = ScaleTransitionBuilder.create()
                .node(((Button) node).getChildrenUnmodifiable().get(0))
                .duration(Duration.seconds(0.25))
                .toX(1.2)
                .toY(1.2)
                .cycleCount(2)
                .autoReverse(true)
                .build();

        scaleTransition2.play();

    }

    private void deAnimate(Node node) {
        ScaleTransition scaleTransition = ScaleTransitionBuilder.create()
                .node(node)
                .duration(Duration.seconds(0.75))
                .toX(1)
                .toY(1)
                .cycleCount(1)
                .autoReverse(false)
                .build();
        scaleTransition.play();
    }

    @FXML
    private void login(ActionEvent event) {
        logIn();
    }

    @FXML
    private void startMouseExitAnimation(MouseEvent event) {
        this.deAnimate((Node) event.getSource());
    }

    @FXML
    private void startMouseEntryAnimation(MouseEvent event) {
        this.animate((Node) event.getSource());
        this.setDescription((Button) event.getSource());
    }

    private void clearInput() {
        this.txtUName.setText("");
        this.txtPword.setText("");
        this.txtUName.requestFocus();
    }

    private boolean isValidUser(String userName, String password) {
        boolean status = false;
        if (userName.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("LOGIN");
            alert.setHeaderText("Wrong credentials");
            alert.setContentText("Please enter username and password");
            alert.initOwner(Alpha.stage);
            alert.show();
            this.txtUName.requestFocus();
        } else {
            try {
                Query query = entitymanager.createQuery("SELECT s FROM Student s WHERE s.userName = ?1 AND s.password = ?2");
                query.setParameter(1, userName);
                query.setParameter(2, new CryptMaster().encryptPass(password));
                if (!query.getResultList().isEmpty()) {
                    currentUser = (Student) query.getSingleResult();
                    Alpha.currentUser = currentUser;

                    ScaleTransition scaleTransition = ScaleTransitionBuilder.create()
                            .node(this.mainMenu)
                            .duration(Duration.seconds(0.75))
                            .toX(-0.5)
                            .toY(-0.75)
                            .cycleCount(2)
                            .autoReverse(true)
                            .build();
                    scaleTransition.play();

                    try {
                        if (currentUser.getAudio().equals("true")) {
                            VoicePrompt.welcomeClip.play(1.0);
                        }
                    } catch (Exception e) {
                    }
                    Notifications.create().position(Pos.BOTTOM_RIGHT).title("System login").text("Welcome, " + currentUser.getUserName()).showInformation();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("LOGIN");
                    alert.setHeaderText("Login Successful");
                    alert.setContentText("Welcome, " + currentUser.getUserName());
//                    alert.initOwner(Alpha.stage); alert.show();
                    this.lblCurrentUser.setText("Current User: " + currentUser.getUserName());
                    disableLogin();

                    status = true;
                    isSessionActive = true;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("LOGIN");
                    alert.setHeaderText("Wrong credentials");
                    alert.setContentText("User information not recorded yet, Please try again or create a new user profile");
                    alert.initOwner(Alpha.stage);
                    alert.show();
                }
            } catch (Exception e) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("LOGIN");
                alert.setHeaderText("Wrong credentials");
                alert.setContentText(e.getMessage());
                alert.initOwner(Alpha.stage);
                alert.show();

            }
            this.clearInput();

        }
        return status;

    }

    private void logIn() {
        this.isValidUser(this.txtUName.getText().trim(), this.txtPword.getText().trim());
    }

    @FXML
    private void scanKey(KeyEvent event) {
        if (event.getCode() == event.getCode().ENTER) {
            logIn();
        }
    }

    private void setDescription(Button button) {
        switch (button.getText()) {
            case "Courses":
                this.lblDescriptionHeader.setText(button.getText());
                this.txaDescription.setText("");
                this.txaDescription.setText("Register the courses for the semester. "
                        + "Associate the courses you are working on with the current semester.");
                break;

            case "Schedule":
                this.lblDescriptionHeader.setText(button.getText());
                this.txaDescription.setText("");
                this.txaDescription.setText("Manage routine activities in the form of time-table. "
                        + "Work on your event log for non-routine activities in the semester.");
                break;

            case "Academics":
                this.lblDescriptionHeader.setText(button.getText());
                this.txaDescription.setText("");
                this.txaDescription.setText("Manage your assignments, quizes and examination in the form of "
                        + "log entries to keep you going throughout the semester.");
                break;

            case "Grading":
                this.lblDescriptionHeader.setText(button.getText());
                this.txaDescription.setText("");
                this.txaDescription.setText("Specify grades for registered courses "
                        + "in a semester through an in-built G.P.A calculator. Set a grade target to motivate your work during the semester");
                break;

            case "Performance":
                this.lblDescriptionHeader.setText(button.getText());
                this.txaDescription.setText("");
                this.txaDescription.setText("Review your grades for each semester and evaluate your performance through the use"
                        + " of charts and graphs.");
                break;

            case "Money":
                this.lblDescriptionHeader.setText(button.getText());
                this.txaDescription.setText("");
                this.txaDescription.setText("Manage your inflows and expenditure. Plan and document your spendings for each semester.");
                break;

            case "Documents":
                this.lblDescriptionHeader.setText(button.getText());
                this.txaDescription.setText("");
                this.txaDescription.setText("Store vital documents and retrieve them as and when needed. Attach documents to "
                        + "dates and activities that make your semester.");
                break;

            case "Settings":
                this.lblDescriptionHeader.setText(button.getText());
                this.txaDescription.setText("");
                this.txaDescription.setText("Manage your personal information, system-wide profiles and preferences such as Current semester details and back-ups.");
                break;

            default:
                this.lblDescriptionHeader.setText("Welcome");
                this.txaDescription.setText("");
                this.txaDescription.setText("This system is an S.R.C. initiative designed to augment the online students information system.");
                break;
        }
    }

    private void disableLogin() {
        this.txtPword.setDisable(true);
        this.txtUName.setDisable(true);
        this.btnLogin.setDisable(true);
        this.lblUserName.setDisable(true);
        this.lblPassword.setDisable(true);
        this.newUserPane.setDisable(true);
        this.signOutHyperlink.setDisable(false);
        this.mainMenu.setDisable(false);
        this.lblCurrentUser.setVisible(true);

    }

    public void enableLogin() {
        this.txtPword.setDisable(false);
        this.txtUName.setDisable(false);
        this.btnLogin.setDisable(false);
        this.lblUserName.setDisable(false);
        this.lblPassword.setDisable(false);
        this.newUserPane.setDisable(false);
        this.signOutHyperlink.setDisable(true);
        this.mainMenu.setDisable(true);
        this.lblCurrentUser.setVisible(false);
        setDescription(this.btnLogin);
        Alpha.currentUser = null;
        currentUser = null;
    }

    @FXML
    private void doSignOut(ActionEvent event) {
         try {
            if (currentUser.getAudio().equals("true")) {
                VoicePrompt.standbyClip.play(1.0);
            }
        } catch (Exception e) {
        }
        enableLogin();
        isSessionActive = false;
    }

    private void animateSponsor() {
        TranslateTransition translateTransition = TranslateTransitionBuilder.create()
                .duration(Duration.seconds(15))
//                .node(this.logo)
                .fromX(1240)
                .toX(-5)
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();

        translateTransition.play();
          translateTransition =
                TranslateTransitionBuilder.create()
                .duration(Duration.seconds(15))
                .fromX(1240)
                .toX(-5)
                .cycleCount(2)
                .autoReverse(true)
                .build();    
        FadeTransition fadeTransition
                = FadeTransitionBuilder.create()
                .duration(Duration.seconds(10))
                .fromValue(1)
                .toValue(0)
                .cycleCount(2)
                .autoReverse(true)
                .build();

        ScaleTransition scaleTransition
                = ScaleTransitionBuilder.create()
                .duration(Duration.seconds(15))
                .toX(-1)
                .toY(1)
                .cycleCount(2)
                .autoReverse(true)
                .build();
        // create sequential transition to do 4 transitions one after another  
        SequentialTransition sequentialTransition, sequentialTransition1;
        sequentialTransition = SequentialTransitionBuilder.create()
                .node(this.logo)
                .children(fadeTransition, scaleTransition)
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();

        sequentialTransition1 = SequentialTransitionBuilder.create()
                .node(this.logo1)
                .children(fadeTransition, scaleTransition)
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();

//        sequentialTransition1.play();
        sequentialTransition.play();

    }

    @FXML
    private void startNewProfile(ActionEvent event) {
        playStarter();
        Alpha.startUser(this.baseContainer);
    }

    @FXML
    private void startSettings(ActionEvent event) {
        playStarter();
        Alpha.startSettings(this.baseContainer);
    }

    @FXML
    private void startCourses(ActionEvent event) {
        playStarter();
        Alpha.startCourses(this.baseContainer);
    }

    @FXML
    private void startSchedule(ActionEvent event) {
        playStarter();
        Alpha.startScheduler(this.baseContainer);
    }

    @FXML
    private void startDocuments(ActionEvent event) {
        playStarter();
        Alpha.startDocuments(this.baseContainer);
    }

    @FXML
    private void startWallet(ActionEvent event) {
        playStarter();
        Alpha.startWallet(this.baseContainer);
    }

    @FXML
    private void startGrading(ActionEvent event) {
        playStarter();
        Alpha.startGrading(this.baseContainer);
    }

    @FXML
    private void startPerformance(ActionEvent event) {
        playStarter();
        Alpha.startPerformance(this.baseContainer);

    }

    @FXML
    private void startAcademics(ActionEvent event) {
        playStarter();
        Alpha.startAcademics(this.baseContainer);
    }

    private void playStarter() {
        try {
            if (currentUser.getAudio().equals("true")) {
                VoicePrompt.actionClip.play(1.0);
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void refreshPage(MouseEvent event) {
        if (isSessionActive) {
            this.disableLogin();
        } else {
            this.enableLogin();
            CoursesController.refresh = true;
            ScheduleController.refresh = true;
            AcademicsController.refresh = true;
            GradingController.refresh = true;
            PerformanceController.refresh = true;
            WalletController.refresh = true;
            DocumentsController.refresh = true;
//            Alpha.refreshPages();
        }
    }

    @FXML
    private void startSCRPage(ActionEvent event) {
        Alpha.startSRC(this.baseContainer);
    }

    @FXML
    private void startUEWLink(ActionEvent event) {
        this.openLink("U.E.W Website");
    }

    @FXML
    private void startOSISLink(ActionEvent event) {
        this.openLink("Students' Information Portal");
    }

    @FXML
    private void startLibraryLink(ActionEvent event) {
        this.openLink("Online Library");

    }

    @FXML
    private void openResource(ActionEvent event) {
    }

    private void openLink(String key) {
        try {
            if (key.equals("Students' Information Portal")) {

                String url = "https://sip.uew.edu.gh/login/login.php";
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));

            } else if (key.equals("Online Library")) {
                String url = "https://www.uew.edu.gh/library";
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));

            } else if (key.equals("U.E.W Website")) {

                String url = "https://www.uew.edu.gh";
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));

            }
//    else if (cboResource.getSelectedItem().toString().equals("S.R.C Website")){
//         try{
//            
//           JOptionPane.showMessageDialog(null,"SRC web site goes here");
//        }catch(Exception e){
//                JOptionPane.showMessageDialog(null,e);
//        }
//    }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Links");
            alert.setHeaderText("Error accessing links");
            alert.setContentText(e.getMessage());
            alert.initOwner(Alpha.stage);
            alert.show();
        }
    }

    private void loadResourceCombo() {
        this.cboResource.getItems().add("S.R.C Constitution");
        this.cboResource.getItems().add("Students' Hand-book");
    }

    private model.System getMailReference() {
        try {
            model.System thisSystemItem = this.entitymanager.createNamedQuery("System.findByName",
                    model.System.class)
                    .setParameter("name", "email")
                    .getSingleResult();
            return thisSystemItem;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Students Profile Partner.");
            alert.setHeaderText("Email Reference");
            alert.setContentText(e.getMessage());
            alert.initOwner(Alpha.stage);
            alert.show();
//            Dialogs.create().lightweight().title("Current Event Reference").message(e.getMessage()).showError();
            return null;
        }

    }

    private model.System getMailAuth() {
        try {
            model.System thisSystemItem = this.entitymanager.createNamedQuery("System.findByName",
                    model.System.class)
                    .setParameter("name", "mailAuth")
                    .getSingleResult();
            return thisSystemItem;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Students Profile Partner.");
            alert.setHeaderText("Email Reference");
            alert.setContentText(e.getMessage());
            alert.initOwner(Alpha.stage);
            alert.show();
//            Dialogs.create().lightweight().title("Current Event Reference").message(e.getMessage()).showError();
            return null;
        }

    }

    private void sendMail(Student thisStudent) {
        String newPass = "";
        String content = "";
        try {
            newPass = CodeMaker.generateCode(16, CodeMaker.CodeType.ALPHANUMERIC);
            content = "Hi " + thisStudent.getUserName() + ", your new password is " + newPass;
        } catch (Exception e) {
        }

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        Session session = null;
        try {

            this.mailSender = new CryptMaster().decryptData(getMailReference().getId());
            this.mailAuth = new CryptMaster().decryptData(getMailAuth().getId());
            session = Session.getDefaultInstance(properties, new MailMessenger(this.mailSender, this.mailAuth));
        } catch (Exception ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Password Reset");
        alert.setContentText("The system is about to reset your password.\nPlease ensure active internet connection and press OK to continue.");
         try {
               
                    VoicePrompt.internetClip.play(1.0);
                
            } catch (Exception e) {
            }
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.mailSender));
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(thisStudent.getEmail()));
            message.setSubject("Students' Profile Partner Password Reset");
            //text only content 
            message.setContent(content, "text/plain");

            Transport.send(message);
            this.entitymanager.getTransaction().begin();
            thisStudent.setPassword(new CryptMaster().encryptPass(newPass));
            this.entitymanager.merge(thisStudent);
            this.entitymanager.getTransaction().commit();
            Notifications.create().position(Pos.CENTER).title("Password Reset").text("Message sent").showInformation();
            try {
                if (currentUser.getAudio().equals("true")) {
                    VoicePrompt.passwordClip.play(1.0);
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Students' Profile Partner.");
            alert.setHeaderText("Password mailing error");
            alert.setContentText(e.getMessage());
            alert.initOwner(Alpha.stage);
            alert.show();
        }
    }

    @FXML
    private void resetPassword(ActionEvent event) {

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("TestName");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextField userName = new TextField();
        userName.setPromptText("Username");
        TextField email = new TextField();
        email.setPromptText("Email");

        gridPane.add(new Label("User name:"), 0, 0);
        gridPane.add(userName, 1, 0);
        gridPane.add(new Label("Email:"), 0, 1);
        gridPane.add(email, 1, 1);

        dialog.getDialogPane().setContent(gridPane);
        dialog.initOwner(Alpha.stage);

        // Request focus on the username field by default.
        Platform.runLater(() -> userName.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(userName.getText(), email.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            Query query = entitymanager.createQuery("SELECT s FROM Student s WHERE s.userName = ?1 AND s.email = ?2");
            query.setParameter(1, pair.getKey());
            query.setParameter(2, pair.getValue());
            if (!query.getResultList().isEmpty()) {
                Student thisStudent = (Student) query.getSingleResult();
                this.sendMail(thisStudent);
            } else {
                Notifications.create().darkStyle().position(Pos.CENTER).title("Password Reset").text("No such record exits in the system. Please use existing useraname and email.").showError();
            }
        });

    }
    String mailSender;
    String mailAuth;
    String userMail;

    @FXML
    private void startInfo(ActionEvent event) {
        Alpha.startInfo();
    }
}
