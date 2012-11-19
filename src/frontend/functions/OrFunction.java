package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class OrFunction extends BinaryInt {
  public static final String NAME = "|";
  public OrFunction(){
    super( NAME, "or" );
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.or( a, b );
  }
}
