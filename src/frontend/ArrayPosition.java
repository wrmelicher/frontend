package frontend;

import java.io.PrintStream;

public abstract class ArrayPosition extends Variable {
  private ArrayVariable parent;
  public ArrayPosition( TypeData t ){
    super( t );
  }
  public abstract void read_value();
  public ArrayVariable getParent() {
    return parent;
  }
  public void setParent( ArrayVariable v ){
    parent = v;
    set_debug_name( v.debug_name()+"_index" );
  }
}
