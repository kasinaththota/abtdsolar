package com.abtd.solarbackend.exception;

import com.abtd.solarbackend.common.response.ErrorResponse;
import com.abtd.solarbackend.customer.exception.CustomerNotFoundException;
import com.abtd.solarbackend.customer.exception.DuplicateCustomerException;
import com.abtd.solarbackend.inventory.exception.DuplicateInventoryException;
import com.abtd.solarbackend.inventory.exception.InvalidInventoryException;
import com.abtd.solarbackend.inventory.exception.InventoryNotFoundException;
import com.abtd.solarbackend.product.exception.DuplicateProductException;
import com.abtd.solarbackend.product.exception.ProductNotFoundException;
import com.abtd.solarbackend.purchase.exception.DuplicatePurchaseException;
import com.abtd.solarbackend.purchase.exception.InvalidPurchaseException;
import com.abtd.solarbackend.purchase.exception.PurchaseNotFoundException;
import com.abtd.solarbackend.sales.exception.DuplicateSaleException;
import com.abtd.solarbackend.sales.exception.InvalidSaleException;
import com.abtd.solarbackend.sales.exception.SaleNotFoundException;
import com.abtd.solarbackend.user.exception.DuplicateUserException;
import com.abtd.solarbackend.user.exception.InvalidUserException;
import com.abtd.solarbackend.user.exception.UserNotFoundException;
import com.abtd.solarbackend.vendor.exception.DuplicateVendorException;
import com.abtd.solarbackend.vendor.exception.VendorNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();
    }

    // ===========================================================
    // NOT FOUND (404)
    // ===========================================================

    @ExceptionHandler({
            CustomerNotFoundException.class,
            ProductNotFoundException.class,
            VendorNotFoundException.class,
            InventoryNotFoundException.class,
            PurchaseNotFoundException.class,
            SaleNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(
            RuntimeException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(
                        HttpStatus.NOT_FOUND,
                        ex.getMessage(),
                        request));
    }

    // ===========================================================
    // DUPLICATE (409)
    // ===========================================================

    @ExceptionHandler({
            DuplicateCustomerException.class,
            DuplicateProductException.class,
            DuplicateVendorException.class,
            DuplicateInventoryException.class,
            DuplicatePurchaseException.class,
            DuplicateSaleException.class,
            DuplicateUserException.class
    })
    public ResponseEntity<ErrorResponse> handleDuplicateExceptions(
            RuntimeException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildErrorResponse(
                        HttpStatus.CONFLICT,
                        ex.getMessage(),
                        request));
    }

    // ===========================================================
    // BAD REQUEST (400)
    // ===========================================================

    @ExceptionHandler({
            InvalidInventoryException.class,
            InvalidPurchaseException.class,
            InvalidSaleException.class,
            InvalidUserException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(
            RuntimeException ex,
            HttpServletRequest request) {

        return ResponseEntity.badRequest()
                .body(buildErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        request));
    }

    // ===========================================================
    // VALIDATION (400)
    // ===========================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity.badRequest()
                .body(buildErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        message,
                        request));
    }

    // ===========================================================
    // GLOBAL (500)
    // ===========================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.getMessage(),
                        request));
    }
}