/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Location;
import model.Programme;
import model.Schedule;
import model.Semester;
import model.Student;
import org.controlsfx.control.Notifications;
import view.Alpha;

import view.ScreensController;

/**
 * FXML Controller class
 *
 * @author Jake
 */
public class SettingsController implements Initializable, ControlledScreen {

    public Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    public double width = screenSize.getWidth(), height = screenSize.getHeight();
    ScreensController myController;
    @FXML
    private Color x4;
    @FXML
    private Font x3;
    @FXML
    private AnchorPane imageAnchor;
    @FXML
    private TextField txtUsername;
    @FXML
    private Button btnupdateUser;
    @FXML
    private Button btnreadUserData;
    @FXML
    private Button btnclearUserInput;
    @FXML
    private Button btndeleteUser;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtPassword2;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtIndexNumber;
    @FXML
    private TextField txtTelephoneNumber;
    @FXML
    private TextField txtEmailAddress;
    @FXML
    private ComboBox<String> cboHall;
    @FXML
    private ComboBox<String> cboProgram;
    @FXML
    private TextArea txaNote1;
    @FXML
    private MenuItem logoutMenuItem;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private MenuItem coursesMenuItem;
    @FXML
    private MenuItem schedulerMenuItem;
    @FXML
    private MenuItem academicsMenuItem;
    @FXML
    private MenuItem gradingMenuItem;
    @FXML
    private MenuItem performanceMenuItem;
    @FXML
    private MenuItem documentsMenuItem;
    @FXML
    private MenuItem settingsMenuItem;
    @FXML
    private ImageView imgUser;
    private boolean isImageSet;
    private byte[] fileInByte;
    private Student currentUser;
    private EntityManager entitymanager;
    Map<String, ImageView> images = new HashMap<>();
    private static String[] day = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    Semester currentSemester;
    @FXML
    private ComboBox<String> cboCurrentSemester;
    @FXML
    private Button btnaddNewUser1;
    private boolean updater;
    @FXML
    private AnchorPane imageAnchor1;
    @FXML
    private ImageView imgUser1;
    @FXML
    private DatePicker cboSemesterStartDate;
    @FXML
    private DatePicker cboSemesterEndDate;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnOutputFolder;
    @FXML
    private Button btnBackupDatabase;
    @FXML
    private Button btnRestoreDatabase;
    @FXML
    private TextField txtOutputFolder;
    @FXML
    private TextField txtLastbackup;
    @FXML
    private TextField txtRestoreDatabase;
    @FXML
    private MenuItem walletMenuItem;
    static SimpleDateFormat sdf = new SimpleDateFormat("EEEE MMM dd, yyyy  @ hh:mm:ss a");
    private String zipName;
    @FXML
    private TextArea txaDescription;
    @FXML
    private TitledPane personalInstructionPane;
    @FXML
    private TitledPane GeneralInstructionPane;
    @FXML
    private TextArea txaDescription1;
    @FXML
    private Hyperlink photoLoader;
    @FXML
    private Accordion passwordContainer;
    @FXML
    private TitledPane GeneralInstructionPane1;
    @FXML
    private PasswordField txtPassword3;
    @FXML
    private RadioButton rdtOn;
    @FXML
    private ToggleGroup sound;
    @FXML
    private RadioButton rdtOff;

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        baseContainer.setPrefSize(width - (width * 0.5 / 100), height - (height * 4 / 100));
    }
    @FXML
    private BorderPane baseContainer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        entitymanager = DBConnection.getEntityManger();
        this.images = new HashMap<>();
        loadSemesterCombo();
        this.cboCurrentSemester.setOnAction((action) -> {

        });
    }

    @FXML
    private void addNewUser(ActionEvent event) {
        if (validateStringLength(this.txtPassword2.getText().trim()) && confirmPassword(this.txtPassword2.getText().trim(), this.txtPassword3.getText().trim())) {
            currentUser = new Student();
            try {
                this.entitymanager.getTransaction().begin();
                this.entitymanager.persist(this.setUser(currentUser));
                this.entitymanager.getTransaction().commit();
                this.clearUserInput();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "User account created");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.getDialogPane().setContentText("A new user account has been created successfully");
                alert.getDialogPane().setHeaderText("creating user account");
                alert.initOwner(Alpha.stage);
                alert.showAndWait();

                Alpha.startIndex(this.baseContainer);

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "User account Error");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.getDialogPane().setContentText(e.getMessage());
                alert.getDialogPane().setHeaderText("Error creating user account");
                alert.showAndWait();
                this.entitymanager.getTransaction().rollback();
            }

        }
    }

    @FXML
    private void updateUser(ActionEvent event) {
        try {
            if (this.txtPassword.getText().isEmpty()) {

            } else if ((validateStringLength(this.txtPassword2.getText().trim()) && confirmPassword(this.txtPassword2.getText().trim(), this.txtPassword3.getText().trim()))) {
                if (!this.txtPassword.getText().trim().equals(currentUser.getPassword())) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Confirm password update");
                    alert.setContentText("You are about to update the existing password");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (!(result.get() == ButtonType.OK)) {
                        return;
                    }
                }

            }
//		else {
//                return;
//            }
            try {
                this.entitymanager.getTransaction().begin();
                this.entitymanager.merge(this.setUser(currentUser));
                this.entitymanager.getTransaction().commit();
                this.clearUserInput();
                Alpha.currentUser = currentUser;
                this.getCurrentUser();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "User Account Update");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.getDialogPane().setContentText("The current user account has been updated successfully");
                alert.getDialogPane().setHeaderText("creating user account");
                alert.showAndWait();

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "User account Error");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.getDialogPane().setContentText(e.getMessage());
                alert.getDialogPane().setHeaderText("Error updating user account");
                alert.showAndWait();
                this.entitymanager.getTransaction().rollback();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "User account Error");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText("Please supply user inputs");
            alert.getDialogPane().setHeaderText("Error verifying user input");
            alert.showAndWait();
            return;
        }

    }

    @FXML
    private void readUserData(ActionEvent event) {
        try {
            getCurrentUser();
        } catch (Exception e) {

        }
    }

    @FXML
    private void clearUserInput(ActionEvent event) {
        this.clearUserInput();
    }

    @FXML
    private void deleteUser(ActionEvent event) {
        try {
            if (currentUser.getId().length() > 0);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("No user selected");
            alert.setContentText("Please select the user to delete");
            alert.showAndWait();
            return;
        }
//        

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirm deleting user");
        alert.setContentText("You are about to delete the current user account");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                entitymanager.getTransaction().begin();
                entitymanager.remove(currentUser);
                entitymanager.getTransaction().commit();
            } catch (Exception e) {
                this.entitymanager.getTransaction().rollback();
            }

        }
    }

    @FXML
    private void checkDigit(KeyEvent event) {
        validateDigitInput(event);
    }

    @FXML
    private void loadIndexPage(ActionEvent event) {
        Alpha.startIndex(this.baseContainer);
    }

    @FXML
    private void doLogout(ActionEvent event) {
        Alpha.logout(this.baseContainer);
    }

    @FXML
    private void doExit(ActionEvent event) {
        Alpha.closeApp(this.baseContainer);
    }

    @FXML
    private void startCourses(ActionEvent event) {
        Alpha.startCourses(this.baseContainer);
    }

    @FXML
    private void startScheduler(ActionEvent event) {
        Alpha.startScheduler(this.baseContainer);
    }

    @FXML
    private void startAcademics(ActionEvent event) {
        Alpha.startAcademics(this.baseContainer);
    }

    @FXML
    private void startGrading(ActionEvent event) {
        Alpha.startGrading(this.baseContainer);
    }

    @FXML
    private void startPerformance(ActionEvent event) {
        Alpha.startPerformance(this.baseContainer);
    }

    @FXML
    private void startWallet(ActionEvent event) {
        Alpha.startWallet(this.baseContainer);
    }

    @FXML
    private void startDocuments(ActionEvent event) {
        Alpha.startDocuments(this.baseContainer);
    }

    @FXML
    private void startSettings(ActionEvent event) {
        Alpha.startSettings(this.baseContainer);
    }

    private void clearUserInput() {
        this.txtUsername.setText("");
        this.txtPassword.setText("");
        this.txtPassword2.setText("");
        this.txtPassword3.setText("");
        this.txtFirstName.setText("");
        this.txtLastName.setText("");
        this.txtIndexNumber.setText("");
        this.txtTelephoneNumber.setText("");
        this.txtEmailAddress.setText("");
        this.txaNote1.setText("");
        this.cboHall.getSelectionModel().select(-1);
        this.cboProgram.getSelectionModel().select(-1);
        this.imageAnchor.getChildren().clear();
//        this.imageAnchor.getChildren().add(this.images.get("default"));
        this.txtUsername.requestFocus();
    }

    private void validateDigitInput(KeyEvent event) {
        if (!event.getCode().isDigitKey() && !event.getCode().isArrowKey() && event.getCode() != event.getCode().BACK_SPACE && event.getCode() != event.getCode().DELETE && event.getCode() != event.getCode().TAB) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Input Error");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText("Please supply digits only for this field");
            alert.getDialogPane().setHeaderText("Digits Expected");
            alert.showAndWait();
            String value = ((TextField) event.getSource()).getText().replace(event.getCharacter(), "");
            ((TextField) event.getSource()).requestFocus();
            ((TextField) event.getSource()).setText(value);
        }
    }

    private boolean isValidUserInputs() {
        boolean status = false;
        if (this.txtUsername.getText().isEmpty() || this.txtFirstName.getText().isEmpty() || this.txtLastName.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Input Error");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText("Please supply inputs for the specified fields");
            alert.getDialogPane().setHeaderText("Incomplete inputs");
            alert.showAndWait();
            status = false;
        } else {
            status = true;
        }
        return status;
    }

    @FXML
    private void getSelectedImage() {
        isImageSet = false;
        fileInByte = null;
        File selectedFile;
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Image File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );

            selectedFile = fileChooser.showOpenDialog(this.baseContainer.getScene().getWindow());

            File myFile = selectedFile;
            File storage = new File(myFile.getAbsolutePath());
            FileInputStream myFIS = new FileInputStream(storage);
            ByteArrayOutputStream myBAOS = new ByteArrayOutputStream();
            fileInByte = new byte[(int) storage.length()];
            myFIS.read(fileInByte);

            myBAOS.write(fileInByte);
            fileInByte = myBAOS.toByteArray();
            this.isImageSet = true;

            imgUser1.setVisible(false);
            imgUser.setVisible(true);

            this.imgUser1.setVisible(false);
            imgUser = new ImageView(new Image(myFile.toURI().toURL().toString(), 262, 270, false, true));
            this.imageAnchor.getChildren().add(imgUser);
        } catch (Exception e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR, "Image File Upload Error");
//            alert.initModality(Modality.APPLICATION_MODAL);
//            alert.getDialogPane().setContentText(e.getMessage());
//            alert.getDialogPane().setHeaderText("Error uploading image");
//            alert.showAndWait();
        }

    }

    private Student setUser(Student user) {
        if (isValidUserInputs()) {

            if (user.getId() == null) {
                String code;
                if (this.txtFirstName.getText().length() > 4) {
                    code = this.txtFirstName.getText().substring(0, 4);
                } else {
                    code = this.txtFirstName.getText();
                }
                Random rand = new Random(System.currentTimeMillis());
                currentUser.setId(code + rand.nextInt(99) + "-" + LocalDate.now().toString());
            }

            //uploading user image
            if (isImageSet) {
                currentUser.setPhoto(this.fileInByte);
            }
            if (!this.txtUsername.getText().isEmpty()) {
                currentUser.setUserName(this.txtUsername.getText().trim());
            }
            if (!this.txtPassword.getText().isEmpty() && !this.txtPassword2.getText().isEmpty()) {
                try {
                    currentUser.setPassword(new CryptMaster().encryptPass(this.txtPassword2.getText().trim()));
                } catch (Exception ex) {
                    Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (!this.txtFirstName.getText().isEmpty()) {
                currentUser.setFistName(this.txtFirstName.getText().trim());
            }
            if (!this.txtLastName.getText().isEmpty()) {
                currentUser.setLastName(this.txtLastName.getText().trim());
            }
            if (!this.txaNote1.getText().isEmpty()) {
                currentUser.setComment(this.txaNote1.getText().trim());
            }
            if (!this.txtTelephoneNumber.getText().isEmpty()) {
                currentUser.setTelephone(this.txtTelephoneNumber.getText().trim());
            }
            if (!this.txtIndexNumber.getText().isEmpty()) {
                currentUser.setIndexNumber(this.txtIndexNumber.getText().trim());
            }
            if (!this.txtEmailAddress.getText().isEmpty()) {
                currentUser.setEmail(this.txtEmailAddress.getText().trim());
            }
            currentUser.setProgram(getProgramItem(this.cboProgram.getSelectionModel().getSelectedItem()));
            currentUser.setHall(this.cboHall.getSelectionModel().getSelectedItem());
        }
        return currentUser;
    }

    private Programme getProgramItem(String Id) {
        return this.entitymanager.createNamedQuery("Programme.findByName", Programme.class).setParameter("name", Id).getSingleResult();
    }

    private boolean validateStringLength(String value) {
        if (value.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Password Error");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText("Please enter your password.");
            alert.getDialogPane().setHeaderText("No password found");
            alert.showAndWait();
            this.txtPassword.requestFocus();
            return false;
        } else if (value.length() < 8) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Password Length Error");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText("Password should be eight characters or more.");
            alert.getDialogPane().setHeaderText("Password too short");
            alert.showAndWait();
            this.txtPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean confirmPassword(String value, String value2) {
        if (value.equals(value2)) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Password Mismatch");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText("Type the same passwor to confirm.");
            alert.getDialogPane().setHeaderText("Password Mismatch");
            alert.showAndWait();
            this.txtPassword2.setText("");
            this.txtPassword2.requestFocus();
            return false;
        }
    }

    private void getCurrentUser() {
        try {
            this.clearUserInput();
            currentUser = Alpha.currentUser;
            if (!currentUser.getUserName().isEmpty()) {
                this.txtUsername.setText(currentUser.getUserName());
            }
            this.txtPassword.setText("");
            this.txtPassword2.setText("");
            this.txtFirstName.setText(currentUser.getFistName());
            this.txtLastName.setText(currentUser.getLastName());
            this.txtIndexNumber.setText(currentUser.getIndexNumber());
            this.txtTelephoneNumber.setText(currentUser.getTelephone());
            this.txtEmailAddress.setText(currentUser.getEmail());
            this.txaNote1.setText(currentUser.getComment());
            this.txtOutputFolder.setText(currentUser.getOutputDir());
            this.cboHall.getSelectionModel().select(currentUser.getHall());
            this.cboProgram.getSelectionModel().select(currentUser.getProgram().getName());
            this.txtLastbackup.setText(Alpha.currentUser.getLastBackup());
            this.txtRestoreDatabase.setText(Alpha.currentUser.getLastRetore());
            //clearing the images map before populating it.
            if (images.size() < 1) {
                images.put("default", this.imgUser);
            } else {
                imageAnchor.getChildren().clear();
                imageAnchor.getChildren().add(this.images.get("default"));
            }

            if (Alpha.currentUser.getPhoto().length > 0) {
                FileOutputStream fos = null;
                try {
                    File imageFile = new File(Alpha.currentUser.getId());
                    imageFile.deleteOnExit();
                    fos = new FileOutputStream(imageFile.getName());
                    fos.write(Alpha.currentUser.getPhoto());
                    fos.close();
                    ImageView currentImage = new ImageView(new Image(imageFile.toURI().toURL().toString(), 180, 200, false, true));
                    this.images.put(Alpha.currentUser.getId(), currentImage);

                } catch (Exception ex) {

                }
            }
            if (images.containsKey(Alpha.currentUser.getId())) {
                imageAnchor.getChildren().add(this.images.get(Alpha.currentUser.getId()));
                imageAnchor.setPrefWidth(180);
                imageAnchor.setCenterShape(true);
            } else {
                imageAnchor.getChildren().add(this.images.get("default"));
            }
            Query query = this.entitymanager.createQuery("SELECT s FROM Semester s WHERE s.studentID = ?1 AND s.name = ?2");
            query.setParameter(1, currentUser);
            query.setParameter(2, currentUser.getCurrentSem());
            if (!query.getResultList().isEmpty()) {

                this.currentSemester = (Semester) query.getResultList().get(0);
                this.cboCurrentSemester.getSelectionModel().select((currentSemester.getName()));
                try {
                    if (!currentSemester.getStartDate().isEmpty()) {
                        this.cboSemesterStartDate.getEditor().setText(currentSemester.getStartDate());
                    }
                    if (!currentSemester.getEndDate().isEmpty()) {
                        this.cboSemesterEndDate.getEditor().setText(currentSemester.getEndDate());
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(e.getMessage());
                }
            }
            if (Alpha.currentUser.getAudio().equals("true")) {
                this.sound.selectToggle(rdtOn);
            } else {
                this.sound.selectToggle(rdtOff);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Current user error");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText(e.getMessage());
            alert.getDialogPane().setHeaderText("User info retrieval incomplete");
            alert.initOwner(Alpha.stage);
//            alert.show();
        }
    }

    private void loadSemesterCombo() {
        this.cboCurrentSemester.getItems().add("Year 1 Semester 1");
        this.cboCurrentSemester.getItems().add("Year 1 Semester 2");
        this.cboCurrentSemester.getItems().add("Year 2 Semester 1");
        this.cboCurrentSemester.getItems().add("Year 2 Semester 2");
        this.cboCurrentSemester.getItems().add("Year 3 Semester 1");
        this.cboCurrentSemester.getItems().add("Year 3 Semester 2");
        this.cboCurrentSemester.getItems().add("Year 4 Semester 1");
        this.cboCurrentSemester.getItems().add("Year 4 Semester 2");
    }

    private void loadHalls() {
        if (this.txtUsername.getText().isEmpty()) {
            Query query = entitymanager.createQuery("SELECT l FROM Location l WHERE l.type LIKE 'hall'");
            List<Location> hallList = query.getResultList();
            hallList.stream().forEach((hall) -> {
                if (hall.getCampus().equalsIgnoreCase(DBConnection.campus)) {
                    this.cboHall.getItems().add(hall.getName());
                }
            });
        }
    }

    private void loadProgrammes() {
        if (this.txtUsername.getText().isEmpty()) {
            Query query = entitymanager.createQuery("SELECT p FROM Programme p ");
            List<Programme> hallList = query.getResultList();
            hallList.stream().forEach((programme) -> {
                if (programme.getCampus().equalsIgnoreCase(DBConnection.campus)) {
                    this.cboProgram.getItems().add(programme.getName());
                }
            });
        }
    }

    @FXML
    private void updateOtherSettings(ActionEvent event) {
        try {
            Query query = this.entitymanager.createQuery("SELECT s FROM Semester s WHERE s.studentID = ?1 AND s.name = ?2");
            query.setParameter(1, currentUser);
            query.setParameter(2, this.cboCurrentSemester.getSelectionModel().getSelectedItem());
            if (query.getResultList().isEmpty()) {
                this.currentSemester = new Semester();
                currentSemester.setId(currentUser.getId() + this.cboCurrentSemester.getSelectionModel().getSelectedItem());
                currentSemester.setStudentID(currentUser);
                currentSemester.setName(this.cboCurrentSemester.getSelectionModel().getSelectedItem());
                if (!this.cboSemesterStartDate.getEditor().getText().isEmpty()) {
                    currentSemester.setStartDate(this.cboSemesterStartDate.getEditor().getText());
                }
                if (!this.cboSemesterEndDate.getEditor().getText().isEmpty()) {
                    currentSemester.setEndDate(this.cboSemesterEndDate.getEditor().getText());
                }
                currentSemester.setGpa(0.0);
                currentSemester.setTotalCredit(0);
                currentSemester.setNumberOfCourses(0);
                this.entitymanager.getTransaction().begin();
                this.entitymanager.persist(currentSemester);
                Alpha.currentUser.setCurrentSem(this.cboCurrentSemester.getSelectionModel().getSelectedItem());
                this.entitymanager.merge(Alpha.currentUser);
                this.entitymanager.getTransaction().commit();
                this.entitymanager.getTransaction().begin();
                for (int i = 0; i < day.length; i++) {
                    Schedule mySchedule = new Schedule();
                    mySchedule.setSemesterID(this.currentSemester);
                    mySchedule.setDay("" + (i + 1) + ". " + day[i]);
                    this.entitymanager.persist(mySchedule);
                }
                this.entitymanager.getTransaction().commit();
            } else {
                Semester someSemester = (Semester) query.getResultList().get(0);
                this.cboCurrentSemester.getSelectionModel().select((someSemester.getName()));
                if (!this.cboSemesterStartDate.getEditor().getText().isEmpty()) {
                    someSemester.setStartDate(this.cboSemesterStartDate.getEditor().getText());
                }
                if (!this.cboSemesterEndDate.getEditor().getText().isEmpty()) {
                    someSemester.setEndDate(this.cboSemesterEndDate.getEditor().getText());
                }
                this.entitymanager.getTransaction().begin();
                this.entitymanager.merge(someSemester);
                Alpha.currentUser.setCurrentSem(this.cboCurrentSemester.getSelectionModel().getSelectedItem());
                this.entitymanager.merge(Alpha.currentUser);
                this.entitymanager.getTransaction().commit();
            }
            CoursesController.refresh = true;
            ScheduleController.refresh = true;
            AcademicsController.refresh = true;
            GradingController.refresh = true;
            PerformanceController.refresh = true;
            WalletController.refresh = true;
            DocumentsController.refresh = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Semester Update");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText("Current semester has been successfully changed.");
            alert.getDialogPane().setHeaderText("Update successful");
            alert.initOwner(Alpha.stage);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Semester Update Error");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText(e.getMessage());
            alert.getDialogPane().setHeaderText("Update failed");
            alert.initOwner(Alpha.stage);
            alert.show();
        }

    }

    private void prepareCurrentSemester() {
        try {
            Query query = this.entitymanager.createQuery("SELECT s FROM Semester s WHERE s.studentID = ?1 AND s.name = ?2");
            query.setParameter(1, currentUser);
            query.setParameter(2, currentUser.getCurrentSem());
            if (query.getResultList().isEmpty()) {
                this.entitymanager.getTransaction().begin();
                Semester newSemester = new Semester();
                newSemester.setId(currentUser.getId() + currentUser.getCurrentSem());
                newSemester.setStudentID(currentUser);
                newSemester.setName(currentUser.getCurrentSem());
                newSemester.setGpa(0.0);
                newSemester.setCgpa(0.0);
                if (!this.cboSemesterStartDate.getEditor().getText().isEmpty()) {
                    newSemester.setStartDate(this.cboSemesterStartDate.getEditor().getText());
                }
                if (!this.cboSemesterEndDate.getEditor().getText().isEmpty()) {
                    newSemester.setEndDate(this.cboSemesterEndDate.getEditor().getText());
                }
                this.entitymanager.persist(newSemester);
                this.entitymanager.getTransaction().commit();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Current semester updated");
            alert.initOwner(Alpha.stage);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(e.getMessage());
            alert.initOwner(Alpha.stage);
            alert.show();
        }

    }

    @FXML
    private void loadPageData(MouseEvent event) {
        if (this.currentUser != Alpha.currentUser) {
            loadHalls();
            this.loadProgrammes();
            getCurrentUser();
            this.currentUser = Alpha.currentUser;
        }

    }

    private void getSelectedFolder() {

        File selectedFile;
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose output location");
            selectedFile = fileChooser.showOpenDialog(this.baseContainer.getScene().getWindow());
            Alpha.currentUser.setOutputDir(selectedFile.getParent());
            this.txtOutputFolder.setText(selectedFile.getParent());
            this.entitymanager.getTransaction().begin();
            this.entitymanager.merge(Alpha.currentUser);
            this.entitymanager.getTransaction().commit();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Output folder updated");
            alert.initOwner(Alpha.stage);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "File location error");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText(e.getMessage());
            alert.getDialogPane().setHeaderText("Error recording folder");
            alert.showAndWait();
        }

    }

    private void prepareDefaultSchedule() {
        try {
            this.entitymanager.getTransaction().begin();
            for (int i = 0; i < day.length; i++) {
                Schedule mySchedule = new Schedule();
                mySchedule.setSemesterID(this.currentSemester);
                mySchedule.setDawn(day[1]);
                this.entitymanager.persist(mySchedule);
            }
            this.entitymanager.getTransaction().commit();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.setHeaderText("Error creating schedule format");
            alert.initOwner(Alpha.stage);
            alert.show();
        }
    }

    @FXML
    private void setOutputFolder(ActionEvent event) {

        this.getSelectedFolder();
    }

    @FXML
    private void backupDatabase(ActionEvent event) {
        try {
            FileInputStream fis = null;
            ZipOutputStream zos;
            Connection conn = DBConnection.dbconnect();

            TextInputDialog textInput = new TextInputDialog("");
            textInput.setTitle("Backup Information");
            textInput.getDialogPane().setContentText("Please enter the name for your BACKUP zip file");
            textInput.showAndWait()
                    .ifPresent(response -> {
                        if (response.isEmpty()) {
                            zipName = "Backup";
                        } else {
                            zipName = response;
                        }
                    });

            File tmpFile = File.createTempFile("backup", ".db");
            tmpFile.deleteOnExit();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("backup to " + tmpFile.getAbsolutePath());
            java.util.Date now = new java.util.Date();
            stmt.close();
            conn.close();

            fis = new FileInputStream(tmpFile.getAbsolutePath());
            zos = new ZipOutputStream(new FileOutputStream(Alpha.currentUser.getOutputDir() + "\\" + zipName + ".zip"));
            zos.putNextEntry(new ZipEntry("propartDB.db"));

            int size = (int) tmpFile.length();
            byte[] temp = new byte[size];
            int read;

            while ((read = fis.read(temp)) > 0) {
                zos.write(temp, 0, read);
            }
            zos.closeEntry();
            fis.close();
            zos.close();

            this.entitymanager.getTransaction().begin();
            Alpha.currentUser.setLastBackup(sdf.format(now));
            this.txtLastbackup.setText(sdf.format(now));
            this.entitymanager.merge(Alpha.currentUser);
            this.entitymanager.getTransaction().commit();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Backup Information");
            alert.setContentText("Backup successful. The zip file can be found at your output folder.\n" + Alpha.currentUser.getOutputDir());
            alert.initOwner(Alpha.stage);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Backup Error");
            alert.setContentText(e.getLocalizedMessage());
            alert.initOwner(Alpha.stage);
            alert.show();
        }
    }

    @FXML
    private void restoreDatabase(ActionEvent event) {

        try {
            File tempFile = File.createTempFile("Restore", ".db");
            tempFile.deleteOnExit();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose File");
            File myFile = fileChooser.showOpenDialog(this.baseContainer.getScene().getWindow());

            String fileName = myFile.getName();

            if (fileName.endsWith(".zip")) {

                ZipInputStream zis = new ZipInputStream(new FileInputStream(myFile));
                //get the zipped file list entry
                ZipEntry ze = zis.getNextEntry();

                while (ze != null) {

                    String zipContentName = ze.getName();
                    if (zipContentName.endsWith(".db")) {
                        byte[] buffer = new byte[1024];
                        tempFile.getAbsoluteFile();

                        FileOutputStream fos = new FileOutputStream(tempFile);

                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }

                        fos.close();
                        ze = zis.getNextEntry();

                        break;
                    }

                }

                zis.closeEntry();
                zis.close();

                Connection conn = DBConnection.dbconnect();

                Statement stmt = conn.createStatement();
                stmt.executeUpdate("restore from " + tempFile.getAbsolutePath());

                java.util.Date now = new java.util.Date();

                this.entitymanager.getTransaction().begin();
                Alpha.currentUser.setLastRetore(sdf.format(now));
                this.txtRestoreDatabase.setText(sdf.format(now));
                this.entitymanager.merge(Alpha.currentUser);
                this.entitymanager.getTransaction().commit();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Restore Information");
                alert.setContentText("Restore activity successful");
                alert.initOwner(Alpha.stage);
                alert.show();

            } else if (fileName.equals("propartDB.db")) {

                Connection conn = DBConnection.dbconnect();

                Statement stmt = conn.createStatement();
                stmt.executeUpdate("restore from " + tempFile.getAbsolutePath());

                java.util.Date now = new java.util.Date();

                this.entitymanager.getTransaction().begin();
                Alpha.currentUser.setLastRetore(sdf.format(now));
                this.txtRestoreDatabase.setText(sdf.format(now));
                this.entitymanager.merge(Alpha.currentUser);
                this.entitymanager.getTransaction().commit();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Restore Information");
                alert.setContentText("Restore activity successful");
                alert.initOwner(Alpha.stage);
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Restore Information");
                alert.setContentText("Database file not found");
                alert.initOwner(Alpha.stage);
                alert.show();
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Restore Information");
            alert.setContentText(e.getLocalizedMessage());
            alert.initOwner(Alpha.stage);
            alert.show();
        }
    }

    @FXML
    private void launchAboutHelp(ActionEvent event) {
        Alpha.startAbout(Alpha.stage);
    }

    @FXML
    private void showPersonalInstruction(MouseEvent event) {
        this.personalInstructionPane.setExpanded(true);
    }

    @FXML
    private void showGeneralInstructionPane(MouseEvent event) {
        this.GeneralInstructionPane.setExpanded(true);
    }

    @FXML
    private void hideInstructions(MouseEvent event) {
        this.personalInstructionPane.setExpanded(false);
        this.GeneralInstructionPane.setExpanded(false);
    }

    @FXML
    private void startInfo(ActionEvent event) {
    }

    @FXML
    private void collapsePassword(MouseEvent event) {
    }

    @FXML
    private void changeSoundSettings(ActionEvent event) {

        this.entitymanager.getTransaction().begin();
        if (((RadioButton) event.getSource()).getId().contains("On")) {
            this.currentUser.setAudio("true");
        } else {
            this.currentUser.setAudio("false");
        }
        this.entitymanager.merge(this.currentUser);
        this.entitymanager.getTransaction().commit();
        IndexController.currentUser = this.currentUser;
        Alpha.currentUser = this.currentUser;
        Notifications.create().position(Pos.BOTTOM_RIGHT).title("Audio Settings").text("Audio settings updated").showInformation();
    }

}
