package org.example.demo.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.time.Duration;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    String AUTH_ADMIN_USER_IN_SCOPE = "ADMIN_USER";
    String AUTH_ADMIN_USER = "SCOPE_" + AUTH_ADMIN_USER_IN_SCOPE;
    String AUTH_USER_IN_SCOPE = "USER";
    String AUTH_USER = "SCOPE_" + AUTH_USER_IN_SCOPE;
    private static final String[] AUTH_WHITELIST = {
            "/pub/**",
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
                .configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.addAllowedMethod("*");
                    corsConfiguration.addAllowedHeader("*");
                    corsConfiguration.addAllowedOriginPattern("*");
                    corsConfiguration.setMaxAge(Duration.ofSeconds(3600));
                    return corsConfiguration;
                })
                .and()
                .requestCache().disable()
                .securityContext().disable()
                .sessionManagement().disable()
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> {
                    httpSecurityOAuth2ResourceServerConfigurer.jwt();
                    httpSecurityOAuth2ResourceServerConfigurer.accessDeniedHandler(new HuokuAccessDeniedHandler());
                    httpSecurityOAuth2ResourceServerConfigurer.authenticationEntryPoint(new HuokuAuthenticationEntryPoint());

                })
                .sessionManagement().disable()
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new HuokuAuthenticationEntryPoint())
                        .accessDeniedHandler(new HuokuAccessDeniedHandler())
                )


                .authorizeHttpRequests(authorize -> authorize
                        .mvcMatchers(AUTH_WHITELIST).permitAll()
                        .mvcMatchers("/admin/**").hasRole("ADMIN")
                        .mvcMatchers("/op-mgr/**").hasAuthority(AUTH_ADMIN_USER)
                        .mvcMatchers("/mch-mgr/**").hasAuthority(AUTH_USER)
                        .mvcMatchers("/my-*/**").hasAuthority(AUTH_USER)
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
