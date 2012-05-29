package frontend;

public class ArrayPositionRunTime extends ArrayPosition {
  private Variable index;
  public ArrayPositionRunTime( ArrayVariable par, Variable ind ){
    super( par );
    index = ind;
  }
  public void compile_assignment( PrintStream ps, Variable other, AssignmentExp owner ) throws CompileException {
    super( ps, other );
    // assume that all size adjustments have already been made
    ArrayData parentData = (ArrayData) getParent().getData();
    String[] index_names = new String[ parentData.getSize() ];
    Variable[] args = new Variable[2];
    args[0] = index;
    for( int i = 0; i < parentData.getSize(); i++){
      String select_name = parent.state_index( ps, i );
      index_names[i] = Variable.temp_var_name();
      
      Function equalfunc = Function.from_name( "==" );
      equalfunc.compile( ps, args, owner );
      
    }
    
  }
}