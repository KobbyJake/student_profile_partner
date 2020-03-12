/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
import javax.persistence.Query;
import model.CourseRead;
import model.Event;
import model.Location;
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
public class ScheduleController implements Initializable, ControlledScreen {

    public Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    public double width = screenSize.getWidth(), height = screenSize.getHeight();
    ScreensController myController;
    javax.persistence.EntityManager entitymanager;
    LocalDate currentDate = LocalDate.now();

    @FXML
    private TabPane contentArea;
    private Semester currentSemester;
    private Schedule currentSchedule;
    private Student currentUser;
    private TableColumn<Schedule, String> colDay;
    private TableColumn<Schedule, String> colMorning;
    private TableColumn<Schedule, String> colMidDay;
    private TableColumn<Schedule, String> colAfternoon;
    private TableColumn<Schedule, String> colEvening;
    private TableColumn<Schedule, String> colNight;
    private TableColumn<Schedule, String> colDawn;
    private Event currentEvent;
    private ObservableList<Event> eventData;
    @FXML
    private MenuItem walletMenuItem;
    private Paragraph pgf;
    public static boolean refresh = true;

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        baseContainer.setPrefSize(width - (width * 0.5 / 100), height - (height * 4 / 100));
    }

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
    private TitledPane routineInput;
    @FXML
    private ComboBox<String> cboRoutineActivity;
    @FXML
    private ComboBox<String> cboRoutineVenue;
    @FXML
    private ComboBox<String> cboDayOfWeek;
    @FXML
    private ComboBox<String> cboTimeOfDay;
    @FXML
    private ComboBox<String> cboStartTime;
    @FXML
    private ComboBox<String> cboEndTime;
    @FXML
    private Button btnSaveRoutine;
    @FXML
    private Button btnDeleteRoutine;
    @FXML
    private Button btnReportRoutine;
    @FXML
    private TitledPane eventInput;
    @FXML
    private ComboBox<String> cboEventVenue;
    @FXML
    private TextField txtEventName;
    @FXML
    private DatePicker cboEventDate;
    @FXML
    private ComboBox<String> cboEventStartTime;
    @FXML
    private TextArea txaEventNote;
    @FXML
    private Button btnSaveEvent;
    @FXML
    private Button btnUpdateEvent;
    @FXML
    private Button btnDeleteEvent;
    @FXML
    private Button btnReportEvent;
    @FXML
    private ComboBox<String> cboCurrentSemester;
    @FXML
    private Tab routineDisplay;
    @FXML
    private TableView<Schedule> tblRoutine;
    @FXML
    private Tab eventDisplay;
    @FXML
    private TableView<Event> tblEvent;
    @FXML
    private ComboBox<String> cboEventFilter;

    private ObservableList<Schedule> data;
    private String semesterMarker = "";
    private String userMarker = "";
    SimpleDateFormat sfm = new SimpleDateFormat("M/d/yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE MMM dd, yyyy");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        entitymanager = DBConnection.getEntityManger();
        this.loadCombos();
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
    private void saveRoutine(ActionEvent event) {
        setSchedule();
    }

    @FXML
    private void deleteRoutine(ActionEvent event) {
        data.stream().forEach((schedule) -> {
            if (((Schedule) schedule).getDay().equals(this.cboDayOfWeek.getSelectionModel().getSelectedItem())) {
                this.currentSchedule = (Schedule) schedule;
            }
        });

        switch (this.cboTimeOfDay.getSelectionModel().getSelectedItem()) {
            case "Morning":
                this.currentSchedule.setMorning(null);
                break;
            case "Mid-Day":
                this.currentSchedule.setMidDay(null);
                break;
            case "Afternoon":
                this.currentSchedule.setAfternoon(null);
                break;
            case "Evening":
                this.currentSchedule.setEvening(null);
                break;
            case "Night":
                this.currentSchedule.setNight(null);
                break;
            case "Dawn":
                this.currentSchedule.setDawn(null);
                break;
        }

        this.entitymanager.getTransaction().begin();
        this.entitymanager.merge(currentSchedule);
        this.entitymanager.getTransaction().commit();
        this.cboRoutineActivity.getEditor().clear();
        this.cboRoutineVenue.getEditor().setText("");
        loadTimeTable();

    }

    @FXML
    private void reportRoutine(ActionEvent event) {
        com.itextpdf.text.Image banner = null;

        String title = this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Routine Schedule.pdf";

        java.util.Date date = new java.util.Date();

        try {
            new Alpha().getBanner("Routine");
            banner = com.itextpdf.text.Image.getInstance("lib/banner.png");
            Document doc = new Document();
            PdfWriter writer;
            if (Alpha.currentUser.getOutputDir().length() < 1) {
                writer = PdfWriter.getInstance(doc, new FileOutputStream(title));
            } else {
                writer = PdfWriter.getInstance(doc, new FileOutputStream(Alpha.currentUser.getOutputDir() + File.separator + title));
            }

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
            pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Routine Schedule");
            pgf.setAlignment(1);
            doc.add(pgf);

            this.tblRoutine.getItems().stream().forEach((item) -> {
                switch (item.getDay()) {
                    case "1. Monday":
                        try {
                            doc.add(new Paragraph(" "));
                            doc.add(new Paragraph("MONDAY"));
                        } catch (Exception e) {
                        }

                        try {

                            String[] parts = item.getMorning().split(",");

                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Morning: " + currentString));
                        } catch (Exception e) {

                        }
                        try {
                            String[] parts = item.getMidDay().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Mid-Day: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getAfternoon().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Afternoon: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getEvening().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Evening: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getNight().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Night: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getDawn().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Dawn: " + currentString));
                        } catch (Exception e) {
                        }
                        break;

                    case "2. Tuesday":
                        try {
                            doc.add(new Paragraph(" "));
                            doc.add(new Paragraph("TUESDAY"));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getMorning().split(",");

                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Morning: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getMidDay().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Mid-Day: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getAfternoon().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Afternoon: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getEvening().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Evening: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getNight().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Night: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getDawn().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Dawn: " + currentString));
                        } catch (Exception e) {
                        }
                        break;
                    case "3. Wednesday":
                        try {
                            doc.add(new Paragraph(" "));
                            doc.add(new Paragraph("WEDNESDAY"));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getMorning().split(",");

                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Morning: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getMidDay().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Mid-Day: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getAfternoon().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Afternoon: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getEvening().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Evening: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getNight().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Night: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getDawn().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Dawn: " + currentString));
                        } catch (Exception e) {
                        }
                        break;
                    case "4. Thursday":
                        try {
                            doc.add(new Paragraph(" "));
                            doc.add(new Paragraph("THURSDAY"));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getMorning().split(",");

                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Morning: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getMidDay().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Mid-Day: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getAfternoon().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Afternoon: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getEvening().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Evening: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getNight().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Night: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getDawn().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Dawn: " + currentString));
                        } catch (Exception e) {
                        }
                        break;
                    case "5. Friday":
                        try {
                            doc.add(new Paragraph(" "));
                            doc.add(new Paragraph("FRIDAY"));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getMorning().split(",");

                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Morning: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getMidDay().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Mid-Day: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getAfternoon().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Afternoon: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getEvening().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Evening: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getNight().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Night: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getDawn().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Dawn: " + currentString));
                        } catch (Exception e) {
                        }
                        break;
                    case "6. Saturday":
                        try {
                            doc.add(new Paragraph(" "));
                            doc.add(new Paragraph("SATURDAY"));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getMorning().split(",");

                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Morning: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getMidDay().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Mid-Day: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getAfternoon().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Afternoon: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getEvening().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Evening: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getNight().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Night: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getDawn().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Dawn: " + currentString));
                        } catch (Exception e) {
                        }
                        break;
                    case "7. Sunday":
                        try {
                            doc.add(new Paragraph(" "));
                            doc.add(new Paragraph("SUNDAY"));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getMorning().split(",");

                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Morning: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getMidDay().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Mid-Day: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getAfternoon().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Afternoon: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getEvening().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Evening: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getNight().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Night: " + currentString));
                        } catch (Exception e) {
                        }
                        try {
                            String[] parts = item.getDawn().split(",");
                            String currentString = parts[0] + " @ " + parts[1].replace("\n\n", "") + " from " + parts[2].replace("\n", "");
                            doc.add(new Paragraph("Dawn: " + currentString));
                        } catch (Exception e) {
                        }
                        break;

                }

            });
            doc.addAuthor("JCyberSolutions 2015 Contact: 0242525727");
            doc.addSubject("Quizzes Log");
            doc.add(new Paragraph(" "));
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

    @FXML
    private void displayRoutine(MouseEvent event) {
        this.contentArea.getSelectionModel().select(this.routineDisplay);
    }

    @FXML
    private void saveEvent(ActionEvent event) {
        try {
            this.entitymanager.getTransaction().begin();
            this.entitymanager.persist(this.setEvent(new Event()));
            this.entitymanager.getTransaction().commit();
            clearEventInput();
            this.getEventTable();

        } catch (Exception e) {
            this.entitymanager.getTransaction().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Event creation error.");
            alert.setContentText(e.getLocalizedMessage());
            alert.initOwner(Alpha.stage); alert.show();
        }
    }

    @FXML
    private void updateEvent(ActionEvent event) {
        try {
            this.entitymanager.getTransaction().begin();
            this.entitymanager.persist(this.setEvent(this.currentEvent));
            this.entitymanager.getTransaction().commit();
            clearEventInput();
            this.getEventTable();

        } catch (Exception e) {
            this.entitymanager.getTransaction().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Event update error.");
            alert.setContentText(e.getLocalizedMessage());
            alert.initOwner(Alpha.stage); alert.show();
        }
    }

    @FXML
    private void deleteEvent(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Confirm delete action");
        alert.setContentText("You are about to delete the selected event.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                this.entitymanager.getTransaction().begin();
                this.entitymanager.remove(this.currentEvent);
                this.entitymanager.getTransaction().commit();
                clearEventInput();
                this.getEventTable();
            } catch (Exception e) {
                this.entitymanager.getTransaction().rollback();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Event delete error.");
                alert.setContentText(e.getLocalizedMessage());
                alert.initOwner(Alpha.stage); alert.show();
            }
        }

    }

    @FXML
    private void ReportEvent(ActionEvent event) {
        com.itextpdf.text.Image banner = null;

        String title = this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Event Schedule.pdf";

        java.util.Date date = new java.util.Date();

        try {
            new Alpha().getBanner("Event");
            banner = com.itextpdf.text.Image.getInstance("lib/banner.png");
            Document doc = new Document();
            PdfWriter writer;
            if (Alpha.currentUser.getOutputDir().length() < 1) {
                writer = PdfWriter.getInstance(doc, new FileOutputStream(title));
            } else {
                writer = PdfWriter.getInstance(doc, new FileOutputStream(Alpha.currentUser.getOutputDir() + File.separator + title));
            }

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
            pgf = new Paragraph(this.cboCurrentSemester.getSelectionModel().getSelectedItem() + " Event Schedule");
            pgf.setAlignment(1);
            doc.add(pgf);
            this.tblEvent.getItems().stream().forEach((item) -> {
                String currentString = "";
                try {
                    currentString = "Title: " + item.getTitle() + " @ " + item.getVenue() + " On  " + this.sdf.format(item.getDate()) + " From " + item.getTime();
                    doc.add(new Paragraph(currentString));
                } catch (Exception ex) {
                    Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
                }

            });
            doc.addAuthor("JCyberSolutions 2015 Contact: 0242525727");
            doc.addSubject("Quizzes Log");
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
    private void displayEvent(MouseEvent event) {
        this.contentArea.getSelectionModel().select(eventDisplay);
    }

    @FXML
    private void loadSemesterSchedule(ActionEvent event) {
        this.loadTimeTable();
        this.getEventTable();
    }

    private boolean filter(Event event) throws ParseException {
        boolean status = false;
        ZonedDateTime zdt = event.getDate().toInstant().atZone(ZoneId.systemDefault());
        LocalDate date = zdt.toLocalDate();
        switch (this.cboEventFilter.getSelectionModel().getSelectedIndex()) {
            case 0:
                if (date.isBefore(LocalDate.now())) {
                    status = true;
                }
                break;
            case 1:
                if (date.isEqual(LocalDate.now())) {
                    status = true;
                }
                break;
            case 2:
                if (date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusWeeks(1))) {
                    status = true;
                }
                break;
            case 3:
                if (date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusWeeks(2))) {
                    status = true;
                }
                break;
            case 4:
                if (date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusWeeks(4))) {
                    status = true;
                }
                break;
        }
        return status;
    }

    @FXML
    private void filterEvent(ActionEvent event) {
        ObservableList<Event> filteredData = FXCollections.observableArrayList();

        if (this.cboEventFilter.getSelectionModel().getSelectedIndex() == 5) {
            this.tblEvent.setItems(eventData);
        } else {
            
                this.eventData.stream().forEach((item) -> {
                    try {
                        if (this.filter(item)) {
                            
                            filteredData.add(item);
                            
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

            this.tblEvent.setItems(filteredData);
        }
    }

    @FXML
    private void LoadPageData(MouseEvent event) {
        getSemesterDetails();
    }

    private void loadCombos() {
        this.cboDayOfWeek.getItems().add("1. Monday");
        this.cboDayOfWeek.getItems().add("2. Tuesday");
        this.cboDayOfWeek.getItems().add("3. Wednesday");
        this.cboDayOfWeek.getItems().add("4. Thursday");
        this.cboDayOfWeek.getItems().add("5. Friday");
        this.cboDayOfWeek.getItems().add("6. Saturday");
        this.cboDayOfWeek.getItems().add("7. Sunday");
        this.cboDayOfWeek.getSelectionModel().select("1. Monday");

        this.cboTimeOfDay.getItems().add("Dawn");
        this.cboTimeOfDay.getItems().add("Morning");
        this.cboTimeOfDay.getItems().add("Mid-Day");
        this.cboTimeOfDay.getItems().add("Afternoon");
        this.cboTimeOfDay.getItems().add("Evening");
        this.cboTimeOfDay.getItems().add("Night");
        this.cboTimeOfDay.getSelectionModel().select("Morning");

        this.cboEventFilter.getItems().add("Past");
        this.cboEventFilter.getItems().add("Today");
        this.cboEventFilter.getItems().add("Next one week");
        this.cboEventFilter.getItems().add("Next two weeks");
        this.cboEventFilter.getItems().add("Next one month");
        this.cboEventFilter.getItems().add("All events");
        this.cboEventFilter.getSelectionModel().select("All events");

        loadTime();
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
                });
                this.cboCurrentSemester.getSelectionModel().select(Alpha.currentUser.getCurrentSem());
                this.loadTimeTable();
                this.getEventTable();
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

    @FXML
    private void resetPage(MouseEvent event) {
        this.semesterMarker = "";
    }

    private void loadTimeTable() {
        Query query = this.entitymanager.createQuery("SELECT c FROM CourseRead c WHERE C.student = ?1 AND c.semester = ?2 ");
        query.setParameter(1, Alpha.currentUser);
        query.setParameter(2, this.cboCurrentSemester.getSelectionModel().getSelectedItem());
        this.cboRoutineActivity.getItems().clear();
        query.getResultList().stream().forEach((item) -> {
            this.cboRoutineActivity.getItems().add(((CourseRead) item).getTitle());
        });

        data = FXCollections.observableArrayList();
        query = this.entitymanager.createQuery("SELECT s FROM Semester s WHERE s.studentID = ?1 AND s.name = ?2");
        query.setParameter(1, Alpha.currentUser);
        query.setParameter(2, this.cboCurrentSemester.getSelectionModel().getSelectedItem());
        if (!query.getResultList().isEmpty()) {
            this.currentSemester = (Semester) query.getSingleResult();
            query = this.entitymanager.createQuery("SELECT s FROM Schedule s WHERE s.semesterID = ?1 ORDER BY s.day ASC");
            query.setParameter(1, this.currentSemester);
            query.getResultList().stream().forEach((item) -> {
                data.add((Schedule) item);
            });
            this.tblRoutine.setItems(data);
            this.setTimeTableColumns();
            this.tblRoutine.getSelectionModel().cellSelectionEnabledProperty().set(true);
        }
    }

    private void setTimeTableColumns() {
        this.tblRoutine.getColumns().clear();
        double netWidth = (this.tblRoutine.getWidth() - (this.tblRoutine.getWidth() * 0.75 / 100));
        colDay = new TableColumn();
        colDay.setVisible(true);
        colDay.setText("");
        colDay.setCellValueFactory(new PropertyValueFactory("Day"));
        colDay.setPrefWidth(netWidth / 9);
        this.tblRoutine.getColumns().add(colDay);

        colMorning = new TableColumn();
        colMorning.setVisible(true);
        colMorning.setText("MORNING");
        colMorning.setCellValueFactory(new PropertyValueFactory("Morning"));
        colMorning.setPrefWidth(netWidth / 4.5);
        this.tblRoutine.getColumns().add(colMorning);

        colMidDay = new TableColumn();
        colMidDay.setVisible(true);
        colMidDay.setText("MID-DAY");
        colMidDay.setCellValueFactory(new PropertyValueFactory("MidDay"));
        colMidDay.setPrefWidth(netWidth / 4.5);
        this.tblRoutine.getColumns().add(colMidDay);

        colAfternoon = new TableColumn();
        colAfternoon.setVisible(true);
        colAfternoon.setText("AFTERNOON");
        colAfternoon.setCellValueFactory(new PropertyValueFactory("Afternoon"));
        colAfternoon.setPrefWidth(netWidth / 4.5);
        this.tblRoutine.getColumns().add(colAfternoon);

        colEvening = new TableColumn();
        colEvening.setVisible(true);
        colEvening.setText("EVENING");
        colEvening.setCellValueFactory(new PropertyValueFactory("Evening"));
        colEvening.setPrefWidth(netWidth / 4.5);
        this.tblRoutine.getColumns().add(colEvening);

        colNight = new TableColumn();
        colNight.setVisible(true);
        colNight.setText("NIGHT");
        colNight.setCellValueFactory(new PropertyValueFactory("Night"));
        colNight.setPrefWidth(netWidth / 4);
        this.tblRoutine.getColumns().add(colNight);

        colDawn = new TableColumn();
        colDawn.setVisible(true);
        colDawn.setText("DAWN");
        colDawn.setCellValueFactory(new PropertyValueFactory("Dawn"));
        colDawn.setPrefWidth(netWidth / 4);
        this.tblRoutine.getColumns().add(colDawn);

    }

    private boolean isValidScheduleInput() {
        boolean status = false;
        if (!this.cboRoutineActivity.getEditor().getText().isEmpty() && !this.cboRoutineVenue.getEditor().getText().isEmpty()) {
            status = true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please specify both activity and venue.");
            alert.setHeaderText("Routine input incomplete.");
            alert.initOwner(Alpha.stage); alert.show();
        }

        return status;
    }

    private void setSchedule() {
        if (this.isValidScheduleInput()) {
            // retrieving day of the schedule
            data.stream().forEach((schedule) -> {
                if (((Schedule) schedule).getDay().equals(this.cboDayOfWeek.getSelectionModel().getSelectedItem())) {
                    this.currentSchedule = (Schedule) schedule;
                }
            });
            String activity = this.cboRoutineActivity.getEditor().getText();
            String venue = this.cboRoutineVenue.getEditor().getText();
            String time = this.cboStartTime.getSelectionModel().getSelectedItem() + " - " + this.cboEndTime.getSelectionModel().getSelectedItem();
            String content = activity + ",\n\n" + venue + ",\n" + time;
            switch (this.cboTimeOfDay.getSelectionModel().getSelectedItem()) {
                case "Morning":
                    this.currentSchedule.setMorning(content);
                    break;
                case "Mid-Day":
                    this.currentSchedule.setMidDay(content);
                    break;
                case "Afternoon":
                    this.currentSchedule.setAfternoon(content);
                    break;
                case "Evening":
                    this.currentSchedule.setEvening(content);
                    break;
                case "Night":
                    this.currentSchedule.setNight(content);
                    break;
                case "Dawn":
                    this.currentSchedule.setDawn(content);
                    break;
            }

            this.entitymanager.getTransaction().begin();
            this.entitymanager.merge(currentSchedule);
            this.entitymanager.getTransaction().commit();
            this.cboRoutineActivity.getEditor().clear();
            this.cboRoutineVenue.getEditor().setText("");
            this.currentSchedule = null;
            loadTimeTable();

        }
    }

    @FXML
    private void displayRoutineInput(MouseEvent event) {
        routineInput.setExpanded(true);
    }

    @FXML
    private void loadCellData(MouseEvent event) {
        this.cboRoutineActivity.getEditor().clear();
        this.cboRoutineVenue.getEditor().clear();
        int index = this.data.indexOf(this.tblRoutine.getSelectionModel().getSelectedItem());
        Schedule newSelection = this.tblRoutine.getSelectionModel().getSelectedItem();
        this.cboDayOfWeek.getSelectionModel().select(this.data.indexOf(newSelection));
        try {
            if (this.tblRoutine.getSelectionModel().getSelectedCells().get(0).getColumn() == 1) {
                this.cboTimeOfDay.getSelectionModel().select("Morning");
                String[] parts = ((String) tblRoutine.getSelectionModel().getSelectedCells().get(0).getTableColumn().getCellData(index)).split(",");
                this.cboRoutineActivity.getEditor().setText(parts[0]);
                this.cboRoutineVenue.getEditor().setText(parts[1]);
                String[] time = parts[2].split("-");
                this.cboStartTime.getSelectionModel().select(time[0].trim());
                this.cboEndTime.getSelectionModel().select(time[1].trim());
            } else if (this.tblRoutine.getSelectionModel().getSelectedCells().get(0).getColumn() == 2) {
                this.cboTimeOfDay.getSelectionModel().select("Mid-Day");
                String[] parts = newSelection.getMidDay().split(",");
                this.cboRoutineActivity.getEditor().setText(parts[0]);
                this.cboRoutineVenue.getEditor().setText(parts[1]);
                String[] time = parts[2].split("-");
                this.cboStartTime.getSelectionModel().select(time[0].trim());
                this.cboEndTime.getSelectionModel().select(time[1].trim());
            } else if (this.tblRoutine.getSelectionModel().getSelectedCells().get(0).getColumn() == 3) {
                this.cboTimeOfDay.getSelectionModel().select("Afternoon");
                String[] parts = newSelection.getAfternoon().split(",");
                this.cboRoutineActivity.getEditor().setText(parts[0]);
                this.cboRoutineVenue.getEditor().setText(parts[1]);
                String[] time = parts[2].split("-");
                this.cboStartTime.getSelectionModel().select(time[0].trim());
                this.cboEndTime.getSelectionModel().select(time[1].trim());
            } else if (this.tblRoutine.getSelectionModel().getSelectedCells().get(0).getColumn() == 4) {
                this.cboTimeOfDay.getSelectionModel().select("Evening");
                String[] parts = newSelection.getEvening().split(",");
                this.cboRoutineActivity.getEditor().setText(parts[0]);
                this.cboRoutineVenue.getEditor().setText(parts[1]);
                String[] time = parts[2].split("-");
                this.cboStartTime.getSelectionModel().select(time[0].trim());
                this.cboEndTime.getSelectionModel().select(time[1].trim());
            } else if (this.tblRoutine.getSelectionModel().getSelectedCells().get(0).getColumn() == 5) {
                this.cboTimeOfDay.getSelectionModel().select("Night");
                String[] parts = newSelection.getNight().split(",");
                this.cboRoutineActivity.getEditor().setText(parts[0]);
                this.cboRoutineVenue.getEditor().setText(parts[1]);
                String[] time = parts[2].split("-");
                this.cboStartTime.getSelectionModel().select(time[0].trim());
                this.cboEndTime.getSelectionModel().select(time[1].trim());
            } else if (this.tblRoutine.getSelectionModel().getSelectedCells().get(0).getColumn() == 6) {
                this.cboTimeOfDay.getSelectionModel().select("Dawn");
                String[] parts = newSelection.getDawn().split(",");
                this.cboRoutineActivity.getEditor().setText(parts[0]);
                this.cboRoutineVenue.getEditor().setText(parts[1]);
                String[] time = parts[2].split("-");
                this.cboStartTime.getSelectionModel().select(time[0].trim());
                this.cboEndTime.getSelectionModel().select(time[1].trim());
            }
        } catch (Exception e) {

        }
    }

    @FXML
    private void displayEventInput(MouseEvent event) {
        this.eventInput.setExpanded(true);
    }

    @FXML
    private void setTimeRange(ActionEvent event) {
        this.cboStartTime.getItems().clear();
        switch (this.cboTimeOfDay.getSelectionModel().getSelectedItem()) {
            case "Morning":
                loadTime();
                break;
            case "Mid-Day":
                this.cboStartTime.getItems().add("10:00 AM");
                this.cboStartTime.getItems().add("10:15 AM");
                this.cboStartTime.getItems().add("10:30 AM");
                this.cboStartTime.getItems().add("10:45 AM");

                this.cboStartTime.getItems().add("11:00 AM");
                this.cboStartTime.getItems().add("11:15 AM");
                this.cboStartTime.getItems().add("11:30 AM");
                this.cboStartTime.getItems().add("11:45 AM");

                this.cboStartTime.getItems().add("12:00 PM");
                this.cboStartTime.getItems().add("12:15 PM");
                this.cboStartTime.getItems().add("12:30 PM");
                this.cboStartTime.getItems().add("12:45 PM");

                this.cboStartTime.getItems().add("01:00 PM");
                this.cboStartTime.getItems().add("01:15 PM");
                this.cboStartTime.getItems().add("01:30 PM");
                this.cboStartTime.getItems().add("01:45 PM");

                this.cboStartTime.getItems().add("02:00 PM");
                this.cboStartTime.getItems().add("02:15 PM");
                this.cboStartTime.getItems().add("02:30 PM");
                this.cboStartTime.getItems().add("02:45 PM");

                this.cboEndTime.getSelectionModel().select("02:45 PM");
                break;
            case "Afternoon":
                this.cboStartTime.getItems().add("12:00 PM");
                this.cboStartTime.getItems().add("12:15 PM");
                this.cboStartTime.getItems().add("12:30 PM");
                this.cboStartTime.getItems().add("12:45 PM");

                this.cboStartTime.getItems().add("01:00 PM");
                this.cboStartTime.getItems().add("01:15 PM");
                this.cboStartTime.getItems().add("01:30 PM");
                this.cboStartTime.getItems().add("01:45 PM");

                this.cboStartTime.getItems().add("02:00 PM");
                this.cboStartTime.getItems().add("02:15 PM");
                this.cboStartTime.getItems().add("02:30 PM");
                this.cboStartTime.getItems().add("02:45 PM");

                this.cboStartTime.getItems().add("03:00 PM");
                this.cboStartTime.getItems().add("03:15 PM");
                this.cboStartTime.getItems().add("03:30 PM");
                this.cboStartTime.getItems().add("03:45 PM");

                this.cboEndTime.getSelectionModel().select("03:45 PM");

                break;
            case "Evening":
                this.cboStartTime.getItems().add("04:00 PM");
                this.cboStartTime.getItems().add("04:15 PM");
                this.cboStartTime.getItems().add("04:30 PM");
                this.cboStartTime.getItems().add("04:45 PM");

                this.cboStartTime.getItems().add("05:00 PM");
                this.cboStartTime.getItems().add("05:15 PM");
                this.cboStartTime.getItems().add("05:30 PM");
                this.cboStartTime.getItems().add("05:45 PM");

                this.cboStartTime.getItems().add("06:00 PM");
                this.cboStartTime.getItems().add("06:15 PM");
                this.cboStartTime.getItems().add("06:30 PM");
                this.cboStartTime.getItems().add("06:45 PM");

                this.cboStartTime.getItems().add("07:00 PM");
                this.cboStartTime.getItems().add("07:15 PM");
                this.cboStartTime.getItems().add("07:30 PM");
                this.cboStartTime.getItems().add("07:45 PM");

                this.cboStartTime.getItems().add("08:00 PM");
                this.cboStartTime.getItems().add("08:15 PM");
                this.cboStartTime.getItems().add("08:30 PM");
                this.cboStartTime.getItems().add("08:45 PM");

                this.cboStartTime.getItems().add("09:00 PM");
                this.cboStartTime.getItems().add("09:15 PM");
                this.cboStartTime.getItems().add("09:30 PM");
                this.cboStartTime.getItems().add("09:45 PM");

                this.cboEndTime.getSelectionModel().select("09:45 PM");

                break;
            case "Night":
                this.cboStartTime.getItems().add("08:00 PM");
                this.cboStartTime.getItems().add("08:15 PM");
                this.cboStartTime.getItems().add("08:30 PM");
                this.cboStartTime.getItems().add("08:45 PM");

                this.cboStartTime.getItems().add("09:00 PM");
                this.cboStartTime.getItems().add("09:15 PM");
                this.cboStartTime.getItems().add("09:30 PM");
                this.cboStartTime.getItems().add("09:45 PM");

                this.cboStartTime.getItems().add("10:00 PM");
                this.cboStartTime.getItems().add("10:15 PM");
                this.cboStartTime.getItems().add("10:30 PM");
                this.cboStartTime.getItems().add("10:45 PM");

                this.cboStartTime.getItems().add("11:00 PM");
                this.cboStartTime.getItems().add("11:15 PM");
                this.cboStartTime.getItems().add("11:30 PM");
                this.cboStartTime.getItems().add("11:45 PM");

                this.cboStartTime.getItems().add("12:00 AM");
                this.cboStartTime.getItems().add("12:15 AM");
                this.cboStartTime.getItems().add("12:30 AM");
                this.cboStartTime.getItems().add("12:45 AM");

                this.cboEndTime.getSelectionModel().select("12:45 AM");

                break;
            case "Dawn":
                this.cboStartTime.getItems().add("12:00 AM");
                this.cboStartTime.getItems().add("12:15 AM");
                this.cboStartTime.getItems().add("12:30 AM");
                this.cboStartTime.getItems().add("12:45 AM");

                this.cboStartTime.getItems().add("1:00 AM");
                this.cboStartTime.getItems().add("1:15 AM");
                this.cboStartTime.getItems().add("1:30 AM");
                this.cboStartTime.getItems().add("1:45 AM");

                this.cboStartTime.getItems().add("2:00 AM");
                this.cboStartTime.getItems().add("2:15 AM");
                this.cboStartTime.getItems().add("2:30 AM");
                this.cboStartTime.getItems().add("2:45 AM");

                this.cboStartTime.getItems().add("3:00 AM");
                this.cboStartTime.getItems().add("3:15 AM");
                this.cboStartTime.getItems().add("3:30 AM");
                this.cboStartTime.getItems().add("3:45 AM");

                this.cboStartTime.getItems().add("4:00 AM");
                this.cboStartTime.getItems().add("4:15 AM");
                this.cboStartTime.getItems().add("4:30 AM");
                this.cboStartTime.getItems().add("4:45 AM");

                this.cboStartTime.getItems().add("5:00 AM");
                this.cboStartTime.getItems().add("5:15 AM");
                this.cboStartTime.getItems().add("5:30 AM");
                this.cboStartTime.getItems().add("5:45 AM");

                this.cboEndTime.getSelectionModel().select("5:45 AM");

                break;
        }
        this.cboStartTime.getSelectionModel().select(0);
    }

    private void loadTime() {
        this.cboStartTime.getItems().add("06:00 AM");
        this.cboStartTime.getItems().add("06:15 AM");
        this.cboStartTime.getItems().add("06:30 AM");
        this.cboStartTime.getItems().add("06:45 AM");

        this.cboStartTime.getItems().add("07:00 AM");
        this.cboStartTime.getItems().add("07:15 AM");
        this.cboStartTime.getItems().add("07:30 AM");
        this.cboStartTime.getItems().add("07:45 AM");

        this.cboStartTime.getItems().add("08:00 AM");
        this.cboStartTime.getItems().add("08:15 AM");
        this.cboStartTime.getItems().add("08:30 AM");
        this.cboStartTime.getItems().add("08:45 AM");

        this.cboStartTime.getItems().add("09:00 AM");
        this.cboStartTime.getItems().add("09:15 AM");
        this.cboStartTime.getItems().add("09:30 AM");
        this.cboStartTime.getItems().add("09:45 AM");

        this.cboStartTime.getItems().add("10:00 AM");
        this.cboStartTime.getItems().add("10:15 AM");
        this.cboStartTime.getItems().add("10:30 AM");
        this.cboStartTime.getItems().add("10:45 AM");

        this.cboStartTime.getItems().add("11:00 AM");
        this.cboStartTime.getItems().add("11:15 AM");
        this.cboStartTime.getItems().add("11:30 AM");
        this.cboStartTime.getItems().add("11:45 AM");

        this.cboStartTime.getItems().add("12:00 PM");
        this.cboStartTime.getItems().add("12:15 PM");
        this.cboStartTime.getItems().add("12:30 PM");
        this.cboStartTime.getItems().add("12:45 PM");

        this.cboStartTime.getItems().add("01:00 PM");
        this.cboStartTime.getItems().add("01:15 PM");
        this.cboStartTime.getItems().add("01:30 PM");
        this.cboStartTime.getItems().add("01:45 PM");

        this.cboStartTime.getItems().add("02:00 PM");
        this.cboStartTime.getItems().add("02:15 PM");
        this.cboStartTime.getItems().add("02:30 PM");
        this.cboStartTime.getItems().add("02:45 PM");

        this.cboStartTime.getItems().add("03:00 PM");
        this.cboStartTime.getItems().add("03:15 PM");
        this.cboStartTime.getItems().add("03:30 PM");
        this.cboStartTime.getItems().add("03:45 PM");

        this.cboStartTime.getItems().add("04:00 PM");
        this.cboStartTime.getItems().add("04:15 PM");
        this.cboStartTime.getItems().add("04:30 PM");
        this.cboStartTime.getItems().add("04:45 PM");

        this.cboStartTime.getItems().add("05:00 PM");
        this.cboStartTime.getItems().add("05:15 PM");
        this.cboStartTime.getItems().add("05:30 PM");
        this.cboStartTime.getItems().add("05:45 PM");

        this.cboStartTime.getItems().add("06:00 PM");
        this.cboStartTime.getItems().add("06:15 PM");
        this.cboStartTime.getItems().add("06:30 PM");
        this.cboStartTime.getItems().add("06:45 PM");

        this.cboStartTime.getItems().add("07:00 PM");
        this.cboStartTime.getItems().add("07:15 PM");
        this.cboStartTime.getItems().add("07:30 PM");
        this.cboStartTime.getItems().add("07:45 PM");

        this.cboStartTime.getItems().add("08:00 PM");
        this.cboStartTime.getItems().add("08:15 PM");
        this.cboStartTime.getItems().add("08:30 PM");
        this.cboStartTime.getItems().add("08:45 PM");

        this.cboStartTime.getItems().add("09:00 PM");
        this.cboStartTime.getItems().add("09:15 PM");
        this.cboStartTime.getItems().add("09:30 PM");
        this.cboStartTime.getItems().add("09:45 PM");

        this.cboStartTime.getItems().add("10:00 PM");
        this.cboStartTime.getItems().add("10:15 PM");
        this.cboStartTime.getItems().add("10:30 PM");
        this.cboStartTime.getItems().add("10:45 PM");

        this.cboStartTime.getItems().add("11:00 PM");
        this.cboStartTime.getItems().add("11:15 PM");
        this.cboStartTime.getItems().add("11:30 PM");
        this.cboStartTime.getItems().add("11:45 PM");

        this.cboStartTime.getItems().add("12:00 AM");
        this.cboStartTime.getItems().add("12:15 AM");
        this.cboStartTime.getItems().add("12:30 AM");
        this.cboStartTime.getItems().add("12:45 AM");

        this.cboStartTime.getItems().add("1:00 AM");
        this.cboStartTime.getItems().add("1:15 AM");
        this.cboStartTime.getItems().add("1:30 AM");
        this.cboStartTime.getItems().add("1:45 AM");

        this.cboStartTime.getItems().add("2:00 AM");
        this.cboStartTime.getItems().add("2:15 AM");
        this.cboStartTime.getItems().add("2:30 AM");
        this.cboStartTime.getItems().add("2:45 AM");

        this.cboStartTime.getItems().add("3:00 AM");
        this.cboStartTime.getItems().add("3:15 AM");
        this.cboStartTime.getItems().add("3:30 AM");
        this.cboStartTime.getItems().add("3:45 AM");

        this.cboStartTime.getItems().add("4:00 AM");
        this.cboStartTime.getItems().add("4:15 AM");
        this.cboStartTime.getItems().add("4:30 AM");
        this.cboStartTime.getItems().add("4:45 AM");

        this.cboStartTime.getItems().add("5:00 AM");
        this.cboStartTime.getItems().add("5:15 AM");
        this.cboStartTime.getItems().add("5:30 AM");
        this.cboStartTime.getItems().add("5:45 AM");

        this.cboEndTime.getItems().addAll(this.cboStartTime.getItems());
        this.cboEventStartTime.getItems().addAll(this.cboStartTime.getItems());
        this.cboEventStartTime.getSelectionModel().select("7:00 AM");
        this.cboStartTime.getSelectionModel().select("7:00 AM");
        this.cboEndTime.getSelectionModel().select("10:00 AM");
        this.cboEventDate.setValue(LocalDate.now());

    }

    private boolean isValidEventInputs() {
        boolean status = false;
        if (this.txtEventName.getText().isEmpty() || this.cboEventVenue.getEditor().getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please supply inputs for event name and venue.");
            alert.initOwner(Alpha.stage); alert.show();
        } else {
            status = true;
        }
        return status;
    }

    private Event setEvent(Event event) {
        if (this.isValidEventInputs()) {
            if (event.getId() == null) {
                String code;
                if (this.txtEventName.getText().length() > 4) {
                    code = this.txtEventName.getText().substring(0, 4);
                } else {
                    code = this.txtEventName.getText();
                }
                Random rand = new Random(System.currentTimeMillis());
                event.setId(code + rand.nextInt(99));
            }
            event.setSemesterID(currentSemester);
            event.setDate(Date.valueOf(this.cboEventDate.getValue()));
            event.setVenue(this.cboEventVenue.getEditor().getText());
            event.setTime(this.cboEventStartTime.getSelectionModel().getSelectedItem());
            event.setType("Event");
            event.setTitle(this.txtEventName.getText());
            event.setNote(this.txaEventNote.getText());
        }
        return event;
    }

    private void getEventTable() {
        this.eventData = FXCollections.observableArrayList();
        try {
            Query query = this.entitymanager.createQuery("SELECT e FROM Event e WHERE e.semesterID = ?1 AND e.type = 'Event'");
            query.setParameter(1, this.currentSemester);
            query.getResultList().stream().forEach((event) -> {
                this.eventData.add((Event) event);
            });
            this.tblEvent.setItems(eventData);
            this.setEventTableColumns();
            this.tblEvent.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Event> obs, Event oldSelection, Event newSelection) -> {

                if (newSelection != null) {
                    this.currentEvent = (Event) newSelection;
                    Event content = (Event) newSelection;
                    this.txtEventName.setText(content.getTitle());
                    this.cboEventVenue.getEditor().setText(content.getVenue());
                    this.cboEventStartTime.getSelectionModel().select(content.getTime());
                    try {
                        this.cboEventDate.getEditor().setText(sfm.format(content.getDate()));
                    } catch (ParseException ex) {
                        Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.txaEventNote.setText(content.getNote());

                }

            });
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(e.getLocalizedMessage());
            alert.setHeaderText("Error preparing events' table");
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
        column16.setPrefWidth(netWidth / 8);
        this.tblEvent.getColumns().add(column16);

        TableColumn<Event, String> column17 = new TableColumn();
        column17.setVisible(true);
        column17.setText("TIME");
        column17.setCellValueFactory(new PropertyValueFactory("time"));
        column17.setPrefWidth(netWidth / 8);
        this.tblEvent.getColumns().add(column17);

        TableColumn<Event, String> column18 = new TableColumn();
        column18.setVisible(true);
        column18.setText("NOTE");
        column18.setCellValueFactory(new PropertyValueFactory("note"));
        column18.setPrefWidth(netWidth / 4);
        this.tblEvent.getColumns().add(column18);
    }

    private void clearEventInput() {
        this.txtEventName.clear();
        this.txaEventNote.clear();
        this.cboEventVenue.getEditor().clear();
        this.cboEventDate.setValue(LocalDate.now());
    }

    private void setLocations() {
        Query query = entitymanager.createQuery("SELECT l FROM Location l WHERE l.type LIKE 'venue' AND l.campus LIKE " + "'" + DBConnection.campus + "'");
        List<Location> locationList = query.getResultList();
        locationList.stream().forEach((venue) -> {
            this.cboEventVenue.getItems().add(venue.getName());
        });
        this.cboRoutineVenue.getItems().addAll(this.cboEventVenue.getItems());
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
