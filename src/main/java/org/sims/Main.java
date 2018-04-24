package org.sims;

import org.sims.discovery.manager.DiscoveryManager;
import org.sims.discovery.manager.HybernateResourceManager;
import org.sims.discovery.mdns.DnsDiscovery;
import org.sims.discovery.ws.WsDiscovery;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class Main {
  public static void main(String[] args) {


    ApplicationContext ctx = SpringApplication.run(Main.class, args);

    HybernateResourceManager resourceManager = new HybernateResourceManager();
    ctx.getAutowireCapableBeanFactory().autowireBeanProperties(
      resourceManager, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true
    );
    
    DiscoveryManager manager = new DiscoveryManager(resourceManager);
    

    manager.registerDiscovery(WsDiscovery.class);
    manager.registerDiscovery(DnsDiscovery.class);
    manager.initAll();

    manager.startAll();
  }
}