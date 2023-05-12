package spring.cloud.example.gtd.edge.ratelimit;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class IpAddressKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(final ServerWebExchange exchange) {
        var ipAddress = exchange.getRequest()
                .getRemoteAddress()
                .getAddress()
                .getHostAddress();
        return Mono.just(ipAddress);
    }
}
