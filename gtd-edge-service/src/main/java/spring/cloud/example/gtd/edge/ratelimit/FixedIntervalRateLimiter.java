package spring.cloud.example.gtd.edge.ratelimit;

import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedIntervalRateLimiter implements RateLimiter<FixedIntervalRateLimiter.Config> {

    private static final Duration DEFAULT_DURATION = Duration.of(1, ChronoUnit.SECONDS);

    private static final int DEFAULT_LIMIT = 5;

    private static final Config DEFAULT_CONFIG = new Config(DEFAULT_LIMIT, DEFAULT_DURATION);

    private final Duration duration;

    private final int limit;

    private final ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    public FixedIntervalRateLimiter(final Duration duration, final int limit) {
        this.duration = duration;
        this.limit = limit;
    }

    @Override
    public Mono<Response> isAllowed(final String routeId, final String id) {
        var counter = counters.computeIfAbsent(id, key -> new AtomicInteger(0));
        if (counter.incrementAndGet() <= limit) {
            return Mono.just(new Response(true, Map.of()));
        } else {
            if (counter.get() == limit + 1) {
                // schedule counter reset
                Mono.delay(duration).doOnTerminate(() -> counters.remove(id)).subscribe();
            }
            return Mono.just(new Response(false, Map.of()));
        }
    }

    @Override
    public Map<String, Config> getConfig() {
        return Map.of("default", newConfig());
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    @Override
    public Config newConfig() {
        return DEFAULT_CONFIG;
    }

    public static class Config {
        private final int limit;
        private final Duration duration;

        public Config(final int limit, final Duration duration) {
            this.limit = limit;
            this.duration = duration;
        }

        public int getLimit() {
            return limit;
        }

        public Duration getDuration() {
            return duration;
        }
    }
}
