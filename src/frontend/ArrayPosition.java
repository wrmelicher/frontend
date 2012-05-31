package frontend;

import java.io.PrintStream;

public abstract class ArrayPosition extends Variable {
  private ArrayVariable parent;
  private boolean is_valid;
  public ArrayPosition( ArrayVariable par ){
    super( ((ArrayData)par.getData()).getElementData() );
    parent = par;
  }

  private void setData(){}

  public void compile_assignment( PrintStream ps, Variable other, AssignmentExp owner ) throws CompileException {
    ArrayData parentData = (ArrayData) parent.getData();

    // expand size
    if( other.getData().bit_count() > parentData.getElementData().bit_count() && getData().supports_extend() ){
      // must resize elements
      extend_size( ps, other.getData().bit_count() );
    }
  }
  public ArrayVariable getParent() {
    return parent;
  }
  private void extend_size( PrintStream ps, int to ){
    ArrayData parentData = (ArrayData) parent.getData();
    String[] before_extends = new String[ parentData.getSize() ];
    for( int i = 0; i < parentData.getSize(); i++){
      String select_name = parent.state_index( ps, i );
      before_extends[i] = Variable.temp_var_name();
      ps.println( before_extends[i] + " " + getData().extend_operation() + " " + select_name + " " + to );
    }

    ps.print( parent.new_name() + " concat" );
    for( int i = 0; i < parentData.getSize(); i++ ){
      ps.print( " " + before_extends[i] );
    }
    ps.println();
  }
}