package frontend;

import java.util.*;

public abstract class Statement {
  private int linenum;
  
  private List<Statement> children
    = new LinkedList<Statement>();

  private Statement parent = null;
  private boolean changed = true;
  
  public Statement( int line ){
    linenum = line;
  }

  public void add_child( Statement s ){
    children.add(s);
    s.set_parent( this );
  }

  public List<Statement> children(){
    return children;
  }

  public void set_changed(){
    if( parent != null && changed == false ){
      parent.set_changed();
    }
    changed = true;
  }
  
  public void not_changed(){
    changed = false;
  }

  public boolean has_changed(){
    return changed;
  }

  private void set_parent( Statement s ){
    parent = s;
  }
  
  public abstract void compile() throws CompileException;

  public CompileException error( String mess ){
    return new CompileException( mess, linenum );
  }

  public int getLine(){
    return linenum;
  }
}
