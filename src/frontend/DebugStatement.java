package frontend;

public class DebugStatement extends Statement {
  private Expression val;
  public DebugStatement( int line, Expression e ){
    super(line);
    val = e;
  }
  public void compile() throws CompileException {
    if( ProgramTree.DEBUG >= 1 ){
      val.compile();
      AbstractVariable v = val.returnVar();
      ProgramTree.error.println( v.var().toString() );
    }
  }
}
