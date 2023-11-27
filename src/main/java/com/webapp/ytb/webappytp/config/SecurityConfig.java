package com.webapp.ytb.webappytp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //repertoires authorises sans etre authentifie
        String[] staticResources  =  {
            "/css/**",
            "/images/**",
            "/fonts/**",
            "/scripts/**",
        };

        http
        .authorizeHttpRequests(authorizeRequests ->
        authorizeRequests
            .requestMatchers(staticResources).permitAll()
            .requestMatchers("/", "/accueil").permitAll()
            .requestMatchers("/login").permitAll()
            .anyRequest().authenticated()
    )
    .formLogin(formLogin ->
        formLogin
            .loginPage("/accueil")
            .defaultSuccessUrl("/accueil_admin") // Redirection par défaut après connexion
            .permitAll()
    )
    .logout(logout ->
        logout
            .permitAll()
    );

        return http.build();


    }



    @SuppressWarnings("deprecation")
    @Bean
    public NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }



}