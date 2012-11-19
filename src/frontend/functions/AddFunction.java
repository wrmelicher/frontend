package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class AddFunction extends BinaryInt {
  public static final String NAME = "+";
  public AddFunction(){
    super( NAME, "add" );
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.add( a, b );
  }
}
