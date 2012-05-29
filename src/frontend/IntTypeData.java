package frontend;

import java.math.BigInteger;

class IntTypeData extends TypeData {
  private BigInteger magnitude;
  private boolean signed;
  
  public IntTypeData( BigInteger mag, boolean sign ) {
    super( Type.IntType );
    magnitude = mag;
    signed = sign;
  }
  public IntTypeData( int mag, boolean sign ){
    this( new BigInteger( mag+"" ), sign );
  }
  public int bit_count(){
    return signed ? magnitude.bitLength()+2 :  magnitude.bitLength() + 1;
  }
  public boolean signed(){
    return signed;
  }
  public static IntTypeData add( IntTypeData a, IntTypeData b ){
    return new IntTypeData( a.magnitude.add( b.magnitude ), a.signed || b.signed );
  }
  public boolean supports_extend(){
    return true;
  }
  public String extend_operation(){
    if( signed() ){
      return "sextend";
    } else {
      return "zextend";
    }
  }
  
}