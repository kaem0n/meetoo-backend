package kaem0n.meetoo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthConfig {
    @Bean
    PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    SecurityFilterChain sfc(HttpSecurity hs) throws Exception {
        hs.formLogin(http -> http.disable());
        hs.csrf(http -> http.disable());
        hs.sessionManagement(http -> http.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        hs.authorizeHttpRequests(http -> http.requestMatchers("/**").permitAll());
        hs.cors(Customizer.withDefaults());

        return hs.build();
    }

    @Bean
    CorsConfigurationSource ccs(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
