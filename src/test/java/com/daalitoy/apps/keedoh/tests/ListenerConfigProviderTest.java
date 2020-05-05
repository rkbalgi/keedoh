package com.daalitoy.apps.keedoh.tests;

import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.data.providers.ListenerConfigProvider;
import com.daalitoy.apps.keedoh.ui.util.KeedohConstants;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

public class ListenerConfigProviderTest {

    @Test
    public void ListenerConfigProvider_readFromClasspathResource() {

        System.setProperty(
                KeedohConstants.KEEDOH_CONFIG_DIR,
                Paths.get(System.getProperty("user.home"), ".keedoh").toString());
        List<ListenerConfig> configs = new ListenerConfigProvider().allConfigs();
        Assert.assertTrue(configs.size() > 0);
    }
}
