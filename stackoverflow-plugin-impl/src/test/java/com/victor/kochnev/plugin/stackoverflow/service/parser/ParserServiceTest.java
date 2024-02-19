package com.victor.kochnev.plugin.stackoverflow.service.parser;

import com.victor.kochnev.plugin.stackoverflow.BaseBootTest;
import com.victor.kochnev.plugin.stackoverflow.exception.ParseDescriptionException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserServiceTest extends BaseBootTest {
    @Autowired
    ParserService parserService;

    @ParameterizedTest
    @ArgumentsSource(ValidLinksArgumentsProvider.class)
    void parseValidLinksTest(String path, Long expectedQuestionId) {
        //Action
        Long actualResult = parserService.parseQuestionId(path);

        //Assert
        assertEquals(expectedQuestionId, actualResult);
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidLinksArgumentsProvider.class)
    void parseInvalidLinksTest(String path) {
        //Action and Assert
        assertThrows(ParseDescriptionException.class, () -> parserService.parseQuestionId(path));
    }

    static class ValidLinksArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c", 1642028L),
                    Arguments.of("https://stackoverflow.com/questions/59427642/how-to-update-sqlite/59429952#59429952", 59427642L)
            );
        }
    }

    static class InvalidLinksArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("https://stackoverflow.com/search?q=unsupported%20link"),
                    Arguments.of("htts://github.com/spring-projects/spring-framework"),
                    Arguments.of("https://stackoeverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    Arguments.of("https://stacoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    Arguments.of("htts:/stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    Arguments.of("")

            );
        }
    }
}
