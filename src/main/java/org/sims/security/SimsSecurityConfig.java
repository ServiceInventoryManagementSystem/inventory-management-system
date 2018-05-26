package org.sims.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;


@Configuration
@EnableWebSecurity
public class SimsSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private AuthenticationEntryPoint authEntryPoint;
  @Value("${sims.auth.username:admin}")
  public String username;
  @Value("${sims.auth.password:password}")
  public String password;
  @Value("${sims.auth.allow:HEAD|OPTIONS}")
  public String allow;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry r = http.csrf().disable().authorizeRequests();
    String[] nonRestrictedActions = allow.split("\\s*[,|]\\s*");
    for(String action : nonRestrictedActions){
      r = r.antMatchers(HttpMethod.resolve(action)).permitAll();
      System.out.println("ENABLE:" + action + " " + HttpMethod.resolve(action));
    }
    r.anyRequest().authenticated()
      .and().httpBasic()
      .authenticationEntryPoint(authEntryPoint);
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser(username).password("{noop}" + password).roles("all");
    System.out.println("USERNAME: " + username);
  }

}