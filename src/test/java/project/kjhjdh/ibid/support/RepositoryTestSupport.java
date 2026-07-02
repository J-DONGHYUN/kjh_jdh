package project.kjhjdh.ibid.support;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import project.kjhjdh.ibid.TestcontainersConfiguration;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestcontainersConfiguration.class, DbCleaner.class})
@DataJpaTest
public abstract class RepositoryTestSupport {

    @Autowired
    private DbCleaner dbCleaner;

    @BeforeEach
    void cleanUp() {
        dbCleaner.clean();
    }
}
