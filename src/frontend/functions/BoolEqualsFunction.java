package frontend.functions;
import frontend.*;
public class BoolEqualsFunction extends BinaryBool {
  public static final String NAME = "equals";
  public BoolEqualsFunction(){
    super(NAME);
  }
  protected String op(){
    return "equ";
  }
  protected BoolData data_out( BoolData a, BoolData b ){
    return BoolData.equals( a, b );
  }
}
