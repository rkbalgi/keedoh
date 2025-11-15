package com.daalitoy.apps.keedoh.ui.util;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

public class KeedohUtils {

  private static final Rectangle bounds =
      GraphicsEnvironment.getLocalGraphicsEnvironment()
          .getDefaultScreenDevice()
          .getDefaultConfiguration()
          .getBounds();

  public KeedohUtils() {
    // TODO Auto-generated constructor stub
  }

  public static int getHeight() {
    return (bounds.height);
  }

  public static double getWidth() {
    return (bounds.width);
  }
}
