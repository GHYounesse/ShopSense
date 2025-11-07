package com.app.shopsense.services.payment;


import com.app.shopsense.dtos.payment.PaymentIntentDto;
import com.app.shopsense.dtos.payment.PaymentRequest;
import com.app.shopsense.exceptions.AppException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class PaymentService {

    private static final BigDecimal CENTS_MULTIPLIER = BigDecimal.valueOf(100);
    private static final BigDecimal MIN_AMOUNT = BigDecimal.valueOf(0.50); // $0.50 minimum
    private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(999999.99); // Maximum amount
    private static final String DEFAULT_CURRENCY = "usd";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PaymentService.class);

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.currency:usd}")
    private String defaultCurrency;

    /**
     * Initialize Stripe API key once during bean creation
     */
    @PostConstruct
    public void init() {
        if (stripeSecretKey == null || stripeSecretKey.trim().isEmpty()) {
            log.error("Stripe secret key is not configured");
            throw new IllegalStateException("Stripe secret key must be configured");
        }
        Stripe.apiKey = stripeSecretKey;
        log.info("Stripe API initialized successfully");
    }

    /**
     * Creates a payment intent with the specified amount
     *
     * @param amount the payment amount in dollars
     * @return PaymentIntent object
     * @throws AppException if payment creation fails
     */
    public PaymentIntent createPaymentIntent(BigDecimal amount) {
        try {
            return createStripePaymentIntent(amount, defaultCurrency, null);
        } catch (StripeException e) {
        log.error("Stripe error while creating payment intent: {}", e.getMessage(), e);
        throw new AppException(
                "Failed to create payment: " + e.getUserMessage(),
                HttpStatus.BAD_REQUEST
        );
    }
    }

    /**
     * Creates a payment intent with amount and currency
     *
     * @param amount   the payment amount
     * @param currency the currency code (e.g., "usd", "eur")
     * @return PaymentIntent object
     * @throws AppException if payment creation fails
     */
    public PaymentIntent createPaymentIntent(BigDecimal amount, String currency) {
        try {
            return createStripePaymentIntent(amount, currency, null);
        } catch (StripeException e) {
        log.error("Stripe error while creating payment intent: {}", e.getMessage(), e);
        throw new AppException(
                "Failed to create payment: " + e.getUserMessage(),
                HttpStatus.BAD_REQUEST
        );
    }
    }

    /**
     * Creates a payment intent with full customization
     *
     * @param paymentRequest the payment request details
     * @return PaymentIntentDto with client secret
     * @throws AppException if payment creation fails
     */
    public PaymentIntentDto createPaymentIntent(PaymentRequest paymentRequest) {
        log.debug("Creating payment intent for amount: {} {}",
                paymentRequest.getAmount(), paymentRequest.getCurrency());

        validatePaymentRequest(paymentRequest);

        try {
            PaymentIntent paymentIntent = createStripePaymentIntent(
                    paymentRequest.getAmount(),
                    paymentRequest.getCurrency(),
                    paymentRequest.getMetadata()
            );

            log.info("Payment intent created successfully: {}", paymentIntent.getId());

            return PaymentIntentDto.builder()
                    .clientSecret(paymentIntent.getClientSecret())
                    .paymentIntentId(paymentIntent.getId())
                    .amount(paymentRequest.getAmount())
                    .currency(paymentRequest.getCurrency())
                    .status(paymentIntent.getStatus())
                    .build();

        } catch (StripeException e) {
            log.error("Stripe error while creating payment intent: {}", e.getMessage(), e);
            throw new AppException(
                    "Failed to create payment: " + e.getUserMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    /**
     * Retrieves a payment intent by ID
     *
     * @param paymentIntentId the payment intent ID
     * @return PaymentIntent object
     * @throws AppException if retrieval fails
     */
    public PaymentIntent getPaymentIntent(String paymentIntentId) {
        log.debug("Retrieving payment intent: {}", paymentIntentId);

        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            log.info("Payment intent retrieved: {} with status: {}",
                    paymentIntentId, paymentIntent.getStatus());
            return paymentIntent;

        } catch (StripeException e) {
            log.error("Error retrieving payment intent {}: {}", paymentIntentId, e.getMessage());
            throw new AppException(
                    "Failed to retrieve payment: " + e.getUserMessage(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    /**
     * Cancels a payment intent
     *
     * @param paymentIntentId the payment intent ID
     * @return cancelled PaymentIntent
     * @throws AppException if cancellation fails
     */
    public PaymentIntent cancelPaymentIntent(String paymentIntentId) {
        log.debug("Cancelling payment intent: {}", paymentIntentId);

        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            PaymentIntent cancelledIntent = paymentIntent.cancel();

            log.info("Payment intent cancelled: {}", paymentIntentId);
            return cancelledIntent;

        } catch (StripeException e) {
            log.error("Error cancelling payment intent {}: {}", paymentIntentId, e.getMessage());
            throw new AppException(
                    "Failed to cancel payment: " + e.getUserMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    /**
     * Confirms a payment intent
     *
     * @param paymentIntentId the payment intent ID
     * @return confirmed PaymentIntent
     * @throws AppException if confirmation fails
     */
    public PaymentIntent confirmPaymentIntent(String paymentIntentId) {
        log.debug("Confirming payment intent: {}", paymentIntentId);

        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            PaymentIntent confirmedIntent = paymentIntent.confirm();

            log.info("Payment intent confirmed: {}", paymentIntentId);
            return confirmedIntent;

        } catch (StripeException e) {
            log.error("Error confirming payment intent {}: {}", paymentIntentId, e.getMessage());
            throw new AppException(
                    "Failed to confirm payment: " + e.getUserMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // ==================== Private Helper Methods ====================

    /**
     * Core method to create payment intent with all options
     */
    public PaymentIntent createStripePaymentIntent(
            BigDecimal amount,
            String currency,
            Map<String, String> metadata
    ) throws StripeException {

        validateAmount(amount);
        validateCurrency(currency);

        PaymentIntentCreateParams.Builder paramsBuilder = PaymentIntentCreateParams.builder()
                .setAmount(convertToSmallestUnit(amount))
                .setCurrency(currency.toLowerCase())
                .addPaymentMethodType("card");
//                .setAutomaticPaymentMethods(
//                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
//                                .setEnabled(true)
//                                .build()
//                );

        if (metadata != null && !metadata.isEmpty()) {
            paramsBuilder.putAllMetadata(metadata);
        }

        return PaymentIntent.create(paramsBuilder.build());
    }

    /**
     * Converts dollar amount to cents (smallest currency unit)
     */
    private Long convertToSmallestUnit(BigDecimal amount) {
        return amount.multiply(CENTS_MULTIPLIER).longValue();
    }

    /**
     * Validates payment amount
     */
    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new AppException("Amount cannot be null", HttpStatus.BAD_REQUEST);
        }

        if (amount.compareTo(MIN_AMOUNT) < 0) {
            throw new AppException(
                    String.format("Amount must be at least $%.2f", MIN_AMOUNT),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (amount.compareTo(MAX_AMOUNT) > 0) {
            throw new AppException(
                    String.format("Amount cannot exceed $%.2f", MAX_AMOUNT),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Check for excessive decimal places
        if (amount.scale() > 2) {
            throw new AppException(
                    "Amount cannot have more than 2 decimal places",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    /**
     * Validates currency code
     */
    private void validateCurrency(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            throw new AppException("Currency cannot be null or empty", HttpStatus.BAD_REQUEST);
        }

        if (currency.length() != 3) {
            throw new AppException(
                    "Invalid currency code. Must be 3 characters (e.g., USD, EUR)",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    /**
     * Validates payment request
     */
    private void validatePaymentRequest(PaymentRequest request) {
        if (request == null) {
            throw new AppException("Payment request cannot be null", HttpStatus.BAD_REQUEST);
        }

        validateAmount(request.getAmount());
        validateCurrency(request.getCurrency());
    }
}
