package com.example.webapi.services;

import com.example.webapi.model.RandomCatFact;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RandomAPIService {
    private static final String CAT_FACT_API = "https://catfact.ninja/fact";

    public RandomCatFact getCatFact() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(CAT_FACT_API, RandomCatFact.class);
    }
}
