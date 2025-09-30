package com.apc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF for API endpoints
            .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll() // Allow authentication endpoints
                .antMatchers("/h2-console/**").permitAll() // Allow H2 console access
                .antMatchers("/", "/login.html", "/register.html", "/dashboard.html", "/inventory.html", "/billing.html", "/purchase.html").permitAll() // Allow all static pages
                .antMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll() // Allow static resources
                .antMatchers("/api/**").authenticated() // Require authentication for API endpoints only
                .anyRequest().permitAll() // Allow all other requests (for static content)
            .and()
            .httpBasic() // Keep HTTP Basic for API endpoints
            .and()
            .formLogin().disable(); // Disable default form login

        // Allow H2 console to be embedded in frames
        http.headers().frameOptions().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}