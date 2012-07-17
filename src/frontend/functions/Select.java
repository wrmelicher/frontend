package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class Select extends Function {
  public static final String NAME = "select";
  public Select(){
    super( NAME, new Type[] {Type.IntType, Type.IntType, Type.IntType} );
  }

  public Variable compile_func_checked
    ( Variable<IntTypeData>[] args,
      Statement owner ) throws CompileException {
    int from = Variable.get_val_from_var(args[1],owner);
    int to = Variable.get_val_from_var(args[2],owner);

    Variable out = new Variable
      ( IntTypeData.select(args[0].getData(), from, to) );
    if( !out.getData().is_constant() ){
      ProgramTree.output.println(out.new_name()
				 + " select " +
				 args[0].cur_name() +
				 " " + from + " " + to );
    }
    return out;
  }
  
  public Variable compile_func
    ( Variable[] args,
      Statement owner ) throws CompileException{
    return compile_func_checked((Variable<IntTypeData>[])args, owner);
  }
}
