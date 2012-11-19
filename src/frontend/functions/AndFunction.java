package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class AndFunction extends BinaryInt {
  public static final String NAME = "&";
  public AndFunction(){
    super( NAME, "and" );
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.and( a, b );
  }
}
