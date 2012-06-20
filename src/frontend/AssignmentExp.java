package frontend;

public class AssignmentExp extends Expression {
  private Expression dest_exp;
  private AbstractVariable dest;
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
  public AbstractVariable returnVar(){
    return dest;
  }
  public void compile() throws CompileException{
    dest_exp.compile();
    dest = dest_exp.returnVar();
    source.compile();
    AbstractVariable sourceVar = source.returnVar();
    if( !sourceVar.getType().satisfies( dest.getType() )  ){
      throw error( "Variable \""+dest.debug_name()+"\" is not of type "+sourceVar.getType().name());
    }

    dest.compile_assignment( sourceVar, this );

  }
}