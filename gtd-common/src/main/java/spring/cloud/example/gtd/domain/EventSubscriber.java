package spring.cloud.example.gtd.domain;

import java.util.concurrent.CompletableFuture;

public interface EventSubscriber<T> {

    CompletableFuture<Void> onEvent(T event);
}
