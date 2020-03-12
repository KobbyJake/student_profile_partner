/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Dimension;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.CourseRead;
import model.CourseRegistry;
import model.Data;
import view.Alpha;
import view.ScreensController;
import javax.persistence.Query;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author Jake
 */
public class CoursesController implements Initializable, ControlledScreen {

    public static boolean refresh = true;
    public Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    public double width = screenSize.getWidth(), height = screenSize.getHeight();
    ScreensController myController;
    javax.persistence.EntityManager entitymanager;
    private boolean thisCourseExists;
    private Query query;
    @FXML
    private MenuItem walletMenuItem;
    private CourseRead currentCourseRead;
    private Paragraph pgf;
    private int counter;
    @FXML
    private TitledPane sidePane;
    @FXML
    private TitledPane sidePane1;
    @FXML
    private TextField txtCourseCode;
    @FXML
    private TextField txtCourseTitle;
    @FXML
    private TextField txtLecturer;
    @FXML
    private ComboBox<String> cmbCreditHours;
    @FXML
    private Button btnAddCourse;
    @FXML
    private Button btnClearInputs;
    @FXML
    private Button btnUpdateCourse;

    private CourseRegistry courseRegistryItem;

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        baseContainer.setPrefSize(width - (width * 0.5 / 100), height - (height * 4 / 100));
    }

    @FXML
    private TextField txtSemester;
    private ObservableList<CourseRead> semesterCourseList = FXCollections.observableArrayList();
    int totalCredit = 0;

    List<CourseRead> registeredCourseList;
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
    private TextField txtUname;
    @FXML
    private TextField txtProgram;
    @FXML
    private TextField txtNumberOfCourses;
    @FXML
    private TextField txtTotalCredit;
    @FXML
    private TableView<Data> tblCourses;
    @FXML
    private ListView<String> courseList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        entitymanager = DBConnection.getEntityManger();
        populateCredits();
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
    private void LoadPageData(MouseEvent event) {
        loadProfile();
    }

    private void loadProfile() {
        if (refresh) {
            refresh = false;
            this.txtUname.setText(Alpha.currentUser.getUserName());
            this.txtProgram.setText(Alpha.currentUser.getProgram().getName());
            this.txtSemester.setText(Alpha.currentUser.getCurrentSem());
            loadCourseRegistry();
        }

    }

    private void setTableColumns() {
        double netWidth = (this.tblCourses.getWidth() - (this.tblCourses.getWidth() * 0.75 / 100));

        TableColumn<Data, String> column11 = new TableColumn();
        column11.setVisible(true);
        column11.setText("CODE");
        column11.setCellValueFactory(new PropertyValueFactory("first"));
        column11.setPrefWidth(netWidth / 6);
        this.tblCourses.getColumns().add(column11);

        TableColumn<Data, String> column12 = new TableColumn();
        column12.setVisible(true);
        column12.setText("TITLE");
        column12.setCellValueFactory(new PropertyValueFactory("second"));
        column12.setPrefWidth(netWidth / 2.3);
        this.tblCourses.getColumns().add(column12);

        TableColumn<Data, String> column13 = new TableColumn();
        column13.setVisible(true);
        column13.setText("CREDITS");
        column13.setCellValueFactory(new PropertyValueFactory("third"));
        column13.setPrefWidth(netWidth / 10);
        this.tblCourses.getColumns().add(column13);

        TableColumn<Data, String> column14 = new TableColumn();
        column14.setVisible(true);
        column14.setText("LECTURER");
        column14.setCellValueFactory(new PropertyValueFactory("node1"));
        column14.setPrefWidth(netWidth / 4);
        this.tblCourses.getColumns().add(column14);

        TableColumn<Data, String> column15 = new TableColumn();
        column15.setVisible(true);
        column15.setText("");
        column15.setCellValueFactory(new PropertyValueFactory("node2"));
        column15.setPrefWidth(netWidth / 20);
        this.tblCourses.getColumns().add(column15);
    }

    private CourseRead findCourseReadByName(String name) {
        return this.entitymanager.createNamedQuery("CourseRead.findByCode",
                CourseRead.class)
                .setParameter("code", name)
                .getSingleResult();
    }
//

    private void getExistingCourses() {
        this.totalCredit = 0;
        this.semesterCourseList.clear();
        ObservableList<String> registeredCourses = FXCollections.observableArrayList();
        try {
            Query query = this.entitymanager.createQuery("SELECT c FROM CourseRead c WHERE c.student = ?1 AND c.semester = ?2 ");
            query.setParameter(1, Alpha.currentUser);
            query.setParameter(2, this.txtSemester.getText());
            query.getResultList().stream().forEach((item) -> {
                this.semesterCourseList.add((CourseRead) item);
                registeredCourses.add(((CourseRead) item).getTitle());
                totalCredit += ((CourseRead) item).getCredit();
            });

            this.courseList.setItems(registeredCourses);
            this.txtTotalCredit.setText(String.valueOf(totalCredit));
            this.txtNumberOfCourses.setText("" + registeredCourses.size());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.setHeaderText("Error retrieving existing semester course list");
            alert.initOwner(Alpha.stage);
            alert.show();
        }
    }

    @FXML
    private void resetPage(MouseEvent event) {
    }

    private void loadCourseRegistry() {
        switch (this.txtSemester.getText()) {
            case "Year 1 Semester 1":
                query = entitymanager.createQuery("SELECT c FROM CourseRegistry c WHERE c.code LIKE '%11%'");
                break;
            case "Year 1 Semester 2":
                query = entitymanager.createQuery("SELECT c FROM CourseRegistry c WHERE c.code LIKE '%12%'");
                break;
            case "Year 2 Semester 1":
                query = entitymanager.createQuery("SELECT c FROM CourseRegistry c WHERE c.code LIKE '%23%'");
                break;
            case "Year 2 Semester 2":
                query = entitymanager.createQuery("SELECT c FROM CourseRegistry c WHERE c.code LIKE '%24%'");
                break;
            case "Year 3 Semester 1":
                query = entitymanager.createQuery("SELECT c FROM CourseRegistry c WHERE c.code LIKE '%35%'");
                break;
            case "Year 3 Semester 2":
                query = entitymanager.createQuery("SELECT c FROM CourseRegistry c WHERE c.code LIKE '%36%'");
                break;
            case "Year 4 Semester 1":
                query = entitymanager.createQuery("SELECT c FROM CourseRegistry c WHERE c.code LIKE '%47%'");
                break;
            case "Year 4 Semester 2":
//                query = entitymanager.createQuery("SELECT c FROM CourseRegistry c WHERE c.code LIKE 'EDC48%' OR c.code LIKE 'IT_48%' OR c.code LIKE 'GPD48%'");
                query = entitymanager.createQuery("SELECT c FROM CourseRegistry c WHERE c.code LIKE '%48%'");
                break;
        }
        List<CourseRegistry> programmeList = query.getResultList();

        //retrieving existing courseRead
//            query = this.entitymanager.createQuery("SELECT c FROM CourseRead c WHERE C.student = ?1 AND c.semester = ?2 AND c.status = 'active'");
        query = this.entitymanager.createQuery("SELECT c FROM CourseRead c WHERE c.student = ?1 AND c.semester = ?2");
        query.setParameter(1, Alpha.currentUser);
        query.setParameter(2, Alpha.currentUser.getCurrentSem());
        registeredCourseList = query.getResultList();
        try {
            Data content = new Data();
            ObservableList regTableList = content.prepareCourses(programmeList, registeredCourseList);
            regTableList.stream().forEach((item) -> {

                setCheckboxAction(item);

            });
            this.tblCourses.setItems(regTableList);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.setHeaderText("Error handling checked course");
            alert.initOwner(Alpha.stage);
            alert.show();
        }
        this.tblCourses.getColumns().clear();
        setTableColumns();
        this.tblCourses.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Data> obs, Data oldSelection, Data newSelection) -> {

            if (newSelection != null) {

                this.txtCourseCode.setText(((Data) newSelection).getFirst());
                this.txtCourseTitle.setText(((Data) newSelection).getSecond());
                this.cmbCreditHours.getSelectionModel().select("" + ((Data) newSelection).getThird());
                this.txtLecturer.setText(((TextField) ((Data) newSelection).getNode1()).getText());
                this.btnAddCourse.setDisable(true);
                this.btnUpdateCourse.setDisable(false);
            }

        });
        getExistingCourses();

    }

    private void setCheckboxAction(Object item) {
        ((CheckBox) ((Data) item).getNode2()).setOnAction((event) -> {

            this.persistCourseRead(item);
            //refreshing selected courses list
            this.totalCredit = 0;
            this.semesterCourseList.clear();
            ObservableList<String> registeredCourses = FXCollections.observableArrayList();

            try {
                query = this.entitymanager.createQuery("SELECT c FROM CourseRead c WHERE C.student = ?1 AND c.semester = ?2 AND c.status = 'active'");
                query.setParameter(1, Alpha.currentUser);
                query.setParameter(2, this.txtSemester.getText());
                query.getResultList().stream().forEach((crItem2) -> {
                    this.semesterCourseList.add((CourseRead) crItem2);
                    registeredCourses.add(((CourseRead) crItem2).getTitle());
                    totalCredit += ((CourseRead) crItem2).getCredit();
                });

                this.courseList.setItems(registeredCourses);
                this.txtTotalCredit.setText(String.valueOf(totalCredit));
                this.txtNumberOfCourses.setText("" + registeredCourses.size());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.setHeaderText("Error retrieving existing semester course list");
                alert.initOwner(Alpha.stage);
                alert.show();
            }
            this.currentCourseRead = null;
//                       

        });
    }
public void persistCourseRead(Object item){
     this.thisCourseExists = false;
            try {

                currentCourseRead = this.findCourseReadByName(((Data) item).getFirst());
                if (currentCourseRead.getId().startsWith(Alpha.currentUser.getId()) && currentCourseRead.getStudent().equals(Alpha.currentUser)) {
                    this.thisCourseExists = true;
                } else {
                    currentCourseRead = new CourseRead();
                    currentCourseRead.setId(Alpha.currentUser.getId() + ((Data) item).getFirst());
                }
            } catch (Exception e) {
                currentCourseRead = new CourseRead();
                currentCourseRead.setId(Alpha.currentUser.getId() + ((Data) item).getFirst());
            }
            currentCourseRead.setCode(((Data) item).getFirst());
            currentCourseRead.setTitle(((Data) item).getSecond());

            currentCourseRead.setSemester(this.txtSemester.getText());
            currentCourseRead.setStudent(Alpha.currentUser);
            currentCourseRead.setCredit(Integer.parseInt(((Data) item).getThird()));
            currentCourseRead.setGrade("IC");
            currentCourseRead.setResitGrade("");
            currentCourseRead.setLecturer(((TextField) ((Data) item).getNode1()).getText());
            if (((CheckBox) ((Data) item).getNode2()).isSelected()) {
                currentCourseRead.setStatus("active");
            } else {
                currentCourseRead.setStatus("inactive");
            }
            try {
                this.entitymanager.getTransaction().begin();
                if (!thisCourseExists) {
                    this.entitymanager.persist(currentCourseRead);
                } else {
                    this.entitymanager.merge(currentCourseRead);
                }
                this.entitymanager.getTransaction().commit();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error updating current user course");
                alert.setContentText(e.getMessage());
                alert.initOwner(Alpha.stage);
                alert.show();
            }
}
    @FXML
    private void createReport(ActionEvent event) {
        com.itextpdf.text.Image banner = null;

        String title;
        if (Alpha.currentUser.getOutputDir().length() < 1) {
            title = this.txtSemester.getText() + " registered Course list.pdf";
        } else {
            title = Alpha.currentUser.getOutputDir() + this.txtSemester.getText() + " registered Course list.pdf";
        }

        java.util.Date date = new java.util.Date();

        try {
            new Alpha().getBanner("Courses");
            banner = com.itextpdf.text.Image.getInstance("lib/banner.png");
            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(title));
            ReportMaker report = new ReportMaker();
            writer.setPageEvent(report);
            doc.setMargins((float) 36.0, (float) 36.0, (float) 65.0, (float) 36.0);

            doc.open();

            doc.add(banner);
            pgf = new Paragraph("Name : " + Alpha.currentUser.getFistName() + " " + Alpha.currentUser.getLastName() + "                  Index number : " + Alpha.currentUser.getIndexNumber());
            pgf.setAlignment(1);
            doc.add(pgf);
            pgf = new Paragraph("Program : " + Alpha.currentUser.getProgram().getName());
            pgf.setAlignment(1);
            doc.add(pgf);
            doc.add(new Paragraph("______________________________________________________________________________"));
            pgf = new Paragraph(this.txtSemester.getText() + " Registered Course List");
            pgf.setAlignment(1);
            doc.add(pgf);
            doc.add(new Paragraph(" "));
            counter = 0;
            this.registeredCourseList.stream().forEach((course) -> {
                if (course.getStatus().equals("active")) {
                    try {
                        ++counter;
                        pgf = new Paragraph("" + counter + ".   " + "CODE: " + course.getCode() + "    " + "TITLE: " + course.getTitle() + "   " + "" + "CREDITS: " + course.getCredit() + " hrs.  " + "LECTURER: " + course.getLecturer());
                        doc.add(pgf);
                        doc.add(new Paragraph(" "));
                    } catch (DocumentException ex) {
                        Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            doc.addAuthor("JCyberSolutions 2015 Contact: 0242525727");
            doc.addSubject("Course Registration Report");
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("                                                                                                              SIGN..................... "));
//            doc.add(new Paragraph("                                                                                                              " + sdf.format(date) + "."));
            doc.close();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Report Feedback");
            alert.setHeaderText("Report created successfully");
            alert.setContentText("The report is stored at " + Alpha.currentUser.getOutputDir() + "\nYou are about to open the report created");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + title);
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().setContentText(e.getMessage());
            alert.getDialogPane().setHeaderText("Report Error");
            alert.initOwner(Alpha.stage);
            alert.show();
        }
    }

    @FXML
    private void expandPane(MouseEvent event) {
//        this.sidePane.setExpanded(true);
    }

    @FXML
    private void launchAboutHelp(ActionEvent event) {
        Alpha.startAbout(Alpha.stage);
    }

    private void comfirmCourseRegistration(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setContentText("Course registration successful");
        alert.getDialogPane().setHeaderText("Course Registration");
        alert.initOwner(Alpha.stage);
        alert.show();
    }

    @FXML
    private void startInfo(ActionEvent event) {
        Alpha.startInfo();
    }
    
    
    @FXML
    private void addCourse(ActionEvent event) {
        if (this.isValidInputs()) {
            try {
                this.entitymanager.getTransaction().begin();
                this.entitymanager.persist(setCourse(new CourseRegistry()));
                this.entitymanager.getTransaction().commit();
                
                Data thisItem = new Data().createCourseItem(setCourse(new CourseRegistry()), this.txtLecturer.getText());
                this.persistCourseRead(thisItem);
                this.setCheckboxAction(thisItem);
                this.tblCourses.getItems().add(thisItem);
                this.clearInputs();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Course Registry Error");
                alert.setContentText(e.getLocalizedMessage());
                alert.show();
            }
        }
    }

    @FXML
    private void clearInputs(ActionEvent event) {
        this.clearInputs();
    }

    @FXML
    private void updateCourse(ActionEvent event) {
        if (isValidInputs()) {
            try {
                this.entitymanager.getTransaction().begin();
                this.entitymanager.merge(setCourse(this.courseRegistryItem));
                this.entitymanager.getTransaction().commit();

                Data thisItem = new Data().createCourseItem(this.courseRegistryItem, this.txtLecturer.getText());
                this.persistCourseRead(thisItem);
                this.setCheckboxAction(thisItem);
                this.tblCourses.getItems().add(thisItem);
                this.clearInputs();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Course Registry Update Error");
                alert.setContentText(e.getLocalizedMessage());
                alert.show();
            }
        }
    }

    private CourseRegistry setCourse(CourseRegistry thisCourse) {
        try {
            if (thisCourse.getCode().isEmpty()) ;
        } catch (Exception e) {
            thisCourse.setCode(this.txtCourseCode.getText().trim());
        }
        thisCourse.setTitle(this.txtCourseTitle.getText().trim());
        thisCourse.setCredit(Integer.parseInt(this.cmbCreditHours.getSelectionModel().getSelectedItem()));
        return thisCourse;
    }

    private boolean isValidInputs() {
        if (this.txtCourseCode.getText().isEmpty() || this.txtCourseCode.getText().toCharArray().length < 6) {
            Notifications.create().title("Incomplete input").text("Please supply a valid course code").showWarning();
            return false;
        } else if (this.txtCourseTitle.getText().isEmpty()) {
            Notifications.create().title("Incomplete input").text("Please supply a valid course title").showWarning();
            return false;
        } else if (this.cmbCreditHours.getSelectionModel().getSelectedIndex() == -1) {
            Notifications.create().title("Incomplete input").text("Please specify credit hours").showWarning();
            return false;
        }
        return true;
    }

    private void clearInputs() {
        this.txtCourseCode.clear();
        this.txtCourseTitle.clear();
        this.txtLecturer.clear();
        this.btnUpdateCourse.setDisable(true);
        this.btnAddCourse.setDisable(false);
        this.tblCourses.getSelectionModel().clearSelection();
        this.cmbCreditHours.getSelectionModel().select(-1);
    }

    private void populateCredits() {
        this.cmbCreditHours.getItems().add("3");
        this.cmbCreditHours.getItems().add("6");
        this.cmbCreditHours.getItems().add("2");
    }
}
