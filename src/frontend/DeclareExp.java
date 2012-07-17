package frontend;

public class DeclareExp extends Expression {
  private DummyVariable dest;
  private Expression source;
  
  public DeclareExp( int line, DummyVariable var, Expression first ){
    super( line );
    dest = var;
    source = first;
    add_child( source );
  }

  public boolean has_side_effects(){
    return true;
  }

  protected ExpSignature.ExpressionType type(){
    return ExpSignature.ExpressionType.ASSIGNMENT;
  }

  public void compile_exp() throws CompileException {
    source.compile();
    Variable first = source.returnVar().var();
    Variable var = first.copy( dest.debug_name() );
    dest.set_call_depth( UserFunction.call_depth() );
    dest.push_var( var );
    var.compile_assignment( first, this );
    set_ret(dest);
  }
}
