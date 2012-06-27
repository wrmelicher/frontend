package frontend;

import java.io.PrintStream;

public class ArrayAccessExp extends Expression {
  private Expression index;
  private AbstractVariable arr;
  private ArrayPosition out;
  public ArrayAccessExp( int linenum, AbstractVariable a, int i ){
    this( linenum, a, new Variable<IntTypeData>( new IntTypeData( i ) ) );
  }
  public ArrayAccessExp( int linenum, AbstractVariable a, AbstractVariable<IntTypeData> i ){
    this( linenum, a, new VariableExp( linenum, i ) );
  }
  public ArrayAccessExp( int linenum, AbstractVariable a, Expression ind ){
    super( linenum );
    index = ind;
    arr = a;
  }
  
  public AbstractVariable returnVar(){
    return out;
  }
  
  public void compile() throws CompileException {
    
    if( arr.var().getType() != Type.ArrayType ){
      throw error("Cannot index non-array variable "+arr.debug_name() );
    }
    ArrayVariable arr_real = ArrayVariable.get_from_abstract_var( arr );
    index.compile();
    Variable ind_var = index.returnVar().var();
    if( ind_var.getType() != Type.IntType )
      throw error("Cannot index variable with non-integer type" );
    out = arr_real.at( ind_var, this );
  }
  
}