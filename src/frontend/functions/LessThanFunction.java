package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class LessThanFunction extends BinaryInt {
  public static final String NAME = "<";
  public LessThanFunction(){
    super(NAME);
  }
  public String op( IntTypeData a, IntTypeData b ){
    if( a.signed() || b.signed() ){
      return "lts";
    } else {
      return "ltu";
    }
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.lessthan( a, b );
  }
}
