package frontend.functions;
import frontend.*;
public class BoolOr extends BinaryBool {
  public static final String NAME = "or";
  public BoolOr(){
    super(NAME,"or");
  }
  protected BoolData data_out(BoolData a, BoolData b) {
    return BoolData.or( a, b );
  }
}
