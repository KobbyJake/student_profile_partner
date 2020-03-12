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
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javax.persistence.Query;
import model.Document;
import model.Semester;
import view.Alpha;
import view.ScreensController;

/**
 * FXML Controller class
 *
 * @author Jake
 */
public class DocumentsController implements Initializable, ControlledScreen {
public static boolean refresh = true;
    public Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    public double width = screenSize.getWidth(), height = screenSize.getHeight();
    ScreensController myController;
    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        baseContainer.setPrefSize(width - (width * 0.5 / 100), height - (height * 4 / 100));
    }
    
    
    private Document currentDocument;
    private String selectedFileName;
    @FXML
    private Button btnRetrieve;
    @FXML
    private MenuItem walletMenuItem;

   
    private String semesterMarker = "";
    private String userMarker = "";
    private ObservableList<Document> documentData;
    private Semester currentSemester;
    javax.persistence.EntityManager entitymanager;

    private boolean isFileSet;
    private byte[] fileInByte;
    @FXML
    private BorderPane baseContainer;
    @FXML
    private Color x4;
    @FXML
    private Font x3;
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
    private TitledPane documentInput;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextArea txaNote;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnReport;
    @FXML
    private Label lblFile;
    @FXML
    private ComboBox<String> cboSection;
    @FXML
    private Hyperlink htpFile;
    @FXML
    private ComboBox<String> cboCurrentSemester;
    @FXML
    private TableView<Document> tblDocument;
    @FXML
    private ComboBox<String> cboOutputFilter;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        entitymanager = DBConnection.getEntityManger();
        this.loadCombos();
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

    @FXML
    private void saveDocument(ActionEvent event) {
        if (this.isValidDocumentInput()) {
            try {
                this.entitymanager.getTransaction().begin();
                this.entitymanager.persist(this.setDocument(new Document()));
                this.entitymanager.getTransaction().commit();
                this.loadDocumentsTable();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Document saved");
                alert.initOwner(Alpha.stage); alert.show();
                clearDocumentInput();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Document Error");
                alert.setContentText(e.getLocalizedMessage());
                alert.initOwner(Alpha.stage); alert.show();
            }
        }
    }

    @FXML
    private void updateDocument(ActionEvent event) {
        if (this.isValidDocumentInput()) {
            if (this.currentDocument == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("No record selected");
                alert.setContentText("Please select an entry to update");
                alert.initOwner(Alpha.stage); alert.show();
            } else {
                try {
                    this.entitymanager.getTransaction().begin();
                    this.entitymanager.merge(this.setDocument(this.currentDocument));
                    this.entitymanager.getTransaction().commit();
                    this.loadDocumentsTable();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Document updated");
                    alert.initOwner(Alpha.stage); alert.show();
                    clearDocumentInput();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Document Error");
                    alert.setContentText(e.getLocalizedMessage());
                    alert.initOwner(Alpha.stage); alert.show();
                }
            }
        }
    }

    @FXML
    private void deleteDocument(ActionEvent event) {
        if (this.currentDocument == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("No record selected");
            alert.setContentText("Please select an entry to delete");
            alert.initOwner(Alpha.stage); alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Confirm delete action");
            alert.setContentText("You are about to delete the selected record.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    this.entitymanager.getTransaction().begin();
                    this.entitymanager.remove(this.currentDocument);
                    this.entitymanager.getTransaction().commit();
                    this.loadDocumentsTable();
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Document deleted");
                    alert.initOwner(Alpha.stage); alert.show();
                    clearDocumentInput();
                } catch (Exception e) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Document Error");
                    alert.setContentText(e.getLocalizedMessage());
                    alert.initOwner(Alpha.stage); alert.show();
                }
            }

        }

    }

    @FXML
    private void ReportDocuments(ActionEvent event) {
    }

    @FXML
    private void loadSemesterSchedule(ActionEvent event) {
        this.loadDocumentsTable();
    }

    @FXML
    private void resetPage(MouseEvent event) {
        this.semesterMarker = "";
    }

    @FXML
    private void LoadPageData(MouseEvent event) {
        getSemesterDetails();
    }

    @FXML
    private void filterOutput(ActionEvent event) {
        this.clearDocumentInput();
        this.loadDocumentsTable();
    }

    private void loadCombos() {
        this.cboOutputFilter.getItems().add("All sections");
        this.cboOutputFilter.getItems().add("Courses");
        this.cboOutputFilter.getItems().add("Schedule");
        this.cboOutputFilter.getItems().add("Academics");
        this.cboOutputFilter.getItems().add("Grading");
        this.cboOutputFilter.getItems().add("Performance");
        this.cboOutputFilter.getItems().add("Others");

        this.cboSection.getItems().add("Courses");
        this.cboSection.getItems().add("Schedule");
        this.cboSection.getItems().add("Academics");
        this.cboSection.getItems().add("Grading");
        this.cboSection.getItems().add("Performance");
        this.cboSection.getItems().add("Others");

        this.cboOutputFilter.getSelectionModel().select(0);
        this.cboSection.getSelectionModel().select(0);
    }

    private void getSemesterDetails() {
        //retrieving current semester
       if (refresh) {
            refresh = false;
            this.cboCurrentSemester.getSelectionModel().select(Alpha.currentUser.getCurrentSem());

            try {
                this.cboCurrentSemester.getItems().clear();
                Query query = this.entitymanager.createQuery("SELECT s FROM Semester s WHERE s.studentID = ?1");
                query.setParameter(1, Alpha.currentUser);
                query.getResultList().stream().forEach((semester) -> {
                    Semester currentSem = (Semester) semester;
                    this.cboCurrentSemester.getItems().add(currentSem.getName());
                    if (currentSem.getName().equals(this.cboCurrentSemester.getSelectionModel().getSelectedItem())) {
                        this.currentSemester = currentSem;
                    }
                });
                this.cboCurrentSemester.getSelectionModel().select(Alpha.currentUser.getCurrentSem());
                this.loadDocumentsTable();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.setHeaderText("Error retrieving registered semester list.");
                alert.initOwner(Alpha.stage); alert.show();
            }
            this.userMarker = Alpha.currentUser.getUserName();
            this.semesterMarker = Alpha.currentUser.getCurrentSem();
            
        }
    }

    private void loadDocumentsTable() {
        Query query = this.entitymanager.createQuery("SELECT s FROM Semester s WHERE s.studentID = ?1 AND s.name = ?2");
        query.setParameter(1, Alpha.currentUser);
        query.setParameter(2, this.cboCurrentSemester.getSelectionModel().getSelectedItem());
        if (!query.getResultList().isEmpty()) {
            this.currentSemester = (Semester) query.getSingleResult();
        }
        this.documentData = FXCollections.observableArrayList();
        try {
//            query = this.entitymanager.createQuery("SELECT d FROM Document d WHERE d.semesterID = ?1 AND d.reference LIKE 'System' OR d.reference LIKE 'SR%'");
            query = this.entitymanager.createQuery("SELECT d FROM Document d WHERE d.semesterID = ?1 AND d.reference NOT LIKE 'System' AND d.reference NOT LIKE 'SR%'");
            query.setParameter(1, this.currentSemester);
            query.getResultList().stream().forEach((doc) -> {
                if (this.cboOutputFilter.getSelectionModel().getSelectedItem().equals("All sections")) {
                    this.documentData.add((Document) doc);
                } else if (((Document) doc).getReference().equals(this.cboOutputFilter.getSelectionModel().getSelectedItem())) {
                    this.documentData.add((Document) doc);
                }

            });

            this.tblDocument.setItems(this.documentData);
            this.setDocumentsTableColumns();
            this.tblDocument.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Document> obs, Document oldSelection, Document newSelection) -> {

                if (newSelection != null) {
                    documentInput.setExpanded(true);
                    this.currentDocument = newSelection;
                    this.txtTitle.setText(newSelection.getTitle());
                    this.lblFile.setText(newSelection.getFileName());
                    this.fileInByte = newSelection.getFile();
                    this.cboSection.getSelectionModel().select(newSelection.getReference());
                    this.txaNote.setText(newSelection.getNote());

                }

            });
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(e.getLocalizedMessage());
            alert.setHeaderText("Error preparing Documents table");
            alert.initOwner(Alpha.stage); alert.show();
        }
    }

    private void setDocumentsTableColumns() {
        double netWidth = (this.tblDocument.getWidth() - (this.tblDocument.getWidth() * 0.75 / 100));
        tblDocument.getColumns().clear();

        TableColumn<Document, String> column12 = new TableColumn();
        column12.setVisible(true);
        column12.setText("TITLE");
        column12.setCellValueFactory(new PropertyValueFactory("title"));
        column12.setPrefWidth(netWidth / 3);
        this.tblDocument.getColumns().add(column12);

        TableColumn<Document, String> column13 = new TableColumn();
        column13.setVisible(true);
        column13.setText("File");
        column13.setCellValueFactory(new PropertyValueFactory("fileName"));
        column13.setPrefWidth(netWidth / 4);
        this.tblDocument.getColumns().add(column13);

        TableColumn<Document, String> column16 = new TableColumn();
        column16.setVisible(true);
        column16.setText("SECTION");
        column16.setCellValueFactory(new PropertyValueFactory("reference"));
        column16.setPrefWidth(netWidth / 5);
        this.tblDocument.getColumns().add(column16);

        TableColumn<Document, String> column17 = new TableColumn();
        column17.setVisible(true);
        column17.setText("COMMENTS");
        column17.setCellValueFactory(new PropertyValueFactory("note"));
        column17.setPrefWidth(netWidth / 4.55);
        this.tblDocument.getColumns().add(column17);

    }

    private void getSelectedFile() {
        this.isFileSet = false;
        this.fileInByte = null;
        File selectedFile;
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose File");
            selectedFile = fileChooser.showOpenDialog(this.baseContainer.getScene().getWindow());
            this.selectedFileName = selectedFile.getName();
            this.lblFile.setText(this.selectedFileName);
            File myFile = selectedFile;
            File storage = new File(myFile.getAbsolutePath());
            FileInputStream myFIS = new FileInputStream(storage);
            ByteArrayOutputStream myBAOS = new ByteArrayOutputStream();
            fileInByte = new byte[(int) storage.length()];
            myFIS.read(fileInByte);
            myBAOS.write(fileInByte);
            fileInByte = myBAOS.toByteArray();
            this.isFileSet = true;
        } catch (Exception e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR, "Image File Upload Error");
//            alert.initModality(Modality.APPLICATION_MODAL);
//            alert.getDialogPane().setContentText(e.getMessage());
//            alert.getDialogPane().setHeaderText("Error uploading image");
//            alert.showAndWait();
        }

    }

    private Document setDocument(Document doc) {
	    if (doc.getId() == null) {
                String code;
                if (this.txtTitle.getText().length() > 4) {
                    code = this.txtTitle.getText().substring(0, 4);
                } else {
                    code = this.txtTitle.getText();
                }
                Random rand = new Random(System.currentTimeMillis());
                doc.setId(code + rand.nextInt(99) + "-" + LocalDate.now().toString());
            }

        doc.setSemesterID(currentSemester);
        doc.setTitle(this.txtTitle.getText());
        doc.setReference(this.cboSection.getSelectionModel().getSelectedItem());
        if (!this.isFileSet) {
            this.getSelectedFile();
            doc.setFile(fileInByte);
        } else {
            doc.setFile(fileInByte);
        }
        doc.setFileName(this.selectedFileName);
        doc.setNote(this.txaNote.getText());
        return doc;
    }

    private boolean isValidDocumentInput() {
        boolean status = false;
        if (this.txtTitle.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please enter the title for this document entry.");
            alert.showAndWait();

        } else {
            status = true;
        }
        return status;
    }

    private void clearDocumentInput() {
        this.txtTitle.setText("");
        this.txaNote.setText("");
        this.cboSection.getSelectionModel().select("Courses");
        this.isFileSet = false;
        this.lblFile.setText("");
        this.txtTitle.requestFocus();
        this.currentDocument = null;
    }

    @FXML
    private void uploadFile(ActionEvent event) {
        this.getSelectedFile();
    }

    @FXML
    private void retrieveDocument(ActionEvent event) {
        try {
            FileOutputStream fos = new FileOutputStream("" + Alpha.currentUser.getOutputDir() + File.pathSeparator + this.selectedFileName);
            fos.write(this.fileInByte);
            fos.close();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("File retrieval");
            alert.setContentText("Your document is successfuly saved at " + Alpha.currentUser.getOutputDir() + "\n Would you like to open it now?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + Alpha.currentUser.getOutputDir() + File.pathSeparator + this.selectedFileName);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error retrieving file");
            alert.setContentText(e.getLocalizedMessage());
            alert.initOwner(Alpha.stage); alert.show();
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

}
