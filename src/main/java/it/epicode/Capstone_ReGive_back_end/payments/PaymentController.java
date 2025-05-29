
package it.epicode.Capstone_ReGive_back_end.payments;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
public class PaymentController {

    @PostConstruct
    public void init() {
        String stripeKey = System.getenv("STRIPE_SECRET_KEY");
        if (stripeKey == null || stripeKey.isEmpty()) {
            throw new IllegalStateException("STRIPE_SECRET_KEY environment variable not set");
        }
        Stripe.apiKey = stripeKey;
    }

    @PostMapping("/create-payment-intent")
    public Map<String, String> createPaymentIntent(@RequestBody Map<String, Object> data) throws Exception {
        long amount = ((Number) data.get("amount")).longValue();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("eur")
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());

        return response;
    }
}
