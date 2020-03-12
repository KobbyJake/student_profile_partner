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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
import view.Alpha;
import view.ScreensController;

/**
 * FXML Controller class
 *
 * @author Jake
 */
public class UserController implements Initializable, ControlledScreen {

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
    private Button btnaddNewUser;
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
    private MenuItem exitMenuItem;
    private ImageView imgUser;
    private boolean isImageSet;
    private byte[] fileInByte;
    public static Student currentUser;
    private EntityManager entitymanager;
    Map<String, ImageView> images = new HashMap<>();
    private ComboBox<String> cboSemester;
    private static String[] day = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    Semester currentSemester;
    @FXML
    private ImageView imageFrame;
    @FXML
    private ImageView imageFrame1;
    private boolean status;
    private Programme thisProg;
    private List<Programme> programList;
    private List<Location> hallList;
    private Location thisHall;

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
    }

    @FXML
    private void addNewUser(ActionEvent event) {
        if (validateStringLength(this.txtPassword.getText().trim()) && confirmPassword(this.txtPassword.getText().trim(), this.txtPassword2.getText().trim())) {
            currentUser = new Student();
            try {
                Query query = this.entitymanager.createQuery("SELECT s FROM Student s WHERE s.userName = ?1");
                query.setParameter(1, this.txtUsername.getText());
                if (query.getResultList().isEmpty()) {
                    this.entitymanager.getTransaction().begin();
                    this.entitymanager.persist(this.setUser(currentUser));
                    query = this.entitymanager.createQuery("SELECT s FROM Semester s WHERE s.studentID = ?1 AND s.name = ?2");
                    query.setParameter(1, currentUser);
                    query.setParameter(2, currentUser.getCurrentSem());
                    if (query.getResultList().isEmpty()) {
                        Semester newSemester = new Semester();
                        newSemester.setId(currentUser.getId() + currentUser.getCurrentSem());
                        newSemester.setStudentID(currentUser);
                        newSemester.setName(currentUser.getCurrentSem());
                        newSemester.setNumberOfCourses(0);
                        newSemester.setTotalCredit(0);
                        newSemester.setGpa(0.0);
                        newSemester.setCgpa(0.0);
                        this.entitymanager.persist(newSemester);
                        for (int i = 0; i < day.length; i++) {
                            Schedule mySchedule = new Schedule();
                            mySchedule.setSemesterID(newSemester);
                            mySchedule.setDay("" + (i + 1) + ". " + day[i]);
                            this.entitymanager.persist(mySchedule);
                        }
                    }

                    this.entitymanager.getTransaction().commit();
                    this.clearUserInput();
                    try {

                        VoicePrompt.registrationClip.play(1.0);

                    } catch (Exception e) {
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "User account created");
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.getDialogPane().setContentText("A new user account has been created successfully");
                    alert.getDialogPane().setHeaderText("creating user account");
                    alert.showAndWait();

                    Alpha.startIndex(this.baseContainer);

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "User Error");
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.getDialogPane().setContentText("User name already in use by another profile, kindly modify or choose a new one.");
                    alert.getDialogPane().setHeaderText("Error creating user account");
                    alert.showAndWait();
                    this.txtUsername.clear();
                    this.txtUsername.requestFocus();
                }

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
            if ((validateStringLength(this.txtPassword.getText().trim()) && confirmPassword(this.txtPassword.getText().trim(), this.txtPassword2.getText().trim()))) {
                if (!this.txtPassword.getText().trim().equals(currentUser.getPassword())) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Confirm password update");
                    alert.setContentText("You are about to update the existing password");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (!(result.get() == ButtonType.OK)) {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "User account Error");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText("Please supply user inputs");
            alert.getDialogPane().setHeaderText("Error verifying user input");
            alert.showAndWait();
            return;
        }

        try {
            this.entitymanager.getTransaction().begin();
            this.entitymanager.merge(this.setUser(currentUser));
            this.entitymanager.getTransaction().commit();
            this.clearUserInput();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "User account created");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText("The current user account has been created updated successfully");
            alert.getDialogPane().setHeaderText("creating user account");
            alert.showAndWait();

            Alpha.startIndex(this.baseContainer);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "User account Error");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText(e.getMessage());
            alert.getDialogPane().setHeaderText("Error updating user account");
            alert.showAndWait();
            this.entitymanager.getTransaction().rollback();
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
    private void doExit(ActionEvent event) {
        Alpha.closeApp(this.baseContainer);
    }

    private void clearUserInput() {
        this.txtUsername.setText("");
        this.txtPassword.setText("");
        this.txtPassword2.setText("");
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
        if (this.txtUsername.getText().isEmpty() || this.txtPassword.getText().isEmpty() || this.txtFirstName.getText().isEmpty() || this.txtLastName.getText().isEmpty()) {
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

            imageFrame1.setVisible(false);
            imageFrame.setVisible(true);

            this.imageFrame1.setVisible(false);
            imageFrame = new ImageView(new Image(myFile.toURI().toURL().toString(), 180, 200, false, true));
            this.imageAnchor.getChildren().add(imageFrame);

        } catch (Exception e) {
//           
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
//            this.getSelectedImage();
            if (isImageSet) {
                currentUser.setPhoto(this.fileInByte);
            }

            currentUser.setUserName(this.txtUsername.getText().trim());
            try {
                currentUser.setPassword(new CryptMaster().encryptPass(this.txtPassword.getText().trim()));
            } catch (Exception ex) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
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
            setNewProgram();
            setNewHall();
            currentUser.setProgram(thisProg);
            currentUser.setHall(thisHall.getName());
            currentUser.setAudio("true");
            currentUser.setCurrentSem("Year 1 Semester 1");
            currentUser.setOutputDir("");
        }
        return currentUser;
    }

    private Programme getProgramItem(String Id) {
        return this.entitymanager.createNamedQuery("Programme.findByName", Programme.class).setParameter("name", Id).getSingleResult();
    }

    private boolean validateStringLength(String value) {
        if (value.length() == 0) {
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

    @FXML
    private void loadHalls() {
        if (this.cboHall.getItems().isEmpty()) {
            Query query = entitymanager.createQuery("SELECT l FROM Location l WHERE l.type LIKE 'hall'");
             hallList = query.getResultList();
            this.cboHall.getItems().clear();
            hallList.stream().forEach((hall) -> {
                    this.cboHall.getItems().add(hall.getName());
            });
        }
        this.loadProgrammes();
    }

    private void loadProgrammes() {
        if (this.cboProgram.getItems().isEmpty()) {
            Query query = entitymanager.createQuery("SELECT p FROM Programme p ");
            programList = query.getResultList();
            programList.stream().forEach((programme) -> {
                if (programme.getCampus().equalsIgnoreCase(DBConnection.campus)) {
                    this.cboProgram.getItems().add(programme.getName());
                }
            });
        }
    }

    private void getCurrentUser() {
        currentUser = Alpha.currentUser;
        try {
            Query query = this.entitymanager.createQuery("SELECT s FROM Semester s WHERE s.studentID = ?1 AND s.name = ?2");
            query.setParameter(1, currentUser);
            query.setParameter(2, currentUser.getCurrentSem());
            if (query.getResultList().isEmpty()) {
                this.currentSemester = new Semester();
                this.currentSemester.setId(currentUser.getId() + currentUser.getCurrentSem());
                this.currentSemester.setStudentID(currentUser);
                this.currentSemester.setName(currentUser.getCurrentSem());
                this.currentSemester.setNumberOfCourses(0);
                this.currentSemester.setTotalCredit(0);
                this.currentSemester.setGpa(0.0);
                this.currentSemester.setCgpa(0.0);
                this.entitymanager.getTransaction().begin();
                this.entitymanager.persist(this.currentSemester);
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
                this.cboSemester.getItems().clear();
                query.getResultList().stream().forEach((sem) -> {
                    this.cboSemester.getItems().add(((Semester) sem).getName());
                });
                this.cboSemester.getSelectionModel().select(Alpha.currentUser.getCurrentSem());
            }
        } catch (Exception e) {
        }
        this.txtUsername.setText(currentUser.getUserName());
        this.txtPassword.setText("");
        this.txtPassword2.setText("");
        this.txtFirstName.setText(currentUser.getFistName());
        this.txtLastName.setText(currentUser.getLastName());
        this.txtIndexNumber.setText(currentUser.getIndexNumber());
        this.txtTelephoneNumber.setText(currentUser.getTelephone());
        this.txtEmailAddress.setText(currentUser.getEmail());
        this.txaNote1.setText(currentUser.getComment());
        this.cboHall.getSelectionModel().select(currentUser.getHall());
        this.cboProgram.getSelectionModel().select(currentUser.getProgram().getName());

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

    }

    @FXML
    private void launchAboutHelp(ActionEvent event) {
        Alpha.startAbout(Alpha.stage);
    }

    @FXML
    private void startInfo(ActionEvent event) {
        Alpha.startInfo();
    }

    private void setNewProgram() {
        String prog = this.cboProgram.getEditor().getText().trim();
        status = false;
        programList.forEach((item) -> {
            if (item.getName().equals(prog)) {
                status = true;
                thisProg = item;
            }
        });
        if (!status) {
            createNewProgramme(prog);
        }

    }

    private void createNewProgramme(String prog) {
        try {
            this.entitymanager.getTransaction().begin();
            thisProg = new Programme();
            thisProg.setName(prog);
            thisProg.setDuration("4");
            this.entitymanager.persist(thisProg);
            this.entitymanager.getTransaction().commit();
        } catch (Exception e) {
        }
    }

    private void setNewHall() {
        String hall = this.cboHall.getEditor().getText().trim();

        
         status = false;
        this.hallList.forEach((item) -> {
            if (item.getName().equals(hall)) {
                status = true;
                thisHall = item;
            }
        });
        if (!status) {
           setCreateNewHall(hall);
        }
        
    }

    private void setCreateNewHall(String hall) {
        try {
            this.entitymanager.getTransaction().begin();
            thisHall = new Location();
            thisHall.setId(hall);
            thisHall.setName(hall);
            this.entitymanager.persist(thisHall);
            this.entitymanager.getTransaction().commit();
        } catch (Exception e) {
        }
    }

}
