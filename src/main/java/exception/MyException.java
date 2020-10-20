package exception;

public class MyException extends RuntimeException{
    private static final long serialVersionUID = 6334912929831829203L;

    public MyException(String msg) {
        super(msg);
    }
}
