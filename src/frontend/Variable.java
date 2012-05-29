package frontend;

import java.util.Set;
import java.util.HashSet;
import java.io.PrintStream;

public class Variable {
  private long id;
  private String high_level_name;
  private long cur_assignment;
  private boolean is_input;
  private static long id_counter = 0;

  public Variable( TypeData atype ){
    this( unused_name(), atype );
  }
  
  public Variable( String name, TypeData atype ) {
    super( atype );
    id = ++id_counter;
    high_level_name = name;
    cur_assignment = 0;
    is_input = false;
  }

  public static String temp_var_name(){
    return Variable.unused_name() + (++id_counter);
  }
  public static String unused_name(){
    return "temp_var_";
  }
  
  
  public long getId() {
    return id;
  }
  public long getCurAssign() {
    return cur_assignment;
  }
  
  public boolean equals( Object obj ){
    if( obj instanceof Variable ){
      Variable v = (Variable) obj;
      return v.getId() == getId();
    }
    return false;
  }
  public int hashCode(){
    return (int) id;
  }
  public String debug_name(){
    return high_level_name;
  }
  public String cur_name(){
    String cur = high_level_name + "_" + id;
    if( is_input && cur_assignment == 0 ){
      return debug_name();
    }
    if( cur_assignment == 0 )
      return cur;
    else 
      return cur + "_" + cur_assignment;
  }
  public String new_name(){
    inc_name();
    return cur_name();
  }

  public void inc_name(){
    cur_assignment++;
  }
  public void mark_as_input(){
    is_input = true;
  }

  public void compile_assignment( PrintStream os, Variable other, AssignmentExp owner ) throws CompileException {
    os.println( new_name() + " set " + other.cur_name() );
    setData( other.getData() );
  }
}
