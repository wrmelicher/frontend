package frontend;

import java.util.*;

public abstract class Expression extends Statement {

  private AbstractVariable out;
  protected static IfExpression cond_scope = null;
  
  private boolean has_side_effects = false;
  private ExpSignature sig = null;
  
  public Expression( int line ){
    super( line );
  }

  // TODO: don't store in a linked list
  private static List<SigPair> sigs =
    new LinkedList<SigPair>();

  public void compile() throws CompileException {
    ExpSignature s = null;
    if( !has_side_effects() ){
      s = signature();
      for( SigPair pair : sigs ){
	if( pair.key.matches( s ) ){
	  set_ret( pair.val.copy() );
	  return;
	}
      }
    }
    compile_exp();
    if( s == null ){
      s = signature();
    }
    sigs.add( new SigPair( s, out ) );
  }

  // TODO: don't recompute signature everytime

  public ExpSignature signature() throws CompileException{
    if( has_changed() || sig == null ){
      sig = sig();
    }
    return sig;
  }

  protected ExpSignature sig() throws CompileException {
    ExpSignature ans = new ExpSignature(type());
    for( Statement s : children() ){
      if( s instanceof Expression ){
	ans.depends((Expression)s);
      }
    }
    return ans;
  }

  protected abstract ExpSignature.ExpressionType type();
  protected abstract void compile_exp() throws CompileException;
  public abstract boolean has_side_effects() throws CompileException;
  
  public AbstractVariable returnVar(){
    return out;
  }

  public void set_ret( AbstractVariable o ){
    out = o;
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
