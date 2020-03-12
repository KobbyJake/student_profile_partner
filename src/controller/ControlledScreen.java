package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import view.ScreensController;
import java.awt.Dimension;

/**
 *
 * @author Kobby
 */
public interface ControlledScreen {
//    public static Dimension screenSize;
   //This method will allow the injection of the parent ScreenPane
    public void setScreenParent(ScreensController screenPage);
    public String CurrentUser=null;
    
    
}
