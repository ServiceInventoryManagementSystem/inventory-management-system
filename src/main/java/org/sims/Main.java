package org.sims;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.catalina.startup.HostConfig;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.net.InetAddresses;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class Main {
  public static void main(String[] args) {

    ApplicationContext ctx = SpringApplication.run(Main.class, args);

   
    HybernateResourceManager resourceManager = new HybernateResourceManager();
    
    SimsConfig config = new SimsConfig(ctx.getEnvironment());
    
    ctx.getAutowireCapableBeanFactory().autowireBeanProperties(
      resourceManager, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true
    );
    
    DiscoveryManager manager = new DiscoveryManager(resourceManager);

    WsDiscovery.WsSettings wsSettings = new WsDiscovery.WsSettings(config.address);
    DnsDiscovery.DnsSettings dnsSettings = new DnsDiscovery.DnsSettings(config.address, ctx.getEnvironment());

    manager.registerDiscovery(WsDiscovery.class, wsSettings);
    manager.registerDiscovery(DnsDiscovery.class, dnsSettings);

    manager.initAll();
    manager.startAll();
  }

  public static class SimsConfig{
    public InetAddress address;
    public SimsConfig(Environment env){
      NetworkInterface networkInterface = null;
      try{
        address = Inet4Address.getLocalHost();
        networkInterface = NetworkInterface.getByInetAddress(address);
      }catch(SocketException | UnknownHostException e){
        System.err.println("Could not get addr or if");
        System.err.println(e);
        System.exit(-1);
      }
  
      String iface = env.getProperty("sims.bindIf");
      String ip = env.getProperty("sims.bindIp");
      
      try{
        if(iface != null){
          networkInterface = NetworkInterface.getByName(iface);
          Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
          while(addresses.hasMoreElements()){
            InetAddress addr = addresses.nextElement();
            if(!addr.isLoopbackAddress()){
              address = addr;
              break;
            }
          }
        }
  
        if(ip != null){
          address = Inet4Address.getByName(ip);
        }
  
      }catch(SocketException | UnknownHostException e){
        System.err.println(e);
      }
    }
  }

}

