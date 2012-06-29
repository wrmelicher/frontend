package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class IntEqualsFunction extends Function {
  public static final String NAME = "==";
  public IntEqualsFunction(){
    super(NAME, new Type[] { Type.IntType, Type.IntType }, 2 );
  }
  public Variable<BoolData> compile_checked( Variable<IntTypeData>[] args, Statement owner ) throws CompileException {
    
    PrintStream ps = ProgramTree.output;
    
    Variable<BoolData> out = new Variable<BoolData>( IntTypeData.equals( args[0].getData(), args[1].getData() ) );
    String op = "equ";
    if( !out.getData().is_constant() ) {
      int len = Variable.maxArgLength( args );
      String[] actual_args = Variable.padArgsToLength( args, len );
      ps.print( out.new_name() + " " + op );
      for( int i = 0; i < args.length; i++){
	ps.print( " " + actual_args[i] );
      }
      ps.println();
    }
    return out;
  }
  public Variable compile_func( Variable[] args, Statement owner ) throws CompileException{
    return compile_checked( (Variable<IntTypeData>[]) args, owner );
  }
}