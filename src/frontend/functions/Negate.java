package frontend.functions;

import frontend.*;

public class Negate extends Function {
  public static final String NAME = "-";
  public Negate(){
    super("-", new Type[] { Type.IntType }, 1 );
  }
  public Variable compile_func( Variable[] args, Statement owner ) throws CompileException {
    // safe cast because types are already checked
    IntTypeData a = IntTypeData.negate( (IntTypeData) args[0].getData() );
    Variable out = new Variable( a );
    if( ! a.is_constant() ){
      ProgramTree.output.println( out.new_name() + " negate " + args[0].cur_name() );
    }
    return out;
  }
}