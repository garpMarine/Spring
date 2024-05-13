package com.adobe.orderapp.cfg;

import com.adobe.orderapp.service.PostService;
import com.adobe.orderapp.service.UserService;
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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Configuration
@EnableCaching
@EnableScheduling
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
@EnableAsync
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
    UserService userService() {
        RestClient restClient = RestClient.create("https://jsonplaceholder.typicode.com/");
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        return  factory.createClient(UserService.class);
    }

    @Bean
    public OpenAPI springOrderAppOpenApi() {
        return  new OpenAPI()
                .info(new Info().title("Shopping Application")
                        .description("Spring Boot RESTful API")
                        .version("1.0.0")
                        .license(new License().name("Apache 2.0")));
    }


    @Bean(name = "servicePool")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(10);
        executor.setCorePoolSize(3);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("post-user-pool-");
        executor.initialize();
        return executor;
      //  return Executors.newFixedThreadPool(100);
    }
}
