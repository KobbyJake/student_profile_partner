package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
//import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Kobby
 */
public class DBConnection {

    static Properties props = new Properties();
    static Map propsMap = new HashMap();
    String hostName = null;
    String port = null;
    String database = null;
    String userName = null;
    String password = null;
    String driver = null;
    String jndi = null;
    String dbURL = null;
    String location = null;
    static String campus = "Kumasi";

    public DBConnection() {
        //Looks for properties file in the root of project directory

        try (InputStream in = Files.newInputStream(FileSystems.getDefault().getPath(System.getProperty("user.dir") + File.separator + "dbProps.properties"));) {

            props.load(in);
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        loadProperties();
    }

    public final void loadProperties() {
        hostName = props.getProperty("hostName");

        port = props.getProperty("portNumber");

        database = props.getProperty("dbName");

        userName = props.getProperty("userName");
        propsMap.put("javax.persistence.jdbc.user", userName);

//        password = props.getProperty("password");
        password = "ZD5AsF8RN5Uf2teA";
        propsMap.put("javax.persistence.jdbc.password", password);

        driver = props.getProperty("driver");
        propsMap.put("javax.persistence.jdbc.driver", driver);
        dbURL = "jdbc:mysql://lib//propartDB.db";
        propsMap.put("javax.persistence.jdbc.url", dbURL);
        campus = props.getProperty("campus");

    }


    public static javax.persistence.EntityManager getEntityManger() {
        javax.persistence.EntityManager entitymanager = null;
        try {
            EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("ProPartnerPU", propsMap);
            entitymanager = emfactory.createEntityManager();

        } catch (Exception ex) {

            // adding alert
            {
                Alert someAlert = new Alert(Alert.AlertType.ERROR, "Password Mismatch");
                someAlert.initModality(Modality.APPLICATION_MODAL);
                someAlert.getDialogPane().setHeaderText("JDBC connection error");
                someAlert.getDialogPane().setContentText(ex.getMessage());
                someAlert.showAndWait();
            }
        }
        return entitymanager;
    }
public static  Connection conn = null;
	public static Connection dbconnect(){
		try{
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:lib//propartDB.db");
		}catch(Exception ex){
                    ex.printStackTrace();
//			JOptionPane.showMessageDialog(null,ex);
		}
		return conn;
	}
}
