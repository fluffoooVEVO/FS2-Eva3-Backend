package cl.duoc.kembang.exception;

public class EmailEnUsoException extends RuntimeException {

    public EmailEnUsoException(String mensaje) {
        super(mensaje);
    }
}