package linguarium.auth.local.exception;

public class NoPrincipalsFoundException extends IllegalStateException {
    public NoPrincipalsFoundException(String message) {
        super(message);
    }
}
