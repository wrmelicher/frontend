package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class LessThanEquals extends BinaryInt {
  public static final String NAME = "<=";
  public LessThanEquals(){
    super( NAME );
  }
  public String op( IntTypeData a, IntTypeData b ){
    if( a.signed() || b.signed() ){
      return "ltes";
    } else {
      return "lteu";
    }
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.lessthanEqual( a, b );
  }
}
