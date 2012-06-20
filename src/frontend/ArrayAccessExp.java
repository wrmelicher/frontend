package frontend;

import java.io.PrintStream;

public class ArrayAccessExp extends Expression {
  private Expression index;
  private ArrayVariable arr;
  private ArrayPosition out;
  public ArrayAccessExp( int linenum, ArrayVariable a, int i ){
    this( linenum, a, new Variable<IntTypeData>( new IntTypeData( i ) ) );
  }
  public ArrayAccessExp( int linenum, ArrayVariable a, AbstractVariable<IntTypeData> i ){
    this( linenum, a, new VariableExp( linenum, i ) );
  }
  public ArrayAccessExp( int linenum, ArrayVariable a, Expression ind ){
    super( linenum );
    index = ind;
    arr = a;
  }
  
  public AbstractVariable returnVar(){
    return out;
  }
  
  public void compile() throws CompileException {
    index.compile();
    AbstractVariable ind_var = index.returnVar();
    if( ind_var.getType() != Type.IntType )
      throw error("Cannot index variable with non-integer type" );
    out = arr.at( ind_var );
  }
  
}