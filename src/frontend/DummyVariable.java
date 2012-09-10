package frontend;

import java.util.List;
import java.util.LinkedList;

public class DummyVariable<T extends TypeData>
  implements AbstractVariable<T>, Changer {
  private String debug;

  private LinkedList<Boolean> assigned =
    new LinkedList<Boolean>();
  private String other_debug;

  private LinkedList<Variable<T> > values
    = new LinkedList<Variable<T> >();

  private List<Changer> notifiers
    = new LinkedList<Changer>();

  private int call_depth = 0;

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
    v.remove_notifier( this );
    set_changed();
    v.set_debug_name( other_debug );
    return v;
  }

  public void start_func(){
    assigned.push( false );
  }

  public void set_changed(){
    for( Changer c : notifiers ){
      c.set_changed();
    }
  }

  public void set_call_depth( int i ){
    call_depth = i;
  }

  public int call_depth(){
    return call_depth;
  }

  public void notify_changes( Changer c ){
    notifiers.add( c );
  }
  
  public void exit_func(){
    if( assigned.peek() )
      pop_var();
    assigned.pop();
  }

  public void push_var( AbstractVariable<T> a ){
    if( assigned.size() > 0 )
      assigned.set( 0, true );
    values.push( a.var() );
    a.var().notify_changes( this );
    other_debug = a.var().debug_name();
    a.var().set_debug_name( debug );
    
    set_changed();
  }
}
