package spring.cloud.example.gtd.domain;

public interface EventPublisher<T> {

    void log(T event);
}
