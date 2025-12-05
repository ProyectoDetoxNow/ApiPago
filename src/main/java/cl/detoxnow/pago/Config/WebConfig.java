package cl.detoxnow.pago.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:3000",
                                "https://ecomerceev2-scarletjara-projects.vercel.app",
                                "https://ecomerceev2-git-main-scarletjara-projects.vercel.app",
                                "https://ecomerceev2-git-draft-scarletjara-projects.vercel.app",
                                "https://ecomerceev2-git-preview-scarletjara-projects.vercel.app",
                                "https://detoxnow.vercel.app"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false);
            }
        };
    }
}
