package spring.cloud.example.gtd.edge.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;

@Configuration
public class HealthCheckConfiguration {

    @Bean
    public ReactiveHealthContributor healthcheckServices(@Autowired WebClient.Builder builder) {

        var webClient = builder.build();
        var registry = new LinkedHashMap<String, ReactiveHealthIndicator>();

        registry.put("command-service", () -> determineHealth("http://gtd-command-service", webClient));
        registry.put("query-service", () -> determineHealth("http://gtd-query-service", webClient));

        return CompositeReactiveHealthContributor.fromMap(registry);
    }

    private Mono<Health> determineHealth(final String baseUrl, final WebClient webClient) {
        var url = baseUrl + "/actuator/health";

        return webClient.get().uri(url).retrieve()
                .bodyToMono(String.class)
                .map(s -> new Health.Builder().up().build())
                .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
                .log();
    }
}
