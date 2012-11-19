package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class Subtraction extends BinaryInt {
  public Subtraction(){
    super("-", new Type[] { Type.IntType, Type.IntType }, "sub" );
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.sub( a, b );
  }
}
