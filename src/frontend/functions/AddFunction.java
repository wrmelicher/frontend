package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class AddFunction extends Function {
  public static final String NAME = "+";
  public AddFunction(){
    super( NAME, new Type[] { Type.IntType, Type.IntType}, 2 );
  }
  public Variable<IntTypeData> compile_checked( Variable<IntTypeData>[] args, Statement owner ) throws CompileException {
    IntTypeData data = new IntTypeData( 0 );
    for( int i = 0; i < args.length; i++){
      data = IntTypeData.add( data, args[i].getData() );
    }
    PrintStream ps = ProgramTree.output;
    Variable<IntTypeData> out = new Variable<IntTypeData>( data );
    if( !out.getData().is_constant() ){
      String[] actual_args = Variable.padArgsToLength( args, data.bit_count() );
      ps.print( out.cur_name() + " add" );
      for( int i = 0; i < args.length; i++){
	ps.print( " "+actual_args[i] );
      }
      ps.println();
    }
    return out;
  }
  public Variable compile_func( Variable[] args, Statement owner ) throws CompileException{
    return compile_checked( (Variable<IntTypeData>[]) args, owner );
  }
}