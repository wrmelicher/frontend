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
  }

  public boolean has_side_effects(){
    return true;
  }

  public ExpSignature sig(){
    return null;
  }
  
  public void compile_exp() throws CompileException{
    dest_exp.compile();
    AbstractVariable dest = dest_exp.returnVar();
    source.compile();
    Variable sourceVar = source.returnVar().var();
    if( !sourceVar.getType().satisfies( dest.var().getType() ) ){
      throw error( "Variable \""+dest.debug_name()+"\" is not of type "+sourceVar.getType().name());
    }
    if( !dest.var().allows_assignemnt() ){
      throw error( "Cannot assign to variable "+dest.debug_name() );
    }
    dest.var().compile_assignment( sourceVar, this );
    
    set_ret( dest.var() );
  }
}
