package frontend;

import java.math.BigInteger;
import java.util.Scanner;
import java.lang.Math;

public class IntTypeData extends TypeData {
  private BigInteger magnitude;
  private boolean is_const;
  private boolean is_input;
  private boolean is_signed;

  private static final BigInteger TWO = BigInteger.ONE.add( BigInteger.ONE );

  public IntTypeData( int val ){
    this( new BigInteger( val+"" ) );
  }
  public IntTypeData( BigInteger val ){
    super( Type.IntType );
    is_const = true;
    magnitude = val.abs();
  }
  public IntTypeData(){
    is_const = false;
    magnitude = BigInteger.ZERO;
    is_input = false;
    is_signed = false;
  }
  public IntTypeData( boolean input, BigInteger mag, boolean signed ) {
    super( Type.IntType );
    magnitude = mag;
    is_const = false;
    is_input = input;
    is_signed = signed;
  }

  public int input_bit_width() {
    return magnitude.bitLength() + ( is_signed ? 1 : 0 );
  }

  public static IntTypeData add( IntTypeData a, IntTypeData b ){
    if( a.is_constant() && b.is_constant() ){
      return new IntTypeData( a.magnitude.add(b.magnitude) );
    } else {
      return new IntTypeData();
    }
  }
  public static IntTypeData negate( IntTypeData a ) {
    if( a.is_constant() ){
      return new IntTypeData( a.magnitude.negate() );
    }
    return new IntTypeData();
  }
  public static IntTypeData subtraction( IntTypeData a, IntTypeData b ){
    if( a.is_constant() && b.is_constant() ){
      return new IntTypeData( a.magnitude.subtract( b.magnitude ) );
    }
    return new IntTypeData();
  }
  public static BoolData equals_op( IntTypeData a, IntTypeData b ){
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
  public static BoolData lessthanEqual( IntTypeData a, IntTypeData b ) {
    if( a.is_constant() && b.is_constant() ){
      return new BoolData( a.magnitude.compareTo( b.magnitude ) <= 0 ? BoolData.TRUE : BoolData.FALSE );
    } else {
      return new BoolData( BoolData.MAYBE );
    }
  }
  public static IntTypeData and( IntTypeData a, IntTypeData b ){
    if( a.is_constant() && b. is_constant() ){
      return new IntTypeData( a.magnitude.and( b.magnitude ) );
    } else {
      return new IntTypeData();
    }
  }
  public static IntTypeData shift_left( IntTypeData a, int by ){
    if(a.is_constant()){
      return new IntTypeData(a.magnitude.shiftLeft(by));
    } else {
      return new IntTypeData();
    }
  }
  public static IntTypeData shift_right( IntTypeData a, int by ){
    if(a.is_constant()){
      return new IntTypeData(a.magnitude.shiftRight(by));
    } else {
      return new IntTypeData();
    }
  }
  public static IntTypeData or( IntTypeData a, IntTypeData b ){
    if( a.is_constant() && b. is_constant() ){
      return new IntTypeData( a.magnitude.or( b.magnitude ) );
    } else {
      return new IntTypeData();
    }
  }
  public static IntTypeData xor( IntTypeData a, IntTypeData b ){
    if( a.is_constant() && b. is_constant() ){
      return new IntTypeData( a.magnitude.xor( b.magnitude ) );
    } else {
      return new IntTypeData();
    }
  }

  private static BigInteger ones( int i ){
    return BigInteger.ONE.shiftLeft( i ).subtract( BigInteger.ONE );
  }

  public static IntTypeData not( IntTypeData a ) {
    if( a.is_constant() ){
      return new IntTypeData( a.magnitude.xor( ones(a.bit_count()) ) );
    } else {
      return new IntTypeData();
    }
  }

  public static IntTypeData concat( IntTypeData a, IntTypeData b ){
    if( a.is_constant() && b.is_constant() ){
      IntTypeData ans = new IntTypeData( a.magnitude.shiftLeft( b.bit_count() ).add(b.magnitude) );
      return ans;
    } else {
      return new IntTypeData();
    }
  }

  public static IntTypeData select( IntTypeData a, int from, int to ){
    if( a.is_constant() ){
      return new IntTypeData( a.magnitude.and(ones(to-from).shiftLeft(from)).shiftRight(from) );
    } else {
      return new IntTypeData();
    }
  }
  
  public static IntTypeData decode( IntTypeData a ) {
    int i = a.magnitude.intValue();
    if( a.is_constant() ){
      BigInteger out = BigInteger.ZERO;
      if( i < 0 ){
	// throw error somewhere
	return null;
      }
      out = out.setBit( i );
      return new IntTypeData( out );
    } else {
      return new IntTypeData();
    }
  }

  public boolean is_constant() {
    return is_const;
  }

  public String constant_name(){
    return magnitude;
  }

  public int value(){
    return magnitude.intValue();
  }

  public TypeData conditional( TypeData other ){
    // TODO: allow conditionals of varying types
    assert (other instanceof IntTypeData) : "conditional return types do not match";
    IntTypeData intother = (IntTypeData) other;
    return new IntTypeData();     
  }
  
  public BigInteger user_input( String debug_name, int party, Scanner in ) {
    System.out.print( getType().name() + " " + debug_name + " of party " + party + " value (between "+ ( signed() ? magnitude.negate() : BigInteger.ZERO ) + " and " + magnitude + "): ");
    System.out.println();
    return in.nextBigInteger();
  }

  public String toString(){
    if( is_constant() ){
      return magnitude.toString();
    } else {
      return "Int";
    }
  }
}
