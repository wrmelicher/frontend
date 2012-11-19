package frontend.functions;

import frontend.*;

public class GreaterThanFunction extends BinaryInt {
  public static final String NAME = ">";
  public GreaterThanFunction(){
    super(NAME,"gt");
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.lessthan( b, a );
  }
  
}
