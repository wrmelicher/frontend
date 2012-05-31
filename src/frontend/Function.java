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
  
  public abstract Variable compile( PrintStream ps, Variable[] args, Expression owner ) throws CompileException;

  static {
    new IntAddFunction();
    new IntEqualsFunction();
  }

  protected static String padArgToLength( PrintStream ps, Variable arg, int to ){
    if( arg.getData().bit_count() == to ){
      return arg.cur_name();
    }
    String name = Variable.temp_var_name();
    ps.println( name + " " + arg.getData().extend_operation() + " " + arg.cur_name() + " "+ to );
    return name;
  }

  protected static int maxArgLength( Variable[] args ){
    int max = 0;
    for( int i = 0; i < args.length; i++ ){
      max = Math.max( args[i].getData().bit_count(), max );
    }
    return max;
  }
  
  protected static String[] padArgsToMaxLength( PrintStream ps, Variable[] args ){
    int max = Function.maxArgLength( args );
    String[] ans = new String[ args.length ];
    for( int i = 0; i < args.length; i++){
      ans[i] = Function.padArgToLength( ps, args[i], max );
    }
    return ans;
  }
  
  static class IntAddFunction extends Function {
    public IntAddFunction(){
      super( "+" );
    }
    public Variable compile( PrintStream ps, Variable[] args, Expression owner ) throws CompileException{
      boolean signed = false;
      if( args.length != 2 ){
	throw owner.error( "Add function must have 2 arguments" );
      }
      for( int i = 0; i < args.length; i++){
	if( !( args[i].getType() == Type.IntType ) ){
	  throw owner.error( "Add expression must have integer arguments" );
	}
	if( ((IntTypeData) args[i].getData()).signed() ){
	  signed = true;
	}
      }

      int length = Function.maxArgLength( args );
      String[] actual_args = Function.padArgsToMaxLength( ps, args );
      IntTypeData data = new IntTypeData( 0, false );
      for( int i = 0; i < args.length; i++){
	data = IntTypeData.add( data, (IntTypeData)args[i].getData() );
      }
      Variable out = new Variable( data );
      if( !out.getData().is_constant() ){
	ps.print( out.cur_name() + " add" );
	for( int i = 0; i < args.length; i++){
	  ps.print( " "+actual_args[i] );
	}
	ps.println();
      }
      return out;
    }
  }

  static class IntEqualsFunction extends Function {
    public static final String NAME = "==";
    public IntEqualsFunction(){
      super(NAME);
    }
    public Variable compile( PrintStream ps, Variable[] args, Expression owner ) throws CompileException{
      if( args.length != 2 ){
	throw owner.error( "\""+NAME+"\" operator must have two arguments" );
      }
      boolean signed = false;
      for( int i = 0; i < args.length; i++){
	if( !(args[i].getType() != Type.IntType ) ){
	  throw owner.error( "\""+NAME+"\" operator must be applied to ints" );
	}
	if( ((IntTypeData) args[i].getData()).signed() ){
	  signed = true;
	}
      }
      int length = Function.maxArgLength( args );
      String[] actual_args = padArgsToMaxLength( ps, args );

      Variable out = new Variable( IntTypeData.equals( (IntTypeData)args[0].getData(), (IntTypeData)args[1].getData() ) );
      String op = "equ";
      if( !out.getData().is_constant() ) {
	ps.print( out.new_name() + " " + op );
	for( int i = 0; i < args.length; i++){
	  ps.print( " " + actual_args[i] );
	}
	ps.println();
      }
      return out;
    }
  }
  
}