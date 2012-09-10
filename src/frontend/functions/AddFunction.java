package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class AddFunction extends BinaryInt {
  public static final String NAME = "+";
  public AddFunction(){
    super( NAME );
  }
  public String op( IntTypeData a, IntTypeData b ){
    return "add";
  }
  public int size_to_padd( IntTypeData a, IntTypeData b ){
    return IntTypeData.add(a,b).bit_count();
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.add( a, b );
  }
}
