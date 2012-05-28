package frontend;

import java.io.PrintStream;

public class ArrayVariable extends Variable {
  
  public ArrayVariable(String name, ArrayData elem){
    super( name, elem );
  }
  public void compile_assignment( PrintStream os, Variable other, AssignmentExp owner ) throws CompileException {
    os.println( new_name() + " set " + other.cur_name() );
    setData( other.getData() );
  }
  public String state_index( PrintStream ps, int i ) {

    ArrayData parentData = (ArrayData) getData();
    String temp_name = Variable.temp_var_name();
    ps.println( temp_name + " select " + parent.cur_name() + " " + (i) * parentData.getElementData().bit_count() + " " (i+1) * parent.getElementData().bit_count() );
    return temp_name;
  }
}