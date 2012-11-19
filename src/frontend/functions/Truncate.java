package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class Truncate extends BinaryInt {
  public static final String NAME = "trunc";
  public Truncate(){
    super( NAME, "trunc" );
  }  
  public TypeData data_type( IntTypeData a, IntTypeData b ) throws CompileException{
    if( !b.is_constant() ){
      throw own.error(NAME+" operation requires shift amount be known at run time");
    }
    int by = b.value();
    if( by > a.bit_count() )
      by = a.bit_count();
    return IntTypeData.select( a, 0, by );
  }
}
