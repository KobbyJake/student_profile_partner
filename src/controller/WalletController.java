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
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javax.persistence.Query;
import model.Event;
import model.Semester;
import view.Alpha;
import view.ScreensController;

/**
 * FXML Controller class
 *
 * @author Jake
 */
public class WalletController implements Initializable, ControlledScreen {

    public Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    public double width = screenSize.getWidth(), height = screenSize.getHeight();
    ScreensController myController;
    @FXML
    private MenuItem walletMenuItem;
    public static boolean refresh = true;
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
    javax.persistence.EntityManager entitymanager;

    SimpleDateFormat sfm = new SimpleDateFormat("M/d/yyyy");

    @FXML
    private Button btnClear;
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
    private TitledPane walletInput;
    @FXML
    private RadioButton rdtExpenditure;
    @FXML
    private ToggleGroup IncomeOrExpenditure;
    @FXML
    private RadioButton rdtIncome;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtAmount;
    @FXML
    private DatePicker cboDate;
    @FXML
    private ComboBox<String> cboTime;
    @FXML
    private TextArea txaNote;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnReport;
    @FXML
    private Button btnDelete;
    @FXML
    private ComboBox<String> cboCurrentSemester;
    @FXML
    private TableView<Event> tblEvent;
    @FXML
    private ComboBox<String> cboOutputFilter;
    @FXML
    private Label txtTotal;

    private boolean decimalPoint = false;
    private int decimalPlaces = 0;
    private double income = 0, expenditure = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        entitymanager = DBConnection.getEntityManger();
        this.loadCombos();
        this.loadTime();
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
    private void saveRecord(ActionEvent event) {
        if (isValidWalletInput()) {
            try {
                this.entitymanager.getTransaction().begin();
                this.entitymanager.persist(this.setEvent(new Event()));
                this.entitymanager.getTransaction().commit();
                clearEventInput();
                this.loadEventTable();

            } catch (Exception e) {
                this.entitymanager.getTransaction().rollback();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Entry creation error.");
                alert.setContentText(e.getLocalizedMessage());
                alert.initOwner(Alpha.stage); alert.show();
            }
        }

    }

    @FXML
    private void updateRecord(ActionEvent event) {
        if (isValidWalletInput()) {
            try {
                this.entitymanager.getTransaction().begin();
                this.entitymanager.merge(this.setEvent(this.currentEvent));
                this.entitymanager.getTransaction().commit();
                this.clearEventInput();
                this.loadEventTable();

            } catch (Exception e) {
                this.entitymanager.getTransaction().rollback();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Entry update error.");
                alert.setContentText(e.getLocalizedMessage());
                alert.initOwner(Alpha.stage); alert.show();
            }
        }

    }

    @FXML
    private void ReportWallet(ActionEvent event) {

        com.itextpdf.text.Image banner = null;

        String title = this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Wallet Log.pdf";

        java.util.Date date = new java.util.Date();

        try {
            new Alpha().getBanner("Wallet");
            banner = com.itextpdf.text.Image.getInstance("lib/banner.png");
            Document doc = new Document();
            PdfWriter writer;
            if (Alpha.currentUser.getOutputDir().length() < 1) {
                writer = PdfWriter.getInstance(doc, new FileOutputStream(title));
            } else {
                writer = PdfWriter.getInstance(doc, new FileOutputStream(Alpha.currentUser.getOutputDir() + File.separator + title));
            }
            Paragraph heading;
            PdfPTable incomeTable = new PdfPTable(4);
            PdfPTable expenditureTable = new PdfPTable(4);
            PdfPTable mainTable = new PdfPTable(5);
            ReportMaker report = new ReportMaker();
            writer.setPageEvent(report);
            doc.setMargins((float) 36.0, (float) 36.0, (float) 65.0, (float) 36.0);

            doc.open();

            doc.add(banner);
            Paragraph pgf = new Paragraph("Name : " + Alpha.currentUser.getFistName() + " " + Alpha.currentUser.getLastName() + "                  Index number : " + Alpha.currentUser.getIndexNumber());
            pgf.setAlignment(1);
            doc.add(pgf);
            pgf = new Paragraph("Program : " + Alpha.currentUser.getProgram().getName());
            pgf.setAlignment(1);
            doc.add(pgf);
            doc.add(new Paragraph("______________________________________________________________________________"));
             pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Wallet Log");
            pgf.setAlignment(1);
            doc.add(pgf);

            switch (this.cboOutputFilter.getSelectionModel().getSelectedItem()) {
                case "Income":
                    incomeTable.addCell("Title");
                    incomeTable.addCell("Amount GH¢");
                    incomeTable.addCell("Date");
                    incomeTable.addCell("Time");
                    this.tblEvent.getItems().stream().forEach((item) -> {
                        incomeTable.addCell(item.getTitle());
                        incomeTable.addCell(item.getAmount().toString());
                        try {
                            incomeTable.addCell(sdf.format(item.getDate()));
                        } catch (ParseException ex) {
                            Logger.getLogger(WalletController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        incomeTable.addCell(item.getTime());
                    });
                    doc.add(incomeTable);
                    doc.add(new Paragraph(" "));
                    doc.add(new Paragraph(txtTotal.getText()));
                    
                    break;
                case "Expenditure":
                    expenditureTable.addCell("Title");
                    expenditureTable.addCell("Amount GH¢");
                    expenditureTable.addCell("Date");
                    expenditureTable.addCell("Time");
                    this.tblEvent.getItems().stream().forEach((item) -> {
                        incomeTable.addCell(item.getTitle());
                        incomeTable.addCell(item.getAmount().toString());
                        try {
                            incomeTable.addCell(sdf.format(item.getDate()));
                        } catch (ParseException ex) {
                            Logger.getLogger(WalletController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        incomeTable.addCell(item.getTime());
                    });
                    doc.add(expenditureTable);
                    doc.add(new Paragraph(" "));
                    doc.add(new Paragraph(txtTotal.getText()));
                    break;
                case "Income and Expenditure":
                    mainTable.addCell("Title");
                    mainTable.addCell("Amount GH¢");
                    mainTable.addCell("Date");
                    mainTable.addCell("Time");
                    mainTable.addCell("Category");
                    this.tblEvent.getItems().stream().forEach((item) -> {
                        mainTable.addCell(item.getTitle());
                        mainTable.addCell(item.getAmount().toString());
                        try {
                            mainTable.addCell(sdf.format(item.getDate()));
                        } catch (ParseException ex) {
                            Logger.getLogger(WalletController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        mainTable.addCell(item.getTime());
                        mainTable.addCell(item.getType());
                    });
                    doc.add(mainTable);
                    doc.add(new Paragraph(" "));
                    doc.add(new Paragraph(txtTotal.getText()));
                    break;
            }
            

            doc.addAuthor("JCyberSolutions 2015 Contact: 0242525727");
            doc.addSubject("Performance Report");
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
                if (!Alpha.currentUser.getOutputDir().isEmpty()) {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + Alpha.currentUser.getOutputDir() + File.separator + title);
                } else {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + title);
                }
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().setContentText(e.getMessage());
            alert.getDialogPane().setHeaderText("Report Error");
            alert.initOwner(Alpha.stage); alert.show();
        }

    }

    @FXML
    private void deleteRecord(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Confirm delete action");
        alert.setContentText("You are about to delete the selected entry.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                this.entitymanager.getTransaction().begin();
                this.entitymanager.remove(this.currentEvent);
                this.entitymanager.getTransaction().commit();
                clearEventInput();
                this.loadEventTable();
            } catch (Exception e) {
                this.entitymanager.getTransaction().rollback();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Entry delete error.");
                alert.setContentText(e.getLocalizedMessage());
                alert.initOwner(Alpha.stage); alert.show();
            }
        }
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

    @FXML
    private void filterOutput(ActionEvent event) {
        this.loadEventTable();
    }

    @FXML
    private void checkDigit(KeyEvent event) {
        validateDigitInput(event);
    }

    private void loadCombos() {
        this.cboOutputFilter.getItems().add("Income and Expenditure");
        this.cboOutputFilter.getItems().add("Income");
        this.cboOutputFilter.getItems().add("Expenditure");
        this.cboOutputFilter.getSelectionModel().select(0);
    }

    private void loadTime() {
        this.cboTime.getItems().add("06:00 AM");
        this.cboTime.getItems().add("06:15 AM");
        this.cboTime.getItems().add("06:30 AM");
        this.cboTime.getItems().add("06:45 AM");

        this.cboTime.getItems().add("07:00 AM");
        this.cboTime.getItems().add("07:15 AM");
        this.cboTime.getItems().add("07:30 AM");
        this.cboTime.getItems().add("07:45 AM");

        this.cboTime.getItems().add("08:00 AM");
        this.cboTime.getItems().add("08:15 AM");
        this.cboTime.getItems().add("08:30 AM");
        this.cboTime.getItems().add("08:45 AM");

        this.cboTime.getItems().add("09:00 AM");
        this.cboTime.getItems().add("09:15 AM");
        this.cboTime.getItems().add("09:30 AM");
        this.cboTime.getItems().add("09:45 AM");

        this.cboTime.getItems().add("10:00 AM");
        this.cboTime.getItems().add("10:15 AM");
        this.cboTime.getItems().add("10:30 AM");
        this.cboTime.getItems().add("10:45 AM");

        this.cboTime.getItems().add("11:00 AM");
        this.cboTime.getItems().add("11:15 AM");
        this.cboTime.getItems().add("11:30 AM");
        this.cboTime.getItems().add("11:45 AM");

        this.cboTime.getItems().add("12:00 PM");
        this.cboTime.getItems().add("12:15 PM");
        this.cboTime.getItems().add("12:30 PM");
        this.cboTime.getItems().add("12:45 PM");

        this.cboTime.getItems().add("01:00 PM");
        this.cboTime.getItems().add("01:15 PM");
        this.cboTime.getItems().add("01:30 PM");
        this.cboTime.getItems().add("01:45 PM");

        this.cboTime.getItems().add("02:00 PM");
        this.cboTime.getItems().add("02:15 PM");
        this.cboTime.getItems().add("02:30 PM");
        this.cboTime.getItems().add("02:45 PM");

        this.cboTime.getItems().add("03:00 PM");
        this.cboTime.getItems().add("03:15 PM");
        this.cboTime.getItems().add("03:30 PM");
        this.cboTime.getItems().add("03:45 PM");

        this.cboTime.getItems().add("04:00 PM");
        this.cboTime.getItems().add("04:15 PM");
        this.cboTime.getItems().add("04:30 PM");
        this.cboTime.getItems().add("04:45 PM");

        this.cboTime.getItems().add("05:00 PM");
        this.cboTime.getItems().add("05:15 PM");
        this.cboTime.getItems().add("05:30 PM");
        this.cboTime.getItems().add("05:45 PM");

        this.cboTime.getItems().add("06:00 PM");
        this.cboTime.getItems().add("06:15 PM");
        this.cboTime.getItems().add("06:30 PM");
        this.cboTime.getItems().add("06:45 PM");

        this.cboTime.getItems().add("07:00 PM");
        this.cboTime.getItems().add("07:15 PM");
        this.cboTime.getItems().add("07:30 PM");
        this.cboTime.getItems().add("07:45 PM");

        this.cboTime.getItems().add("08:00 PM");
        this.cboTime.getItems().add("08:15 PM");
        this.cboTime.getItems().add("08:30 PM");
        this.cboTime.getItems().add("08:45 PM");

        this.cboTime.getItems().add("09:00 PM");
        this.cboTime.getItems().add("09:15 PM");
        this.cboTime.getItems().add("09:30 PM");
        this.cboTime.getItems().add("09:45 PM");

        this.cboTime.getItems().add("10:00 PM");
        this.cboTime.getItems().add("10:15 PM");
        this.cboTime.getItems().add("10:30 PM");
        this.cboTime.getItems().add("10:45 PM");

        this.cboTime.getItems().add("11:00 PM");
        this.cboTime.getItems().add("11:15 PM");
        this.cboTime.getItems().add("11:30 PM");
        this.cboTime.getItems().add("11:45 PM");

        this.cboTime.getItems().add("12:00 AM");
        this.cboTime.getItems().add("12:15 AM");
        this.cboTime.getItems().add("12:30 AM");
        this.cboTime.getItems().add("12:45 AM");

        this.cboTime.getItems().add("1:00 AM");
        this.cboTime.getItems().add("1:15 AM");
        this.cboTime.getItems().add("1:30 AM");
        this.cboTime.getItems().add("1:45 AM");

        this.cboTime.getItems().add("2:00 AM");
        this.cboTime.getItems().add("2:15 AM");
        this.cboTime.getItems().add("2:30 AM");
        this.cboTime.getItems().add("2:45 AM");

        this.cboTime.getItems().add("3:00 AM");
        this.cboTime.getItems().add("3:15 AM");
        this.cboTime.getItems().add("3:30 AM");
        this.cboTime.getItems().add("3:45 AM");

        this.cboTime.getItems().add("4:00 AM");
        this.cboTime.getItems().add("4:15 AM");
        this.cboTime.getItems().add("4:30 AM");
        this.cboTime.getItems().add("4:45 AM");

        this.cboTime.getItems().add("5:00 AM");
        this.cboTime.getItems().add("5:15 AM");
        this.cboTime.getItems().add("5:30 AM");
        this.cboTime.getItems().add("5:45 AM");

        this.cboTime.getSelectionModel().select("7:00 AM");
        this.cboDate.setValue(LocalDate.now());
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
                this.loadEventTable();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.setHeaderText("Error retrieving registered semester list");
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
            query = this.entitymanager.createQuery("SELECT e FROM Event e WHERE e.semesterID = ?1 AND e.type IN ('Income','Expenditure')");
            query.setParameter(1, this.currentSemester);
            this.income = 0;
            this.expenditure = 0;
            query.getResultList().stream().forEach((event) -> {
                //Sorting table data
                if (this.cboOutputFilter.getSelectionModel().getSelectedItem().equals("Income and Expenditure")) {
                    this.eventData.add((Event) event);
                } else if (this.cboOutputFilter.getSelectionModel().getSelectedItem().equals("Income")) {
                    if (((Event) event).getType().equals("Income")) {
                        this.eventData.add((Event) event);
                    }
                } else {
                    if (!((Event) event).getType().equals("Income")) {
                        this.eventData.add((Event) event);
                    }
                }
//Computing totals
                if (((Event) event).getType().equals("Income")) {
                    this.income += ((Event) event).getAmount();
                } else {
                    this.expenditure += ((Event) event).getAmount();
                }
            });

            //Displaying totals
            if (this.cboOutputFilter.getSelectionModel().getSelectedItem().equals("Income and Expenditure")) {
                txtTotal.setText("Total income:   GH¢" + this.income + "     Total expenditure:    GH¢" + this.expenditure);
            } else if (this.cboOutputFilter.getSelectionModel().getSelectedItem().equals("Income")) {
                txtTotal.setText("Total income:   GH¢" + this.income);
            } else {
                txtTotal.setText("Total expenditure:    GH¢" + this.expenditure);
            }

            this.tblEvent.setItems(eventData);
            this.setEventTableColumns();
            this.tblEvent.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Event> obs, Event oldSelection, Event newSelection) -> {

                if (newSelection != null) {
                    walletInput.setExpanded(true);
                    this.currentEvent = newSelection;
                    this.txtTitle.setText(newSelection.getTitle());
                    this.txtAmount.setText("" + newSelection.getAmount());
                    try {
                        this.cboDate.getEditor().setText(sfm.format(newSelection.getDate()));
                    } catch (ParseException ex) {
                        Logger.getLogger(WalletController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.cboTime.getSelectionModel().select(newSelection.getTime());

                    if (newSelection.getType().equals("Income")) {
                        this.rdtIncome.setSelected(true);
                    } else {
                        this.rdtExpenditure.setSelected(true);
                    }
                    this.txaNote.setText(newSelection.getNote());

                }

            });
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(e.getLocalizedMessage());
            alert.setHeaderText("Error preparing Wallet table");
            alert.initOwner(Alpha.stage); alert.show();
        }

    }

    private void setEventTableColumns() {
        double netWidth = (this.tblEvent.getWidth() - (this.tblEvent.getWidth() * 0.75 / 100));
        tblEvent.getColumns().clear();

        TableColumn<Event, String> column12 = new TableColumn();
        column12.setVisible(true);
        column12.setText("TITLE");
        column12.setCellValueFactory(new PropertyValueFactory("title"));
        column12.setPrefWidth(netWidth / 4);
        this.tblEvent.getColumns().add(column12);

        TableColumn<Event, String> column13 = new TableColumn();
        column13.setVisible(true);
        column13.setText("AMOUNT GH¢");
        column13.setCellValueFactory(new PropertyValueFactory("amount"));
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

    private Event setEvent(Event event) {
        if (event.getId() == null) {
                    String code;
                    if (this.txtTitle.getText().length() > 4) {
                        code = this.txtTitle.getText().substring(0, 4);
                    } else {
                        code = this.txtTitle.getText();
                    }
                    Random rand = new Random(System.currentTimeMillis());
                    event.setId(code + rand.nextInt(99));
                }
        event.setSemesterID(currentSemester);
        if (this.rdtIncome.isSelected()) {
            event.setType("Income");
        } else {
            event.setType("Expenditure");
        }
        event.setTitle(this.txtTitle.getText());
        event.setAmount(Double.parseDouble(this.txtAmount.getText()));
        event.setDate(Date.valueOf(this.cboDate.getValue().toString()));
        event.setTime(this.cboTime.getSelectionModel().getSelectedItem());
        event.setNote(this.txaNote.getText());

        return event;
    }

    private boolean isValidWalletInput() {
        boolean status = false;
        if (this.txtTitle.getText().isEmpty() || this.txtAmount.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please supply inputs for entry title and amount.");
            alert.showAndWait();
        } else {
            status = true;
        }
        return status;
    }

    private void clearEventInput() {
        this.txtTitle.setText("");
        this.txtAmount.setText("");
        this.txaNote.setText("");
        this.cboDate.setValue(LocalDate.now());
        this.cboTime.setValue("7:00 AM");
        this.rdtExpenditure.setSelected(true);
        this.decimalPlaces = 0;
        this.decimalPoint = false;
    }

    private void validateDigitInput(KeyEvent event) {
        if (!event.getCode().isDigitKey() && !event.getCode().isArrowKey() && event.getCode() != event.getCode().BACK_SPACE && event.getCode() != event.getCode().DELETE && event.getCode() != event.getCode().TAB && event.getCode() != event.getCode().PERIOD) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Input Error");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText("Please supply digits only for this field");
            alert.getDialogPane().setHeaderText("Digits Expected");
            alert.showAndWait();
            String value = ((TextField) event.getSource()).getText().replace(event.getCharacter(), "");
            ((TextField) event.getSource()).requestFocus();
            ((TextField) event.getSource()).setText(value);
        } else if (event.getCode().isDigitKey()) {
            if (decimalPoint) {

                if (++decimalPlaces > 2) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Invalid key entry");
                    alert.setContentText("Only two decimal places allowed");
                    alert.initOwner(Alpha.stage); alert.show();
                    String value = ((TextField) event.getSource()).getText().replace(event.getCharacter(), "");
                    ((TextField) event.getSource()).requestFocus();
                    ((TextField) event.getSource()).setText(value);
                }
            }

        } else if (event.getCode() == event.getCode().PERIOD) {
            if (decimalPoint) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Invalid key entry");
                alert.setContentText("Only one decimal point allowed");
                alert.initOwner(Alpha.stage); alert.show();
                String value = ((TextField) event.getSource()).getText().replace(event.getCharacter(), "");
                ((TextField) event.getSource()).requestFocus();
                ((TextField) event.getSource()).setText(value);

            } else {
                decimalPoint = true;
            }
        }
    }

    @FXML
    private void clearInput(ActionEvent event) {
        clearEventInput();
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
