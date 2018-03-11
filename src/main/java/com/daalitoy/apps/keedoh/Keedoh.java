package com.daalitoy.apps.keedoh;

import com.daalitoy.apps.keedoh.guice.GuiceInjector;
import com.daalitoy.apps.keedoh.ui.frames.KeedohMainFrame;

import javax.swing.*;

public class Keedoh {

    /**
     * @param args
     */
    public static void main(String[] args) {

        GuiceInjector.getInjector();

        // MahoutDbServer.getInstance().start();
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

    /*for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
    if (laf.getName().equals("Nimbus")) {
    	try {
    		UIManager.setLookAndFeel(laf.getClassName());
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }*/
        // System.out.println(laf);

        SwingUtilities.invokeLater(
                () -> {
                    KeedohMainFrame mainFrame = new KeedohMainFrame();
                    GuiceInjector.getInjector().injectMembers(mainFrame);
                    mainFrame.showFrame();
                });
    }
}
