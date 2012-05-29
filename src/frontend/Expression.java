package frontend;

import java.io.PrintStream;

public abstract class Expression extends Statement {

  public Expression( int linenum ){
    super( linenum );
  }
  
  public abstract Value returnVar();
  
  public abstract void compile( PrintStream os ) throws CompileException;
  
}