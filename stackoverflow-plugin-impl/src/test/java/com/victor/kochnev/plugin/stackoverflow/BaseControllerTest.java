package com.victor.kochnev.plugin.stackoverflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

@AutoConfigureMockMvc(addFilters = false)
public abstract class BaseControllerTest extends BaseBootTest {
    @Autowired
    private MockMvc mvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public MvcResult post(String uri, Object body) {
        var request = getRequest(uri, body, HttpMethod.POST);
        return mvc.perform(request).andReturn();
    }

    @SneakyThrows
    public MvcResult delete(String uri, Object body) {
        var request = getRequest(uri, body, HttpMethod.DELETE);
        return mvc.perform(request).andReturn();
    }

    protected void assertHttpStatusOk(MvcResult mvcResult) {
        assertHttpStatus(mvcResult, HttpStatus.OK);
    }

    protected void assertHttpStatus(MvcResult mvcResult, HttpStatus httpStatus) {
        var response = mvcResult.getResponse();
        Assertions.assertEquals(httpStatus.value(), response.getStatus());
    }

    private MockHttpServletRequestBuilder getRequest(String uri, Object request, HttpMethod httpMethod) throws JsonProcessingException {
        return MockMvcRequestBuilders.request(httpMethod, uri)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
    }

    protected <T> T getResponseDto(MvcResult mvcResult, Class<T> clazz) {
        try {
            String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            return objectMapper.readValue(content, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
