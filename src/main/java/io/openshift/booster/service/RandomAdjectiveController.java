package io.openshift.booster.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.Spliterator;

@RestController
@RequestMapping(value = "/api/adjective")
public class RandomAdjectiveController {

    private final AdjectiveRepository repository;

    public RandomAdjectiveController(AdjectiveRepository repository) {
        this.repository = repository;
    }


    @GetMapping
    public AdjectiveResponse getRandomAdjective() {

        List<Adjective> adjectives = (List<Adjective>)repository.findAll();
        AdjectiveResponse adjectiveResponse = new AdjectiveResponse(adjectives.get(new Random().nextInt(adjectives.size())));
        return adjectiveResponse;
    }

}
