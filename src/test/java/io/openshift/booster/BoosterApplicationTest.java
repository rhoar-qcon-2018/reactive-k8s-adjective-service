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

package io.openshift.booster;

import java.util.Collections;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.openshift.booster.service.Adjective;
import io.openshift.booster.service.AdjectiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BoosterApplicationTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private AdjectiveRepository adjectiveRepository;

    @Before
    public void beforeTest() {
        adjectiveRepository.deleteAll();
        RestAssured.baseURI = String.format("http://localhost:%d/api/v1/adjectives", port);
    }


    @Test
    public void testGetAllFromEmptyDB(){
        Adjective artless = new Adjective("artless");
        Adjective basecourt = new Adjective("base-court");

        when().get()
                .then()
                .statusCode(200)
                .body("body", hasItems(artless.getBody(), basecourt.getBody()));
    }

    @Test
    public void testGetAll() {
        Adjective artless = adjectiveRepository.save(new Adjective("artless"));
        Adjective basecourt = adjectiveRepository.save(new Adjective("base-court"));
        when().get()
                .then()
                .statusCode(200)
                .body("id", hasItems(artless.getId(), basecourt.getId()))
                .body("body", hasItems(artless.getBody(), basecourt.getBody()));
    }

    @Test
    public void testGetOne() {
        Adjective bawdy = adjectiveRepository.save(new Adjective("bawdy"));
        when().get(String.valueOf(bawdy.getId()))
                .then()
                .statusCode(200)
                .body("id", is(bawdy.getId()))
                .body("body", is(bawdy.getBody()));
    }

    @Test
    public void testGetNotExisting() {
        when().get("0")
                .then()
                .statusCode(404);
    }

    @Test
    public void testPost() {
        given().contentType(ContentType.JSON)
                .body(Collections.singletonMap("body", "artless"))
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("id", not(isEmptyString()))
                .body("body", is("artless"));
    }

    @Test
    public void testPostWithWrongPayload() {
        given().contentType(ContentType.JSON)
                .body(Collections.singletonMap("id", 0))
                .when()
                .post()
                .then()
                .statusCode(422);
    }

    @Test
    public void testPostWithNonJsonPayload() {
        given().contentType(ContentType.XML)
                .when()
                .post()
                .then()
                .statusCode(415);
    }

    @Test
    public void testPostWithEmptyPayload() {
        given().contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(415);
    }

    @Test
    public void testPut() {
        Adjective artless = adjectiveRepository.save(new Adjective("artless"));
        given().contentType(ContentType.JSON)
                .body(Collections.singletonMap("body", "artless"))
                .when()
                .put(String.valueOf(artless.getId()))
                .then()
                .statusCode(200)
                .body("id", is(artless.getId()))
                .body("body", is("artless"));

    }

    @Test
    public void testPutNotExisting() {
        given().contentType(ContentType.JSON)
                .body(Collections.singletonMap("name", "Lemon"))
                .when()
                .put("/0")
                .then()
                .statusCode(404);
    }

    @Test
    public void testPutWithWrongPayload() {
        Adjective beslubbering = adjectiveRepository.save(new Adjective("beslubbering"));
        given().contentType(ContentType.JSON)
                .body(Collections.singletonMap("id", 0))
                .when()
                .put(String.valueOf(beslubbering.getId()))
                .then()
                .statusCode(422);
    }

    @Test
    public void testPutWithNonJsonPayload() {
        Adjective bootless = adjectiveRepository.save(new Adjective("bootless"));
        given().contentType(ContentType.XML)
                .when()
                .put(String.valueOf(bootless.getId()))
                .then()
                .statusCode(415);
    }

    @Test
    public void testPutWithEmptyPayload() {
        Adjective cockered = adjectiveRepository.save(new Adjective("cockered"));
        given().contentType(ContentType.JSON)
                .when()
                .put(String.valueOf(cockered.getId()))
                .then()
                .statusCode(415);
    }

    @Test
    public void testDelete() {
        Adjective boilbrained = adjectiveRepository.save(new Adjective("boil-brained"));
        when().delete(String.valueOf(boilbrained.getId()))
                .then()
                .statusCode(204);
        assertFalse(adjectiveRepository.existsById(boilbrained.getId()));
    }

    @Test
    public void testDeleteNotExisting() {
        when().delete("/0")
                .then()
                .statusCode(404);
    }

}
