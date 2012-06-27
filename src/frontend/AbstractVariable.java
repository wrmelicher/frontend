package frontend;

public interface AbstractVariable<T extends TypeData> {
  public Variable<T> var();
  public String debug_name();

}