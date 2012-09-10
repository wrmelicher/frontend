package frontend;

import java.util.List;
import java.util.LinkedList;

public class ExpressionContainer extends Statement {
  private List<Statement> statements;
  public ExpressionContainer( int line ){
    super( line );
    statements = new LinkedList<Statement>();
  }
  public void add_statement( Statement s ){
    statements.add( s );
    add_child( s );
  }
  public List<Statement> statements(){
    return statements;
  }
  public void compile() throws CompileException {
    for( Statement s : statements() ){
      s.compile();
    }
  }
}
