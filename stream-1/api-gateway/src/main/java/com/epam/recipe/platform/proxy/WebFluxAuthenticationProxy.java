hpackage com.epam.recipe.platform.proxy;

import com.epam.recipe.platform.request.LoginRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class WebFluxAuthenticationProxy {

    private WebClient.Builder webClientBuilder;

    public WebFluxAuthenticationProxy(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<String> getToken(LoginRequest loginRequest) {
        return webClientBuilder
                .build()
                .post() // Use POST method
                .uri("http://user-service/users/login")
                .contentType(MediaType.APPLICATION_JSON) // Set content type
                .bodyValue(loginRequest) // Add your request body object
                .retrieve()
                .bodyToMono(String.class)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> validateToken(String token) {
        return webClientBuilder.build().get()
                .uri("http://user-service/users/validate-token?token=" + token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
