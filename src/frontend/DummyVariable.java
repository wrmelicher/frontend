package frontend;

import java.util.LinkedList;

public class DummyVariable<T extends TypeData>
  implements AbstractVariable<T> {
  private String debug;

  private boolean assigned = false;

  private LinkedList<AbstractVariable<T> > values = new LinkedList<AbstractVariable<T> >();

  public DummyVariable( String name ){
    debug = name;
  }

  public Variable<T> var(){
    assert values.size() >= 1 : "Variable "+debug_name()+" is not bound";
    return values.peek().var();
  }

  public String debug_name(){
    return debug;
  }

  public AbstractVariable pop_var(){
    return values.pop();
  }

  public void start_func(){
    assigned = false;
  }

  public void exit_func(){
    if( assigned )
      pop_var();
  }
  
  public void push_var( AbstractVariable<T> a ){
    assigned = true;
    values.push( a.var() );
    a.var().set_debug_name( debug );
  }
}
