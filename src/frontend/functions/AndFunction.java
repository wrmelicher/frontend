package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class AndFunction extends BinaryInt {
  public static final String NAME = "and";
  public AndFunction(){
    super( NAME );
  }
  public String op(){
    return "and";
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.and( a, b );
  }
}