package frontend;

import java.math.BigInteger;

class IntTypeData extends TypeData {
  private BigInteger magnitude;
  private boolean signed;
  private int value;
  private boolean is_const;

  public IntTypeData( int val ){
    super( Type.IntType );
    value = val;
    signed = value < 0 ? true : false;
    magnitude = new BigInteger( (value+1) + "" );
    is_const = true;
  }
  public IntTypeData( BigInteger mag, boolean sign ) {
    super( Type.IntType );
    magnitude = mag;
    signed = sign;
    is_const = false;
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
    if( a.is_constant() && b.is_constant() ){
      return new IntTypeData( a.value + b.value );
    } else {
      return new IntTypeData( a.magnitude.add( b.magnitude ), a.signed || b.signed );
    }
  }
  public static BoolData equals( IntTypeData a, IntTypeData b ){
    if( a.is_constant() && b.is_constant() ){
      return new BoolData( a.value == b.value ? BoolData.TRUE : BoolData.FALSE );
    } else {
      return new BoolData( BoolData.MAYBE );
    }
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
 
  public boolean is_constant() {
    return is_const;
  }
  public String constant_name(){
    return value+":"+bit_count();
  }
  public int value(){
    // only valid if is_constant() is true
    return value;
  }
}