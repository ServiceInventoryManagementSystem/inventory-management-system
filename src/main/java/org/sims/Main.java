package org.sims;

<<<<<<< HEAD
=======
import java.net.UnknownHostException;
>>>>>>> 1d13b11... Hacky way to pass settings along

import org.sims.discovery.manager.DiscoveryManager;
import org.sims.discovery.manager.HybernateResourceManager;
import org.sims.discovery.mdns.DnsDiscovery;
import org.sims.discovery.models.BasicService;
import org.sims.discovery.models.IHasId;
import org.sims.discovery.models.IRelatedParty;
import org.sims.discovery.models.IService;
import org.sims.discovery.models.ServiceAdapter;
import org.sims.discovery.models.ServiceWrapper;
import org.sims.discovery.ws.WsDiscovery;
import org.sims.utils.MagicWrapper;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;



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

    try{
      manager.registerDiscovery(WsDiscovery.class, new WsDiscovery.WsSettings());
      manager.registerDiscovery(DnsDiscovery.class, new DnsDiscovery.DnsSettings());
    } catch(UnknownHostException e){
      System.err.println(e);
    }
    manager.initAll();
    manager.startAll();

  }
}

