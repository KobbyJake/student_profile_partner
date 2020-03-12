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
import static controller.GradingController.df;
import java.awt.Dimension;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
import javafx.scene.text.Font;
import javafx.util.Duration;
import javax.persistence.Query;
import javax.swing.ImageIcon;
import model.CourseRead;
import model.Semester;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.jdbc.JDBCCategoryDataset;
import org.jfree.ui.RectangleAnchor;
import view.Alpha;
import view.ScreensController;

/**
 * FXML Controller class
 *
 * @author Jake
 */
public class PerformanceController implements Initializable, ControlledScreen {

    public static boolean refresh = true;
    public Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    public double width = screenSize.getWidth(), height = screenSize.getHeight();
    ScreensController myController;
    private Paragraph pgf;

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        baseContainer.setPrefSize(width - (width * 0.5 / 100), height - (height * 4 / 100));
    }

    private int counter;
    private double totalGradePointAverage;
    private int totalCredit;
    private double totalGradePoint;
    private double gradePointAverage;
    private double point;
    private int ACounter;
    private int BPlusCounter;
    private int BCounter;
    private int CPlusCounter;
    private int CCounter;
    private int DPlusCounter;
    private int DCounter;
    private int ECounter;

    
    private Semester currentSemester;
    javax.persistence.EntityManager entitymanager;

    @FXML
    private BorderPane baseContainer;
    @FXML
    private javafx.scene.paint.Color x4;
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
    private MenuItem walletMenuItem;
    @FXML
    private MenuItem documentsMenuItem;
    @FXML
    private MenuItem settingsMenuItem;
    @FXML
    private TitledPane semesterOutput;
    @FXML
    private TextField txtTotalCourses;
    @FXML
    private TextField txtTotalCredits;
    @FXML
    private TextField txtTotalGradePoint;
    @FXML
    private TextField txtGradePointAverage;
    @FXML
    private TextArea txaGradePointSummary;
    @FXML
    private Label lblCurrentSemester;
    @FXML
    private TitledPane generalOutput;
    @FXML
    private Label lblClass;
    @FXML
    private TextField txtCGPA;
    @FXML
    private TextField txtClass;
    @FXML
    private TextArea txaGPASummary;
    @FXML
    private TableView<CourseRead> tblGrading;
    @FXML
    private ComboBox<String> cboCurrentSemester, cboChart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        entitymanager = DBConnection.getEntityManger();
        animateClassLabel();
        loadCombo();
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
        try {
            Query query = this.entitymanager.createQuery("SELECT c FROM CourseRead c WHERE C.student = ?1 AND c.semester LIKE ?2 AND c.status LIKE 'active'");
            query.setParameter(1, Alpha.currentUser);
            query.setParameter(2, this.cboCurrentSemester.getSelectionModel().getSelectedItem() + "%");
            if (this.cboCurrentSemester.getSelectionModel().getSelectedIndex() == this.cboCurrentSemester.getItems().size() - 1) {
                query.setParameter(2, "Year" + "%");
            }
            ObservableList tableList = FXCollections.observableArrayList();
            query.getResultList().stream().forEach((item) -> {
                tableList.add(item);
            });
            this.tblGrading.setItems(tableList);
            this.setTableColumns();
            totalCredit = 0;
            this.calculateGPA();
            this.lblCurrentSemester.setText(this.cboCurrentSemester.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.setHeaderText("Error retrieving duration course list");
            alert.initOwner(Alpha.stage); alert.show();
        }
    }

    @FXML
    private void resetPage(MouseEvent event) {
    }

    @FXML
    private void LoadPageData(MouseEvent event) {
        getSemesterDetails();
    }

    private void animateClassLabel() {
        TranslateTransition translateTransition = TranslateTransitionBuilder.create()
                .duration(Duration.seconds(5))
                .node(this.lblClass)
                .fromX(-5)
                .toX(180)
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();

        translateTransition.play();
    }

    private void getSemesterDetails() {
        //retrieving current semester
        if (refresh) {
            refresh = false;
            try {
                this.cboCurrentSemester.getItems().clear();
                Query query = this.entitymanager.createQuery("SELECT s FROM Semester s WHERE s.studentID = ?1");
                query.setParameter(1, Alpha.currentUser);
                this.counter = 0;
                this.totalGradePointAverage = 0.0;
                this.txaGPASummary.clear();
                query.getResultList().stream().forEach((semester) -> {
                    Semester currentSem = (Semester) semester;
                    this.cboCurrentSemester.getItems().add(currentSem.getName());
                    // adding combination of semester as year at combo level
                    if (currentSem.getName().contains("Semester 2")) {
                        this.cboCurrentSemester.getItems().add(currentSem.getName().replace(" Semester 2", ""));
                    }

                    //setting current semester
                    if (currentSem.getName().equals(Alpha.currentUser.getCurrentSem())) {
                        this.currentSemester = currentSem;
                    }

                    if (((Semester) semester).getGpa() > 0) {
                        this.totalGradePointAverage += ((Semester) semester).getGpa();
                        String summary = "G.P.A. for " + ((Semester) semester).getName() + " is  :" + df.format(((Semester) semester).getGpa());
                        ++counter;

                        StringBuilder sbuilder = new StringBuilder(this.txaGPASummary.getText());
                        if (!txaGPASummary.getText().isEmpty()) {
                            sbuilder.append(".\n\n");
                        }
                        sbuilder.append(summary);
                        this.txaGPASummary.setText(sbuilder.toString());

                    }

                });
                // adding an item for all semesters to date
                this.cboCurrentSemester.getItems().add("All semesters");

                double cgpa = this.totalGradePointAverage / counter;
                this.txtCGPA.setText(df.format(cgpa));

                if (cgpa >= 3.5) {
                    txtClass.setText("FIRST CLASS");
                } else if (cgpa >= 3.0) {
                    txtClass.setText("SECOND CLASS UPPER DIVISION");
                } else if (cgpa >= 2.5) {
                    txtClass.setText("SECOND CLASS LOWER DIVISION");
                } else if (cgpa >= 2.0) {
                    txtClass.setText("THIRD CLASS");
                } else if (cgpa >= 1.0) {
                    txtClass.setText("PASS");
                } else {
                    txtClass.setText("NONE");
                }

                this.cboCurrentSemester.getSelectionModel().select(Alpha.currentUser.getCurrentSem());
                this.lblCurrentSemester.setText(Alpha.currentUser.getCurrentSem());

                query = this.entitymanager.createQuery("SELECT c FROM CourseRead c WHERE C.student = ?1 AND c.semester LIKE ?2 AND c.status LIKE 'active'");
                query.setParameter(1, Alpha.currentUser);
                query.setParameter(2, this.cboCurrentSemester.getSelectionModel().getSelectedItem());
                ObservableList tableList = FXCollections.observableArrayList();
                query.getResultList().stream().forEach((item) -> {
                    tableList.add(item);
                });
                this.tblGrading.setItems(tableList);
                this.setTableColumns();
                totalCredit = 0;
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.setHeaderText("Error retrieving existing semester course list");
                alert.initOwner(Alpha.stage); alert.show();
            }
          
//            this.calculateGPA();

        }
    }

    private void setTableColumns() {
        this.tblGrading.getColumns().clear();
        double netWidth = (this.tblGrading.getWidth() - (this.tblGrading.getWidth() * 0.75 / 100));

        TableColumn<CourseRead, String> column15 = new TableColumn();
        column15.setVisible(true);
        column15.setText("SEMESTER");
        column15.setCellValueFactory(new PropertyValueFactory("semester"));
        column15.setPrefWidth(netWidth / 6);
        this.tblGrading.getColumns().add(column15);

        TableColumn<CourseRead, String> column11 = new TableColumn();
        column11.setVisible(true);
        column11.setText("CODE");
        column11.setCellValueFactory(new PropertyValueFactory("code"));
        column11.setPrefWidth(netWidth / 9);
        this.tblGrading.getColumns().add(column11);

        TableColumn<CourseRead, String> column12 = new TableColumn();
        column12.setVisible(true);
        column12.setText("TITLE");
        column12.setCellValueFactory(new PropertyValueFactory("title"));
        column12.setPrefWidth(netWidth / 2);
        this.tblGrading.getColumns().add(column12);

        TableColumn<CourseRead, String> column13 = new TableColumn();
        column13.setVisible(true);
        column13.setText("CREDITS");
        column13.setCellValueFactory(new PropertyValueFactory("credit"));
        column13.setPrefWidth(netWidth / 9);
        this.tblGrading.getColumns().add(column13);

        TableColumn<CourseRead, String> column14 = new TableColumn();
        column14.setVisible(true);
        column14.setText("GRADE");
        column14.setCellValueFactory(new PropertyValueFactory("grade"));
        column14.setPrefWidth(netWidth / 9);
        this.tblGrading.getColumns().add(column14);

    }

    private void calculateGPA() {
        this.txaGradePointSummary.setText("");
        this.totalGradePoint = 0.0;
        counter = 0;
        gradePointAverage = 0.0;
        totalCredit = 0;
        this.point = 0.0;
        this.ACounter = 0;
        this.BCounter = 0;
        this.BPlusCounter = 0;
        this.CCounter = 0;
        this.CPlusCounter = 0;
        this.DPlusCounter = 0;
        this.DCounter = 0;
        this.ECounter = 0;
        this.tblGrading.getItems().forEach((CourseRead data) -> {

            if (!data.getGrade().equals("IC")) {
                // retrieving gradepoint
                switch (data.getGrade()) {
                    case "A":
                        point = 4;
                        this.ACounter++;
                        break;
                    case "B+":
                        point = 3.5;
                        this.BPlusCounter++;
                        break;
                    case "B":
                        point = 3;
                        this.BCounter++;
                        break;
                    case "C+":
                        point = 2.5;
                        this.CPlusCounter++;
                        break;
                    case "C":
                        point = 2;
                        this.CCounter++;
                        break;
                    case "D+":
                        point = 1.5;
                        this.DPlusCounter++;
                        break;
                    case "D":
                        point = 1;
                        this.DCounter++;
                        break;
                    default:
                        point = 0;
                        this.ECounter++;
                        break;
                }

                double gradePoint = point * data.getCredit();
                this.totalCredit += data.getCredit();
                this.totalGradePoint += gradePoint;
                this.gradePointAverage = totalGradePoint / totalCredit;
                ++counter;
            }

            this.txtTotalGradePoint.setText("" + this.totalGradePoint);
            this.txtGradePointAverage.setText("" + df.format(gradePointAverage));
            this.txtTotalCourses.setText("" + (counter));
            this.txtTotalCredits.setText("" + totalCredit);

        });

        String content = this.txaGradePointSummary.getText();
        
        if (this.ACounter > 0) {
            this.txaGradePointSummary.setText(content + "\nTotal A obtained : " + this.ACounter);
        }
        if (this.BPlusCounter > 0) {
            content = this.txaGradePointSummary.getText();
            this.txaGradePointSummary.setText(content + "\n\nTotal B+ obtained : " + this.BPlusCounter);
        }
        if (this.BCounter > 0) {
            content = this.txaGradePointSummary.getText();
            this.txaGradePointSummary.setText(content + "\n\nTotal B obtained : " + this.BCounter);
        }
        if (this.CPlusCounter > 0) {
            content = this.txaGradePointSummary.getText();
            this.txaGradePointSummary.setText(content + "\n\nTotal C+ obtained : " + this.CPlusCounter);
        }
        if (this.CCounter > 0) {
            content = this.txaGradePointSummary.getText();
            this.txaGradePointSummary.setText(content + "\n\nTotal C obtained : " + this.CCounter);
        }
        if (this.DPlusCounter > 0) {
            content = this.txaGradePointSummary.getText();
            this.txaGradePointSummary.setText(content + "\n\nTotal D+ obtained : " + this.DPlusCounter);
        }
        if (this.DCounter > 0) {
            content = this.txaGradePointSummary.getText();
            this.txaGradePointSummary.setText(content + "\n\nTotal D obtained : " + this.DCounter);
        }
        if (this.ECounter > 0) {
            content = this.txaGradePointSummary.getText();
            this.txaGradePointSummary.setText(content + "\n\nTotal E obtained : " + this.ECounter);
        }
    }

    private void createChart() {

        try {
            JFreeChart chart = null;
            String queryGPA = "select name,gpa from semester where studentID LIKE '" + Alpha.currentUser.getId() + "' and gpa > 0";
            String queryCGPA = "select name,cgpa from semester where studentID LIKE '" + Alpha.currentUser.getId() + "' and gpa > 0";
            JDBCCategoryDataset datasetGPA = new JDBCCategoryDataset(DBConnection.dbconnect(), queryGPA);
            JDBCCategoryDataset datasetCGPA = new JDBCCategoryDataset(DBConnection.dbconnect(), queryCGPA);
            ChartFrame frame;
            if (this.cboChart.getSelectionModel().getSelectedItem().equals("Semester G.P.A. Bar Chart")) {

                chart = ChartFactory.createBarChart(this.cboChart.getSelectionModel().getSelectedItem().toUpperCase(), "Semester", "Grade Point Average", datasetGPA, PlotOrientation.VERTICAL, true, true, false);
                frame = new ChartFrame("STUDENTS' PROFILE PARTNER", chart);
                frame.setIconImage(new ImageIcon("lib//logo.png").getImage());
                frame.setVisible(true);
                frame.setSize(screenSize);
                frame.setAlwaysOnTop(true);
            }
            if (this.cboChart.getSelectionModel().getSelectedItem().equals("Semester G.P.A. Area Chart")) {

                chart = ChartFactory.createAreaChart(this.cboChart.getSelectionModel().getSelectedItem().toUpperCase(), "Semester", "Grade Point Average", datasetGPA, PlotOrientation.VERTICAL, true, true, false);
                frame = new ChartFrame("STUDENTS' PROFILE PARTNER", chart);
                frame.setIconImage(new ImageIcon("lib//logo.png").getImage());
                frame.setVisible(true);
                frame.setSize(screenSize);
                frame.setAlwaysOnTop(true);
            }
            if (this.cboChart.getSelectionModel().getSelectedItem().equals("Semester G.P.A. Line Chart")) {

                chart = ChartFactory.createLineChart(this.cboChart.getSelectionModel().getSelectedItem().toUpperCase(), "Semester", "Grade Point Average", datasetGPA, PlotOrientation.VERTICAL, true, true, false);
                frame = new ChartFrame("STUDENTS' PROFILE PARTNER", chart);
                frame.setIconImage(new ImageIcon("lib//logo.png").getImage());
                frame.setVisible(true);
                frame.setSize(screenSize);
                frame.setAlwaysOnTop(true);
            }
            if (this.cboChart.getSelectionModel().getSelectedItem().equals("Semester C.G.P.A. Bar Chart")) {
                chart = ChartFactory.createBarChart(this.cboChart.getSelectionModel().getSelectedItem().toUpperCase(), "Semester", "Cummulative Grade Point Average", datasetCGPA, PlotOrientation.VERTICAL, true, true, false);
                frame = new ChartFrame("STUDENTS' PROFILE PARTNER", chart);
                frame.setIconImage(new ImageIcon("lib//logo.png").getImage());
                frame.setVisible(true);
                frame.setSize(screenSize);
                frame.setAlwaysOnTop(true);
            }
            if (this.cboChart.getSelectionModel().getSelectedItem().equals("Semester C.G.P.A. Line Chart")) {
                chart = ChartFactory.createLineChart(this.cboChart.getSelectionModel().getSelectedItem().toUpperCase(), "Semester", "Cummulative Grade Point Average", datasetCGPA, PlotOrientation.VERTICAL, true, true, false);
                frame = new ChartFrame("STUDENTS' PROFILE PARTNER", chart);
                frame.setIconImage(new ImageIcon("lib//logo.png").getImage());
                frame.setVisible(true);
                frame.setSize(screenSize);
                frame.setAlwaysOnTop(true);
            }
            if (this.cboChart.getSelectionModel().getSelectedItem().equals("Semester C.G.P.A. Area Chart")) {
                chart = ChartFactory.createAreaChart(this.cboChart.getSelectionModel().getSelectedItem().toUpperCase(), "Semester", "Cummulative Grade Point Average", datasetCGPA, PlotOrientation.VERTICAL, true, true, false);
                frame = new ChartFrame("STUDENTS' PROFILE PARTNER", chart);
                frame.setIconImage(new ImageIcon("lib//logo.png").getImage());
                frame.setVisible(true);
                frame.setSize(screenSize);
                frame.setAlwaysOnTop(true);
            }

            CategoryPlot cPlot = chart.getCategoryPlot();
            ValueMarker firstClass = new ValueMarker(3.5);
            firstClass.setLabel("First Class");
            firstClass.setPaint(Color.BLACK);
            firstClass.setLabelAnchor(RectangleAnchor.TOP);

            ValueMarker secondClassUpper = new ValueMarker(3.0);
            secondClassUpper.setLabel("Second Class Upper");
            secondClassUpper.setLabelAnchor(RectangleAnchor.TOP);
            secondClassUpper.setPaint(Color.BLACK);

            ValueMarker secondClassLower = new ValueMarker(2.5);
            secondClassLower.setLabel("Second Class Lower");
            secondClassLower.setLabelAnchor(RectangleAnchor.TOP);
            secondClassLower.setPaint(Color.BLACK);

            ValueMarker ThirdClass = new ValueMarker(2.0);
            ThirdClass.setLabel("Third Class");
            ThirdClass.setLabelAnchor(RectangleAnchor.TOP);
            ThirdClass.setPaint(Color.BLACK);

            ValueMarker pass = new ValueMarker(1.5);
            pass.setLabel("Pass");
            pass.setLabelAnchor(RectangleAnchor.TOP);
            pass.setPaint(Color.BLACK);

            ValueMarker failClass = new ValueMarker(1.0);
            failClass.setLabel("Fail");
            failClass.setLabelAnchor(RectangleAnchor.TOP);
            failClass.setPaint(Color.BLACK);
            cPlot.addRangeMarker(firstClass);
            cPlot.addRangeMarker(secondClassUpper);
            cPlot.addRangeMarker(secondClassLower);
            cPlot.addRangeMarker(ThirdClass);
            cPlot.addRangeMarker(pass);
            cPlot.addRangeMarker(failClass);
            cPlot.setBackgroundImage(new ImageIcon("lib//logo.png").getImage());
            cPlot.setBackgroundImageAlpha((float) 0.15);

//            BarRenderer renderer = new BarRenderer();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.setHeaderText("Error retrieving chart data");
//            alert.initOwner(Alpha.stage); alert.show();
        }
        this.cboChart.getSelectionModel().select(0);

    }

    private void loadCombo() {
        this.cboChart.getItems().add("Select Chart");
        this.cboChart.getItems().add("Semester G.P.A. Bar Chart");
        this.cboChart.getItems().add("Semester G.P.A. Line Chart");
        this.cboChart.getItems().add("Semester G.P.A. Area Chart");
        this.cboChart.getItems().add("Semester C.G.P.A. Bar Chart");
        this.cboChart.getItems().add("Semester C.G.P.A. Line Chart");
        this.cboChart.getItems().add("Semester C.G.P.A. Area Chart");
        this.cboChart.getSelectionModel().select(0);
    }

    @FXML
    private void loadChart(ActionEvent event) {
        createChart();
    }

    @FXML
    private void reportPerformance(ActionEvent event) {
        com.itextpdf.text.Image banner = null;

        String title = this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Performance Report.pdf";

        java.util.Date date = new java.util.Date();

        try {
            new Alpha().getBanner("Performance");
            banner = com.itextpdf.text.Image.getInstance("lib/banner.png");
            Document doc = new Document();
            PdfWriter writer;
            if (Alpha.currentUser.getOutputDir().length() < 1) {
                writer = PdfWriter.getInstance(doc, new FileOutputStream(title));
            } else {
                writer = PdfWriter.getInstance(doc, new FileOutputStream(Alpha.currentUser.getOutputDir() + File.separator + title));
            }
            Paragraph heading;
            PdfPTable semTable;
            PdfPTable sem2Table;
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
            pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Performance Report");
            pgf.setAlignment(1);
            doc.add(pgf);

            switch (this.cboCurrentSemester.getSelectionModel().getSelectedItem()) {
                case "Year 1 Semester 1":
                    semTable = new PdfPTable(4);
                    this.setReportTableColumns(semTable);

                    this.tblGrading.getItems().stream().forEach((item) -> {
                        semTable.addCell(item.getCode());
                        semTable.addCell(item.getTitle());
                        semTable.addCell(item.getCredit().toString());
                        semTable.addCell(item.getGrade());
                    });
                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem());
                    heading.setAlignment(1);
//                    doc.add(new Paragraph(heading));
                    doc.add(semTable);
                    doc.add(new Paragraph(" "));
                    break;
                case "Year 1 Semester 2":
                    semTable = new PdfPTable(4);
                    this.setReportTableColumns(semTable);

                    this.tblGrading.getItems().stream().forEach((item) -> {
                        semTable.addCell(item.getCode());
                        semTable.addCell(item.getTitle());
                        semTable.addCell(item.getCredit().toString());
                        semTable.addCell(item.getGrade());
                    });
                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem());
                    heading.setAlignment(1);
//                    doc.add(new Paragraph(heading));
                    doc.add(semTable);
                    doc.add(new Paragraph(" "));
                    break;
                case "Year 2 Semester 1":
                    semTable = new PdfPTable(4);
                    this.setReportTableColumns(semTable);

                    this.tblGrading.getItems().stream().forEach((item) -> {
                        semTable.addCell(item.getCode());
                        semTable.addCell(item.getTitle());
                        semTable.addCell(item.getCredit().toString());
                        semTable.addCell(item.getGrade());
                    });
                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem());
                    heading.setAlignment(1);
//                    doc.add(new Paragraph(heading));
                    doc.add(semTable);
                    doc.add(new Paragraph(" "));
                    break;
                case "Year 2 Semester 2":
                    semTable = new PdfPTable(4);
                    this.setReportTableColumns(semTable);

                    this.tblGrading.getItems().stream().forEach((item) -> {
                        semTable.addCell(item.getCode());
                        semTable.addCell(item.getTitle());
                        semTable.addCell(item.getCredit().toString());
                        semTable.addCell(item.getGrade());
                    });
                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem());
                    heading.setAlignment(1);
//                    doc.add(new Paragraph(heading));
                    doc.add(semTable);
                    doc.add(new Paragraph(" "));
                    break;
                case "Year 3 Semester 1":
                    semTable = new PdfPTable(4);
                    this.setReportTableColumns(semTable);

                    this.tblGrading.getItems().stream().forEach((item) -> {
                        semTable.addCell(item.getCode());
                        semTable.addCell(item.getTitle());
                        semTable.addCell(item.getCredit().toString());
                        semTable.addCell(item.getGrade());
                    });
                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem());
                    heading.setAlignment(1);
//                    doc.add(new Paragraph(heading));
                    doc.add(semTable);
                    doc.add(new Paragraph(" "));
                    break;
                case "Year 3 Semester 2":
                    semTable = new PdfPTable(4);
                    this.setReportTableColumns(semTable);

                    this.tblGrading.getItems().stream().forEach((item) -> {
                        semTable.addCell(item.getCode());
                        semTable.addCell(item.getTitle());
                        semTable.addCell(item.getCredit().toString());
                        semTable.addCell(item.getGrade());
                    });
                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem());
                    heading.setAlignment(1);
//                    doc.add(new Paragraph(heading));
                    doc.add(semTable);
                    doc.add(new Paragraph(" "));
                    break;
                case "Year 4 Semester 1":
                    semTable = new PdfPTable(4);
                    this.setReportTableColumns(semTable);

                    this.tblGrading.getItems().stream().forEach((item) -> {
                        semTable.addCell(item.getCode());
                        semTable.addCell(item.getTitle());
                        semTable.addCell(item.getCredit().toString());
                        semTable.addCell(item.getGrade());
                    });
                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem());
                    heading.setAlignment(1);
//                    doc.add(new Paragraph(heading));
                    doc.add(semTable);
                    doc.add(new Paragraph(" "));
                    break;
                case "Year 4 Semester 2":
                    semTable = new PdfPTable(4);
                    this.setReportTableColumns(semTable);

                    this.tblGrading.getItems().stream().forEach((item) -> {
                        semTable.addCell(item.getCode());
                        semTable.addCell(item.getTitle());
                        semTable.addCell(item.getCredit().toString());
                        semTable.addCell(item.getGrade());
                    });
                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem());
                    heading.setAlignment(1);
//                    doc.add(new Paragraph(heading));
                    doc.add(semTable);
                    doc.add(new Paragraph(" "));
                    break;
                case "Year 1":
                    semTable = new PdfPTable(4);
                    this.setReportTableColumns(semTable);

                    sem2Table = new PdfPTable(4);
                    this.setReportTableColumns(sem2Table);

                    this.tblGrading.getItems().stream().forEach((item) -> {
                        if (item.getSemester().contains("Semester 1")) {
                            semTable.addCell(item.getCode());
                            semTable.addCell(item.getTitle());
                            semTable.addCell(item.getCredit().toString());
                            semTable.addCell(item.getGrade());
                        } else {
                            sem2Table.addCell(item.getCode());
                            sem2Table.addCell(item.getTitle());
                            sem2Table.addCell(item.getCredit().toString());
                            sem2Table.addCell(item.getGrade());

                        }
                    });
                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Semester 1");
                    heading.setAlignment(1);
                    doc.add(new Paragraph(heading));
                    doc.add(semTable);
                    doc.add(new Paragraph(" "));

                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Semester 2");
                    heading.setAlignment(1);
                    doc.add(new Paragraph(heading));
                    doc.add(sem2Table);
                    doc.add(new Paragraph(" "));
                    break;
                case "Year 2":
                    semTable = new PdfPTable(4);
                    this.setReportTableColumns(semTable);

                    sem2Table = new PdfPTable(4);
                    this.setReportTableColumns(sem2Table);

                    this.tblGrading.getItems().stream().forEach((item) -> {
                        if (item.getSemester().contains("Semester 1")) {
                            semTable.addCell(item.getCode());
                            semTable.addCell(item.getTitle());
                            semTable.addCell(item.getCredit().toString());
                            semTable.addCell(item.getGrade());
                        } else {
                            sem2Table.addCell(item.getCode());
                            sem2Table.addCell(item.getTitle());
                            sem2Table.addCell(item.getCredit().toString());
                            sem2Table.addCell(item.getGrade());

                        }
                    });
                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Semester 1");
                    heading.setAlignment(1);
                    doc.add(new Paragraph(heading));
                    doc.add(semTable);
                    doc.add(new Paragraph(" "));

                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Semester 2");
                    heading.setAlignment(1);
                    doc.add(new Paragraph(heading));
                    doc.add(sem2Table);
                    doc.add(new Paragraph(" "));
                    break;
                case "Year 3":
                    semTable = new PdfPTable(4);
                    this.setReportTableColumns(semTable);

                    sem2Table = new PdfPTable(4);
                    this.setReportTableColumns(sem2Table);

                    this.tblGrading.getItems().stream().forEach((item) -> {
                        if (item.getSemester().contains("Semester 1")) {
                            semTable.addCell(item.getCode());
                            semTable.addCell(item.getTitle());
                            semTable.addCell(item.getCredit().toString());
                            semTable.addCell(item.getGrade());
                        } else {
                            sem2Table.addCell(item.getCode());
                            sem2Table.addCell(item.getTitle());
                            sem2Table.addCell(item.getCredit().toString());
                            sem2Table.addCell(item.getGrade());

                        }
                    });
                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Semester 1");
                    heading.setAlignment(1);
                    doc.add(new Paragraph(heading));
                    doc.add(semTable);
                    doc.add(new Paragraph(" "));

                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Semester 2");
                    heading.setAlignment(1);
                    doc.add(new Paragraph(heading));
                    doc.add(sem2Table);
                    doc.add(new Paragraph(" "));
                    break;
                case "Year 4":
                    semTable = new PdfPTable(4);
                    this.setReportTableColumns(semTable);

                    sem2Table = new PdfPTable(4);
                    this.setReportTableColumns(sem2Table);

                    this.tblGrading.getItems().stream().forEach((item) -> {
                        if (item.getSemester().contains("Semester 1")) {
                            semTable.addCell(item.getCode());
                            semTable.addCell(item.getTitle());
                            semTable.addCell(item.getCredit().toString());
                            semTable.addCell(item.getGrade());
                        } else {
                            sem2Table.addCell(item.getCode());
                            sem2Table.addCell(item.getTitle());
                            sem2Table.addCell(item.getCredit().toString());
                            sem2Table.addCell(item.getGrade());

                        }
                    });
                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Semester 1");
                    heading.setAlignment(1);
                    doc.add(new Paragraph(heading));
                    doc.add(semTable);
                    doc.add(new Paragraph(" "));

                    heading = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Semester 2");
                    heading.setAlignment(1);
                    doc.add(new Paragraph(heading));
                    doc.add(sem2Table);
                    doc.add(new Paragraph(" "));
                    break;
                case "All semesters":
                    semTable = new PdfPTable(4);
                    this.setReportTableColumns(semTable);

                    sem2Table = new PdfPTable(4);
                    this.setReportTableColumns(sem2Table);

                    PdfPTable y2sem1Table = new PdfPTable(4);
                    this.setReportTableColumns(y2sem1Table);

                    PdfPTable y2sem2Table = new PdfPTable(4);
                    this.setReportTableColumns(y2sem2Table);

                    PdfPTable y3sem1Table = new PdfPTable(4);
                    this.setReportTableColumns(y3sem1Table);

                    PdfPTable y3sem2Table = new PdfPTable(4);
                    this.setReportTableColumns(y3sem2Table);

                    PdfPTable y4sem1Table = new PdfPTable(4);
                    this.setReportTableColumns(y4sem1Table);

                    PdfPTable y4sem2Table = new PdfPTable(4);
                    this.setReportTableColumns(y4sem2Table);

                    this.tblGrading.getItems().stream().forEach((item) -> {
                        if (item.getSemester().equals("Year 1 Semester 1")) {
                            semTable.addCell(item.getCode());
                            semTable.addCell(item.getTitle());
                            semTable.addCell(item.getCredit().toString());
                            semTable.addCell(item.getGrade());
                        } else if (item.getSemester().equals("Year 1 Semester 2")) {
                            sem2Table.addCell(item.getCode());
                            sem2Table.addCell(item.getTitle());
                            sem2Table.addCell(item.getCredit().toString());
                            sem2Table.addCell(item.getGrade());

                        } else if (item.getSemester().equals("Year 2 Semester 1")) {
                            y2sem1Table.addCell(item.getCode());
                            y2sem1Table.addCell(item.getTitle());
                            y2sem1Table.addCell(item.getCredit().toString());
                            y2sem1Table.addCell(item.getGrade());
                        } else if (item.getSemester().equals("Year 2 Semester 2")) {
                            y2sem2Table.addCell(item.getCode());
                            y2sem2Table.addCell(item.getTitle());
                            y2sem2Table.addCell(item.getCredit().toString());
                            y2sem2Table.addCell(item.getGrade());

                        } else if (item.getSemester().equals("Year 3 Semester 1")) {
                            y3sem1Table.addCell(item.getCode());
                            y3sem1Table.addCell(item.getTitle());
                            y3sem1Table.addCell(item.getCredit().toString());
                            y3sem1Table.addCell(item.getGrade());
                        } else if (item.getSemester().equals("Year 3 Semester 2")) {
                            y3sem2Table.addCell(item.getCode());
                            y3sem2Table.addCell(item.getTitle());
                            y3sem2Table.addCell(item.getCredit().toString());
                            y3sem2Table.addCell(item.getGrade());

                        } else if (item.getSemester().equals("Year 4 Semester 1")) {
                            y4sem1Table.addCell(item.getCode());
                            y4sem1Table.addCell(item.getTitle());
                            y4sem1Table.addCell(item.getCredit().toString());
                            y4sem1Table.addCell(item.getGrade());
                        } else if (item.getSemester().equals("Year 4 Semester 2")) {
                            y4sem2Table.addCell(item.getCode());
                            y4sem2Table.addCell(item.getTitle());
                            y4sem2Table.addCell(item.getCredit().toString());
                            y4sem2Table.addCell(item.getGrade());

                        }
                    });
//                    try {
                    if (semTable.getRows().size() > 1) {
                        heading = new Paragraph("Year1 Semester 1");
                        heading.setAlignment(1);
                        doc.add(new Paragraph(heading));
                        doc.add(semTable);
                        doc.add(new Paragraph(" "));
                    }

                    if (sem2Table.getRows().size() > 1) {
                        heading = new Paragraph("Year1 Semester 2");
                        heading.setAlignment(1);
                        doc.add(new Paragraph(heading));
                        doc.add(sem2Table);
                        doc.add(new Paragraph(" "));
                    }

                    if (y2sem1Table.getRows().size() > 1) {
                        heading = new Paragraph("Year2 Semester 1");
                        heading.setAlignment(1);
                        doc.add(new Paragraph(heading));
                        doc.add(y2sem1Table);
                        doc.add(new Paragraph(" "));
                    }

                    if (y2sem2Table.getRows().size() > 1) {
                        heading = new Paragraph("Year2 Semester 2");
                        heading.setAlignment(1);
                        doc.add(new Paragraph(heading));
                        doc.add(y2sem2Table);
                        doc.add(new Paragraph(" "));
                    }
                    if (y3sem1Table.getRows().size() > 1) {
                        heading = new Paragraph("Year3 Semester 1");
                        heading.setAlignment(1);
                        doc.add(new Paragraph(heading));
                        doc.add(y3sem1Table);
                        doc.add(new Paragraph(" "));
                    }

                    if (y3sem2Table.getRows().size() > 1) {
                        heading = new Paragraph("Year3 Semester 2");
                        heading.setAlignment(1);
                        doc.add(new Paragraph(heading));
                        doc.add(y3sem2Table);
                        doc.add(new Paragraph(" "));
                    }
                    if (y4sem1Table.getRows().size() > 1) {
                        heading = new Paragraph("Year4 Semester 1");
                        heading.setAlignment(1);
                        doc.add(new Paragraph(heading));
                        doc.add(y4sem1Table);
                        doc.add(new Paragraph(" "));
                    }

                    if (y4sem2Table.getRows().size() > 1) {
                        heading = new Paragraph("Year4 Semester 2");
                        heading.setAlignment(1);
                        doc.add(new Paragraph(heading));
                        doc.add(y4sem2Table);
                        doc.add(new Paragraph(" "));
                    }
//                    } catch (Exception e) {
//                    }
                    break;
            }
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Total grade points: " + this.txtTotalGradePoint.getText() + "                                          Grade points average: " + this.txtGradePointAverage.getText()));
            if (this.cboCurrentSemester.getSelectionModel().getSelectedItem().equals("All semesters")) {
                doc.add(new Paragraph("Cummulative grade point average: " + txtCGPA.getText() + "                 Class: " + txtClass.getText()));

            }
            doc.addAuthor("JCyberSolutions 2015 Contact: 0242525727");
            doc.addSubject("Performance Report");
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("                                                                                                              SIGN..................... "));
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

    private void setReportTableColumns(PdfPTable table) {
        table.addCell("Code");
        table.addCell("Title");
        table.addCell("Credits");
        table.addCell("Grade");
    }

    @FXML
    private void expandCumulativeOutput(MouseEvent event) {
        generalOutput.setExpanded(true);
    }

    @FXML
    private void expandOutputPane(MouseEvent event) {
        semesterOutput.setExpanded(true);
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
