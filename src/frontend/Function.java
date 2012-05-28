package frontend;

import java.util.Map;
import java.util.HashMap;
import java.io.PrintStream;
import java.lang.Math;

public abstract class Function {
  private static Map<String, Function> func_map = new HashMap<String, Function>();
  private String name;
  public Function( String aname ){
    name = aname;
    func_map.put( name, this );
  }
  public static Function from_name( String s ){
    return func_map.get(s);
  }
  
  public abstract Variable compile( PrintStream ps, Variable[] args, FunctionExp owner ) throws CompileException;

  static {
    new AddFunction();
    new EqualsFunction();
  }

  
  static class AddFunction extends Function {
    public AddFunction(){
      super( "+" );
    }
    public Variable compile( PrintStream ps, Variable[] args, Expression owner ) throws CompileException{
      boolean signed = false;
      int size_of_operation = 0;
      for( int i = 0; i < args.length; i++){
	if( !( args[i].getType() == Type.IntType ) ){
	  throw owner.error( "Add expression must have integer arguments" );
	}
	size_of_operation = Math.max( args[i].getData().bit_count(), size_of_operation );
	if( ((IntTypeData) args[i].getData()).signed() ){
	  signed = true;
	}
      }
      String[] actual_args = new String[ args.length ];
      IntTypeData data = new IntTypeData( 0, false );
      for( int i = 0; i < args.length; i++){
	if( size_of_operation > args[i].getData().bit_count() ){
	  actual_args[i] = Variable.temp_var_name();
	  ps.println( actual_args[i] + args[i].getData().extend_operation() + args[i].cur_name() + " "+ size_of_operation );
	} else {
	  actual_args[i] = args[i].cur_name();
	}
	data = IntTypeData.add( data, (IntTypeData)args[i].getData() );
      }
      Variable out = new IntVariable( data );      
      ps.print( out.cur_name() + " add" );
      for( int i = 0; i < args.length; i++){
	ps.print( " "+actual_args[i] );
      }
      ps.println();
      return out;
    }
  }

  static class EqualsFunction extends Function {
    public static final String NAME = "==";
    public EqualsFunction(){
      super(NAME);
    }
    public Variable compile( PrintStream ps, Variable[] args, Expression owner ) throws CompileException{
      if( args.length != 2 ){
	throw owner.error( "\""+NAME+"\" operator must have two arguments" );
      }
      for( int i = 0; i < args.length; i++){
	if( !(args[i].getType() != Type.IntType ) ){
	  throw owner.error( "\""+NAME+"\" operator must be applied to ints" );
	}
      }
      
    }
  }
  
}