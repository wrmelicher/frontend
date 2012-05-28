package frontend;
import java.io.PrintStream;
abstract class Statement {
  private int linenum;
  public Statement( int line ){
    linenum = line;
  }
  public abstract void compile( PrintStream os ) throws CompileException;

  public CompileException error( String mess ){
    return new CompileException( mess, linenum );
  }
}