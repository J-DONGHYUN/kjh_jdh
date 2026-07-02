package project.kjhjdh.ibid.support;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import project.kjhjdh.ibid.TestcontainersConfiguration;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import({TestcontainersConfiguration.class, DbCleaner.class})
public abstract class IntegrationTestSupport {

    @Autowired
    private DbCleaner dbCleaner;

    @BeforeEach
    void cleanUp() {
        dbCleaner.clean();
    }
}
