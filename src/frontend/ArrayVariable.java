package frontend;

import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

public class ArrayVariable extends Variable<ArrayData> {
  private List<ArrayPosition> givenPositions;
  private ArrayPositionCompileTime[] given_compile;
  
  public ArrayVariable(String name, ArrayData elem){
    super( name, elem );
    givenPositions = new ArrayList<ArrayPosition>();
    given_compile = new ArrayPositionCompileTime[ elem.getSize() ];
    for( int i = 0; i < elem.getSize(); i++ ){
      given_compile[i] = new ArrayPositionCompileTime( this, i );
      givenPositions.add( given_compile[i] );
    }
  }

  public static ArrayVariable get_from_abstract_var( AbstractVariable v ){
    assert v.getType() == Type.ArrayType : "Error, value is not an array";

    if( v instanceof FunctionArgument ){
      return get_from_abstract_var( ((FunctionArgument)v).getVar() );
    } else if( v instanceof ArrayVariable ) {
      return (ArrayVariable) v;
    } else {
      return null;
    }
    
  }
  
  /*
  public void compile_assignment( AbstractVariable other, AssignmentExp owner ) throws CompileException {
    
    if( other.getType() != getType() ){
      throw owner.error("Cannot assign type "+other.getType().name()+" to variable of type "+getType().name());
    }
    ProgramTree.output.println( new_name() + " set " + other.cur_name() );
    setData( other.getData() );
    invalidate_indices();
    }*/
  
  public String state_index( int i ) {
    String temp_name = Variable.temp_var_name();
    state_index( i, temp_name );
    return temp_name;
  }
  
  public void state_index( int i, String name ){
    ArrayData parentData = getData();
    
    ProgramTree.output.println( name + " select " + cur_name() + " " + (i) * parentData.getElementData().bit_count() + " " + (i+1) * parentData.getElementData().bit_count() );
  }
  
  public ArrayPosition at( AbstractVariable<IntTypeData> v ){
    IntTypeData d = v.getData();
    ArrayData parentData = getData();
    ArrayPosition ans; 
    if( d.is_constant() ){
      int i = d.value();
      if( i >= parentData.getSize() || i < 0){
	// throw owner.error("Array reference out of bounds. Array index: "+i+" Array size: "+parentData.getSize());
	return null;
      }
      ans = given_compile[ i ];
    } else {
      ans = new ArrayPositionRunTime( this, v );
      givenPositions.add( ans );
    }
    return ans;
  }
  
  public void invalidate_indices(){
    for( ArrayPosition pos : givenPositions ){
      pos.invalidate();
    }
  }
  
  public int element_size(){
    return getData().getElementData().bit_count();
  }

  public void join_indices(){
    join_indices( true );
  }
  
  public void join_indices( boolean invalidate ){

    if( ProgramTree.DEBUG >= 2 ){
      ProgramTree.output.println("\n//joining indices of array "+debug_name() );
    }
    
    ArrayList<String> args = new ArrayList<String>();
    int prev = 0;
    TypeData new_elem_data = getData().getElementData();
    for( ArrayPositionCompileTime p : given_compile ){
      new_elem_data = new_elem_data.conditional( p.getData() );
    }
    for( int i = 0; i < getData().getSize(); i++ ){
      if( given_compile[i].is_changed() && given_compile[i].is_valid() ){
	if( prev != i ) {
	  String temp = Variable.temp_var_name();
	  ProgramTree.output.println(temp+" select "+ cur_name() + " " + ( prev * element_size() ) + " " + ( i * element_size() ) );
	  args.add( temp );
	}
	prev = i + 1;
	String changed_add = given_compile[i].padTo( new_elem_data.bit_count() );
	given_compile[i].setData( new_elem_data );
	args.add( changed_add );
      }
    }
    if( prev != getData().getSize() ){
      String temp = Variable.temp_var_name();
      ProgramTree.output.println(temp+" select "+ cur_name() + " " + ( prev * element_size() ) + " " + ( getData().getSize() * element_size() ) );
      args.add( temp );
    }
    ProgramTree.output.print( new_name() + " concatls" );
    for( String s : args ){
      ProgramTree.output.print( " " + s );
    }
    ProgramTree.output.println();
    ProgramTree.output.println("//ending join indices\n");
    if( invalidate )
      invalidate_indices();
  }
}