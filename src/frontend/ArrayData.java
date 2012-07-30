package frontend;
import java.util.Scanner;
import java.math.BigInteger;
import java.util.*;
public class ArrayData extends TypeData {
  private List<ArrayPositionCompileTime> indices;
  
  public ArrayData( List<ArrayPositionCompileTime> l ){
    super( Type.ArrayType );
    indices = l;
  }

  public static ArrayData all_same( TypeData d, int num ){
    ArrayList<ArrayPositionCompileTime> new_data =
      new ArrayList<ArrayPositionCompileTime>();
    for( int i = 0; i < num; i++ ){
      new_data.add( new ArrayPositionCompileTime(d, i) );
    }
    return new ArrayData( new_data );
  }

  public ArrayData copy( Statement owner ) throws CompileException {
    ArrayList<ArrayPositionCompileTime> new_data =
      new ArrayList<ArrayPositionCompileTime>();
    for( int i = 0; i < indices.size(); i++ ){
      ArrayPositionCompileTime add =
	new ArrayPositionCompileTime( indices.get(i).getData(), i );
      add.compile_assignment( indices.get(i), owner );
      new_data.add(add);
    }
    return new ArrayData( new_data );
  }

  public List<ArrayPositionCompileTime> elems(){
    return indices;
  }

  public int getSize(){
    return indices.size();
  }

  public int bit_count(){
    return getSize() * any_elem().bit_count();
  }
  
  public boolean is_constant() {
    return false;
  }
  
  public String constant_name() {
    return "";
  }

  public ArrayPositionCompileTime at( int i ) {
    return indices.get(i);
  }

  public TypeData any_elem(){
    TypeData d = indices.get(0).getData();
    for( ArrayPositionCompileTime a : indices ){
      d = a.getData().conditional(d);
    }
    return d;
  }

  public TypeData conditional( TypeData other ){
    // what to do...
    assert false : "operation not allowed";
    assert (other instanceof ArrayData) : "conditional return types do not match";
    ArrayData intother = (ArrayData) other;
    return null;
  }
  
  public BigInteger user_input( String debug_name, int party, Scanner in ) {
    BigInteger ret = BigInteger.ZERO;
    TypeData elem_type = any_elem();
    for( int i = 0; i < getSize(); i++){
      BigInteger at = elem_type.user_input( debug_name+"["+i+"]", party, in );
      ret = ret.add( at.shiftLeft( i * elem_type.bit_count() ) );
    }
    return ret;
  }
  
  public String toString(){
    return "array["+getSize()+"] " + any_elem().toString();
  }
}
