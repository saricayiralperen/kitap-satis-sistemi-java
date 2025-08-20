package com.alperen.kitapsatissistemi.config;

import com.alperen.kitapsatissistemi.security.RateLimitingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
public class SecurityConfig {
    
    @Autowired
    private RateLimitingFilter rateLimitingFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            // CSRF korumasını sadece API endpoint'leri için devre dışı bırak
            .csrf()
                .ignoringAntMatchers("/api/**", "/h2-console/**")
                .and()
            .authorizeRequests()
                // Public web sayfaları
                .antMatchers("/", "/home", "/index").permitAll()
                .antMatchers("/kitaplar", "/kitaplar/**").permitAll()
                .antMatchers("/kategoriler", "/kategoriler/**").permitAll()
                .antMatchers("/kategori-listesi").permitAll()
                .antMatchers("/about", "/contact", "/privacy").permitAll()
                .antMatchers("/kullanici/login", "/kullanici/register").permitAll()
                // Static resources
                .antMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/lib/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                // Public API endpoints
                .antMatchers("/api/kullanicilar/register", "/api/kullanicilar/login").permitAll()
                .antMatchers("/api/kategoriler", "/api/kategoriler/*/kitaplar").permitAll()
                .antMatchers("/api/kitaplar", "/api/kitaplar/*").permitAll()
                // H2 Console (sadece development için)
                .antMatchers("/h2-console/**").permitAll()
                // Swagger UI
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                // Admin sayfaları - authentication gerekli
                .antMatchers("/admin/login").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                // Kullanıcı sayfaları - authentication gerekli
                .antMatchers("/kullanici/profil", "/kullanici/logout").authenticated()
                // Sepet işlemleri - sepete ekleme herkese açık, diğerleri authentication gerekli
                .antMatchers("/sepet/count", "/sepet/ekle").permitAll()
                .antMatchers("/sepet/**", "/favoriler/**", "/siparisler/**").authenticated()
                // Protected API endpoints
                .antMatchers("/api/favoriler/**", "/api/siparisler/**", "/api/siparis-detaylar/**").authenticated()
                .antMatchers("/api/kullanicilar/**").authenticated()
                // Diğer tüm endpoint'ler için authentication gerekli
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/kullanici/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/kullanici/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            .and()
            .sessionManagement()
                .sessionConcurrency(sessionConcurrency -> sessionConcurrency
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
                )
            .and()
            .headers()
                .frameOptions().deny() // Clickjacking koruması
                .contentTypeOptions().and() // MIME type sniffing koruması
                .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                    .maxAgeInSeconds(31536000)
                    .includeSubDomains(true)
                ) // HSTS
            .and()
            .headers().frameOptions().sameOrigin() // H2 Console için sadece same origin
            .and()
            
            // Rate limiting filter ekle
            .addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Sadece belirli origin'lere izin ver - güvenlik için kısıtlandı
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:8081", 
            "https://localhost:8081",
            "http://127.0.0.1:8081",
            "https://127.0.0.1:8081"
        ));
        // Sadece gerekli HTTP metodlarına izin ver
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        // Sadece gerekli header'lara izin ver
        configuration.setAllowedHeaders(Arrays.asList(
            "Content-Type", 
            "Authorization", 
            "X-Requested-With",
            "X-CSRF-TOKEN"
        ));
        configuration.setAllowCredentials(true);
        // Preflight request cache süresi
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
