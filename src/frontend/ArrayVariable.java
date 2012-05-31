package frontend;

import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

public class ArrayVariable extends Variable {
  private List<ArrayPosition> givenPositions;
  public ArrayVariable(String name, ArrayData elem){
    super( name, elem );
    givenPositions = new ArrayList<ArrayPosition>();
  }
  public void compile_assignment( PrintStream os, Variable other, AssignmentExp owner ) throws CompileException {
    os.println( new_name() + " set " + other.cur_name() );
    setData( other.getData() );
  }
  public String state_index( PrintStream ps, int i ) {
    String temp_name = Variable.temp_var_name();
    state_index( ps, i, temp_name );
    return temp_name;
  }
  public void state_index( PrintStream ps, int i, String name ){
    ArrayData parentData = (ArrayData) getData();
    ps.println( name + " select " + cur_name() + " " + (i) * parentData.getElementData().bit_count() + " " + (i+1) * parentData.getElementData().bit_count() );
  }
  public ArrayPosition at( PrintStream ps, Variable v, Expression owner ) throws CompileException {
    if( v.getType() != Type.IntType ){
      throw owner.error("Array reference requires \""+Type.IntType.name() );
    }
    IntTypeData d = (IntTypeData) v.getData();
    ArrayData parentData = (ArrayData) getData();
    if( d.is_constant() ){
      int i = d.value();
      if( i >= parentData.getSize() || i < 0){
	throw owner.error("Array reference out of bounds. Array index: "+i+" Array size: "+parentData.getSize());
      }
      ArrayPosition ans = new ArrayPositionCompileTime( this, i );
      state_index( ps, i, ans.new_name() );
      return ans;
    } else {
      ArrayPosition ans = new ArrayPositionRunTime( this, v );
      
      return ans;
    }
  }
}