package frontend;

import java.io.PrintStream;

public class AssignmentExp extends Expression {
  private Variable dest;
  private Expression source;
  private String outputName;
  public AssignmentExp( int linenum, Variable adest, Expression asource ){
    super( linenum );
    dest = adest;
    source = asource;
  }
  public Variable returnVar(){
    return dest;
  }
  public void compile( PrintStream os ) throws CompileException{
    source.compile( os );
    Variable sourceVar = source.returnVar();
    if( !dest.getType().equals( sourceVar.getType() ) ){
      throw error( "Variable \""+dest.debug_name()+"\" is not of type "+sourceVar.getType().name());
    }
    dest.compile_assignment( os, sourceVar, this );
  }
}