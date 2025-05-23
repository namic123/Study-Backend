package hello;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnvironmentCheck {

    private final Environment env;

    @PostConstruct
    public void init() {
        String url = env.getProperty("url");
        String user = env.getProperty("user");
        String password = env.getProperty("password");
        log.info("env url={}", url);
        log.info("env user={}", user);
        log.info("env pwd={}", password);
    }
}