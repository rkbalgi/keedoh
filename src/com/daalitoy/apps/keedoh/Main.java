package com.daalitoy.apps.keedoh;

import javax.swing.SwingUtilities;

import com.daalitoy.apps.keedoh.ui.frames.KeedohMainFrame;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// MahoutDbServer.getInstance().start();

		/*for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
			if (laf.getName().equals("Nimbus")) {
				try {
					UIManager.setLookAndFeel(laf.getClassName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println(laf);
		}*/
		
		

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new KeedohMainFrame();

			}
		});

	}

}
