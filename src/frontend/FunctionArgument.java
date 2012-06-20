package frontend;

public class FunctionArgument<T extends TypeData> implements AbstractVariable<T> {
  private AbstractVariable<T> actual = null;
  private String debug;
  public FunctionArgument( String name ){
    debug = name;
  }
  public Type getType(){
    if( actual == null )
      return Type.ANYTYPE;
    return actual.getType();
  }
  public T getData(){
    return actual.getData();
  }
  public void setData( T t ){
    actual.setData( t );
  }
  public String debug_name(){
    return debug;
  }
  public String cur_name(){
    return actual.cur_name();
  }
  public String new_name(){
    return actual.new_name();
  }
  public String padTo( int i ){
    return actual.padTo( i );
  }
  public String name_at_point( Variable.Snapshot sn ){
    return actual.name_at_point( sn );
  }
  public Variable.Snapshot snapshot(){
    return actual.snapshot();
  }
  public void reset_from_snap( Variable.Snapshot s ){
    actual.reset_from_snap( s );
  }
  public void compile_assignment( AbstractVariable other, Statement owner ) throws CompileException {
    if( actual == null )
      actual = other;
    actual.compile_assignment( other, owner );
  }
  public AbstractVariable<T> getVar(){
    return actual;
  }
  public void setVar( AbstractVariable<T> a ){
    actual = a;
  }
}