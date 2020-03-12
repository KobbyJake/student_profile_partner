/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.FileOutputStream;
import javafx.scene.control.Alert;
import javafx.scene.media.AudioClip;
import javax.persistence.Query;
import view.Alpha;

/**
 *
 * @author Jake
 */
public class VoicePrompt {

	javax.persistence.EntityManager entitymanager = DBConnection.getEntityManger();
	public static AudioClip welcomeClip;
	public static AudioClip registrationClip;
	public static AudioClip goodbyeClip;
	public static AudioClip passwordClip;
	public static AudioClip actionClip;
	public static AudioClip internetClip;
	public static AudioClip standbyClip;
	public static AudioClip menuClip;
	public static AudioClip goodjobClip;
	

	public VoicePrompt() {
		//        retrieving audios
		try {

			Query query = this.entitymanager.createQuery("SELECT s FROM System s WHERE s.description LIKE 'System sound'");

			if (query.getResultList().size() > 0) {

				query.getResultList().stream().forEach((item) -> {
					
					if (((model.System) item).getName().contains("welcome")) {
						File file = new File("lib/welcome.wav");
						file.deleteOnExit();
						try {
							FileOutputStream fos = new FileOutputStream("lib/welcome.wav");
							fos.write(((model.System) item).getFile());
							fos.close();
							welcomeClip = new AudioClip(file.toURI().toURL().toString());
						} catch (Exception e) {
						}
					}
					if (((model.System) item).getName().contains("registration")) {
						File file = new File("lib/registration.wav");
						file.deleteOnExit();
						try {
							FileOutputStream fos = new FileOutputStream("lib/registration.wav");
							fos.write(((model.System) item).getFile());
							fos.close();
							registrationClip = new AudioClip(file.toURI().toURL().toString());
						} catch (Exception e) {
						}
					}
					if (((model.System) item).getName().contains("goodbye")) {
						File file = new File("lib/goodbye.wav");
						file.deleteOnExit();
						try {
							FileOutputStream fos = new FileOutputStream("lib/goodbye.wav");
							fos.write(((model.System) item).getFile());
							fos.close();
							goodbyeClip = new AudioClip(file.toURI().toURL().toString());
						} catch (Exception e) {
						}
					}
					if (((model.System) item).getName().contains("password reset")) {
						File file = new File("lib/password reset.wav");
						file.deleteOnExit();
						try {
							FileOutputStream fos = new FileOutputStream("lib/password reset.wav");
							fos.write(((model.System) item).getFile());
							fos.close();
							passwordClip = new AudioClip(file.toURI().toURL().toString());
						} catch (Exception e) {
						}
					}
					if (((model.System) item).getName().contains("action")) {
						File file = new File("lib/action.wav");
						file.deleteOnExit();
						try {
							FileOutputStream fos = new FileOutputStream("lib/action.wav");
							fos.write(((model.System) item).getFile());
							fos.close();
							actionClip = new AudioClip(file.toURI().toURL().toString());
						} catch (Exception e) {
						}
					}
					if (((model.System) item).getName().contains("internet")) {
						File file = new File("lib/internet.wav");
						file.deleteOnExit();
						try {
							FileOutputStream fos = new FileOutputStream("lib/internet.wav");
							fos.write(((model.System) item).getFile());
							fos.close();
							internetClip = new AudioClip(file.toURI().toURL().toString());
						} catch (Exception e) {
						}
					}
					if (((model.System) item).getName().contains("standby")) {
						File file = new File("lib/standby.wav");
						file.deleteOnExit();
						try {
							FileOutputStream fos = new FileOutputStream("lib/standby.wav");
							fos.write(((model.System) item).getFile());
							fos.close();
							standbyClip = new AudioClip(file.toURI().toURL().toString());
						} catch (Exception e) {
						}
					}
					if (((model.System) item).getName().contains("menu")) {
						File file = new File("lib/menu.wav");
						file.deleteOnExit();
						try {
							FileOutputStream fos = new FileOutputStream("lib/menu.wav");
							fos.write(((model.System) item).getFile());
							fos.close();
							menuClip = new AudioClip(file.toURI().toURL().toString());
						} catch (Exception e) {
						}
					}
					if (((model.System) item).getName().contains("good job")) {
						File file = new File("lib/goodjob.wav");
						file.deleteOnExit();
						try {
							FileOutputStream fos = new FileOutputStream("lib/goodjob.wav");
							fos.write(((model.System) item).getFile());
							fos.close();
							goodjobClip = new AudioClip(file.toURI().toURL().toString());
						} catch (Exception e) {
						}
					}
				});

			}
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Error retrieving system sounds");
			alert.setContentText(e.getLocalizedMessage());
			alert.initOwner(Alpha.stage);
			alert.show();
		}
	}

}
