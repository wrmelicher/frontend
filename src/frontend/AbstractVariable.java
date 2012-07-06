package frontend;

public interface AbstractVariable<T extends TypeData> {
  public Variable<T> var();
  public String debug_name();
  public void notify_changes( Changer c );
  public int call_depth();
}
