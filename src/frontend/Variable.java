package frontend;

import java.util.*;
import java.io.PrintStream;

public class Variable<T extends TypeData> implements AbstractVariable<T> {
  private long id;
  private String high_level_name;
  private long cur_assignment;
  private long cur_read_version;
  private boolean is_input;
  private T type;
  private boolean is_declared = false;
  private VariableExp reference = null;
  private Map<Integer, String> lengths;
  
  private static long id_counter = 0;
  public Variable( T atype ){
    this( unused_name(), atype );
  }
  public static int get_val_from_var( AbstractVariable v, Statement owner ) throws CompileException {
    if( v.getType() != Type.IntType ){
      throw owner.error( "Require integer type" );
    }
    if( !v.getData().is_constant() ){
      throw owner.error( "Require integer constant" );
    }
    if( v instanceof FunctionArgument ){
      return get_val_from_var( ((FunctionArgument) v).getVar(), owner );
    } 
    if( v instanceof Variable ){
      Variable<IntTypeData> d = (Variable<IntTypeData>)v;
      return d.getData().value();
    }
    return -1;
  }
  
  public Variable( String name, T atype ) {
    type = atype;
    id = ++id_counter;
    high_level_name = name;
    cur_assignment = 0;
    cur_read_version = 0;
    is_input = false;
    lengths = new HashMap<Integer, String>();
  }

  public Variable( String name ){
    this( name, null );
  }

  public static String temp_var_name(){
    return Variable.unused_name() + (++id_counter);
  }
  public static String unused_name(){
    return "temp_var_";
  }
  public T getData(){
    return type;
  }
  public void setData( T t ){
    type = t;
  }
  public Type getType(){
    return type.getType();
  }
  
  public int hashCode(){
    return (int) id;
  }
  public boolean equals( Object other ){
    if( other instanceof Variable ){
      return ((Variable) other).id == id;
    }
    return false;
  }
  
  public String debug_name(){
    return high_level_name;
  }
  
  public String cur_name(){
    if( getData().is_constant() ){
      return getData().constant_name();
    }
    String cur = high_level_name + "_" + id;
    if( cur_read_version == 0 )
      return cur;
    else 
      return cur + "_" + cur_read_version;
  }
  
  public String new_name(){
    inc_name();
    lengths.clear();
    return cur_name();
  }

  private void inc_name(){
    cur_assignment++;
    cur_read_version = cur_assignment;
  }
  public void mark_as_input(){
    is_input = true;
  }

  public String padTo( int i ){
    if( i == getData().bit_count() )
      return cur_name();
    if( lengths.containsKey( i ) )
      return lengths.get( i );
    else {
      String op = getData().extend_operation();
      if( i < getData().bit_count() ){
	op = "trunc";
      }
      String out = Variable.temp_var_name();
      ProgramTree.output.println( out + " " + op + " " + cur_name() + " " + i );
      lengths.put( i, out );
      return out;
    }
  }
  public String padTo( int i, String name ){
    if( i == getData().bit_count() )
      return cur_name();
    if( lengths.containsKey( i ) )
      return lengths.get( i );
    else {
      String op = getData().extend_operation();
      if( i < getData().bit_count() ){
	op = "trunc";
      }
      String out = Variable.temp_var_name();
      ProgramTree.output.println( out + " " + op + " " + name + " " + i );
      lengths.put( i, out );
      return out;
    }
  }
  
  public static int maxArgLength( AbstractVariable[] args ){
    int max = 0;
    for( int i = 0; i < args.length; i++ ){
      max = Math.max( args[i].getData().bit_count(), max );
    }
    return max;
  }
  
  public static String[] padArgsToLength( AbstractVariable[] args, int len ){
    String[] ans = new String[ args.length ];
    for( int i = 0; i < args.length; i++){
      ans[i] = args[i].padTo( len );
    }
    return ans;
  }

  public String name_at_point( Variable.Snapshot sn ){
    if( !sn.owner().equals( this ) )
      return "ERROR";
    long temp = cur_read_version;
    T data = getData();
    cur_read_version = sn.read;
    type = (T) sn.data;
    String ans = cur_name();
    cur_read_version = temp;
    type = data;
    return ans;
  }

  public void reset_from_snap( Variable.Snapshot prev ){
    if( !prev.owner().equals( this ) )
      return;
    lengths.clear();
    cur_read_version = prev.read;
    setData( (T)prev.data );
  }
  
  public Variable.Snapshot snapshot(){
    return new Snapshot( this );
  }

  public class Snapshot {
    public Long read;
    public T data;
    private Variable<T> owner;
    public Snapshot(Variable<T> own){
      owner = own;
      read = cur_read_version;
      data = getData();
    }
    public Variable owner(){
      return owner;
    }
    public String name_at(){
      return owner.name_at_point( this );
    }
    public int length_at(){
      return data.bit_count();
    }
    public String padTo( int i ){
      Variable.Snapshot temp = owner.snapshot();
      owner.reset_from_snap( this );
      String ans = owner.padTo( i );
      owner.reset_from_snap( temp );
      return ans;
    }
  }

  public void compile_assignment( AbstractVariable other, Statement owner ) throws CompileException {
    if( ProgramTree.DEBUG >= 2 ){
      ProgramTree.output.println("// begin assignment of "+debug_name());
    }
    if( Expression.cond_scope != null ){
      Expression.cond_scope.register_assignment( this );
    }
    /*if( type != null && other.getType() != getType() ){
      throw owner.error("Cannot assign type "+other.getType().name()+" to variable of type "+getType().name());
      }*/
    type = (T)other.getData();
    if( !other.getData().is_constant() ) {
      String other_name = other.cur_name();
      ProgramTree.output.println( new_name() + " set " + other_name );
    }
    if( ProgramTree.DEBUG >= 2 ){
      ProgramTree.output.println("// end assignment of "+debug_name() );
    }
  }
}
