package frontend.functions;

import frontend.*;

public class GreaterThanFunction extends BinaryInt {
  public static final String NAME = ">";
  public GreaterThanFunction(){
    super(NAME);
  }
  public String op( IntTypeData a, IntTypeData b ){
    if( a.signed() || b.signed() ){
      return "gts";
    } else {
      return "gtu";
    }
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.lessthan( b, a );
  }
  
}
