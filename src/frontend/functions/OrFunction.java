package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class OrFunction extends BinaryInt {
  public static final String NAME = "or";
  public OrFunction(){
    super( NAME );
  }
  public String op(){
    return "or";
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.or( a, b );
  }
}
