package cl.duoc.kembang.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresDeValidacion(MethodArgumentNotValidException excepcion) {
        Map<String, String> listaDeErrores = new HashMap<>();
        for (FieldError error : excepcion.getBindingResult().getFieldErrors()) {
            listaDeErrores.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(listaDeErrores, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoNoEncontrado(ResourceNotFoundException excepcion) {
        return respuesta(excepcion.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Map<String, String>> manejarStockInsuficiente(StockInsuficienteException excepcion) {
        return respuesta(excepcion.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailEnUsoException.class)
    public ResponseEntity<Map<String, String>> manejarEmailEnUso(EmailEnUsoException excepcion) {
        return respuesta(excepcion.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<Map<String, String>> manejarCredencialesInvalidas(CredencialesInvalidasException excepcion) {
        return respuesta(excepcion.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Map<String, String>> respuesta(String mensaje, HttpStatus estado) {
        Map<String, String> cuerpo = new HashMap<>();
        cuerpo.put("error", mensaje);
        return new ResponseEntity<>(cuerpo, estado);
    }
}