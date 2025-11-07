package com.daalitoy.apps.keedoh.guice.modules;

import com.daalitoy.apps.keedoh.data.providers.ConnectorConfigProvider;
import com.daalitoy.apps.keedoh.data.providers.ListenerConfigProvider;
import com.daalitoy.apps.keedoh.system.JsonSpecManager;
import com.daalitoy.apps.keedoh.system.SpecManager;
import com.google.inject.AbstractModule;

public class KeedohDefaultModule extends AbstractModule {

  @Override
  public void configure() {

    ConnectorConfigProvider connectorConfigProvider = new ConnectorConfigProvider();
    ListenerConfigProvider listenerConfigProvider = new ListenerConfigProvider();

    bind(ConnectorConfigProvider.class).toInstance(connectorConfigProvider);
    bind(ListenerConfigProvider.class).toInstance(listenerConfigProvider);

    bind(SpecManager.class).to(JsonSpecManager.class).asEagerSingleton();
  }
}
