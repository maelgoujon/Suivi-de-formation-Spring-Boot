package com.webapp.ytb.webappytp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final UserDetailsService userDetailsService;

        public SecurityConfig(UserDetailsService userDetailsService) {
                this.userDetailsService = userDetailsService;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                // repertoires authorises sans etre authentifie
                String[] staticResources = {
                                "/css/**",
                                "/images/**",
                                "/fonts/**",
                                "/js/**",
                };

                http
                                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                                .requestMatchers(staticResources).permitAll()
                                                .requestMatchers("/", "/accueil").permitAll()
                                                .requestMatchers("/login").permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(formLogin -> formLogin
                                                .loginPage("/accueil")
                                                .loginProcessingUrl("/login") // This should match the action in your
                                                                              // form
                                                .defaultSuccessUrl("/redirectByRole")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/log_out") // specify the URL to redirect after
                                                                              // logout
                                                .permitAll())
                                .userDetailsService(userDetailsService) 
                                .authenticationProvider(authenticationProvider()); 

                return http.build();

        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

}