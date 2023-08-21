package br.com.rponte.rinhadev.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public abstract class SpringBootIntegrationTest {

    protected static final LocalDate TODAY = LocalDate.now();

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;


    protected Map<String, Object> violation(String field, String message) {
        return Map.of(
                "field", field,
                "message", message
        );
    }

    protected String toJson(Object payload) throws JsonProcessingException {
        return mapper.writeValueAsString(payload);
    }

}
