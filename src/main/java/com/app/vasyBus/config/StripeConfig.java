package com.app.vasyBus.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import static com.stripe.Stripe.*;

@Configuration
@Getter
public class StripeConfig {

    @Value("${stripe.secret.key}")
    private String secretKey;

    @Value("${stripe.publishable.key}")
    private String publishableKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostConstruct
    public void initStripe() {
        Stripe.apiKey = secretKey;

    }
}