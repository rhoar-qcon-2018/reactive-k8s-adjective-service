package io.openshift.booster;

import com.jayway.restassured.RestAssured;
import io.openshift.booster.service.Adjective;
import io.openshift.booster.service.AdjectiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.get;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RandomAdjectiveTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private AdjectiveRepository adjectiveRepository;

    @Before
    public void beforeTest() {
        adjectiveRepository.deleteAll();
        RestAssured.baseURI = String.format("http://localhost:%d/api/adjective", port);

        Adjective artless = adjectiveRepository.save(new Adjective("artless"));
        Adjective basecourt = adjectiveRepository.save(new Adjective("base-court"));
        Adjective clayBrained = adjectiveRepository.save(new Adjective("clay-brained"));
        Adjective elfSkinned = adjectiveRepository.save(new Adjective("elf-skinned"));

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
