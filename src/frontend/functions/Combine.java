package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class Combine extends Function {
  public static final String NAME = "cat";
  public Combine(){
    super( NAME, new Type[] { Type.IntType, Type.IntType } );
  }
  public Variable compile_func
    ( Variable[] args,
      Statement owner ) throws CompileException {
    IntTypeData ans = IntTypeData.concat( (IntTypeData)(args[1].getData()),
					  (IntTypeData)(args[0].getData()) );
    Variable out = new Variable( ans );
    if( !ans.is_constant() ){
      ProgramTree.output.println( out.new_name() +
				  " concat "+
				  args[0].cur_name() +
				  " " + args[1].cur_name() );
    }
    return out;
  }
}
