package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class Subtraction extends Function {
  public Subtraction(){
    super("-", new Type[] { Type.IntType, Type.IntType } );
  }
  public Variable compile_func( Variable[] args, Statement owner ) throws CompileException {
    IntTypeData data = IntTypeData.subtraction
      ( (IntTypeData) args[0].getData(), (IntTypeData) args[1].getData() );
    
    PrintStream ps = ProgramTree.output;
    Variable<IntTypeData> out = new Variable<IntTypeData>( data );
    if( !out.getData().is_constant() ){
      String[] actual_args = Variable.padArgsToLength( args, data.bit_count() );
      ps.print( out.new_name() + " sub" );
      for( int i = 0; i < args.length; i++){
	ps.print( " "+actual_args[i] );
      }
      ps.println();
    }
    return out;
  }
}
