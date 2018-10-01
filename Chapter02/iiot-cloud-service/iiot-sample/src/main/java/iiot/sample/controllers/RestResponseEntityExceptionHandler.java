package iiot.sample.controllers;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import iiot.sample.service.error.ErrorResponse;
import iiot.sample.domain.error.ErrorCode;
import iiot.sample.domain.error.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by 212568770 on 3/29/17.
 */
@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { ServiceException.class})
    protected ResponseEntity<Object> handleServiceException(ServiceException e, WebRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(new ErrorResponse(e.getErrorCode().name(), e.toString(), request.getContextPath()), httpHeaders, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(value = { JpaSystemException.class})
    protected ResponseEntity<Object> handleJpaException(JpaSystemException e, WebRequest request) {
        if (e.getRootCause() instanceof ServiceException){
            return handleServiceException((ServiceException) e.getRootCause(), request);
        }
        log.warn("", e);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.SERVICE_FAILURE.name(), ErrorCode.SERVICE_FAILURE.getDefaultMessage(), request.getContextPath()), httpHeaders, ErrorCode.SERVICE_FAILURE.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("", e);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        if (e.getRootCause() instanceof UnrecognizedPropertyException){
            UnrecognizedPropertyException unrecognizedPropertyException = (UnrecognizedPropertyException) e.getRootCause();
            final String msg = String.format("Unrecognized property %s , valid properties are %s", unrecognizedPropertyException.getPropertyName(), unrecognizedPropertyException.getKnownPropertyIds());
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.BAD_REQUEST_BODY_PROPERTIES.name(), msg, request.getContextPath()), ErrorCode.BAD_REQUEST_BODY_PROPERTIES.getHttpStatus());
        }
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.BAD_REQUEST_BODY.name(), "Bad message format", request.getContextPath()), ErrorCode.BAD_REQUEST_BODY.getHttpStatus());
    }
}
