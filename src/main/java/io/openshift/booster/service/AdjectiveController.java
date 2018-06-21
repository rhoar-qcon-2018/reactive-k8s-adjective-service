/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.openshift.booster.service;

import com.fasterxml.jackson.databind.ser.std.IterableSerializer;
import io.openshift.booster.exception.NotFoundException;
import io.openshift.booster.exception.UnprocessableEntityException;
import io.openshift.booster.exception.UnsupportedMediaTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/api/adjectives")
public class AdjectiveController {

    private final AdjectiveRepository repository;

    public AdjectiveController(AdjectiveRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/{id}")
    public Adjective get(@PathVariable("id") Integer id) {
        verifyAdjectiveExists(id);

        return repository.findOne(id);
    }

    @GetMapping
    public List<Adjective> getAll() {

        Spliterator<Adjective> adjectives;

        List<Adjective> results = (List<Adjective>) repository.findAll();

        if(results.isEmpty()){
            initdb();
            adjectives = results.spliterator();
        }else{
            adjectives = repository.findAll().spliterator();
        }


        return StreamSupport
                .stream(adjectives, false)
                .collect(Collectors.toList());
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Adjective post(@RequestBody(required = false) Adjective adjective) {
        verifyCorrectPayload(adjective);

        return repository.save(adjective);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Adjective put(@PathVariable("id") Integer id, @RequestBody(required = false) Adjective adjective) {
        verifyAdjectiveExists(id);
        verifyCorrectPayload(adjective);

        adjective.setId(id);
        return repository.save(adjective);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        verifyAdjectiveExists(id);

        repository.delete(id);
    }

    private void verifyAdjectiveExists(Integer id) {
        if (!repository.exists(id)) {
            throw new NotFoundException(String.format("Adjective with id=%d was not found", id));
        }
    }

    private void verifyCorrectPayload(Adjective adjective) {
        if (Objects.isNull(adjective)) {
            throw new UnsupportedMediaTypeException("Invalid payload!");
        }

        if (Objects.isNull(adjective.getBody()) || adjective.getBody().trim().length() == 0) {
            throw new UnprocessableEntityException("The body is required!");
        }

        if (!Objects.isNull(adjective.getId())) {
            throw new UnprocessableEntityException("Id was invalidly set on request.");
        }
    }

    private void initdb() {

        List<String> adjectives2 = Arrays.asList(
            new String[]{
                    "artless", "base-court", "beslubbering"
            }
        );

        for(String s : adjectives2){
            repository.save(new Adjective(s));
        }

    }
}
