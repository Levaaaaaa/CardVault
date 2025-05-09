package cardvault.CardVault.security.config;

import cardvault.CardVault.security.config.filters.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;
//    private final AuthenticationManager authenticationManager;
    //private final JwtService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
//        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(config.getAuthenticationManager(), jwtService);
//        jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/cards/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/transactions/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
//                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager manager, JwtService jwtService) {
//        return new JwtAuthenticationFilter(manager, jwtService);
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
