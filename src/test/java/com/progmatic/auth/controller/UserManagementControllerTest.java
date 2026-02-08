package com.progmatic.auth.controller;

import com.progmatic.auth.config.SpringWebConfiguration;
import com.progmatic.auth.service.UserManagementService;
import com.progmatic.auth.utils.RegisterUserRequest;
import com.progmatic.auth.utils.ValidationErrorResponse;
import com.progmatic.auth.wrapper.ValidationErrorTestCase;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = {UserManagementController.class})
@ContextConfiguration(classes = {SpringWebConfiguration.class})
public class UserManagementControllerTest {
  @Autowired private MockMvc mockMvc;
  @MockitoBean private UserManagementService userManagementService;

  @ParameterizedTest(name = "{0}")
  @MethodSource("validationErrorTestCase")
  public void testCreateUser_ValidationErrors(
      String name, RegisterUserRequest input, ValidationErrorResponse expected) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    MvcResult responseContent =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/auth/users/register")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(input)))
            .andExpect(
                result ->
                    Assertions.assertThat(result.getResolvedException())
                        .isInstanceOf(MethodArgumentNotValidException.class))
            .andExpect(MockMvcResultMatchers.status().isUnprocessableContent())
            .andReturn();

    ValidationErrorResponse response =
        objectMapper.readValue(
            responseContent.getResponse().getContentAsString(), ValidationErrorResponse.class);
    Assertions.assertThat(response.getMessages()).isEqualTo(expected.getMessages());
  }

  private static Stream<Arguments> validationErrorTestCase() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    List<ValidationErrorTestCase> validationErrorTestCases =
        objectMapper.readValue(
            new ClassPathResource("test-data/validation-error-test-case.json").getInputStream(),
            new TypeReference<List<ValidationErrorTestCase>>() {});
    return validationErrorTestCases.stream()
        .map(
            testCase ->
                Arguments.of(testCase.getName(), testCase.getInput(), testCase.getExpected()));
  }
}
