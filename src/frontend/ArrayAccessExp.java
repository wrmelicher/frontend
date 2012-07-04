package frontend;

import java.io.PrintStream;

public class ArrayAccessExp extends Expression {
  private Expression index;
  private AbstractVariable arr;
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
    index.setParent( this );
  }
  
  public void compile_exp() throws CompileException {
    
    if( arr.var().getType() != Type.ArrayType ){
      throw error("Cannot index non-array variable "+arr.debug_name() );
    }
    ArrayVariable arr_real = ArrayVariable.get_from_abstract_var( arr );
    index.compile();
    Variable ind_var = index.returnVar().var();
    if( ind_var.getType() != Type.IntType )
      throw error("Cannot index variable with non-integer type" );
    set_ret( arr_real.at( ind_var, this ) );
  }

  public boolean has_side_effects(){
    return false;
  }

  public ExpSignature sig(){
    ExpSignature ans = new ExpSignature( ExpSignature.ExpressionType.ARRAYACCESS );
    ans.depends( arr.var().snapshot() );
    ans.depends( index );
    return ans;
  }
  
}
