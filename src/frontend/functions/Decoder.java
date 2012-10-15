package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class Decoder extends Function {
  public static final String NAME = "decoder";
  public Decoder(){
    super( NAME, new Type[] { Type.IntType } );
  }
  public AbstractVariable compile_func
    ( Variable[] args,
      Statement owner ) throws CompileException {
    Variable<IntTypeData> v = (Variable<IntTypeData>)args[0];
    
    IntTypeData out = IntTypeData.decode( v.getData() );
    Variable<IntTypeData> out_var = new Variable( out );
    if( !out.is_constant() ){
      PrintStream ps = ProgramTree.output;
      ps.println( out_var.new_name() + " decode " + args[0].cur_name() + " 1:1" );
    }
    return out_var;
  }
  
}
