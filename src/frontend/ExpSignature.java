package frontend;

import java.util.*;

public class ExpSignature implements Signatured {
  
  private List<Signatured> signs
    = new ArrayList<Signatured>();

  public enum ExpressionType {
    ARRAYACCESS,
    FUNCTION,
    VARIABLE,
    LITERALARRAY,
    ASSIGNMENT
  }

  private ExpressionType exp_type;
  
  public ExpSignature(ExpressionType type){
    exp_type = type;
  }
  
  public void depends( Signatured s ){
    signs.add( s );
  }

  public void depends( Expression e ){
    try{
      signs.add( e.sig() );
    } catch (CompileException ex ){
      ProgramTree.error.println(ex.getMessage());
    }
  }

  public int hashCode(){
    int accum = exp_type * 19;
    for( Signatured s : signs ){
      accum += s.hashCode();
    }
    return accum;
  }

  public boolean equals( Object o ){
    if( !( o instanceof Signatured ) ){
      return false;
    }
    return matches( (Signatured) o );
  }
  
  public boolean matches( Signatured sig ) throws CompileException{
    if( !(sig instanceof ExpSignature) ){
      return false;
    }
    ExpSignature other = (ExpSignature) sig;
    if( exp_type != other.exp_type ){
      return false;
    }
    if( signs.size() != other.signs.size() ){
      return false;
    }

    for( int i = 0; i < signs.size(); i++ ){
      if( !signs.get(i).matches( other.signs.get(i) ) ){
	return false;
      }
    }
    return true;
  }
}
