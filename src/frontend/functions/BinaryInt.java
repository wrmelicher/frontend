package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public abstract class BinaryInt extends Function {
  protected Statement own;
  public BinaryInt(String name){
    super( name, new Type[] { Type.IntType, Type.IntType} );
  }

  public abstract TypeData data_type
    ( IntTypeData a, IntTypeData b ) throws CompileException;
  
  public abstract String op();
  public boolean pad_to(){
    return true;
  }
  
  public Variable compile_checked
    ( Variable<IntTypeData>[] args,
      Statement owner ) throws CompileException {
    own = owner;
    TypeData data = data_type( args[0].getData(),
			       args[1].getData() );
    PrintStream ps = ProgramTree.output;
    Variable out = new Variable( data );
    if( !out.getData().is_constant() ){
      String[] actual_args;
      if( pad_to() ){
	actual_args = Variable.padArgsToLength( args, data.bit_count() );
      } else {
	actual_args = new String[args.length];
	for( int i = 0; i < args.length; i++ ){
	  actual_args[i]=args[i].cur_name();
	}
      }
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
