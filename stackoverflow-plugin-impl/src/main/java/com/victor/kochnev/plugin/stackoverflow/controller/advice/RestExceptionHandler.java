package com.victor.kochnev.plugin.stackoverflow.controller.advice;

import com.victor.kochnev.integration.plugin.api.dto.ErrorMessageDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.ErrorResponseDto;
import com.victor.kochnev.plugin.stackoverflow.controller.ControllerScanMarker;
import com.victor.kochnev.plugin.stackoverflow.exception.StackOverflowIntegrationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice(basePackageClasses = ControllerScanMarker.class)
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(StackOverflowIntegrationException.class)
    public ResponseEntity<Object> handleTopLevelException(StackOverflowIntegrationException ex, WebRequest request) {
        log.error(ExceptionUtils.getMessage(ex));
        ErrorResponseDto errorResponseDto = ex.getErrorResponseDto();
        ErrorMessageDto errorMessageDto = new ErrorMessageDto();
        errorMessageDto.setMessage("Get " + errorResponseDto.getErrorId() + " from StackOverflow");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorMessageDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleTopLevelException(Exception ex, WebRequest request) {
        log.error(ExceptionUtils.getMessage(ex));
        return ResponseEntity.internalServerError().build();
    }

    @Override
    protected ResponseEntity<Object> handleErrorResponseException(ErrorResponseException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ExceptionUtils.getMessage(ex));
        return super.handleErrorResponseException(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ExceptionUtils.getMessage(ex));
        return super.handleMissingServletRequestParameter(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ExceptionUtils.getMessage(ex));
        return super.handleMissingServletRequestPart(ex, headers, status, request);
    }


}
