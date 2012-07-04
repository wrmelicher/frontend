package frontend.functions;

import frontend.*;
import java.io.PrintStream;

public abstract class BinaryBool extends Function {
  
  public BinaryBool(String name){
    super( name, new Type[] {Type.BoolType, Type.BoolType} );
  }
  
  protected abstract String op();
  protected abstract BoolData data_out(BoolData a, BoolData b);
  
  public Variable compile_func( Variable[] args, Statement owner ) throws CompileException {
    BoolData a = (BoolData)args[0].getData();
    BoolData b = (BoolData)args[0].getData();

    PrintStream ps = ProgramTree.output;
    
    BoolData out_data = data_out( a, b );
    Variable<BoolData> ret = new Variable( out_data );
    if( !out_data.is_constant() ){
      String op = op();
      String[] actual_args = new String[ 2 ];
      actual_args[0] = args[0].cur_name();
      actual_args[1] = args[1].cur_name();
      ps.print( ret.new_name() + " " + op );
      for( int i = 0; i < args.length; i++){
	ps.print( " " + actual_args[i] );
      }
      ps.println();
    }
    return ret;
  }
}
