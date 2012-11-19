package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class ShiftRight extends BinaryInt {
  public static final String NAME = ">>";
  public ShiftRight(){
    super( NAME, "shiftr" );
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ) throws CompileException {
    if( !b.is_constant() ){
      throw own.error(NAME+" operation requires shift amount be known at run time");
    }
    int by = b.value();
    return IntTypeData.shift_right( a, by );
  }
}
