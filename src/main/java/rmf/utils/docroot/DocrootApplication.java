package rmf.utils.docroot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DocrootApplication {

    @Autowired
    Environment env;

    @Bean
    WebMvcConfigurer configurer () {
        return new WebMvcConfigurer () {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/files/disk/**").addResourceLocations("file:///");
                registry.addResourceHandler("/files/c:/**").addResourceLocations("file:///c:/");
                registry.addResourceHandler("/files/d:/**").addResourceLocations("file:///d:/");
                registry.addResourceHandler("/files/e:/**").addResourceLocations("file:///e:/");
            }

        };
    }

    public static void main(String[] args) {
        SpringApplication.run(DocrootApplication.class, args);
    }

}

