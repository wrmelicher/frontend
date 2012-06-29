package frontend.functions;
import frontend.*;
public class BoolAnd extends BinaryBool {
  public static final String NAME = "and";
  public BoolAnd(){
    super(NAME);
  }
  protected String op(){
    return "and";
  }
  protected BoolData data_out(BoolData a, BoolData b) {
    return BoolData.and( a, b );
  }
}
