package frontend;

import java.util.Map;
import java.util.HashMap;
import java.io.PrintStream;
import java.lang.Math;
import java.util.List;
import java.util.LinkedList;

import frontend.functions.*;

public abstract class Function {
  private static Map<String, List<Function> > func_map = new HashMap<String, List<Function> >();
  private String name;
  private Type[] required_types;
  private int required_args;
  public Function( String aname, Type[] required, int argnum ){
    name = aname;
    required_types = required;
    required_args = argnum;
    List<Function> ls = func_map.get( name );
    if( ls == null ){
      ls = new LinkedList<Function>();
      func_map.put( name, ls );
    }
    ls.add( this );
  }
  public static List<Function> from_name( String s ){
    List<Function> f = func_map.get(s);
    return f;
  }
  public Type[] getTypes(){
    return required_types;
  }
  public int num_args(){
    return required_args;
  }
  public boolean satisfies( Variable[] args, Statement owner ) throws CompileException {
    if( required_args == -1 ){
      for( int i = required_types.length; i < args.length; i++ ){
	if( !args[i].getType().satisfies( required_types[ required_types.length - 1 ] ) ){
	  throw owner.error( "argument "+i+" must be of type "+required_types[ required_types.length - 1].name() );
	}
      }
    } else {
      if( args.length != required_args )
	throw owner.error( name+" function must have "+required_args+" arguments");
    }
    for( int i = 0; i < required_types.length; i++ ){
      if( !args[i].getType().satisfies( required_types[i] ) ){
	throw owner.error( "argument "+i+" must be of type "+required_types[i].name() );
      }
    }
    return true;
  }

  public static AbstractVariable call( String name, AbstractVariable[] args, Statement owner ) throws CompileException {
    List<Function> funcs = Function.from_name( name );
    if( funcs == null ){
      throw owner.error("Cannot identify function \""+name+"\"");
    }
    Function match = null;
    CompileException mess = null;
    Variable[] real_args = new Variable[ args.length ];
    for( int i = 0; i < args.length; i++){
      real_args[i] = args[i].var();
    }

    for( Function f : funcs ){
      try{
	if( f.satisfies( real_args, owner ) ){
	  match = f;
	  break;
	}
      } catch ( CompileException e ){
	mess = e;
      }
    }
    if( match == null ) {
      throw mess;
    }
    if( ProgramTree.DEBUG >= 2 )
      ProgramTree.output.println("// calling function "+match.name);
    AbstractVariable ans = match.compile(real_args,owner);
    if( ProgramTree.DEBUG >= 2 )
      ProgramTree.output.println("// end function "+match.name);
    return ans;
  }

  public AbstractVariable compile( Variable[] args, Statement owner ) throws CompileException {
    return compile_func( args, owner );
  }
  
  public abstract AbstractVariable compile_func( Variable[] args, Statement owner ) throws CompileException;

  static {
    new AddFunction();
    new IntEqualsFunction();
    new BoolEqualsFunction();
    new LessThanFunction();
    new GreaterThanFunction();
    new Subtraction();
    new Negate();
    new Length();
    new BitWidth();
    new BoolOr();
    new BoolAnd();

  }
  
}