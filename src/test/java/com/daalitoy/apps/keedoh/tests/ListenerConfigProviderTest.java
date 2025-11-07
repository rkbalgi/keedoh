package com.daalitoy.apps.keedoh.tests;

import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.data.providers.ListenerConfigProvider;
import com.daalitoy.apps.keedoh.ui.util.KeedohConstants;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ListenerConfigProviderTest {

  @Test
  @Disabled
  public void ListenerConfigProvider_readFromClasspathResource() {

    System.setProperty(
        KeedohConstants.KEEDOH_CONFIG_DIR,
        Paths.get(System.getProperty("user.home"), ".keedoh").toString());
    List<ListenerConfig> configs = new ListenerConfigProvider().allConfigs();
    Assertions.assertTrue(configs.size() > 0);
  }

  @Test
  public void alwaysWorks() {
    Assertions.assertTrue(true);
  }
}
