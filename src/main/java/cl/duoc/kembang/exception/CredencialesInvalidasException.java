package cl.duoc.kembang.exception;

public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}