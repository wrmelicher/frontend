package frontend;

public interface AbstractVariable<T extends TypeData> {
  public Variable<T> var();
  public String debug_name();
  public void set_changed( VariableExp e );
  public void remove_changed( VariableExp e );
}
