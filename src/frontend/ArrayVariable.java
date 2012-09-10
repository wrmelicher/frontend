package frontend;

import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

public class ArrayVariable extends Variable<ArrayData> implements Changer {
  
  public ArrayVariable(String name, ArrayData elem){
    super( name, elem );
    for( int i = 0; i < elem.getSize(); i++ ){
      elem.at(i).setParent(this);
    }
  }
  public ArrayVariable( ArrayData elem ){
    this( Variable.temp_var_name(), elem );
  }

  public void set_changed(){
    notify_all();
  }

  public static ArrayVariable literal
    ( List<AbstractVariable> cons_vars,
      Statement owner ) throws CompileException {
    /*    if( cons_vars.size() == 0 ){
      throw owner.error("cannot make size 0 array");
      }*/
    
    Variable first;
    Type t;
    if( cons_vars.size() != 0 ){
      first = cons_vars.get(0).var();
      t = first.getType();
    } else {
      t = Type.ANYTYPE;
    }

    for( AbstractVariable v : cons_vars ){
      if( t != v.var().getType() ){
	throw owner.error( "All elements of array must have the same type" );
      }
    }
    ArrayList<ArrayPositionCompileTime> new_arr =
      new ArrayList<ArrayPositionCompileTime>();
    int i = 0;
    for( AbstractVariable index : cons_vars ){
      ArrayPositionCompileTime to_add =
	new ArrayPositionCompileTime( index.var().getData(), i++ );
      to_add.compile_assignment( index.var(), owner );
      new_arr.add( to_add );
    }
    ArrayVariable ans = new ArrayVariable( new ArrayData(new_arr) );
    return ans;
  }
    
  public static ArrayVariable get_from_abstract_var( AbstractVariable v ){
    assert v.var().getType() == Type.ArrayType : "Error, value is not an array";
    return (ArrayVariable) v.var();    
  }
  
  @Override public void compile_assignment
    ( Variable other,
      Statement owner ) throws CompileException {
    if( other.getType() != Type.ArrayType ){
      throw owner.error("Cannot assign "+other.getType().name() +" to array variable");
    }
    ArrayData other_data = (ArrayData) other.getData();
    setData( other_data.copy(owner) );
  }
  
  public String state_index( int i ) {
    String temp_name = Variable.temp_var_name();
    state_index( i, temp_name );
    return temp_name;
  }
  
  public void state_index( int i, String name ){
    ArrayData parentData = getData();
    int block_size = parentData.any_elem().bit_count();
    ProgramTree.output.println( name + " select " +
				cur_name() + " " +
				(i) * block_size + " "
				+ (i+1) * block_size );
  }

  public void init(){
    for( ArrayPositionCompileTime a : getData().elems() ){
      a.read_value();
    }
  }
  
  public ArrayPosition at
    ( AbstractVariable<IntTypeData> v,
      Statement owner ) throws CompileException {
    IntTypeData d = v.var().getData();
    ArrayData parentData = getData();
    ArrayPosition ans; 
    if( d.is_constant() ){
      int i = d.value();
      if( i >= parentData.getSize() || i < 0){
	throw owner.error("Array reference out of bounds. "+
			  "Array index: "+i+" Array size: "+parentData.getSize());
      }
      ans = getData().at( i );
    } else {
      ans = new ArrayPositionRunTime( this, v.var() );
      ans.notify_changes( this );
    }
    return ans;
  }
  
  @Override public Variable copy( String name ){
    return new ArrayVariable( name, getData() );
  }
  
  public void join_indices(){
    
    if( ProgramTree.DEBUG >= 2 )
      ProgramTree.output.println("//joining indices of array "+debug_name() );
    
    ArrayList<String> args = new ArrayList<String>();
    int block_size = getData().any_elem().bit_count();
    for( int i = 0; i < getData().getSize(); i++ ){
      String pos_add = getData().at(i).padTo( block_size );
      args.add( pos_add );
    }
    ProgramTree.output.print( new_name() + " concatls" );
    for( String s : args ){
      ProgramTree.output.print( " " + s );
    }
    ProgramTree.output.println();
    
    if( ProgramTree.DEBUG >= 2 )
      ProgramTree.output.println("//ending join indices\n");
  }

  public Variable.Snapshot snapshot(){
    return new ArraySnapshot( this );
  }

  private class ArraySnapshot extends Variable.Snapshot {
    public ArraySnapshot( ArrayVariable v ){
      super( v );
    }
    @Override public boolean matches( Signatured sig ){
      if( !( sig instanceof ArraySnapshot ) ){
	return false;
      }
      return false;
      /*ArraySnapshot other = (ArraySnapshot) sig;
      boolean ans = super.matches( other );
      for( ArrayPositionCompileTime a : given_compile ){
	if( a.is_changed() )
	  return false;
      }
      return ans;*/
    }
    @Override
    public Variable copy(){
      owner_at();
      ArrayVariable ans = new ArrayVariable( (ArrayData)owner().getData() );
      ans.starting_name = owner().cur_name();
      reset_owner();
      return ans;
    }
  }
}
