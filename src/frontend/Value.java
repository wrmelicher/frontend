package frontend;

public abstract class Value {
  private TypeData type;
  public Value( TypeData atype ){
    type = atype;
  }
  public abstract String cur_name();
  public abstract String debug_name();
  public Type getType() {
    return getData().getType();
  }
  public TypeData getData() {
    return type;
  }
  public void setData( TypeData d ){
    type = d;
  }
}