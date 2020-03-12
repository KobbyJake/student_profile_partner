/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import view.Alpha;

/**
 *
 * @author Kobby
 */
public class Data {

    private StringProperty first;
    private StringProperty second;
    private StringProperty third;
    private StringProperty fourth;
    private StringProperty fifth;
    private StringProperty sixth;
    private StringProperty seventh;
    private StringProperty eighth;
    private StringProperty nineth;
    private StringProperty tenth;
    private StringProperty eleventh;
    private StringProperty twelveth;
    private StringProperty thirteenth;
    private ObjectProperty node1;
    private ObjectProperty node2;
    private ObjectProperty node3;

//                                                  String first,second,third,fourth,fifth,sixth,seventh,eighth,eighth,nineth,tenth,eleventh,twelveth,thirteenth;
    public Data(String first, String second, String third, String fourth, String fifth, String sixth, String seventh, String eighth, String nineth, String tenth, String eleventh, String twelveth, String thirteenth, Object node1, Object node2, Object node3) {
        super();
        this.first = new SimpleStringProperty(first);
        this.second = new SimpleStringProperty(second);
        this.third = new SimpleStringProperty(third);
        this.fourth = new SimpleStringProperty(fourth);
        this.fifth = new SimpleStringProperty(fifth);
        this.sixth = new SimpleStringProperty(sixth);
        this.seventh = new SimpleStringProperty(seventh);
        this.eighth = new SimpleStringProperty(eighth);
        this.nineth = new SimpleStringProperty(nineth);
        this.tenth = new SimpleStringProperty(tenth);
        this.eleventh = new SimpleStringProperty(eleventh);
        this.twelveth = new SimpleStringProperty(twelveth);
        this.thirteenth = new SimpleStringProperty(thirteenth);
        this.node1 = new SimpleObjectProperty(node1);
        this.node2 = new SimpleObjectProperty(node2);
        this.node3 = new SimpleObjectProperty(node3);

    }

    public Data() {
        this("", "", "", "", "", "", "", "", "", "", "", "", "", null, null, null);
    }

    public StringProperty firstProperty() {
        return first;
    }

    public String getFirst() {
        return first.get();
    }

    public void setFirst(String value) {
        this.first.set(value);
    }

    public StringProperty secondProperty() {
        return second;
    }

    public String getSecond() {
        return second.get();
    }

    public void setSecond(String value) {
        this.second.set(value);
    }

    public StringProperty thirdProperty() {
        return third;
    }

    public String getThird() {
        return third.get();
    }

    public void setThird(String value) {
        this.third.set(value);
    }

    public StringProperty fourthProperty() {
        return fourth;
    }

    public String getFourth() {
        return fourth.get();
    }

    public void setFourth(String value) {
        this.fourth.set(value);
    }

    public StringProperty fifthProperty() {
        return fifth;
    }

    public String getFifth() {
        return fifth.get();
    }

    public void setFifth(String value) {
        this.fifth.set(value);
    }

    public StringProperty sixthProperty() {
        return sixth;
    }

    public String getSixth() {
        return sixth.get();
    }

    public void setSixth(String value) {
        this.sixth.set(value);
    }

    public StringProperty seventhProperty() {
        return seventh;
    }

    public String getSeventh() {
        return seventh.get();
    }

    public void setSeventh(String value) {
        this.seventh.set(value);
    }

    public StringProperty eighthProperty() {
        return eighth;
    }

    public String getEighth() {
        return eighth.get();
    }

    public void setEighth(String value) {
        this.eighth.set(value);
    }

    public StringProperty ninethProperty() {
        return nineth;
    }

    public String getNineth() {
        return nineth.get();
    }

    public void setNineth(String value) {
        this.nineth.set(value);
    }

    public StringProperty tenthProperty() {
        return tenth;
    }

    public String getTenth() {
        return tenth.get();
    }

    public void setTenth(String value) {
        this.tenth.set(value);
    }

    public StringProperty eleventhProperty() {
        return eleventh;
    }

    public String getEleventh() {
        return eleventh.get();
    }

    public void setEleventh(String value) {
        this.eleventh.set(value);
    }

    public StringProperty twelvethProperty() {
        return twelveth;
    }

    public String getTwelveth() {
        return twelveth.get();
    }

    public void setTwelveth(String value) {
        this.twelveth.set(value);
    }

    public StringProperty thirteenthProperty() {
        return thirteenth;
    }

    public String getThirteenth() {
        return thirteenth.get();
    }

    public void setThirteenth(String value) {
        this.thirteenth.set(value);
    }

    public ObjectProperty node1Property() {
        return node1;
    }

    public Object getNode1() {
        return node1.get();
    }

    public void setNode1(Object value) {
        this.node1.set(value);
    }

    public ObjectProperty node2Property() {
        return node2;
    }

    public Object getNode2() {
        return node2.get();
    }

    public void setNode2(Object value) {
        this.node2.set(value);
    }

    public ObjectProperty node3Property() {
        return node3;
    }

    public Object getNode3() {
        return node3.get();
    }

    public void setNode3(Object value) {
        this.node3.set(value);
    }

    public ObservableList prepareCourses(List<CourseRegistry> courseList, List<CourseRead> registeredCourseList) {
        ObservableList<Data> data = FXCollections.observableArrayList();
        courseList.forEach((item) -> {
            data.add(createCourseItem((CourseRegistry) item, registeredCourseList));
        });

        return data;
    }

    private Data createCourseItem(CourseRegistry courseReg, List<CourseRead> registeredCourseList) {

        Data courseData = new Data();
        courseData.setFirst(courseReg.getCode());
        courseData.setSecond(courseReg.getTitle());
        courseData.setThird(courseReg.getCredit().toString());
        courseData.setNode1(new TextField());
        courseData.setNode2(new CheckBox());

        registeredCourseList.stream().forEach((item) -> {
            if (item.getCode().equals(courseReg.getCode()) && item.getSemester().equals(Alpha.currentUser.getCurrentSem()) && item.getId().contains(Alpha.currentUser.getId())) {
                TextField someTextField = new TextField();
                someTextField.setText(item.getLecturer());
                courseData.setNode1(someTextField);
                courseData.setFourth(item.getResitGrade());
                if (item.getStatus().equals("active")) {
                    CheckBox thisCheckBox = new CheckBox();
                    thisCheckBox.setSelected(true);
                    courseData.setNode2(thisCheckBox);
                } else {
                    courseData.setNode2(new CheckBox());
                }
            }
        });
        return courseData;
    }

    public Data createCourseItem(CourseRegistry courseReg, String lecturer) {

        Data courseData = new Data();
        courseData.setFirst(courseReg.getCode());
        courseData.setSecond(courseReg.getTitle());
        courseData.setThird(courseReg.getCredit().toString());

        TextField someTextField = new TextField();
        someTextField.setText(lecturer);
        
        courseData.setNode1(someTextField);
        courseData.setNode2(new CheckBox());
        courseData.setNode1(new TextField());
        courseData.setNode2(new CheckBox());

        return courseData;
    }

    public ObservableList<Data> prepareCalculator(List courseList) {
        ObservableList<Data> data = FXCollections.observableArrayList();

        try {

            courseList.forEach((item) -> {
                data.add(createCalculatorItem((CourseRead) item));
            });

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Registration table data creation error");
            alert.setContentText(e.getMessage());
            alert.initOwner(Alpha.stage);
        }
        return data;
    }

    private Data createCalculatorItem(CourseRead courseReg) {
        Data courseData = new Data();
        try {

            ComboBox<String> box = new ComboBox();
            courseData.setFirst(courseReg.getCode());
            courseData.setSecond(courseReg.getTitle());
            courseData.setThird(courseReg.getCredit().toString());
            courseData.setNode1(box);
            courseData.setNode2(new Label("0"));

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Registration table item creation error");
            alert.setContentText(e.getMessage());
            alert.initOwner(Alpha.stage);
        }
        return courseData;
    }

}
