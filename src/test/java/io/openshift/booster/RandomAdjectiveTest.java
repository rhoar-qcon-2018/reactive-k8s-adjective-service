package io.openshift.booster;

import io.restassured.RestAssured;
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

import static io.restassured.RestAssured.get;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RandomAdjectiveTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private AdjectiveRepository adjectiveRepository;

    @Before
    public void beforeTest() {
        adjectiveRepository.deleteAll();
        RestAssured.baseURI = String.format("http://localhost:%d/api/v1/adjective", port);

        adjectiveRepository.save(new Adjective("artless"));
        adjectiveRepository.save(new Adjective("base-court"));
        adjectiveRepository.save(new Adjective("clay-brained"));
        adjectiveRepository.save(new Adjective("elf-skinned"));

    }

    @Test
    public void testGetRandomAdjective() {
        String adj1 = get().toString();
        String adj2 = get().toString();
        String adj3 = get().toString();

        assertFalse(adj1.equalsIgnoreCase(adj2));
        assertFalse(adj1.equalsIgnoreCase(adj3));
        assertFalse(adj2.equalsIgnoreCase(adj3));
    }
}
