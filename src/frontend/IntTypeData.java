package frontend;

import java.math.BigInteger;
import java.util.Scanner;
import java.lang.Math;

public class IntTypeData extends TypeData {
  private BigInteger magnitude;
  private boolean signed;
  private boolean is_const;

  private static final BigInteger TWO = BigInteger.ONE.add( BigInteger.ONE );

  public IntTypeData( int val ){
    this( new BigInteger( val+"" ) );
  }
  public IntTypeData( BigInteger val ){
    super( Type.IntType );
    is_const = true;
    signed = val.compareTo( BigInteger.ZERO ) < 0 ? true : false;
    magnitude = val;
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
    if( magnitude.equals(BigInteger.ZERO) ){
      return 1;
    }
    return signed ? magnitude.bitLength()+1 :  magnitude.bitLength();
  }
  public boolean signed(){
    return signed;
  }
  public static IntTypeData add( IntTypeData a, IntTypeData b ){
    if( a.is_constant() && b.is_constant() ){
      return new IntTypeData( a.magnitude.add(b.magnitude) );
    } else {
      return new IntTypeData( a.magnitude.add( b.magnitude ), a.signed || b.signed );
    }
  }
  public static IntTypeData negate( IntTypeData a ) {
    if( a.is_constant() ){
      return new IntTypeData( a.magnitude.negate() );
    }
    return new IntTypeData( a.magnitude, true );
  }
  public static IntTypeData subtraction( IntTypeData a, IntTypeData b ){
    if( a.is_constant() && b.is_constant() ){
      return new IntTypeData( a.magnitude.subtract( b.magnitude ) );
    }
    return new IntTypeData( a.magnitude.add( b.magnitude ), true );
  }
  public static BoolData equals( IntTypeData a, IntTypeData b ){
    if( a.is_constant() && b.is_constant() ){
      return new BoolData( a.magnitude.compareTo( b.magnitude ) == 0 ? BoolData.TRUE : BoolData.FALSE );
    } else {
      return new BoolData( BoolData.MAYBE );
    }
  }
  public static BoolData lessthan( IntTypeData a, IntTypeData b ) {
    if( a.is_constant() && b.is_constant() ){
      return new BoolData( a.magnitude.compareTo( b.magnitude ) < 0 ? BoolData.TRUE : BoolData.FALSE );
    } else {
      return new BoolData( BoolData.MAYBE );
    }
  }
  public static IntTypeData and( IntTypeData a, IntTypeData b ){
    if( a.is_constant() && b. is_constant() ){
      return new IntTypeData( a.magnitude.and( b.magnitude ) );
    } else {
      int len = Math.min( a.bit_count(), b.bit_count() );
      return new IntTypeData( BigInteger.ONE.shiftLeft( len ).subtract( BigInteger.ONE ), a.signed || b.signed );
    }
  }
  public static IntTypeData shift_left( IntTypeData a, int by ){
    if(a.is_constant()){
      return new IntTypeData(a.magnitude.shiftLeft(by));
    } else {
      return new IntTypeData(a.magnitude.shiftLeft(by), a.signed );
    }
  }
  public static IntTypeData shift_right( IntTypeData a, int by ){
    if(a.is_constant()){
      return new IntTypeData(a.magnitude.shiftRight(by));
    } else {
      return new IntTypeData(a.magnitude.shiftRight(by), a.signed );
    }
  }
  public static IntTypeData or( IntTypeData a, IntTypeData b ){
    if( a.is_constant() && b. is_constant() ){
      return new IntTypeData( a.magnitude.or( b.magnitude ) );
    } else {
      int len = Math.max( a.bit_count(), b.bit_count() );
      return new IntTypeData( BigInteger.ONE.shiftLeft( len ).subtract( BigInteger.ONE ), a.signed || b.signed );
    }
  }
  public static IntTypeData xor( IntTypeData a, IntTypeData b ){
    if( a.is_constant() && b. is_constant() ){
      return new IntTypeData( a.magnitude.xor( b.magnitude ) );
    } else {
      int len = Math.max( a.bit_count(), b.bit_count() );
      return new IntTypeData( BigInteger.ONE.shiftLeft( len ).subtract( BigInteger.ONE ), a.signed||b.signed );
    }
  }

  public static IntTypeData select( IntTypeData a, int from, int to ){
    if( a.bit_count() <= from ){
      return new IntTypeData( BigInteger.ZERO );
    }
    if( a.is_constant() ){
      return new IntTypeData
	( a.magnitude.and
	  ( BigInteger.ONE.shiftLeft
	    (to-from).subtract(BigInteger.ONE).shiftLeft(from) ) );
    } else {
      BigInteger range = BigInteger.ONE.shiftLeft( to - from )
	.subtract( BigInteger.ONE );
      
      return new IntTypeData( range, a.signed );
    }
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
    return magnitude+":"+bit_count();
  }
  public int value(){
    return magnitude.intValue();
  }
  public TypeData conditional( TypeData other ){
    // TODO: allow conditionals of varying types
    assert (other instanceof IntTypeData) : "conditional return types do not match";
    IntTypeData intother = (IntTypeData) other;
    return new IntTypeData( magnitude.max( intother.magnitude ), signed || intother.signed );     
  }
  
  public BigInteger user_input( String debug_name, int party, Scanner in ) {
    System.out.print( getType().name() + " " + debug_name + " of party " + party + " value (between "+ ( signed() ? magnitude.negate() : BigInteger.ZERO ) + " and " + magnitude + "): ");
    System.out.println();
    return in.nextBigInteger();
   }
}
