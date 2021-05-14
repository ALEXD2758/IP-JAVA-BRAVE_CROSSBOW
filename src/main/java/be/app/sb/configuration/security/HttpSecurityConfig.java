package be.app.sb.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class HttpSecurityConfig extends WebSecurityConfigurerAdapter {


    //TODO: IntelliJ complains that it can not find the bean
    // with that qualifier (probably due to the fact I moved HttpSecurityConfig class
    // inside configuration/security package (Scope to redefine?)
    @Qualifier("userDetailsServiceImpl")
    @Autowired
    UserDetailsService userDetailsService;

    //Function for allowing the loading of the static resources
    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring()
                .antMatchers("/css/**");
    }

    //Function configuring the HttpSecurity: login/logout access and HTTP session + cookies
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //Permit access to all for the login
                .antMatchers("/login").permitAll()
                //Authentication request parameters
                .anyRequest().authenticated()
                .and()
                    .formLogin().and().httpBasic()
                .and()
                //Configuration of the logout
                    .logout()
                    .logoutUrl("/logout")
                //Invalidate the current HTTP session and its cookies
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll();
        http.csrf().disable();

    }

    //Method for encoding the password used during the authentication
    void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(new Pbkdf2PasswordEncoder());
    }

    //Bean of PasswordEncoder containing Pbkdf2PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }
}