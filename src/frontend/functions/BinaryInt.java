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
  
  public abstract String op( IntTypeData a, IntTypeData b );
  
  public boolean pad_to(){
    return true;
  }

  public int size_to_padd( IntTypeData a, IntTypeData b ){
    return Math.max( a.bit_count(), b.bit_count() );
  }

  public String[] actual_args( Variable<IntTypeData>[] args, int size ){
    String[] ans;
    if( pad_to() ){
      ans = Variable.padArgsToLength( args, size );
    } else {
      ans = new String[args.length];
      for( int i = 0; i < args.length; i++ ){
	ans[i]=args[i].cur_name();
      }
    }
    return ans;
  }
  
  public Variable compile_checked
    ( Variable<IntTypeData>[] args,
      Statement owner ) throws CompileException {
    own = owner;
    TypeData data = data_type( args[0].getData(),
			       args[1].getData() );
    int size = size_to_padd( args[0].getData(), args[1].getData() );
    PrintStream ps = ProgramTree.output;
    Variable out = new Variable( data );
    if( !out.getData().is_constant() ){
      String[] actual_args = actual_args( args, size );
      ps.print( out.cur_name() + " " + op( args[0].getData(), args[1].getData() ) );
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
