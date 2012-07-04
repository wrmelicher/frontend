package frontend;

import java.util.*;

public abstract class Expression extends Statement implements ExpSignature.signatured {

  private AbstractVariable out;
  
  protected static IfExpression cond_scope = null;
  
  private boolean changed = true;
  private ExpSignature sig = null;
  private Expression parent = null;
  private boolean has_side_effects = false;
  
  public Expression( int line ){
    super( line );
  }
  
  private static List<SigPair> sigs =
    new LinkedList<SigPair>();

  public ExpSignature signature() throws CompileException {
    if( changed() ){
      sig = sig();
      changed = false;
    }
    return sig;
  }

  public boolean changed(){
    return changed;
  }

  public void set_changed(){
    changed = true;
    if( parent != null ){
      parent.set_changed();
    }
  }

  public void set_has_side_effect(){
    has_side_effects = true;
    if( parent != null ){
      parent.set_has_side_effect();
    }
  }

  public void setParent( Expression p ){
    parent = p;
    if( has_side_effects )
      parent.set_has_side_effect();
  }

  public void compile() throws CompileException {
    ExpSignature s = null;
    if( !has_side_effects() ){
      s = signature();
      for( SigPair pair : sigs ) {
	if( pair.key.equals( s ) ){
	  set_ret( pair.val.copy() );
	  return;
	}
      }
    }
    
    compile_exp();
    if( s == null )
      s = signature();
    sigs.add( new SigPair( s, out ) );

  }

  public abstract ExpSignature sig() throws CompileException;
  public abstract void compile_exp() throws CompileException;
  public abstract boolean has_side_effects() throws CompileException;
  
  public AbstractVariable returnVar() {
    return out;
  }

  public void set_ret( AbstractVariable o ){
    out = o;
  }

  public boolean matches( ExpSignature.signatured other )
    throws CompileException {
    
    if( !(other instanceof Expression ) ){
      return false;
    }
    Expression other_exp = (Expression) other;
    return signature().equals( other_exp.signature() );
  }

  private static class SigPair {
    ExpSignature key;
    Variable.Snapshot val;
    public SigPair( ExpSignature s, AbstractVariable v ){
      key = s;
      val = v.var().snapshot();
    }
  }
}
