package com.obss.pokedex.library.rest;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.BindException;
import java.nio.file.AccessDeniedException;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler extends BaseController {

    private final MessageSource messageSource;

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response<MetaResponse> handleRequestForbiddenRequest(AccessDeniedException accessDeniedException,
                                                                Locale locale) {
        log.error("Forbidden Request! - Permission Denied", accessDeniedException);
        return respond(MetaResponse.of(ResponseCode.ERROR.getValue(), StringUtils.hasLength(
                accessDeniedException.getMessage()) ? accessDeniedException.getMessage()
                : "Forbidden Request! - Permission Denied"));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<MetaResponse> handleRequestPropertyBindingError(WebExchangeBindException webExchangeBindException,
                                                                    Locale locale) {
        log.debug("Bad request!", webExchangeBindException);
        return respond(MetaResponse.of(ResponseCode.ERROR.getValue(), StringUtils.hasLength(
                webExchangeBindException.getMessage()) ? webExchangeBindException.getMessage()
                : "Bad request!"));
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<MetaResponse> handleBindException(BindException bindException, Locale locale) {
        log.debug("Bad request!", bindException);
        return respond(MetaResponse.of(ResponseCode.ERROR.getValue(), StringUtils.hasLength(
                bindException.getMessage()) ? bindException.getMessage()
                : "Bad request!"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<MetaResponse> handleInvalidArgumentException(
            MethodArgumentNotValidException methodArgumentNotValidException, Locale locale) {
        log.debug("Method argument not valid. Message: $methodArgumentNotValidException.message",
                methodArgumentNotValidException);
        return respond(MetaResponse.of(ResponseCode.ERROR.getValue(), StringUtils.hasLength(
                methodArgumentNotValidException.getMessage()) ? methodArgumentNotValidException.getMessage()
                : "Method argument not valid."));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Response<MetaResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException methodArgumentTypeMismatchException, Locale locale) {
        log.trace("MethodArgumentTypeMismatchException occurred", methodArgumentTypeMismatchException);
        return respond(MetaResponse.of(ResponseCode.ERROR.getValue(), StringUtils.hasLength(
                methodArgumentTypeMismatchException.getMessage()) ? methodArgumentTypeMismatchException.getMessage()
                : "MethodArgumentTypeMismatchException occurred"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Response<MetaResponse> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException methodNotSupportedException, Locale locale) {
        log.debug("HttpRequestMethodNotSupportedException occurred", methodNotSupportedException);
        return respond(MetaResponse.of(ResponseCode.ERROR.getValue(), StringUtils.hasLength(
                methodNotSupportedException.getMessage()) ? methodNotSupportedException.getMessage()
                : "HttpRequestMethodNotSupportedException occurred"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<MetaResponse> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException,
                                                                 Locale locale) {
        log.debug("HttpRequestMethodNotSupportedException occurred", illegalArgumentException);
        return respond(MetaResponse.of(ResponseCode.ERROR.getValue(), StringUtils.hasLength(
                illegalArgumentException.getMessage()) ? illegalArgumentException.getMessage()
                : "HttpRequestMethodNotSupportedException occurred"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<MetaResponse> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException,
                                                                Locale locale) {
        log.debug("Entity Not Found", entityNotFoundException);
        return respond(MetaResponse.of(ResponseCode.ERROR.getValue(), StringUtils.hasLength(
                entityNotFoundException.getMessage()) ? entityNotFoundException.getMessage()
                : "Entity Not Found"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<MetaResponse> handleException(Exception exception,
                                                  Locale locale) {
        log.debug("General Error", exception);
        return respond(MetaResponse.of(ResponseCode.ERROR.getValue(), StringUtils.hasLength(
                exception.getMessage()) ? exception.getMessage()
                : "General Error"));
    }
}

