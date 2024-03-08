package com.webapp.ytb.webappytp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
                                        .requestMatchers("/accueil_admin").hasAnyRole("ADMIN", "CIP", "EDUCSIMPLE")
                                        .requestMatchers("/accueil_superadmin").hasRole("SUPERADMIN")
                                        .requestMatchers("/ajoutfiche/{id}").hasRole("ADMIN")
                                        .requestMatchers("/fiche/icones/{categorie}").hasRole("ADMIN")
                                        .requestMatchers("/niveauxFiche/{id}").hasRole("ADMIN")
                                        .requestMatchers("/chat/{id}").permitAll()
                                        .requestMatchers("/record/{ficheId}").hasAnyRole("ADMIN", "CIP", "EDUCSIMPLE")
                                        .requestMatchers("/recordaffichage/{ficheId}").hasAnyRole("ADMIN", "CIP", "EDUCSIMPLE")
                                        .requestMatchers("/fiche/icones/upload").hasRole("ADMIN")
                                        .requestMatchers("/modif/{id}").hasRole("SUPERADMIN")
                                        .requestMatchers("/fiche/liste_fiche/{id}").hasAnyRole("ADMIN","CIP","EDUCSIMPLE")
                                        .requestMatchers("/fiche/liste_fiche").hasAnyRole("ADMIN","CIP","EDUCSIMPLE")
                                        .requestMatchers("/mdpmodif/{id}").hasRole("SUPERADMIN")
                                        .requestMatchers("/formation").hasRole("SUPERADMIN")
                                        .requestMatchers("/nombre_essais").hasRole("SUPERADMIN")
                                        .requestMatchers("/session/creer").hasRole("ADMIN") 
                                        .requestMatchers("/materiaux").hasRole("ADMIN")
                                        .requestMatchers("/suivi_progression/{userid}").hasRole("CIP")
                                        .requestMatchers("/suivi_progression").hasRole("USER")
                                        .requestMatchers("/profil_apprenti/{id}").hasAnyRole("ADMIN", "CIP", "EDUCSIMPLE","SUPERADMIN")
                                        .requestMatchers("/login").permitAll()
                                        .requestMatchers("/redirectByRole").permitAll()
                                        .requestMatchers("/select_fiche").hasRole("USER")
                                        .anyRequest().authenticated())
                                .formLogin(formLogin -> formLogin
                                                .loginPage("/accueil")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/redirectByRole")
                                                .permitAll())
                                .logout(logout -> logout.logoutUrl("/log_out").permitAll())
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