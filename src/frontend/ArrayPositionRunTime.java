package frontend;
import java.io.PrintStream;

public class ArrayPositionRunTime extends ArrayPosition {
  private Variable index;
  public ArrayPositionRunTime( ArrayVariable par, Variable ind ){
    super( par );
    index = ind;
  }
  public void compile_assignment( PrintStream ps, Variable other, AssignmentExp owner ) throws CompileException {
    super.compile_assignment( ps, other, owner );
    // assume that all size adjustments have already been made
    ArrayData parentData = (ArrayData) getParent().getData();
    String[] mux_out_names = new String[ parentData.getSize() ];
    Variable[] args = new Variable[2];
    args[0] = index;
    for( int i = 0; i < parentData.getSize(); i++){
      String select_name = getParent().state_index( ps, i );

      args[1] = new Variable( new IntTypeData( i ) ); 
      Function equalfunc = Function.from_name( "==" );
      Variable mux_choice = equalfunc.compile( ps, args, owner );
      
      mux_out_names[i] = Variable.temp_var_name();
      ps.println( mux_out_names[i]+" chose "+mux_choice.cur_name()+" "+other.cur_name()+" "+select_name );
    }
    ps.print( getParent().new_name() + " concat");
    for( int i = 0; i < parentData.getSize(); i++){
      ps.print( " "+mux_out_names[i] );
    }
    ps.println();
  }
}