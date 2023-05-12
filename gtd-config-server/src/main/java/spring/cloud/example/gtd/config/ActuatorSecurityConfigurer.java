package spring.cloud.example.gtd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ActuatorSecurityConfigurer {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // we have to disable CSRF in order to test the encrypt/decrypt endpoints
                // from Insomnia or cURL
                .csrf()
                .disable()
                .httpBasic(withDefaults());
        return http.build();
    }
}