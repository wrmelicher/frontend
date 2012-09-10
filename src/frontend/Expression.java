package frontend;

import java.util.*;

public abstract class Expression extends Statement {

  private AbstractVariable out;
  protected static IfExpression cond_scope = null;
  
  private boolean has_side_effects = false;
  private ExpSignature sig = null;
  private static boolean cse_opts = true;
  
  public Expression( int line ){
    super( line );
  }

  // TODO: don't store in a linked list
  private static Map<ExpSignature,Variable.Snapshot> sigs =
    new HashMap<ExpSignature,Variable.Snapshot>();

  private Variable.Snapshot get_sig( ExpSignature sig ){
    return sigs.get(sig);
  }

  private void put_sig( ExpSignature sig, Variable.Snapshot out ){
    sigs.put(sig,out);
  }
  
  public void compile() throws CompileException {
    ExpSignature s = null;
    if( !has_side_effects() && cse_opts ){
      s = signature();
      Variable.Snapshot ret_val = get_sig(s);
      if( ret_val != null ){
	set_ret( ret_val.copy() );
	return;
      }
    }
    compile_exp();
    if( s == null ){
      s = signature();
    }
    if( !has_side_effects() && cse_opts )
      put_sig( s, out.var().snapshot() );
  }

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
  public abstract boolean has_side_effects();
  
  public AbstractVariable returnVar(){
    return out;
  }

  public void set_ret( AbstractVariable o ){
    out = o;
  }
}
