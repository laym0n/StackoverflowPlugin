package com.victor.kochnev.plugin.stackoverflow.service.parser;

import com.victor.kochnev.plugin.stackoverflow.exception.ParseDescriptionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@Slf4j
public class ParserServiceImpl implements ParserService {
    @Override
    public Integer parseQuestionId(String resourceDescription) {
        try {
            new URI(resourceDescription);
            if (!resourceDescription.startsWith("https://stackoverflow.com")) {
                throw new ParseDescriptionException("URI not starts with https://stackoverflow.com");
            }
            return Integer.parseInt(resourceDescription.split("/")[4]);
        } catch (Exception e) {
            throw new ParseDescriptionException(resourceDescription + " can be parsed", e);
        }
    }
}
