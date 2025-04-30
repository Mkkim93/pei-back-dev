package kr.co.pei.pei_app.config.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.exception.board.BoardDeleteFailedException;
import kr.co.pei.pei_app.application.exception.board.BoardNotFoundException;
import kr.co.pei.pei_app.application.exception.redis.OtpStorageException;
import kr.co.pei.pei_app.application.exception.redis.PasswordTokenExpiredException;
import kr.co.pei.pei_app.application.exception.surveys.SurveyDepartException;
import kr.co.pei.pei_app.application.exception.users.DuplicateException;
import kr.co.pei.pei_app.application.exception.users.UserMailNotFoundException;
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
    public ResponseEntity<ApiResult<String>> handleDuplicateUsernameException(DuplicateException e) {
       log.warn("[GlobalException]: ", e);
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResult.error(
               HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResult<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("[GlobalException]: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("[GlobalException]: ", e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error(HttpStatus.BAD_REQUEST.value(), errors));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult<String> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("[GlobalException]: ", e);
        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ApiResult<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("[GlobalException]: ", e);
        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ApiResult<String> handleAuthenticationException(AuthenticationException e) {
        log.warn("[GlobalException]: ", e);
        return ApiResult.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(OtpStorageException.class)
    public ApiResult<String> handleOtpStorageException(OtpStorageException e) {
        log.warn("[GlobalException]: ", e);
        return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MailSendException.class)
    public ApiResult<String> handleMailSendException(MailSendException e) {
        log.warn("[GlobalException]: ", e);
        return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BoardNotFoundException.class)
    public ApiResult<String> handleBoardNotFoundException(BoardNotFoundException e) {
        log.warn("[GlobalException]: ", e);
        return ApiResult.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BoardDeleteFailedException.class)
    public ApiResult<String> handleBoardDeleteFailedException(BoardDeleteFailedException e) {
        log.warn("[GlobalException]: ", e);
        return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResult<String>> handleDataAccessException(DataAccessException e) {
        log.warn("[GlobalException]: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResult.error(500, e.getMessage(), "데이터베이스 오류가 발생했습니다."));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserMailNotFoundException.class)
    public ApiResult<String> handleUserMailNotFoundException(UserMailNotFoundException e) {
        log.warn("[GlobalException]: ", e);
        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.GONE)
    @ExceptionHandler(PasswordTokenExpiredException.class)
    public ApiResult<String> handlePasswordTokenExpiredException(PasswordTokenExpiredException e) {
        log.warn("[GlobalException]: ", e);
        return ApiResult.error(HttpStatus.GONE.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SurveyDepartException.class)
    public ApiResult<String> handleSurveyDepartException(SurveyDepartException e) {
        log.warn("[GlobalException]: ", e);
        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
