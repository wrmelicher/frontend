package frontend;

import java.io.PrintStream;

public class ArrayAccessExp extends Expression {
  private Variable index;
  private ArrayVariable arr;
  private ArrayPosition out;
  public ArrayAccessExp( int linenum, ArrayVariable a, int i ){
    this( linenum, a, new Variable( new IntTypeData( i ) ) );
  }
  public ArrayAccessExp( int linenum, ArrayVariable a, Variable i ){
    super( linenum );
    index = i;
    arr = a;
  }
  
  public Variable returnVar(){
    return out;
  }
  
  public void compile( PrintStream ps ) throws CompileException {
    out = arr.at( ps, index, this );
  }
  
}