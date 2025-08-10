package com.alperen.kitapsatissistemi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security Konfigürasyonu
 * .NET C# projesindeki güvenlik ayarlarından uyarlanmıştır
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeRequests()
                // Web sayfaları (Thymeleaf templates)
                .antMatchers("/", "/home", "/index").permitAll()
                .antMatchers("/kitaplar", "/kitaplar/**").permitAll()
                .antMatchers("/kategoriler", "/kategoriler/**").permitAll()
                .antMatchers("/sepet", "/sepet/**").permitAll()
                .antMatchers("/favoriler", "/favoriler/**").permitAll()
                .antMatchers("/kullanici", "/kullanici/**").permitAll()
                .antMatchers("/about", "/contact", "/privacy").permitAll()
                // Admin sayfaları - geçici olarak herkese açık
                .antMatchers("/admin", "/admin/**").permitAll()
                // Static resources
                .antMatchers("/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                // API endpoints
                .antMatchers("/api/kullanicilar/register").permitAll()
                .antMatchers("/api/kullanicilar/login").permitAll()
                .antMatchers("/api/kategoriler/**").permitAll()
                .antMatchers("/api/kitaplar/**").permitAll()
                .antMatchers("/api/favoriler/**").permitAll()
                .antMatchers("/api/siparisler/**").permitAll()
                .antMatchers("/api/siparis-detaylar/**").permitAll()
                .antMatchers("/api/kullanicilar/**").permitAll()
                // H2 Console (sadece development için)
                .antMatchers("/h2-console/**").permitAll()
                // Swagger UI
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                // Diğer tüm endpoint'ler için authentication gerekli
                .anyRequest().authenticated()
            .and()
            .headers().frameOptions().disable(); // H2 Console için gerekli
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}