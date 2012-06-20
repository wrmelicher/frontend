package frontend;

public interface AbstractVariable<T extends TypeData> {
  public Type getType();
  public T getData();
  public void setData( T t );
  public String debug_name();
  public String cur_name();
  public String new_name();
  public String padTo( int i );
  public String name_at_point( Variable.Snapshot sn );
  public Variable.Snapshot snapshot();
  public void reset_from_snap( Variable.Snapshot prev );
  public void compile_assignment( AbstractVariable other, Statement owner ) throws CompileException;
}