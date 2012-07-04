package frontend;

import java.util.*;

public class ExpSignature {
  public interface signatured {
    public boolean matches( signatured other )
      throws CompileException;
  }
  
  private List<signatured> signs
    = new ArrayList<signatured>();

  public enum ExpressionType {
    ARRAYACCESS,
    FUNCTION,
    VARIABLE,
    LITERALARRAY
  }

  private ExpressionType exp_type;
  
  public ExpSignature(ExpressionType type){
    exp_type = type;
  }
  
  public void depends( signatured s ){
    signs.add( s );
  }
  
  public boolean equals( ExpSignature other )
    throws CompileException{
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
