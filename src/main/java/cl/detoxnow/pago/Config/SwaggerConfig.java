// package cl.detoxnow.pago.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import io.swagger.v3.oas.models.OpenAPI;
// import io.swagger.v3.oas.models.info.Contact;
// import io.swagger.v3.oas.models.info.Info;

// @Configuration
// public class SwaggerConfig {
//     @Bean
//     public OpenAPI customOpenAPI() {
//         return new OpenAPI()
//                 .info(new Info()
//                         .title("Usuarios API DetoxNow")
//                         .version("1.0.0")
//                         .description("API para manejar usuarios en el sistema DetoxNow")
//                         .termsOfService("https://example.com/terms")
//                         .contact(new Contact()
//                                 .name("Soporte Técnico DetoxNow")
//                                 .url("https://example.com/support")
//                                 .email("sc.jaraf@duocuc.cl")));
//     }
// }