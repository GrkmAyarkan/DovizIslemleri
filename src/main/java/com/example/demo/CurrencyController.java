package com.example.demo;

//RestTemplate
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//WebClient
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class CurrencyController {

//    @Autowired
//    private WebClient.Builder webClientBuilder;
//
//    private static final String API_URL = "https://api.collectapi.com/economy/currencyToAll?int=10&base=USD";
//    private static final String API_KEY = "7AqFR5NGwogu7EMJfi8FQs:3JmofufZbucxNCmf4d357q";
//
//    @GetMapping("/exchange-rates") //http://localhost:8080/exchange-rates
//    public String getExchangeRates() {
//        return webClientBuilder.build()
//                .get()
//                .uri(API_URL)
//                .header(HttpHeaders.AUTHORIZATION, "apikey " + API_KEY)
//                .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                .retrieve()
//                .bodyToMono(String.class)
//                .block(); // API'den gelen cevabı JSON formatında döndürür
//    }



    @Autowired
    private RestTemplate restTemplate;

    private static final String API_URL = "https://api.collectapi.com/economy/allCurrency";
    private static final String API_KEY = "7AqFR5NGwogu7EMJfi8FQs:3JmofufZbucxNCmf4d357q";

//    @GetMapping("/exchange-rates") //http://localhost:8080/exchange-rates
//    public String getExchangeRates() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("authorization", "apikey " + API_KEY);
//        headers.set("content-type", "application/json");
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);
//
//        return response.getBody();
//    }

//    @GetMapping("/exchange-rates") //http://localhost:8080/exchange-rates
//    public String getExchangeRates() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("authorization", "apikey " + API_KEY);
//        headers.set("content-type", "application/json");
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try{
//            Map<String, Object> jsonResponse = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>(){});
//            List<Map<String, Object>> results = (List<Map<String, Object>>) jsonResponse.get("result");
//
//            List<Map<String, Object>> filteredResults = results.stream()
//                    .filter(currency -> currency.get("code").equals("USD") ||
//                            currency.get("code").equals("EUR") ||
//                            currency.get("code").equals("GBP"))
//                    .collect(Collectors.toList());
//            return objectMapper.writeValueAsString(filteredResults);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return "Error processing JSON";
//        }
//    }

    @GetMapping("/doviz-kurlari") //http://localhost:8080/exchange-rates?currency=USD
    public String getFilteredExchangeRates(@RequestParam(value = "doviz", required = false) String doviz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "apikey " + API_KEY);
        headers.set("content-type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> jsonResponse = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            List<Map<String, Object>> results = (List<Map<String, Object>>) jsonResponse.get("result");

            List<Map<String, Object>> filteredResults;
            if (doviz != null && !doviz.isEmpty()) {
                filteredResults = results.stream()
                        .filter(c-> c.get("code").equals(doviz))
                        .collect(Collectors.toList());
            }
            else {
//                filteredResults = results.stream()
//                        .filter(c -> c.get("code").equals("USD") ||
//                                     c.get("code").equals("EUR") ||
//                                     c.get("code").equals("GBP"))
//                        .collect(Collectors.toList());

                filteredResults = results;
            }

            return objectMapper.writeValueAsString(filteredResults);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error processing JSON";
        }
    }

        @GetMapping("/doviz-cevir") //http://localhost:8080/doviz-cevir?to=TRY&base=EUR
    public String getDovizConvert(@RequestParam(value = "base", defaultValue = "USD") String base,
                                  @RequestParam(value = "to", defaultValue = "TRY")String to) {

        String url = "https://api.collectapi.com/economy/exchange?int=10&to="+to+"&base=" + base;

        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "apikey " + API_KEY);
        headers.set("content-type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

//    @GetMapping("/doviz-cevir") //http://localhost:8080/exchange-rates?currency=USD
//    public String getDovizConvert(@RequestParam(value = "base", defaultValue = "TRY") String base,
//                                  @RequestParam(value = "to", defaultValue = "USD")String to) {
//
//        String url = "https://api.collectapi.com/economy/exchange?int=10&to="+to+"&base=" + base;
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("authorization", "apikey " + API_KEY);
//        headers.set("content-type", "application/json");
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            Map<String, Object> jsonResponse = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
//            List<Map<String, Object>> results = (List<Map<String, Object>>) jsonResponse.get("result");
//
//            List<Map<String, Object>> filteredResults;
//            if (doviz != null && !doviz.isEmpty()) {
//                filteredResults = results.stream()
//                        .filter(c-> c.get("code").equals(doviz))
//                        .collect(Collectors.toList());
//            }
//            else {
////                filteredResults = results.stream()
////                        .filter(c -> c.get("code").equals("USD") ||
////                                     c.get("code").equals("EUR") ||
////                                     c.get("code").equals("GBP"))
////                        .collect(Collectors.toList());
//
//                filteredResults = results;
//            }
//
//            return objectMapper.writeValueAsString(filteredResults);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return "Error processing JSON";
//        }
//    }

//    @GetMapping("/exchange-rates/{currency}")
//    public String getFilteredExchangeRates(@PathVariable("currency") String currency) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("authorization", "apikey " + API_KEY);
//        headers.set("content-type", "application/json");
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            Map<String, Object> jsonResponse = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
//            List<Map<String, Object>> results = (List<Map<String, Object>>) jsonResponse.get("result");
//
//            List<Map<String, Object>> filteredResults = results.stream()
//                    .filter(c -> c.get("code").equals(currency))
//                    .collect(Collectors.toList());
//
//            return objectMapper.writeValueAsString(filteredResults);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return "Error processing JSON";
//        }
//    }
}

