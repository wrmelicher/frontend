package frontend;

import java.io.PrintStream;

public abstract class ArrayPosition extends Variable {
  private ArrayVariable parent;
  private boolean is_valid;
  public ArrayPosition( ArrayVariable par ){
    super( par.debug_name()+"_access", par.getData().getElementData() );
    parent = par;
  }

  /*  public String cur_name(){
    if( !is_valid ){
      read_value();
    }
    return super.cur_name();
    }*/
  public boolean is_valid(){
    return is_valid;
  }
  
  public void invalidate(){
    is_valid = false;
    notify_all();
  }
  public void validate(){
    is_valid = true;
  }

  public void read_val(){
    if( !is_valid() ) 
      read_value();
  }
  public abstract void read_value();
  public ArrayVariable getParent() {
    return parent;
  }
  
}
