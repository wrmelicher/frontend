package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class IntEqualsFunction extends BinaryInt {
  public static final String NAME = "equals";
  public IntEqualsFunction(){
    super(NAME);
  }
  public String op(){
    return "equ";
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.equals( a, b );
  }
}
