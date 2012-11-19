package frontend;

import java.util.Map;
import java.util.HashMap;
import java.io.PrintStream;
import java.lang.Math;
import java.util.List;
import java.util.LinkedList;

import frontend.functions.*;

public abstract class Function
  implements Signatured {
  
  private static Map<String, List<Function> > func_map
    = new HashMap<String, List<Function> >();
  private String name;
  private Type[] required_types;

  public Function( String aname, Type[] required ){
    name = aname;
    required_types = required;

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
    return required_types.length;
  }
  public boolean satisfies( int num_args, Statement owner )
    throws CompileException {
    if( num_args != required_types.length )
      throw owner.error
	( name+" function must have "+num_args()+" arguments");
    return true;
  }

  public static Function match
    ( String name,
      int num_args,
      Statement owner ) throws CompileException {
    List<Function> funcs = Function.from_name( name );
    if( funcs == null ){
      throw owner.error("Cannot identify function \""+name+"\"");
    }
    Function match = null;
    CompileException mess = null;

    for( Function f : funcs ){
      try{ 
	if( f.satisfies( num_args, owner ) ){
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
    return match;
  }

  public static AbstractVariable call
    ( String fname,
      AbstractVariable[] args,
      Statement owner ) throws CompileException {
    Function f = Function.match( fname, args.length, owner );
    return Function.call( f, args, owner );
  }

  public static AbstractVariable call
    ( Function f,
      AbstractVariable[] args,
      Statement owner ) throws CompileException {
    Variable[] real_args = new Variable[ args.length ];
    
    for( int i = 0; i < args.length; i++){
      real_args[i] = args[i].var();
    }
    
    if( ProgramTree.DEBUG >= 2 )
      ProgramTree.output.println("// calling function "+f.name);

    for( int i = 0; i < f.required_types.length; i++ ){
      if( !real_args[i].getType().satisfies( f.required_types[i] ) ){
	throw owner.error( "argument "+i+" must be of type "
			   +f.required_types[i].name() );
      }
    }
    
    AbstractVariable ans = f.compile(real_args,owner);
    if( ProgramTree.DEBUG >= 2 )
      ProgramTree.output.println("// end function "+f.name);
    return ans;
  }

  public AbstractVariable compile
    ( Variable[] args,
      Statement owner ) throws CompileException {
    return compile_func( args, owner );
  }

  public String name(){
    return name;
  }
  
  public abstract AbstractVariable compile_func
    ( Variable[] args,
      Statement owner ) throws CompileException;
  public boolean has_side_effects() {
    return false;
  }
  public boolean matches( Signatured other ){
    if( !( other instanceof Function ) ){
      return false;
    }
    Function fother = (Function) other;

    return this == other;
    
  }

  public static void InitFunctions() {
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
    new OrFunction();
    new AndFunction();
    new XorFunction();
    new IncFunction();
    new ShiftLeft();
    new ShiftRight();
    new Truncate();
    new Select();
    new NotFunction();
    new Zeros();
    new BoolNot();
    // new Combine();
    new GreaterThanEquals();
    new LessThanEquals();
    new Decoder();
  }
}
