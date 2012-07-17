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
  private List<Changer> notifiers =
    new LinkedList<Changer>();

  private String starting_name = null;
  private int call_depth;

  private boolean allows_assignemnt = true;
  
  private LinkedList< DebugNameRecord > debug_names
    = new LinkedList< DebugNameRecord >();

  private static long id_counter = 0;
  private static Map<String, UnionSet > equivalent_names
    = new HashMap<String, UnionSet >();
  
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
    call_depth = UserFunction.call_depth();
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
    if( starting_name != null && cur_read_version == 0 ){
      return starting_name;
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
    String ans = cur_name();
    return ans;
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

  public class Snapshot implements
    Signatured {
    public Long read;
    public T data;

    private Variable.Snapshot temp;
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
      String ans = owner_at().cur_name();
      reset_owner();
      return ans;
    }
    public int length_at(){
      return data.bit_count();
    }
    public String padTo( int i ){
      String ans = owner_at().padTo( i );
      reset_owner();
      return ans;
    }
    public Variable<T> owner_at(){
      temp = owner.snapshot();
      owner.reset_from_snap( this );
      return owner;
    }
    public void reset_owner(){
      owner.reset_from_snap( temp );
    }
    public Variable copy(){
      owner_at();
      Variable<T> ans = new Variable( owner().getData() );
      ans.starting_name = owner().cur_name();
      reset_owner();
      return ans;
    }
    public boolean matches( Signatured sig ){
      if( !( sig instanceof Variable.Snapshot ) ){
	return false;
      }
      Variable.Snapshot other = (Variable.Snapshot) sig;
      String other_name = other.name_at();
      boolean ans = owner_at().equivalent( other_name );
      reset_owner();
      return ans;
    }
    public int hashCode(){
      String my_name = name_at();
      UnionSet my_set = Variable.equivalent_names.get( my_name );
      if( my_set != null ){
	return my_set.hashCode();
      }
      return my_name.hashCode();
    }
  }

  public Variable<T> var(){
    return this;
  }

  private boolean equivalent( String other ){
    String name = cur_name();
    UnionSet eq = Variable.equivalent_names.get( name );
    boolean ans = eq == null ? false : eq.contains(other);
    return ans || name.equals( other );
  }

  private static void add_equivalent_name( String one, String two ){
    UnionSet eq_one = Variable.equivalent_names.get(one);
    UnionSet eq_two = Variable.equivalent_names.get(two);
    if( eq_one == null && eq_two == null ){
      eq_one = new UnionSet(one,two);
    } else if( eq_one == null ){
      eq_two.add_str( one );
    } else if( eq_two == null ){
      eq_one.add_str( two );
    } else {
      // I don't think this happens
      UnionSet.merge( eq_one, eq_two );
    }
  }
  
  public Variable copy( String name ){
    return new Variable( name, getData() );
  }

  public void notify_changes( Changer c ){
    notifiers.add(c);
  }

  public void remove_notifier( Changer c ){
    notifiers.remove( c );
  }

  public void notify_all(){
    for( Changer c : notifiers ){
      c.set_changed();
    }
  }

  public int call_depth(){
    return call_depth;
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
      String new_name = new_name();
      ProgramTree.output.println( new_name + " set " + other_name );
      Variable.add_equivalent_name( new_name, other_name );
    }
    notify_all();
    if( ProgramTree.DEBUG >= 2 ){
      ProgramTree.output.println("// end assignment of "+debug_name() );
    }
  }

  private static class DebugNameRecord {
    public long assignment_at;
    public String name_at;
    public DebugNameRecord( long i, String s ){
      assignment_at = i;
      name_at = s;
    }
  }
  
  private static class UnionSet {
    private Set<String> str_set;
    private String identifier;
    public UnionSet( String a, String b ){
      str_set = new HashSet<String>();
      identifier = a;
      add_str(a);
      add_str(b);
    }
    public void add_str( String b ){
      str_set.add(b);
      Variable.equivalent_names.put( b, this );
    }
    public static void merge( UnionSet a, UnionSet b ){
      a.str_set.addAll(b.str_set);
      for( String s : b.str_set ){
	Variable.equivalent_names.put(s,a);
      }
      // TODO: may cause error with hash table if id is changed
    }
    public int hashCode(){
      return identifier.hashCode();
    }
    public boolean contains( String s ){
      return str_set.contains(s);
    }
  }
}
