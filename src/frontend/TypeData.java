package frontend;

public abstract class TypeData {
  private Type type;
  public TypeData( Type t ){
    type = t;
  }
  public Type getType() {
    return type;
  }
  public abstract int bit_count();
  public abstract boolean supports_extend();
  public abstract String extend_operation();
  public abstract boolean is_constant();
  public abstract String constant_name();
}