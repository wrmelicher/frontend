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
      System.out.println(ex.getMessage());
    }
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
