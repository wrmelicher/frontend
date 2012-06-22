package frontend;

public class CompileException extends Exception {
  
  public CompileException( String mess, int linenum ){
    super( "line: " + linenum + " " + mess );
  }
  public CompileException( String mess ){
    super( mess );
  }
}