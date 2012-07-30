package frontend;
import java.io.PrintStream;
import java.util.Scanner;
public class DeclareInputStatement extends Statement {
  private Variable var;
  private int party;
  public DeclareInputStatement( int line, Variable in, int p ){
    super(line);
    var = in;
    party = p;
    var.mark_as_input();
  }

  public Variable var(){
    return var;
  }

  public void compile() throws CompileException {
    ProgramTree.output.println(".input "+var.cur_name()+" "+party+" "+var.getData().bit_count() );
  }

  public void request_val( PrintStream ps, Scanner in ){
    String out_name = var.cur_name();
    ps.println( out_name + " " + var.getData().user_input( var.debug_name(), party, in ) );
  }
  public void request_val( PrintStream ps ){
    String out_name = var.cur_name();
    ps.println( out_name + " " + var.getData().user_input() );
  }
  
  public int party(){
    return party;
  }
}
