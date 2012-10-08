package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class GreaterThanEquals extends BinaryInt {
  public static final String NAME = ">=";
  public GreaterThanEquals(){
    super( NAME );
  }
  public String op( IntTypeData a, IntTypeData b ){
    if( a.signed() || b.signed() ){
      return "gtes";
    } else {
      return "gteu";
    }
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.lessthanEqual( b, a );
  }
}
