package io.openshift.booster.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
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
        if(adjectives.isEmpty()){
            initdb();
            adjectives = (List<Adjective>) repository.findAll();
        }
        AdjectiveResponse adjectiveResponse = new AdjectiveResponse(adjectives.get(new Random().nextInt(adjectives.size())));
        return adjectiveResponse;
    }

    void initdb() {

        List<String> adjectives2 = Arrays.asList(
                new String[]{
                        "artless",
                        "base-court",
                        "bawdy",
                        "bat-fowling",
                        "beslubbering",
                        "beef-witted",
                        "bootless",
                        "beetle-headed",
                        "churlish",
                        "boil-brained",
                        "cockered",
                        "clapper-clawed",
                        "clouted",
                        "clay-brained",
                        "craven",
                        "common-kissing",
                        "currish",
                        "crook-pated",
                        "dankish",
                        "dismal-dreaming",
                        "dissembling",
                        "dizzy-eyed",
                        "droning",
                        "doghearted",
                        "errant",
                        "dread-bolted",
                        "fawning",
                        "earth-vexing",
                        "fobbing",
                        "elf-skinned",
                        "froward",
                        "fat-kidneyed",
                        "frothy",
                        "fen-sucked",
                        "gleeking",
                        "flap-mouthed",
                        "goatish",
                        "fly-bitten",
                        "gorbellied",
                        "folly-fallen",
                        "impertinent",
                        "fool-born",
                        "infectious",
                        "full-gorged",
                        "jarring",
                        "guts-griping",
                        "loggerheaded",
                        "half-faced",
                        "lumpish",
                        "hasty-witted",
                        "mammering",
                        "hedge-born",
                        "mangled",
                        "hell-hated",
                        "mewling",
                        "idle-headed",
                        "paunchy",
                        "ill-breeding",
                        "pribbling",
                        "ill-nurtured",
                        "puking",
                        "knotty-pated",
                        "puny",
                        "milk-livered",
                        "qualling",
                        "motley-minded",
                        "rank",
                        "onion-eyed",
                        "reeky",
                        "plume-plucked",
                        "roguish",
                        "pottle-deep",
                        "ruttish",
                        "pox-marked",
                        "saucy",
                        "reeling-ripe",
                        "spleeny",
                        "rough-hewn",
                        "spongy",
                        "rude-growing",
                        "surly",
                        "rump-fed",
                        "tottering",
                        "shard-borne",
                        "unmuzzled",
                        "sheep-biting",
                        "vain",
                        "spur-galled",
                        "venomed",
                        "swag-bellied",
                        "villainous",
                        "tardy-gaited",
                        "warped",
                        "tickle-brained",
                        "wayward",
                        "toad-spotted",
                        "weedy",
                        "unchin-snouted",
                        "yeasty",
                        "weather-bitten",
                        "cullionly",
                        "whoreson",
                        "fusty",
                        "malmsey-nosed",
                        "caluminous",
                        "rampallian",
                        "wimpled",
                        "lily-livered",
                        "burly-boned",
                        "scurvy-valiant",
                        "misbegotten",
                        "brazen-faced",
                        "odiferous",
                        "unwash'd",
                        "poisonous",
                        "bunch-back'd",
                        "fishified",
                        "leaden-footed",
                        "wart-necked",
                        "muddy-mettled",
                        "pigeon-liver'd",
                        "scale-sideded"
                }
        );

        for (String s : adjectives2) {
            repository.save(new Adjective(s));
        }
    }
}
