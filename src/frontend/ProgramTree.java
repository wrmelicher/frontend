package frontend;

import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

public class ProgramTree {

  private List<Statement> statements;
  private List<Statement> inputs;
  private List<Statement> outputs;
  public ProgramTree(){
    statements = new ArrayList<Statement>();
    inputs = new ArrayList<Statement>();
    outputs = new ArrayList<Statement>();
  }
  
  public void compile( PrintStream ps ) throws CompileException{

    // inputs and stuff
    compile_list( ps, inputs );
    compile_list( ps, statements );
    compile_list( ps, outputs );
    ps.close();
  }
  private void compile_list( PrintStream ps, List<Statement> l ) throws CompileException{
    for( Statement s : l ){
      s.compile( ps );
    }
  }
  public void addStatement( Statement s ){
    if( s instanceof DeclareOutput )
      outputs.add( s );
    else if( s instanceof DeclareInputStatement )
      inputs.add( s );
    else 
      statements.add( s );
  }
}