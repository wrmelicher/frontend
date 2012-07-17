package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class ShiftLeft extends BinaryInt {
  public static final String NAME = "<<";
  public ShiftLeft(){
    super( NAME );
  }
  public String op(){
    return "shiftl";
  }
  public boolean pad_to(){
    return false;
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ) throws CompileException {
    if( !b.is_constant() ){
      throw own.error(NAME+" operation requires shift amount be known at run time");
    }
    int by = b.value();
    return IntTypeData.shift_left( a, by );
  }
}
