package frontend.functions;

import frontend.*;

public class Length extends Function {
  public Length(){
    super("length", new Type[] {Type.ArrayType }, 1 );
  }
  public Variable compile_func( Variable[] args, Statement owner ) throws CompileException {
    ArrayVariable arg = ArrayVariable.get_from_abstract_var( args[0] );
    return new Variable<IntTypeData>( new IntTypeData( arg.getData().getSize() ) );
  }
}

