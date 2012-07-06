package frontend;


public interface Signatured {
    public boolean matches( Signatured other )
      throws CompileException;
}
