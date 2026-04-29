package com.java_api.infra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class UrlValidator implements ConstraintValidator<ValidUrl, String> {
    private final RestTemplate restTemplate;

    public UrlValidator() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3500);
        factory.setReadTimeout(3500);
        this.restTemplate = new RestTemplate(factory);
    }

    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/91.0.4472.124");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            var response = restTemplate.exchange(url, HttpMethod.HEAD, entity, Void.class);

            if (response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is3xxRedirection()) {
                return true;
            }

            return setCustomMessage(context, "The provided link is broken");
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            return setCustomMessage(context, "URL is inaccessible");
        } catch (Exception e) {
            return setCustomMessage(context, "Could not connect to the URL (Timeout or DNS error)");
        }
    }

    private boolean setCustomMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();

        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();

        return false;
    }
}
