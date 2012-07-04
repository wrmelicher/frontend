package frontend;

import java.util.LinkedList;

public class DummyVariable<T extends TypeData>
  implements AbstractVariable<T> {
  private String debug;

  private boolean assigned = false;

  private LinkedList<Variable<T> > values
    = new LinkedList<Variable<T> >();

  private LinkedList<VariableExp> parents 
    = new LinkedList<VariableExp>();

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
    assert values.size() >= 1 : "Variable "+debug_name()+" is not bound";
    Variable v = values.pop();
    for( VariableExp e : parents ){
      v.remove_changed( e );
    }
    return v;
  }

  public void start_func(){
    assigned = false;
  }
  
  public void set_changed( VariableExp e ){
    parents.add( e );
  }
  
  public void exit_func(){
    if( assigned )
      pop_var();
  }

  public void remove_changed( VariableExp e ){
    parents.remove( e );
  }
  
  public void push_var( AbstractVariable<T> a ){
    assigned = true;
    values.push( a.var() );
    a.var().set_debug_name( debug );
    for( VariableExp e : parents ){
      e.set_changed();
      a.var().set_changed( e );
    }
  }
}
