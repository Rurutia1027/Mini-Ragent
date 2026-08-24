package com.emma.miniragent.idm.config;

import com.emma.miniragent.idm.auth.filter.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IdmWebConfiguration implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    public IdmWebConfiguration(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(
                        "/api/chat/**",
                        "/api/knowledge/**",
                        "/api/search/**",
                        "/api/rag/**",
                        "/api/agent/**",
                        "/api/meta/**",
                        "/api/traces/**",
                        "/api/feedback/**",
                        "/api/admin/**");
    }
}
