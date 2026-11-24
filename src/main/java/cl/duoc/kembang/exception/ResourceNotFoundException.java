package cl.duoc.kembang.exception;

// Se lanza cuando se busca algo que no existe (por ejemplo, un producto con un id inexistente).
// Extiende de RuntimeException para poder lanzarla.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}