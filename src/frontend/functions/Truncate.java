package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class Truncate extends BinaryInt {
  public static final String NAME = "trunc";
  public Truncate(){
    super( NAME );
  }
  public String op(){
    return "trunc";
  }
  public boolean pad_to(){
    return false;
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ) throws CompileException{
    if( !b.is_constant() ){
      throw own.error(NAME+" operation requires shift amount be known at run time");
    }
    int by = b.value();
    return IntTypeData.select( a, 0, by );
  }
}
