package com.epam.recipe.platform.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/users/",
            "/users/login",
            "/users/reset-password",
            "/otp/validate-email",
            "/otp/send-otp",
            "/otp/verify-otp",
            "/otp/resend-otp"
    );

    public final Predicate<ServerHttpRequest> publicEndpoints =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}