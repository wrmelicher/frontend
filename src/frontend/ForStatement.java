package frontend;

import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

class ForStatement extends Statement {
  private List<Statement> statements;
  private Variable loop_var;
  private int from;
  private int to;
  public ForStatement( int line, int lower, int upper ){
    super( line );
    statements = new ArrayList<Statement>();
    from = lower;
    to = upper;

  }
  public void addStatement( Statement s ){
    statements.add( s );
  }
  public void compile( PrintStream ps ){
    
  }
}