package com.daalitoy.apps.keedoh.tests;

import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.data.providers.ListenerConfigProvider;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ListenerConfigProviderTest {

    @Test
    public void ListenerConfigProvider_readFromClasspathResource() {

        List<ListenerConfig> configs = new ListenerConfigProvider().allConfigs();
        Assert.assertEquals(2, configs.size());
    }
}
