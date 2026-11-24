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

    // 400: datos invalidos (por ejemplo, nombre vacio o precio negativo).
    // Se activa cuando el controller usa @Valid y alguna validacion falla.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresDeValidacion(MethodArgumentNotValidException excepcion) {

        Map<String, String> listaDeErrores = new HashMap<>();

        // recorremos solo los errores de campos (nombre, precio, stock...)
        for (FieldError error : excepcion.getBindingResult().getFieldErrors()) {
            listaDeErrores.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(listaDeErrores, HttpStatus.BAD_REQUEST);
    }

    // 404: se busco algo que no existe
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoNoEncontrado(ResourceNotFoundException excepcion) {
        Map<String, String> respuestaError = new HashMap<>();
        respuestaError.put("error", excepcion.getMessage());
        return new ResponseEntity<>(respuestaError, HttpStatus.NOT_FOUND);
    }
}