package spring.cloud.example.gtd.edge.fallback;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback")
    public String fallback() {
        return "Fallback (default): Circuit Breaker is open";
    }

    @PostMapping("/fallback")
    public String fallbackForPost() {
        return "Fallback (default): Circuit Breaker is open";
    }
}
