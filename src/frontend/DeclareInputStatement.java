package frontend;
import java.io.PrintStream;

public class DeclareInputStatement extends Statement {
  private Variable var;
  private int party;
  public DeclareInputStatement( int line, Variable in, int p ){
    super(line);
    var = in;
    party = p;
    var.mark_as_input();
  }
  public void compile( PrintStream os ) throws CompileException {
    os.println(".input "+var.cur_name()+" "+party+" "+var.getData().bit_count() );
  }
}