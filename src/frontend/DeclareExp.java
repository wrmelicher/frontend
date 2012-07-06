package frontend;

public class DeclareExp extends Statement {
  private DummyVariable dest;
  private Expression source;
  
  public DeclareExp( int line, DummyVariable var, Expression first ){
    super( line );
    dest = var;
    source = first;
    add_child( source );
  }
  
  public void compile() throws CompileException {
    source.compile();
    Variable first = source.returnVar().var();
    Variable var = first.copy( dest.debug_name() );
    dest.set_call_depth( UserFunction.call_depth() );
    dest.push_var( var );
    var.compile_assignment( first, this );
  }
}
