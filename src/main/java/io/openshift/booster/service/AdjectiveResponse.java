package io.openshift.booster.service;

public class AdjectiveResponse {

    Adjective adjective;

    public AdjectiveResponse(Adjective adjective) {
        this.adjective = adjective;
    }

    public String getAdjective(){
        return this.adjective.getBody();
    }
}
