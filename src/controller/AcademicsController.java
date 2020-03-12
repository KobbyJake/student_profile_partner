/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import static controller.ReportMaker.sdf;
import java.awt.Dimension;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javax.persistence.Query;
import model.CourseRead;
import model.Event;
import model.Location;
import model.Semester;
import view.Alpha;
import view.ScreensController;

/**
 * FXML Controller class
 *
 * @author Jake
 */
public class AcademicsController implements Initializable, ControlledScreen {

    public Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    public double width = screenSize.getWidth(), height = screenSize.getHeight();
    ScreensController myController;
    @FXML
    private MenuItem walletMenuItem;
    private Paragraph pgf;
    private String title;
    @FXML
    private Button btnReportQuizzes;
    @FXML
    private Button btnReportExam;

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        baseContainer.setPrefSize(width - (width * 0.5 / 100), height - (height * 4 / 100));
    }
    private String semesterMarker = "";
    private String userMarker = "";
    private ObservableList<Event> eventData;
    private Semester currentSemester;
    private Event currentEvent;
    public static boolean refresh = true;
    javax.persistence.EntityManager entitymanager;

    SimpleDateFormat sfm = new SimpleDateFormat("M/d/yyyy");
    SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd, yyyy");
    @FXML
    private ComboBox<String> cboExassTitle;
    @FXML
    private RadioButton rdtQuiz;
    @FXML
    private RadioButton rdtExamination;
    @FXML
    private ComboBox<String> cboExassVenue;
    @FXML
    private DatePicker cboExassDate;
    @FXML
    private ComboBox<String> cboExassTime;
    @FXML
    private TextArea txaExassNote;
    @FXML
    private Button btnSaveExass;
    @FXML
    private Button btnUpdateExass;
    @FXML
    private Button btnDeleteExass;
    @FXML
    private ComboBox<String> cboAssignmentTitle;
    @FXML
    private RadioButton rdtIndividual;
    @FXML
    private RadioButton rdtGrouped;
    @FXML
    private DatePicker cboAssignmentDate;
    @FXML
    private ComboBox<String> cboAssignmentTime;
    @FXML
    private TextArea txaAssignmentNote;
    @FXML
    private Button btnSaveAssignment;
    @FXML
    private Button btnUpdateAssignment;
    @FXML
    private Button btnReportAssignment;
    @FXML
    private Button btnDeleteAssignment;
    @FXML
    private ComboBox<String> cboOutputFilter;

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
    private TitledPane exassInput;
    @FXML
    private ToggleGroup QuizOrAssignment;
    @FXML
    private TitledPane assignmentInput;
    @FXML
    private ToggleGroup assigmentType;
    @FXML
    private ComboBox<String> cboCurrentSemester;
    @FXML
    private TableView<Event> tblEvent;
    private ComboBox<String> cboEventFilter;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        entitymanager = DBConnection.getEntityManger();
        this.loadCombos();
        this.loadTime();
        this.setLocations();
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
    private void loadSemesterSchedule(ActionEvent event) {
        this.loadEventTable();
    }

    @FXML
    private void resetPage(MouseEvent event) {
        this.semesterMarker = "";
    }

    @FXML
    private void LoadPageData(MouseEvent event) {
        getSemesterDetails();
    }

    private void loadCombos() {
        this.cboOutputFilter.getItems().add("All categories");
        this.cboOutputFilter.getItems().add("Assignment");
        this.cboOutputFilter.getItems().add("Examination");
        this.cboOutputFilter.getItems().add("Quiz");
        this.cboOutputFilter.getSelectionModel().select(0);
    }

    @FXML
    private void saveExass(ActionEvent event) {
        String source;
        if (rdtExamination.isSelected()) {
            source = "Examination";
        } else {
            source = "Quiz";
        }
        try {
            this.entitymanager.getTransaction().begin();
            this.entitymanager.persist(this.setExass(new Event(), source));
            this.entitymanager.getTransaction().commit();
            this.loadEventTable();
            clearEventInput();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Quiz and Examination record error");
            alert.setContentText(e.getLocalizedMessage());
            alert.initOwner(Alpha.stage); alert.show();
        }
    }

    @FXML
    private void updateExass(ActionEvent event) {
        if (this.currentEvent == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Quiz and Examination record error");
            alert.setContentText("Sorry, no record is currently selected to be updated. Please select an entry.");
            alert.initOwner(Alpha.stage); alert.show();
        } else {
            String source;
            if (rdtExamination.isSelected()) {
                source = "Examination";
            } else {
                source = "Quiz";
            }
            try {
                this.entitymanager.getTransaction().begin();
                this.entitymanager.merge(this.setExass(this.currentEvent, source));
                this.entitymanager.getTransaction().commit();
                this.loadEventTable();
                clearEventInput();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Quiz and Examination update error");
                alert.setContentText(e.getLocalizedMessage());
                alert.initOwner(Alpha.stage); alert.show();
            }

        }
    }

    private void ReportExass(ActionEvent event) {

        com.itextpdf.text.Image banner = null;

        if (Alpha.currentUser.getOutputDir().length() < 1) {
            if (cboOutputFilter.getSelectionModel().getSelectedItem().equals("Examination")) {
                title = this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Examination list.pdf";
                pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Examination List");
            } else if (cboOutputFilter.getSelectionModel().getSelectedItem().equals("Quiz")) {
                title = this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Quizzes list.pdf";
                pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Quizzes List");
            } else {
                title = this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Quizzes and Examination list.pdf";
                pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + "Quizzes and Examination List");
            }
        } else {
            if (cboOutputFilter.getSelectionModel().getSelectedItem().equals("Examination")) {
                title = Alpha.currentUser.getOutputDir() + this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Examination list.pdf";
                pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Examination List");
            } else if (cboOutputFilter.getSelectionModel().getSelectedItem().equals("Quiz")) {
                title = Alpha.currentUser.getOutputDir() + this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Quizzes list.pdf";
                pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Quizzes List");
            } else {
                title = Alpha.currentUser.getOutputDir() + this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Quizzes and Examination list.pdf";
                pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + "Quizzes and Examination List");
            }
        }

        java.util.Date date = new java.util.Date();

        try {
            new Alpha().getBanner("Examination");
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

//             if (cboOutputFilter.getSelectionModel().getSelectedItem().equals("Examination")) {
//                title = this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Examination list.pdf";
//            } else if (cboOutputFilter.getSelectionModel().getSelectedItem().equals("Quiz")) {
//                title = this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Quizzes list.pdf";
//            } else {
//                title = this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Quizzes and Examination list.pdf";
//            }
//            
//            
//            if (this.cboEventFilter.getSelectionModel().getSelectedItem().equalsIgnoreCase("Quiz")) {
//
//                pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Quizzes List");
//            } else if (this.cboEventFilter.getSelectionModel().getSelectedItem().equalsIgnoreCase("Examination")) {
//
//                
//            } else {
//                pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + "Quizzes and Examination List");
//
//            }
//
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.getDialogPane().setContentText("Hi");
//            alert.getDialogPane().setHeaderText("Report Error");
//            alert.initOwner(Alpha.stage); alert.show();
            pgf.setAlignment(1);
            doc.add(pgf);
            doc.add(new Paragraph(" "));
//            int counter = 0;
//            this.tblEvent.getItems().stream().forEach((item) -> {
//                if (item.getType().equals("Assignment")) {
//                    try {
//                        ++counter;
//                        pgf = new Paragraph("" + counter + ".   " + "CODE: " + course.getCode() + "    " + "TITLE: " + course.getTitle() + "   " + "" + "CREDITS: " + course.getCredit() + " hrs.  " + "LECTURER: " + course.getLecturer());
//                        doc.add(pgf);
//                        doc.add(new Paragraph(" "));
//                    } catch (DocumentException ex) {
//                        Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            });

            PdfPTable pTable = new PdfPTable(5);
            pTable.addCell("Title");
            pTable.addCell("Type");
            pTable.addCell("Date");
            pTable.addCell("Time");
            pTable.addCell("Remarks");
            doc.add(new Paragraph(""));

            this.tblEvent.getItems().stream().forEach((item) -> {
                String[] parts = item.getNote().split("::");
                switch (this.cboOutputFilter.getSelectionModel().getSelectedIndex()) {
                    case 0:

                        if (!item.getType().equalsIgnoreCase("Assignment")) {
                            pTable.addCell(item.getTitle());
                            parts = item.getNote().split("::");
                            pTable.addCell(parts[0]);
                            try {
                                pTable.addCell(this.sdf1.format(item.getDate()));
                            } catch (ParseException ex) {
                                Logger.getLogger(AcademicsController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            pTable.addCell(item.getTime());
                            pTable.addCell(parts[1]);
                        }
                        break;
                    case 1:

                        break;
                    case 2:
//                        if (item.getType().contains("Examination")) {
//                        pTable.addCell(item.getTitle());
//                        String[] parts = item.getNote().split("::");
//                        pTable.addCell(parts[0]);
//                        try {
//                            pTable.addCell(this.sdf1.format(item.getDate()));
//                        } catch (ParseException ex) {
//                            Logger.getLogger(AcademicsController.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        pTable.addCell(item.getTime());
//                        pTable.addCell(parts[1]);
//                    }

                        pTable.addCell(item.getTitle());
                        parts = item.getNote().split("::");
                        pTable.addCell(parts[0]);
                        try {
                            pTable.addCell(this.sdf1.format(item.getDate()));
                        } catch (ParseException ex) {
                            Logger.getLogger(AcademicsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        pTable.addCell(item.getTime());
                        pTable.addCell(parts[1]);
                        break;
                    case 3:
//                         if (item.getType().contains("Quiz")) {
//                        pTable.addCell(item.getTitle());
//                        String[] parts = item.getNote().split("::");
//                        pTable.addCell(parts[0]);
//                        try {
//                            pTable.addCell(this.sdf1.format(item.getDate()));
//                        } catch (ParseException ex) {
//                            Logger.getLogger(AcademicsController.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        pTable.addCell(item.getTime());
//                        pTable.addCell(parts[1]);
//                        
//                        

                        pTable.addCell(item.getTitle());

                        pTable.addCell(parts[0]);
                        try {
                            pTable.addCell(this.sdf1.format(item.getDate()));
                        } catch (ParseException ex) {
                            Logger.getLogger(AcademicsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        pTable.addCell(item.getTime());
                        pTable.addCell(parts[1]);
//                    }
                        break;
                }
//                if (cboOutputFilter.getSelectionModel().getSelectedItem().equals("Examination")) {
//                    
//                } else if (cboOutputFilter.getSelectionModel().getSelectedItem().equals("Quiz")) {
//                   
//                } else {
//                    
//                }

            });
            doc.add(pTable);
            doc.addAuthor("JCyberSolutions 2015 Contact: 0242525727");
            doc.addSubject("Quizzes and Examinations Log");
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("                                                                                                              SIGN..................... "));
            doc.add(new Paragraph("                                                                                                              " + sdf.format(date) + "."));
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
            alert.initOwner(Alpha.stage); alert.show();
        }

    }

    @FXML
    private void deleteExass(ActionEvent event) {
        if (this.currentEvent == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Quiz and Examination record error");
            alert.setContentText("Sorry, no record is currently selected to be deleted. Please select an entry.");
            alert.initOwner(Alpha.stage); alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Confirm delete action");
            alert.setContentText("You are about to delete the selected record.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    this.entitymanager.getTransaction().begin();
                    this.entitymanager.remove(this.currentEvent);
                    this.entitymanager.getTransaction().commit();
                    this.loadEventTable();
                    clearEventInput();
                } catch (Exception e) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Event delete error.");
                    alert.setContentText(e.getLocalizedMessage());
                    alert.initOwner(Alpha.stage); alert.show();
                    this.entitymanager.getTransaction().rollback();
                }
            }
        }
    }

    @FXML
    private void saveAssignment(ActionEvent event) {
        try {
            this.entitymanager.getTransaction().begin();
            this.entitymanager.persist(this.setExass(new Event(), "Assignment"));
            this.entitymanager.getTransaction().commit();
            this.loadEventTable();
            clearEventInput();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Quiz and Examination record error");
            alert.setContentText(e.getLocalizedMessage());
            alert.initOwner(Alpha.stage); alert.show();
        }

    }

    @FXML
    private void updateAssignment(ActionEvent event) {
        if (this.currentEvent == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Assignment update error");
            alert.setContentText("Sorry, no record is currently selected to be updated. Please select an entry.");
            alert.initOwner(Alpha.stage); alert.show();
        } else {
            try {
                this.entitymanager.getTransaction().begin();
                this.entitymanager.merge(this.setExass(this.currentEvent, "Assignment"));
                this.entitymanager.getTransaction().commit();
                this.loadEventTable();
                clearEventInput();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Assignment update error");
                alert.setContentText(e.getLocalizedMessage());
                alert.initOwner(Alpha.stage); alert.show();
            }

        }
    }

    @FXML
    private void reportAssignment(ActionEvent event) {
        com.itextpdf.text.Image banner = null;

        String title;
        if (Alpha.currentUser.getOutputDir().length() < 1) {
            title = this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Assignment list.pdf";
        } else {
            title = Alpha.currentUser.getOutputDir() + this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Assignment list.pdf";
        }

        java.util.Date date = new java.util.Date();

        try {
            new Alpha().getBanner("Assignment");
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
            pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Assignment List");
            pgf.setAlignment(1);
            doc.add(pgf);
            doc.add(new Paragraph(" "));
//            int counter = 0;
//            this.tblEvent.getItems().stream().forEach((item) -> {
//                if (item.getType().equals("Assignment")) {
//                    try {
//                        ++counter;
//                        pgf = new Paragraph("" + counter + ".   " + "CODE: " + course.getCode() + "    " + "TITLE: " + course.getTitle() + "   " + "" + "CREDITS: " + course.getCredit() + " hrs.  " + "LECTURER: " + course.getLecturer());
//                        doc.add(pgf);
//                        doc.add(new Paragraph(" "));
//                    } catch (DocumentException ex) {
//                        Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            });

            PdfPTable pTable = new PdfPTable(5);
            pTable.addCell("Title");
            pTable.addCell("Type");
            pTable.addCell("Date");
            pTable.addCell("Time");
            pTable.addCell("Remarks");
            doc.add(new Paragraph(""));
            this.eventData.stream().forEach((item) -> {
                if (item.getType().equalsIgnoreCase("Assignment")) {
                    pTable.addCell(item.getTitle());
                    String[] parts = item.getNote().split("::");
                    pTable.addCell(parts[0]);
                    try {
                        pTable.addCell(this.sdf1.format(item.getDate()));
                    } catch (ParseException ex) {
                        Logger.getLogger(AcademicsController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    pTable.addCell(item.getTime());
                    pTable.addCell(parts[1]);
                }

            });
            doc.add(pTable);
            doc.addAuthor("JCyberSolutions 2015 Contact: 0242525727");
            doc.addSubject("Assignment Log");
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("                                                                                                              SIGN..................... "));
            doc.add(new Paragraph("                                                                                                              " + sdf.format(date) + "."));
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
            alert.initOwner(Alpha.stage); alert.show();
        }
    }

    @FXML
    private void deleteAssignment(ActionEvent event) {
        if (this.currentEvent == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Assignment delete error");
            alert.setContentText("Sorry, no record is currently selected to be deleted. Please select an entry.");
            alert.initOwner(Alpha.stage); alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Confirm delete action");
            alert.setContentText("You are about to delete the selected record.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    this.entitymanager.getTransaction().begin();
                    this.entitymanager.remove(this.currentEvent);
                    this.entitymanager.getTransaction().commit();
                    this.loadEventTable();
                    clearEventInput();
                } catch (Exception e) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Assignment delete error.");
                    alert.setContentText(e.getLocalizedMessage());
                    alert.initOwner(Alpha.stage); alert.show();
                    this.entitymanager.getTransaction().rollback();
                }
            }
        }
    }

    private void reportExamination() {
        com.itextpdf.text.Image banner = null;

        String title;
        if (Alpha.currentUser.getOutputDir().length() < 1) {
            title = this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Examination Schedule.pdf";
        } else {
            title = Alpha.currentUser.getOutputDir() + this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Examination Schedule.pdf";
        }

        java.util.Date date = new java.util.Date();

        try {
            new Alpha().getBanner("Examination");
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
            pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Examination Schedule");
            pgf.setAlignment(1);
            doc.add(pgf);
            doc.add(new Paragraph(" "));
            PdfPTable pTable = new PdfPTable(5);
            pTable.addCell("Title");
            pTable.addCell("Date");
            pTable.addCell("Time");
            pTable.addCell("Venue");
            pTable.addCell("Remarks");
            doc.add(new Paragraph(""));

            this.eventData.stream().forEach((item) -> {
                if (item.getType().equalsIgnoreCase("Examination")) {
                    pTable.addCell(item.getTitle());
                    try {
                        pTable.addCell(this.sdf1.format(item.getDate()));
                    } catch (ParseException ex) {
                        Logger.getLogger(AcademicsController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    pTable.addCell(item.getTime());
                    pTable.addCell(item.getVenue());
                    pTable.addCell(item.getNote());
                }

            });
            doc.add(pTable);
            doc.addAuthor("JCyberSolutions 2015 Contact: 0242525727");
            doc.addSubject("Examination Schedule");
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("                                                                                                              SIGN..................... "));
            doc.add(new Paragraph("                                                                                                              " + sdf.format(date) + "."));
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
            alert.initOwner(Alpha.stage); alert.show();
        }

    }

    @FXML
    private void reportQuizzes() {
        com.itextpdf.text.Image banner = null;

        String title;
        if (Alpha.currentUser.getOutputDir().length() < 1) {
            title = this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Quizzes Log.pdf";
        } else {
            title = Alpha.currentUser.getOutputDir() + this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Quizzes Log.pdf";
        }

        java.util.Date date = new java.util.Date();

        try {
            new Alpha().getBanner("Quiz");
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
            pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Quizzes Log");
            pgf.setAlignment(1);
            doc.add(pgf);
            doc.add(new Paragraph(" "));
            PdfPTable pTable = new PdfPTable(5);
            pTable.addCell("Title");
            pTable.addCell("Date");
            pTable.addCell("Time");
            pTable.addCell("Venue");
            pTable.addCell("Remarks");
            doc.add(new Paragraph(""));
            this.eventData.stream().forEach((item) -> {
                if (item.getType().equalsIgnoreCase("Quiz")) {
                    pTable.addCell(item.getTitle());
                    try {
                        pTable.addCell(this.sdf1.format(item.getDate()));
                    } catch (ParseException ex) {
                        Logger.getLogger(AcademicsController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    pTable.addCell(item.getTime());
                    pTable.addCell(item.getVenue());
                    pTable.addCell(item.getNote());
                }

            });
            doc.add(pTable);
            doc.addAuthor("JCyberSolutions 2015 Contact: 0242525727");
            doc.addSubject("Quizzes Log");
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("                                                                                                              SIGN..................... "));
            doc.add(new Paragraph("                                                                                                              " + sdf.format(date) + "."));
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
            alert.initOwner(Alpha.stage); alert.show();
        }

    }

    @FXML
    private void filterOutput(ActionEvent event) {
        ObservableList<Event> filteredData = FXCollections.observableArrayList();
        if(this.cboOutputFilter.getSelectionModel().getSelectedIndex() == 0){
            this.tblEvent.setItems(eventData);
        }else {
            this.eventData.stream().forEach((item)->{
                if(item.getType().equals(this.cboOutputFilter.getSelectionModel().getSelectedItem())){
                    filteredData.add(item);
                }
            });
            this.tblEvent.setItems(filteredData);
        }
    }

    private void loadTime() {
        this.cboExassTime.getItems().add("06:00 AM");
        this.cboExassTime.getItems().add("06:15 AM");
        this.cboExassTime.getItems().add("06:30 AM");
        this.cboExassTime.getItems().add("06:45 AM");

        this.cboExassTime.getItems().add("07:00 AM");
        this.cboExassTime.getItems().add("07:15 AM");
        this.cboExassTime.getItems().add("07:30 AM");
        this.cboExassTime.getItems().add("07:45 AM");

        this.cboExassTime.getItems().add("08:00 AM");
        this.cboExassTime.getItems().add("08:15 AM");
        this.cboExassTime.getItems().add("08:30 AM");
        this.cboExassTime.getItems().add("08:45 AM");

        this.cboExassTime.getItems().add("09:00 AM");
        this.cboExassTime.getItems().add("09:15 AM");
        this.cboExassTime.getItems().add("09:30 AM");
        this.cboExassTime.getItems().add("09:45 AM");

        this.cboExassTime.getItems().add("10:00 AM");
        this.cboExassTime.getItems().add("10:15 AM");
        this.cboExassTime.getItems().add("10:30 AM");
        this.cboExassTime.getItems().add("10:45 AM");

        this.cboExassTime.getItems().add("11:00 AM");
        this.cboExassTime.getItems().add("11:15 AM");
        this.cboExassTime.getItems().add("11:30 AM");
        this.cboExassTime.getItems().add("11:45 AM");

        this.cboExassTime.getItems().add("12:00 PM");
        this.cboExassTime.getItems().add("12:15 PM");
        this.cboExassTime.getItems().add("12:30 PM");
        this.cboExassTime.getItems().add("12:45 PM");

        this.cboExassTime.getItems().add("01:00 PM");
        this.cboExassTime.getItems().add("01:15 PM");
        this.cboExassTime.getItems().add("01:30 PM");
        this.cboExassTime.getItems().add("01:45 PM");

        this.cboExassTime.getItems().add("02:00 PM");
        this.cboExassTime.getItems().add("02:15 PM");
        this.cboExassTime.getItems().add("02:30 PM");
        this.cboExassTime.getItems().add("02:45 PM");

        this.cboExassTime.getItems().add("03:00 PM");
        this.cboExassTime.getItems().add("03:15 PM");
        this.cboExassTime.getItems().add("03:30 PM");
        this.cboExassTime.getItems().add("03:45 PM");

        this.cboExassTime.getItems().add("04:00 PM");
        this.cboExassTime.getItems().add("04:15 PM");
        this.cboExassTime.getItems().add("04:30 PM");
        this.cboExassTime.getItems().add("04:45 PM");

        this.cboExassTime.getItems().add("05:00 PM");
        this.cboExassTime.getItems().add("05:15 PM");
        this.cboExassTime.getItems().add("05:30 PM");
        this.cboExassTime.getItems().add("05:45 PM");

        this.cboExassTime.getItems().add("06:00 PM");
        this.cboExassTime.getItems().add("06:15 PM");
        this.cboExassTime.getItems().add("06:30 PM");
        this.cboExassTime.getItems().add("06:45 PM");

        this.cboExassTime.getItems().add("07:00 PM");
        this.cboExassTime.getItems().add("07:15 PM");
        this.cboExassTime.getItems().add("07:30 PM");
        this.cboExassTime.getItems().add("07:45 PM");

        this.cboExassTime.getItems().add("08:00 PM");
        this.cboExassTime.getItems().add("08:15 PM");
        this.cboExassTime.getItems().add("08:30 PM");
        this.cboExassTime.getItems().add("08:45 PM");

        this.cboExassTime.getItems().add("09:00 PM");
        this.cboExassTime.getItems().add("09:15 PM");
        this.cboExassTime.getItems().add("09:30 PM");
        this.cboExassTime.getItems().add("09:45 PM");

        this.cboExassTime.getItems().add("10:00 PM");
        this.cboExassTime.getItems().add("10:15 PM");
        this.cboExassTime.getItems().add("10:30 PM");
        this.cboExassTime.getItems().add("10:45 PM");

        this.cboExassTime.getItems().add("11:00 PM");
        this.cboExassTime.getItems().add("11:15 PM");
        this.cboExassTime.getItems().add("11:30 PM");
        this.cboExassTime.getItems().add("11:45 PM");

        this.cboExassTime.getItems().add("12:00 AM");
        this.cboExassTime.getItems().add("12:15 AM");
        this.cboExassTime.getItems().add("12:30 AM");
        this.cboExassTime.getItems().add("12:45 AM");

        this.cboExassTime.getItems().add("1:00 AM");
        this.cboExassTime.getItems().add("1:15 AM");
        this.cboExassTime.getItems().add("1:30 AM");
        this.cboExassTime.getItems().add("1:45 AM");

        this.cboExassTime.getItems().add("2:00 AM");
        this.cboExassTime.getItems().add("2:15 AM");
        this.cboExassTime.getItems().add("2:30 AM");
        this.cboExassTime.getItems().add("2:45 AM");

        this.cboExassTime.getItems().add("3:00 AM");
        this.cboExassTime.getItems().add("3:15 AM");
        this.cboExassTime.getItems().add("3:30 AM");
        this.cboExassTime.getItems().add("3:45 AM");

        this.cboExassTime.getItems().add("4:00 AM");
        this.cboExassTime.getItems().add("4:15 AM");
        this.cboExassTime.getItems().add("4:30 AM");
        this.cboExassTime.getItems().add("4:45 AM");

        this.cboExassTime.getItems().add("5:00 AM");
        this.cboExassTime.getItems().add("5:15 AM");
        this.cboExassTime.getItems().add("5:30 AM");
        this.cboExassTime.getItems().add("5:45 AM");

        this.cboAssignmentTime.getItems().addAll(this.cboExassTime.getItems());
        this.cboExassTime.getSelectionModel().select("7:00 AM");
        this.cboAssignmentTime.getSelectionModel().select("7:00 AM");
        this.cboExassDate.setValue(LocalDate.now());
        this.cboAssignmentDate.setValue(LocalDate.now());
    }

    private void getSemesterDetails() {
        //retrieving current semester
        if (refresh) {
            refresh = false;
            this.cboAssignmentTitle.getItems().clear();
            this.cboCurrentSemester.getSelectionModel().select(Alpha.currentUser.getCurrentSem());
            try {
                Query query = this.entitymanager.createQuery("SELECT c FROM CourseRead c WHERE C.student = ?1 AND c.semester = ?2 ");
                query.setParameter(1, Alpha.currentUser);
                query.setParameter(2, this.cboCurrentSemester.getSelectionModel().getSelectedItem());
                query.getResultList().stream().forEach((item) -> {
                    this.cboAssignmentTitle.getItems().add(((CourseRead) item).getTitle());
                });
                this.cboExassTitle.getItems().clear();
                this.cboExassTitle.getItems().addAll(this.cboAssignmentTitle.getItems());

                this.cboCurrentSemester.getItems().clear();
                query = this.entitymanager.createQuery("SELECT s FROM Semester s WHERE s.studentID = ?1");
                query.setParameter(1, Alpha.currentUser);
                query.getResultList().stream().forEach((semester) -> {
                    Semester currentSem = (Semester) semester;
                    this.cboCurrentSemester.getItems().add(currentSem.getName());
                    if (currentSem.getName().equals(this.cboCurrentSemester.getSelectionModel().getSelectedItem())) {
                        this.currentSemester = currentSem;
                    }
                });
                this.cboCurrentSemester.getSelectionModel().select(Alpha.currentUser.getCurrentSem());
                this.loadEventTable();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.setHeaderText("Error retrieving existing semester course list");
                alert.initOwner(Alpha.stage); alert.show();
            }
            this.userMarker = Alpha.currentUser.getUserName();
            this.semesterMarker = Alpha.currentUser.getCurrentSem();
            
            
        }
    }

    private void loadEventTable() {
        Query query = this.entitymanager.createQuery("SELECT s FROM Semester s WHERE s.studentID = ?1 AND s.name = ?2");
        query.setParameter(1, Alpha.currentUser);
        query.setParameter(2, this.cboCurrentSemester.getSelectionModel().getSelectedItem());
        if (!query.getResultList().isEmpty()) {
            this.currentSemester = (Semester) query.getSingleResult();
        }
        this.eventData = FXCollections.observableArrayList();
        try {
            query = this.entitymanager.createQuery("SELECT e FROM Event e WHERE e.semesterID = ?1 AND e.type IN ('Assignment','Quiz','Examination')");
            query.setParameter(1, this.currentSemester);
            query.getResultList().stream().forEach((event) -> {
                this.eventData.add((Event) event);
            });
            this.tblEvent.setItems(eventData);
            this.setEventTableColumns();
            this.tblEvent.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Event> obs, Event oldSelection, Event newSelection) -> {

                if (newSelection != null) {
                    this.currentEvent = newSelection;
                    if (newSelection.getType().equals("Assignment")) {
                        this.assignmentInput.setExpanded(true);
                        this.cboAssignmentTitle.getEditor().setText(newSelection.getTitle());
                        try {
                            this.cboAssignmentDate.getEditor().setText(sfm.format(newSelection.getDate()));
                        } catch (ParseException ex) {
                            Logger.getLogger(AcademicsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        this.cboAssignmentTime.getSelectionModel().select(newSelection.getTime());
                        String[] parts = newSelection.getNote().split("::");
                        if (parts[0].equals("Individual")) {
                            this.rdtIndividual.setSelected(true);
                        } else {
                            this.rdtGrouped.setSelected(true);
                        }
                        this.txaAssignmentNote.setText(parts[1]);
                    } else {
                        this.exassInput.setExpanded(true);
                        this.cboExassTitle.getEditor().setText(newSelection.getTitle());
                        try {
                            this.cboExassTime.getEditor().setText(sfm.format(newSelection.getDate()));
                        } catch (ParseException ex) {
                            Logger.getLogger(AcademicsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        this.cboExassVenue.getSelectionModel().select(newSelection.getTime());
                        if (newSelection.getType().equals("Quiz")) {
                            this.rdtQuiz.setSelected(true);
                        } else {
                            this.rdtExamination.setSelected(true);
                        }
                        this.cboExassVenue.getEditor().setText(newSelection.getVenue());
                        this.txaExassNote.setText(newSelection.getNote());
                    }
                }

            });
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(e.getLocalizedMessage());
            alert.setHeaderText("Error preparing academics' table");
            alert.initOwner(Alpha.stage); alert.show();
        }
    }

    private void setEventTableColumns() {
        double netWidth = (this.tblEvent.getWidth() - (this.tblEvent.getWidth() * 0.75 / 100));
        tblEvent.getColumns().clear();

        TableColumn<Event, String> column12 = new TableColumn();
        column12.setVisible(true);
        column12.setText("NAME");
        column12.setCellValueFactory(new PropertyValueFactory("title"));
        column12.setPrefWidth(netWidth / 4);
        this.tblEvent.getColumns().add(column12);

        TableColumn<Event, String> column13 = new TableColumn();
        column13.setVisible(true);
        column13.setText("VENUE");
        column13.setCellValueFactory(new PropertyValueFactory("venue"));
        column13.setPrefWidth(netWidth / 4);
        this.tblEvent.getColumns().add(column13);

        TableColumn<Event, String> column16 = new TableColumn();
        column16.setVisible(true);
        column16.setText("DATE");
        column16.setCellValueFactory(new PropertyValueFactory("date"));
        column16.setPrefWidth(netWidth / 6);
        this.tblEvent.getColumns().add(column16);

        TableColumn<Event, String> column17 = new TableColumn();
        column17.setVisible(true);
        column17.setText("TIME");
        column17.setCellValueFactory(new PropertyValueFactory("time"));
        column17.setPrefWidth(netWidth / 6);
        this.tblEvent.getColumns().add(column17);

        TableColumn<Event, String> column18 = new TableColumn();
        column18.setVisible(true);
        column18.setText("CATEGORY");
        column18.setCellValueFactory(new PropertyValueFactory("type"));
        column18.setPrefWidth(netWidth / 6);
        this.tblEvent.getColumns().add(column18);
    }

    private Event setExass(Event event, String source) throws ParseException {
        event.setSemesterID(currentSemester);
        event.setType(source);
        if (source.equals("Assignment")) {
            if (this.isValidAssignmentInput()) {
                if (event.getId() == null) {
                    String code;
                    if (this.cboAssignmentTitle.getEditor().getText().length() > 4) {
                        code = this.cboAssignmentTitle.getEditor().getText().substring(0, 4);
                    } else {
                        code = this.cboAssignmentTitle.getEditor().getText();
                    }
                    Random rand = new Random(System.currentTimeMillis());
                    event.setId(code + rand.nextInt(99));
                }
                event.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(this.cboAssignmentDate.getValue().toString()));
                event.setTime(this.cboAssignmentTime.getSelectionModel().getSelectedItem());
                event.setTitle(this.cboAssignmentTitle.getEditor().getText());
                if (this.rdtGrouped.isSelected()) {
                    event.setNote(this.rdtGrouped.getText() + "::" + this.txaAssignmentNote.getText());
                } else {
                    event.setNote(this.rdtIndividual.getText() + "::" + this.txaAssignmentNote.getText());
                }
            }
        } else {
            if (this.isValidExassInput()) {
                
                if (event.getId() == null) {
                    String code;
                    if (this.cboExassTitle.getEditor().getText().length() > 4) {
                        code = this.cboExassTitle.getEditor().getText().substring(0, 4);
                    } else {
                        code = this.cboExassTitle.getEditor().getText();
                    }
                    Random rand = new Random(System.currentTimeMillis());
                    event.setId(code + rand.nextInt(99));
                }
                
                event.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(this.cboExassDate.getValue().toString()));
                event.setVenue(this.cboExassVenue.getEditor().getText());
                event.setTime(this.cboExassTime.getSelectionModel().getSelectedItem());
                event.setTitle(this.cboExassTitle.getEditor().getText());
                event.setNote(this.txaExassNote.getText());
            }
        }

        return event;
    }

    private boolean isValidAssignmentInput() {
        boolean status = false;
        if (this.cboAssignmentTitle.getEditor().getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please supply input for the title of this record");
            alert.setHeaderText("Incomplete input");
            alert.initOwner(Alpha.stage); alert.show();
        } else {
            status = true;
        }
        return status;
    }

    private boolean isValidExassInput() {
        boolean status = false;
        if (this.cboExassTitle.getEditor().getText().isEmpty() || this.cboExassVenue.getEditor().getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please supply input for the title and venue of this record");
            alert.setHeaderText("Incomplete input");
            alert.initOwner(Alpha.stage); alert.show();
        } else {
            status = true;
        }
        return status;
    }

    private void clearEventInput() {
        this.cboAssignmentDate.setValue(LocalDate.now());
        this.cboExassDate.setValue(LocalDate.now());
        this.cboAssignmentTime.getSelectionModel().select("07:00 AM");
        this.cboExassTime.getSelectionModel().select("07:00 AM");
        this.cboAssignmentTitle.getEditor().setText("");
        this.cboExassTitle.getEditor().setText("");
        this.cboExassVenue.getEditor().setText("");
        this.rdtIndividual.setSelected(true);
        this.rdtExamination.setSelected(true);

    }

    private void setLocations() {
        Query query = entitymanager.createQuery("SELECT l FROM Location l WHERE l.type LIKE 'venue' AND l.campus LIKE " + "'" + DBConnection.campus + "'");
        List<Location> locationList = query.getResultList();
        locationList.stream().forEach((venue) -> {
            this.cboExassVenue.getItems().add(venue.getName());
        });
    }

    private void reportQuizzes(ActionEvent event) {
        this.reportQuizzes();
    }

    @FXML
    private void reportExam(ActionEvent event) {
        this.reportExamination();
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
