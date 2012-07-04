package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public abstract class BinaryInt extends Function {
  public BinaryInt(String name){
    super( name, new Type[] { Type.IntType, Type.IntType} );
  }

  public abstract TypeData data_type
    ( IntTypeData a, IntTypeData b );
  
  public abstract String op();
  
  public Variable compile_checked
    ( Variable<IntTypeData>[] args,
      Statement owner ) throws CompileException {
    
    TypeData data = data_type( args[0].getData(),
			       args[1].getData() );
    PrintStream ps = ProgramTree.output;
    Variable out = new Variable( data );
    if( !out.getData().is_constant() ){
      String[] actual_args
	= Variable.padArgsToLength( args, data.bit_count() );
      ps.print( out.cur_name() + " " + op() );
      for( int i = 0; i < args.length; i++){
	ps.print( " "+actual_args[i] );
      }
      ps.println();
    }
    return out;
  }
  public Variable compile_func
    ( Variable[] args,
      Statement owner ) throws CompileException{
    return compile_checked
      ( (Variable<IntTypeData>[]) args, owner );
  }
}
