package com.adobe.orderapp.cfg;

import com.adobe.orderapp.service.PostService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;


@Configuration
@EnableCaching
@EnableScheduling
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
public class AppConfig {
    @Autowired
    CacheManager cacheManager;

//    @Scheduled(fixedRate = 1000)
    //on the hour nine-to-five weekdays
    @Scheduled(cron ="0 0 9-17 * * MON-FRI")
    public void clearCache() {
        System.out.println("Cache clear!!!");
        cacheManager.getCacheNames().forEach(name -> {
            cacheManager.getCache(name).clear();
        });
    }

    @Bean
    RestTemplate createRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    PostService postService() {
        RestClient restClient = RestClient.create("https://jsonplaceholder.typicode.com/");
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        return  factory.createClient(PostService.class);
    }

    @Bean
    public OpenAPI springOrderAppOpenApi() {
        return  new OpenAPI()
                .info(new Info().title("Shopping Application")
                        .description("Spring Boot RESTful API")
                        .version("1.0.0")
                        .license(new License().name("Apache 2.0")));
    }
}
