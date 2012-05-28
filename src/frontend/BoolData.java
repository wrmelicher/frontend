package frontend;

public class BoolData extends TypeData {
  public static final BoolData INSTANCE = new BoolData();
  public BoolData(){
    super( Type.BoolType );
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
}