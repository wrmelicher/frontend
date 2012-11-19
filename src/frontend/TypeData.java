package frontend;
import java.util.Scanner;
import java.math.BigInteger;
import java.util.Random;

public abstract class TypeData {
  // must be immutable
  private Type type;
  public TypeData( Type t ){
    type = t;
  }

  public Type getType() {
    return type;
  }

  public abstract boolean is_constant();
  public abstract String constant_name();

  public abstract TypeData conditional( TypeData other );

  public abstract BigInteger user_input( String debug_name, int party, Scanner in );

  private static Random rnd = new Random();

  public abstract int input_bit_width();
  
  public BigInteger user_input(){
    return new BigInteger( input_bit_width() , rnd );
  }

}
