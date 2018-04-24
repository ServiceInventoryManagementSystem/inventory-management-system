package org.sims;

import java.io.File;
import java.util.HashMap;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

//import org.sims.discovery.manager.DiscoveryManager;
//import org.sims.discovery.mdns.DnsDiscovery;
//import org.sims.discovery.ws.WsDiscovery;
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


    /*DiscoveryManager manager = new DiscoveryManager();
    ctx.getAutowireCapableBeanFactory().autowireBeanProperties(
            manager, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true
    );

    manager.registerDiscovery(WsDiscovery.class);
    manager.registerDiscovery(DnsDiscovery.class);
    manager.initAll();

    manager.startAll();*/
    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter write = mapper.writerFor(Base64Variant.class);
    //ObjectMapper mapper = new ObjectMapper();
    HashMap<String, String> data = new HashMap<String, String>();
    try{
      write.writeValue(new File("./serviceref.json"), data);
    } catch(Exception e) {

    }
    //write(new File("./serviceref.json"), data);
  }
}