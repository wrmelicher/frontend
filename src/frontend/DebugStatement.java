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
      if( v.var().getType() == Type.ArrayType ){
	ArrayVariable out_arr = (ArrayVariable) v.var();
	out_arr.join_indices();
      }
      ProgramTree.error.println( v.var().toString() );
      ProgramTree.output.println(".output "+v.var().cur_name());
    }
  }
}
