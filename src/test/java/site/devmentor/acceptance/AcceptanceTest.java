package site.devmentor.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AcceptanceTest {

  @Autowired
  private WebApplicationContext context;

  public MockMvc mockMvc;

  @Autowired
  public DatabaseCleaner databaseCleaner;

  @Autowired
  public ObjectMapper objectMapper;

  public static final String DATETIME_FORMAT = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
  public static Matcher<String> datetimeMatcher = Matchers.matchesRegex(DATETIME_FORMAT);

  @BeforeAll
  public void settingSecurity() {
    this.mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
  }

  protected String toBody(Object o) throws JsonProcessingException {
    return this.objectMapper.writeValueAsString(o);
  }

  @BeforeEach
  protected void setUp() {
    databaseCleaner.afterPropertiesSet();
  }

  @AfterEach
  protected void tearDown() {
    databaseCleaner.execute();
  }
}