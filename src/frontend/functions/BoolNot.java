package frontend.functions;

import java.math.BigInteger;
import frontend.*;

public class BoolNot extends Function {
  public static final String NAME = "!";
  public BoolNot(){
    super(NAME, new Type[] { Type.BoolType } );
  }
  public Variable compile_func( Variable[] args, Statement owner ) throws CompileException {
    // safe cast because types are already checked
    BoolData a = BoolData.not( (BoolData) args[0].getData() );
    Variable out = new Variable( a );
    if( ! a.is_constant() ){
      ProgramTree.output.println( out.new_name() + " not " + args[0].cur_name() );
    }
    return out;
  }
}
