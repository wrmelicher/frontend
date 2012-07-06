package frontend;
import java.io.PrintStream;
public class DeclareOutput extends Statement {
  private AbstractVariable out_abs;
  public DeclareOutput( int line, AbstractVariable var ){
    super( line );
    out_abs = var;
  }

  public void compile() throws CompileException {
    PrintStream os = ProgramTree.output;
    Variable out = out_abs.var();
    os.println( out_abs.debug_name()+"_output set " + out.cur_name() );
    os.print(".output "+out_abs.debug_name()+"_output" );
    if( out.getType() == Type.IntType ){
      if( ((IntTypeData) out.getData()).signed() )
	os.print(" signed");
      else
	os.print(" unsigned");
    } else if( out.getType() == Type.ArrayType ){
      ArrayVariable out_arr = (ArrayVariable) out.var();
      out_arr.join_indices();
    }
    
    os.println();
  }
}
