package frontend;

public class BoolData extends TypeData {
  private int possible_values;
  public static final int FALSE = 0;
  public static final int TRUE = 1;
  public static final int MAYBE = 2;
  public BoolData(){
    this( BoolData.MAYBE );
  }
  public BoolData( int possible ){
    super( Type.BoolType );
    possible_values = possible;
  }
  public int bit_count(){
    return 1;
  }
  public boolean supports_extend(){
    return false;
  }
  public String extend_operation() {
    return "";
  }
  public int possValue(){
    return possible_values;
  }
  public static BoolData and( BoolData a, BoolData b ){
    if( a.possible_values == BoolData.FALSE || b.possible_values == BoolData.FALSE ){
      return new BoolData( BoolData.FALSE );
    } else {
      return new BoolData( BoolData.MAYBE );
    }
    
  }
}