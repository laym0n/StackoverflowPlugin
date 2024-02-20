package com.victor.kochnev.plugin.stackoverflow.exception;

import com.victor.kochnev.plugin.stackoverflow.api.dto.ErrorResponseDto;
import lombok.Getter;

@Getter
public class StackOverflowIntegrationException extends ModuleException {
    private ErrorResponseDto errorResponseDto;

    public StackOverflowIntegrationException(ErrorResponseDto errorResponseDto) {
        super(errorResponseDto.toString());
        this.errorResponseDto = errorResponseDto;
    }
}
