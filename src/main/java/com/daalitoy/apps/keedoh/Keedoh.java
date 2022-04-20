package com.daalitoy.apps.keedoh;

import com.daalitoy.apps.keedoh.guice.GuiceInjector;
import com.daalitoy.apps.keedoh.ui.frames.KeedohMainFrame;
import com.google.common.base.Strings;
import com.google.common.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Keedoh {

    private static final Logger log = LogManager.getLogger(Keedoh.class);

    /**
     * @param args
     */
    public static void main(String[] args) {

        // check some prereqs
        String userSpecifiedDir = System.getProperty("keedoh.config.dir", "");
        if (Strings.isNullOrEmpty(userSpecifiedDir)) {
            // let's create a basic setup
            Path keedohDir = Paths.get(System.getProperty("user.home"), ".keedoh");
            System.setProperty("keedoh.config.dir", keedohDir.toString());
            log.info("Keedoh config directory - {}", System.getProperty("keedoh.config.dir"));
            if (keedohDir.toFile().exists()) {
                log.info(
                        "A keedoh setup already exists in user home dir."
                                + " If unintended, please delete the .keedoh folder in home directory and restart the application");
                //System.exit(-2);
            } else {
                try {
                    Path baseDir = Files.createDirectory(keedohDir);
                    Files.createDirectories(baseDir.resolve("scripts"));
                    Files.createDirectories(baseDir.resolve("specs"));

                } catch (IOException e) {
                    log.error("Failed to initialize Keedoh", e);
                    try {
                        Files.delete(keedohDir);
                    } catch (IOException e1) {
                        log.error("cleanup failed", e);
                    }
                }
            }

            try {
                Files.copy(
                        Resources.getResource("keedoh.properties").openStream(),
                        keedohDir.resolve("keedoh.properties"),
                        StandardCopyOption.REPLACE_EXISTING);
                Files.copy(
                        Resources.getResource("specs/specs.yml").openStream(),
                        keedohDir.resolve("specs").resolve("specs/specs.yml"));
                Files.copy(
                        Resources.getResource("specs/sample_spec.yml").openStream(),
                        keedohDir.resolve("specs").resolve("specs/sample_spec.yml"));

                Files.copy(
                        Resources.getResource("scripts/test_iso8583_host.groovy").openStream(),
                        keedohDir.resolve("scripts/test_iso8583_host.groovy"));

                Files.copy(
                        Resources.getResource("connector-configs.json").openStream(),
                        keedohDir.resolve("connector-configs.json"));
                Files.copy(
                        Resources.getResource("listener-configs.json").openStream(),
                        keedohDir.resolve("listener-configs.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.setProperty("keedoh_config_dir", keedohDir.toString());

        } else {
            boolean exists = Files.exists(Paths.get(userSpecifiedDir, "keedoh.properties"));
            if (!exists) {
                log.error(
                        "Corrupt config directory, couldn't find file keedoh.properties. Specified Config Directory = "
                                + userSpecifiedDir);
                System.exit(-1);
            } else {
                // assume everything is good
                System.setProperty("keedoh_config_dir", userSpecifiedDir);
            }
        }
        GuiceInjector.getInjector();

        // , Paths.get(System.getProperty("user.home"),".keedoh"));

        // MahoutDbServer.getInstance().start();
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Throwable t) {
            log.error("", t);
            System.exit(-3);
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
