package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class OrFunction extends BinaryInt {
  public static final String NAME = "|";
  public OrFunction(){
    super( NAME );
  }
  public String op( IntTypeData a, IntTypeData b ){
    return "or";
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.or( a, b );
  }
}
