package kr.co.pei.pei_app.config.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import kr.co.pei.pei_app.application.exception.board.BoardDeleteFailedException;
import kr.co.pei.pei_app.application.exception.board.BoardNotFoundException;
import kr.co.pei.pei_app.application.exception.redis.OtpStorageException;
import kr.co.pei.pei_app.application.exception.users.DuplicateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateException.class)
    public ErrorResult handleDuplicateUsernameException(DuplicateException e) {
       log.error("[GlobalException]: ", e);
       return new ErrorResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> handleIllegalArgumentException(IllegalArgumentException e) {
        log.info("[GlobalException]: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResult(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("[GlobalException]: ", e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResult(HttpStatus.BAD_REQUEST.value(), errors));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResult handleConstraintViolationException(ConstraintViolationException e) {
        log.info("[GlobalException]: ", e);
        return new ErrorResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResult handleEntityNotFoundException(EntityNotFoundException e) {
        log.info("[GlobalException]: ", e);
        return new ErrorResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorResult handleAuthenticationException(AuthenticationException e) {
        return new ErrorResult(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(OtpStorageException.class)
    public ErrorResult handleOtpStorageException(OtpStorageException e) {
        log.info("[GlobalException]: ", e);
        return new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MailSendException.class)
    public ErrorResult handleMailSendException(MailSendException e) {
        log.info("[GlobalException]: ", e);
        return new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BoardNotFoundException.class)
    public ErrorResult handleBoardNotFoundException(BoardNotFoundException e) {
        log.info("[GlobalException]: ", e);
        return new ErrorResult(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BoardDeleteFailedException.class)
    public ErrorResult handleBoardDeleteFailedException(BoardDeleteFailedException e) {
        log.info("[GlobalException]: ", e);
        return new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResult> handleDataAccessException(DataAccessException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResult(500, "데이터베이스 오류가 발생했습니다."));
    }
}
