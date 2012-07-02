package frontend;

import java.util.*;
import java.io.PrintStream;

public class Variable<T extends TypeData> implements AbstractVariable<T> {
  private long id;
  private long cur_assignment;
  private long cur_read_version;
  private boolean is_input;
  private T type;
  private boolean is_declared = false;
  private VariableExp reference = null;
  private Map<Integer, String> lengths;

  private boolean allows_assignemnt = true;
  
  private LinkedList< DebugNameRecord > debug_names
    = new LinkedList< DebugNameRecord >();
  
  private static long id_counter = 0;
  
  public Variable( T atype ){
    this( unused_name(), atype );
  }
  
  public static int get_val_from_var( AbstractVariable v_abs, Statement owner ) throws CompileException {
    Variable v = v_abs.var();
    if( v.getType() != Type.IntType ){
      throw owner.error( "Require integer type" );
    }
    if( !v.getData().is_constant() ){
      throw owner.error( "Require integer constant" );
    }
    Variable<IntTypeData> d = (Variable<IntTypeData>)v;
    return d.getData().value();
  }
  
  public Variable( String name, T atype ){
    type = atype;
    id = ++id_counter;

    cur_assignment = 0;
    cur_read_version = 0;
    is_input = false;
    lengths = new HashMap<Integer, String>();

    set_debug_name( name );
  }

  public Variable( String name ){
    this( name, null );
  }

  public static String temp_var_name(){
    return Variable.unused_name() + "_" + (++id_counter);
  }
  
  public static String unused_name(){
    return "temporary";
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

    String val = debug_names.peek().name_at;
    for( Variable.DebugNameRecord dnr : debug_names ){
      if( dnr.assignment_at < cur_read_version ){
	val = dnr.name_at;
      }
    }
    return val;
  }

  private static String escape_chars( String in ){
    String ans = "";
    for( int i = 0; i < in.length(); i++){
      if( Character.isLetterOrDigit( in.charAt( i ) ) ){
	ans += in.charAt(i);
      } else {
	ans += "_";
      }
    }
    return ans;
  }
  
  public void set_debug_name( String n ){
    
    debug_names.add( new Variable.DebugNameRecord( cur_assignment, escape_chars(n) ) );

  }
  
  public String cur_name(){
    if( getData().is_constant() ){
      return getData().constant_name();
    }
    String cur = debug_name() + "_" + id;
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
  
  public static int maxArgLength( Variable[] args ){
    int max = 0;
    for( int i = 0; i < args.length; i++ ){
      max = Math.max( args[i].getData().bit_count(), max );
    }
    return max;
  }
  
  public static String[] padArgsToLength( Variable[] args, int len ){
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


  public Variable<T> var(){
    return this;
  }

  public boolean allows_assignemnt(){
    return allows_assignemnt;
  }
  public void set_allow_assignment( boolean b ){
    allows_assignemnt = b;
  }

  public Variable copy( String name ){
    return new Variable( name, getData() );
  }

  public void compile_assignment( Variable other, Statement owner ) throws CompileException {
    
    if( ProgramTree.DEBUG >= 2 ){
      ProgramTree.output.println("// begin assignment of "+debug_name());
    }
    if( Expression.cond_scope != null ){
      Expression.cond_scope.register_assignment( this );
    }
    if( type != null && other.getType() != getType() ){
      throw owner.error("Cannot assign type "+other.getType().name()+" to variable of type "+getType().name());
    }
    type = (T)other.getData();
    if( !other.getData().is_constant() ) {
      String other_name = other.cur_name();
      ProgramTree.output.println( new_name() + " set " + other_name );
    }
    if( ProgramTree.DEBUG >= 2 ){
      ProgramTree.output.println("// end assignment of "+debug_name() );
    }
  }

  private class DebugNameRecord {
    public long assignment_at;
    public String name_at;
    public DebugNameRecord( long i, String s ){
      assignment_at = i;
      name_at = s;
    }
  }
}