/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javax.persistence.Query;
import model.CourseRead;
import model.Data;
import model.Document;
import model.Semester;
import org.controlsfx.control.Notifications;
import view.Alpha;
import view.ScreensController;

/**
 * FXML Controller class
 *
 * @author Jake
 */
public class GradingController implements Initializable, ControlledScreen {

	public Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	public double width = screenSize.getWidth(), height = screenSize.getHeight();
	ScreensController myController;
	private Document currentDocument;
	private String selectedFileName;
	private int totalCredit;
	@FXML
	private TitledPane semesterOutput;
	@FXML
	private TitledPane generalOutput;
	private List courseList;
	private ObservableList<Data> calculatorList;
	private double totalGradePoint;
	private double gradePointAverage;
	private int counter;
	private CourseRead currentCourseRead;
	private double totalGradePointAverage;
	@FXML
	private MenuItem walletMenuItem;
	private double cummulativeGPA;
	@FXML
	private Text txtItemInfo;
	private boolean showResitNotification;

	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;
		baseContainer.setPrefSize(width - (width * 0.5 / 100), height - (height * 4 / 100));
	}
	private String semesterMarker = "";
	private String userMarker = "";
	private ObservableList<Document> documentData;
	private Semester currentSemester;
	javax.persistence.EntityManager entitymanager;
	public static boolean refresh = true;

//	private Map<String, CourseRead> gradesMap = new HashMap();

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
	private Label lblCurrentSemester, lblClass;
	@FXML
	private TextField txtCGPA;
	@FXML
	private TextField txtClass;
	@FXML
	private TextArea txaGPASummary;
	@FXML
	private TableView<Data> tblGrading;
	@FXML
	private ComboBox<String> cboCurrentSemester;
	static DecimalFormat df = new DecimalFormat("#.###");

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		entitymanager = DBConnection.getEntityManger();
		animateClassLabel();
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
			Query query = this.entitymanager.createQuery("SELECT s FROM Semester s WHERE s.studentID = ?1 AND s.name = ?2");
			query.setParameter(1, Alpha.currentUser);
			query.setParameter(2, this.cboCurrentSemester.getSelectionModel().getSelectedItem());
			if (!query.getResultList().isEmpty()) {
				this.currentSemester = (Semester) query.getSingleResult();
				this.lblCurrentSemester.setText(this.cboCurrentSemester.getSelectionModel().getSelectedItem());
			}

			query = this.entitymanager.createQuery("SELECT c FROM CourseRead c WHERE C.student = ?1 AND c.semester = ?2 ");
			query.setParameter(1, Alpha.currentUser);
			query.setParameter(2, this.cboCurrentSemester.getSelectionModel().getSelectedItem());
			courseList = query.getResultList();
			totalCredit = 0;
//			this.gradesMap.clear();
			courseList.stream().forEach((item) -> {
				this.totalCredit += ((CourseRead) item).getCredit();
//				this.gradesMap.put(((CourseRead) item).getCode(), ((CourseRead) item));
				//Retrieving grades and points

			});

			this.txtTotalCourses.setText("" + courseList.size());
			this.txtTotalCredits.setText("" + this.totalCredit);
			this.txtGradePointAverage.setText("0");
			this.txtTotalGradePoint.setText("0");
			this.setCalculator();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText(e.getMessage());
			alert.setHeaderText("Error reseting current semester calculations");
			alert.initOwner(Alpha.stage);
			alert.show();
		}

	}

	@FXML
	private void resetPage(MouseEvent event) {
		this.semesterMarker = "";
	}

	@FXML
	private void LoadPageData(MouseEvent event) {
		getSemesterDetails();
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
				this.totalGradePointAverage = 0;
				query.getResultList().stream().forEach((semester) -> {
					Semester currentSem = (Semester) semester;
					this.cboCurrentSemester.getItems().add(currentSem.getName());
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

				query = this.entitymanager.createQuery("SELECT c FROM CourseRead c WHERE C.student = ?1 AND c.semester = ?2 ");
				query.setParameter(1, Alpha.currentUser);
				query.setParameter(2, this.cboCurrentSemester.getSelectionModel().getSelectedItem());
				courseList = query.getResultList();
				totalCredit = 0;
			} catch (Exception e) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText(e.getMessage());
				alert.setHeaderText("Error retrieving existing semester course list");
				alert.initOwner(Alpha.stage);
				alert.show();
			}
			this.userMarker = Alpha.currentUser.getUserName();
			this.semesterMarker = Alpha.currentUser.getCurrentSem();
			this.setCalculator();

		}
	}

	private void setCalculator() {
		Data content = new Data();
		this.txaGradePointSummary.setText("");
		totalGradePoint = 0;
		this.txtItemInfo.setText("");
		 showResitNotification = false;
		calculatorList = content.prepareCalculator(courseList);
		calculatorList.stream().forEach((course) -> {
			this.setGradingScheme((ComboBox) course.getNode1());
			if(course.getFourth().equals("E")){
				showResitNotification = true;
			}
			//retrieving saved grades
			CourseRead thisCourse = ((CourseRead) this.courseList.get(calculatorList.indexOf(course)));

			if (thisCourse.getGrade().equals("")) {

				((ComboBox) course.getNode1()).getSelectionModel().select("IC");
			} else {

				((ComboBox) course.getNode1()).getSelectionModel().select(thisCourse.getGrade());
			}

			switch (((ComboBox) course.getNode1()).getSelectionModel().getSelectedItem().toString()) {
				case "A":
					((Label) course.getNode2()).setText("4");
					break;
				case "B+":
					((Label) course.getNode2()).setText("3.5");
					break;
				case "B":
					((Label) course.getNode2()).setText("3");
					break;
				case "C+":
					((Label) course.getNode2()).setText("2.5");
					break;
				case "C":
					((Label) course.getNode2()).setText("2");
					break;
				case "D+":
					((Label) course.getNode2()).setText("1.5");
					break;
				case "D":
					((Label) course.getNode2()).setText("1");
					break;
				case "E":
					((Label) course.getNode2()).setText("0");
					
					break;
				default:
					((Label) course.getNode2()).setText("0");
					break;
			}

			((ComboBox) course.getNode1()).setOnAction((event) -> {
				this.semesterOutput.setExpanded(true);
				this.currentCourseRead = (CourseRead) this.courseList.get(calculatorList.indexOf(course));
				switch (((ComboBox) course.getNode1()).getSelectionModel().getSelectedItem().toString()) {
					case "A":
						((Label) course.getNode2()).setText("4");
						break;
					case "B+":
						((Label) course.getNode2()).setText("3.5");
						break;
					case "B":
						((Label) course.getNode2()).setText("3");
						break;
					case "C+":
						((Label) course.getNode2()).setText("2.5");
						break;
					case "C":
						((Label) course.getNode2()).setText("2");
						break;
					case "D+":
						((Label) course.getNode2()).setText("1.5");
						break;
					case "D":
						((Label) course.getNode2()).setText("1");
						break;
					case "E":
						((Label) course.getNode2()).setText("0");
						try {
							Toolkit.getDefaultToolkit().beep();

							if (((ComboBox) course.getNode1()).getSelectionModel().getSelectedItem().toString().equals("E")) {
								Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
								alert.setTitle("SELECTION ALERT");
								alert.initOwner(Alpha.stage);
								alert.setHeaderText("Comfirm Grade Selection");
								alert.setContentText("Are you sure you want to save grade 'E'?");
								alert.getButtonTypes().clear();
								alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
								Optional<ButtonType> result = alert.showAndWait();
								if (result.get() == ButtonType.YES) {
									this.currentCourseRead.setResitGrade(((ComboBox) course.getNode1()).getSelectionModel().getSelectedItem().toString());
									course.setFourth("E");
//									CourseRead currentCourse = this.gradesMap.get(course.getFirst());
//									this.gradesMap.remove(course.getFirst());
//									currentCourse.setGrade(((ComboBox) course.getNode1()).getSelectionModel().getSelectedItem().toString());
//									currentCourse.setResitGrade(((ComboBox) course.getNode1()).getSelectionModel().getSelectedItem().toString());
//									this.gradesMap.put(currentCourse.getCode(), currentCourse);
									Notifications.create().darkStyle().position(Pos.BOTTOM_RIGHT).title("SELECTION ALERT").text("This grade requires that you attempt this course again to improve the grade").showInformation();
									calculateGPA();
								} else {
									loadSemesterSchedule(new ActionEvent());
									return;
								}
							}

						} catch (Exception e) {

						}

						break;
					default:
						((Label) course.getNode2()).setText("0");
						break;
				}
				if (course.getFourth().equals("E")&&!((ComboBox) course.getNode1()).getSelectionModel().getSelectedItem().toString().equals("E") ) {

					//set points for resit grade
					Notifications.create().darkStyle().position(Pos.BOTTOM_RIGHT).title("IMPROVED GRADE").text("This is an improved grand in support of a trailed course").showInformation();
					Double current = Double.parseDouble(((Label) course.getNode2()).getText());
					((Label) course.getNode2()).setText("" + current / 2.0);

				}
				
				calculateGPA();

				try {
					
					this.currentCourseRead.setGrade(((ComboBox) course.getNode1()).getSelectionModel().getSelectedItem().toString());
					this.currentSemester.setGpa(this.gradePointAverage);
					this.currentSemester.setTotalCredit(this.totalCredit);
					this.currentSemester.setNumberOfCourses(counter);
					this.entitymanager.getTransaction().begin();
					this.entitymanager.merge(currentCourseRead);
					this.entitymanager.merge(currentSemester);
					this.entitymanager.getTransaction().commit();
					this.setGeneralPerformance();
				} catch (Exception e) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setHeaderText("Error saving current calculation");
					alert.setContentText(e.getLocalizedMessage());
					alert.initOwner(Alpha.stage);
					alert.show();
				}
				this.saveCGPA();
				PerformanceController.refresh = true;
			});
		});
		if(this.showResitNotification){
			Notifications.create().darkStyle().position(Pos.BOTTOM_RIGHT).title("RESIT ALERT").text("This semester's output contains at least one referal paper").showInformation();
		}
		this.tblGrading.setItems(calculatorList);
		this.tblGrading.getColumns().clear();
		setTableColumns();
		calculateGPA();

	}

	private void setGradingScheme(ComboBox box) {
		box.getItems().add("A");
		box.getItems().add("B+");
		box.getItems().add("B");
		box.getItems().add("C+");
		box.getItems().add("C");
		box.getItems().add("D+");
		box.getItems().add("D");
		box.getItems().add("E");
		box.getItems().add("IC");
		box.getSelectionModel().select("IC");
	}

	private void setTableColumns() {
		double netWidth = (this.tblGrading.getWidth() - (this.tblGrading.getWidth() * 0.75 / 100));

		TableColumn<Data, String> column11 = new TableColumn();
		column11.setVisible(true);
		column11.setText("CODE");
		column11.setCellValueFactory(new PropertyValueFactory("first"));
		column11.setPrefWidth(netWidth / 6);
		this.tblGrading.getColumns().add(column11);

		TableColumn<Data, String> column12 = new TableColumn();
		column12.setVisible(true);
		column12.setText("TITLE");
		column12.setCellValueFactory(new PropertyValueFactory("second"));
		column12.setPrefWidth(netWidth / 2);
		this.tblGrading.getColumns().add(column12);

		TableColumn<Data, String> column14 = new TableColumn();
		column14.setVisible(true);
		column14.setText("GRADE");
		column14.setCellValueFactory(new PropertyValueFactory("node1"));
		column14.setPrefWidth(netWidth / 9);
		this.tblGrading.getColumns().add(column14);

		TableColumn<Data, String> column15 = new TableColumn();
		column15.setVisible(true);
		column15.setText("POINT");
		column15.setCellValueFactory(new PropertyValueFactory("node2"));
		column15.setPrefWidth(netWidth / 9);
		this.tblGrading.getColumns().add(column15);

		TableColumn<Data, String> column13 = new TableColumn();
		column13.setVisible(true);
		column13.setText("CREDITS");
		column13.setCellValueFactory(new PropertyValueFactory("third"));
		column13.setPrefWidth(netWidth / 9);
		this.tblGrading.getColumns().add(column13);

	}

	private void calculateGPA() {
		this.txaGradePointSummary.setText("");
		this.totalGradePoint = 0;
		counter = 0;
		gradePointAverage = 0;
		totalCredit = 0;
		this.tblGrading.getItems().forEach((Data data) -> {

			if (!((ComboBox) data.getNode1()).getSelectionModel().getSelectedItem().toString().equals("IC")) {
				String thisSummary;
				double gradePoint = Double.parseDouble(((Label) data.getNode2()).getText()) * Double.parseDouble(data.getThird());
				if (data.getFourth().equals("E")) {

					thisSummary = data.getFirst() + ":   credit by RESIT point makes " + gradePoint;
				} else {
					thisSummary = data.getFirst() + ":   credit by point makes " + gradePoint;
				}
				StringBuilder sbuilder = new StringBuilder(txaGradePointSummary.getText());
				if (!txaGradePointSummary.getText().isEmpty()) {
					sbuilder.append(".\n\n");
				}
				sbuilder.append(thisSummary);
				this.txaGradePointSummary.setText(sbuilder.toString());
				this.totalCredit += Double.parseDouble(data.getThird());
				this.totalGradePoint += gradePoint;
				this.gradePointAverage = totalGradePoint / totalCredit;
				++counter;
			}
			this.txtTotalGradePoint.setText("" + this.totalGradePoint);
			this.txtGradePointAverage.setText("" + df.format(gradePointAverage));
			this.txtTotalCourses.setText("" + (counter));
			this.txtTotalCredits.setText("" + totalCredit);

		});
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

	private void setGeneralPerformance() {
		Query query = this.entitymanager.createQuery("SELECT s FROM Semester s WHERE s.studentID = ?1");
		query.setParameter(1, Alpha.currentUser);
		this.counter = 0;
		this.totalGradePointAverage = 0;
		this.txaGPASummary.setText("");
		query.getResultList().stream().forEach((semester) -> {

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
	}

	private void saveCGPA() {
		try {
			Query query = this.entitymanager.createQuery("SELECT s FROM Semester s WHERE s.studentID = ?1");
			query.setParameter(1, Alpha.currentUser);
			this.counter = 0;
			this.totalGradePointAverage = 0.0;
			query.getResultList().stream().forEach((semester) -> {
				Semester currentSem = (Semester) semester;

				//setting current semester
				if (((Semester) semester).getGpa() > 0) {
					this.totalGradePointAverage += ((Semester) semester).getGpa();
					++counter;
				}
				if (currentSem.getName().equals(this.cboCurrentSemester.getSelectionModel().getSelectedItem())) {
					this.cummulativeGPA = this.totalGradePointAverage / counter;
					this.currentSemester = currentSem;
				}
			});
			this.entitymanager.getTransaction().begin();
			this.currentSemester.setCgpa(this.cummulativeGPA);
			this.entitymanager.getTransaction().commit();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Error saving CGPA");
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
	private void startInfo(ActionEvent event) {
            Alpha.startInfo();
	}

}
