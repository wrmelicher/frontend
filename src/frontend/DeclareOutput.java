package frontend;
import java.io.PrintStream;
public class DeclareOutput extends Statement {
  private Variable out;
  public DeclareOutput( int line, Variable var ){
    super( line );
    out = var;
  }
  public void compile( PrintStream os ) throws CompileException {
    
    os.print(".output "+out.cur_name() );
    if( out instanceof IntVariable ){
      if( ((IntTypeData) out.getData()).signed() )
	os.print(" signed");
      else
	os.print(" unsigned");
    }
    os.println();
  }
}