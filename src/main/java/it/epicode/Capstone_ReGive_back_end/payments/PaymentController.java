
package it.epicode.Capstone_ReGive_back_end.payments;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import it.epicode.Capstone_ReGive_back_end.mails.EmailSenderService;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
public class PaymentController {

    @Autowired
    private EmailSenderService emailSenderService;

    private static final String ADMIN_EMAIL = "giorgiamannino@hotmail.com";

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

        // invio email
        try {
            String subject = "Pagamento andato a buon fine su ReGive ✔️";
            String body = "<h2>Ciao!</h2>" +
                    "<p>Grazie per aver completato il pagamento su <strong>ReGive</strong> — la web app che rende semplice donare oggetti usati, prolungandone il ciclo di vita e diffondendo il valore del riuso.</p>" +
                    "<p>Il tuo pagamento di <strong>" + (amount / 100.0) + "€</strong> è stato effettuato con successo.</p>" +
                    "<hr>" +
                    "<p>Siamo felici di averti con noi in questo percorso verso un riuso consapevole e sostenibile.</p>" +
                    "<p>Grazie per fare la differenza con ReGive! 🌱</p>" +
                    "<br>" +
                    "<p>Il team di ReGive</p>";
            emailSenderService.sendEmail(ADMIN_EMAIL, subject, body);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());

        return response;
    }
}
