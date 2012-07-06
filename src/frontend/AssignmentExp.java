package frontend;

public class AssignmentExp extends Expression {
  private Expression dest_exp;
  private Expression source;
  private String outputName;
  public AssignmentExp( int linenum, AbstractVariable adest, Expression asource ){
    this( linenum, new VariableExp( linenum, adest ), asource );
  }
  public AssignmentExp( int line, Expression adest, Expression asource ){
    super( line );
    dest_exp = adest;
    source = asource;
    add_child( dest_exp );
    add_child( source );
  }

  public AbstractVariable dest(){
    // returns dest if the dest_expression can be evaluated at compile time
    if( dest_exp instanceof VariableExp ){
      return ((VariableExp)dest_exp).var();
    }
    return null;
  }

  public boolean has_side_effects(){
    return true;
  }

  protected ExpSignature.ExpressionType type(){
    return ExpSignature.ExpressionType.ASSIGNMENT;
  }
  
  public void compile_exp() throws CompileException{
    dest_exp.compile();
    AbstractVariable dest = dest_exp.returnVar();
    source.compile();
    Variable sourceVar = source.returnVar().var();
    if( !sourceVar.getType().satisfies( dest.var().getType() ) ){
      throw error( "Variable \""+dest.debug_name()+"\" is not of type "+sourceVar.getType().name());
    }
    dest.var().compile_assignment( sourceVar, this );
    
    set_ret( dest.var() );
  }
}
