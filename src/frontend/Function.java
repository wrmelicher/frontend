package frontend;

import java.util.Map;
import java.util.HashMap;
import java.io.PrintStream;
import java.lang.Math;
import java.util.List;
import java.util.LinkedList;

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
    new IncFunction();
    new LessThanFunction();
    new GreaterThanFunction();
    new Subtraction();
    new Negate();
    new Length();
    new BitWidth();
  }
  
  static class AddFunction extends Function {
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
  
  static class IntEqualsFunction extends Function {
    public static final String NAME = "equals";
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

  static class BoolEqualsFunction extends Function {
    public static final String NAME = "equals";
    public BoolEqualsFunction(){
      super(NAME, new Type[] { Type.BoolType, Type.BoolType }, 2 );
    }
    public Variable<BoolData> compile_checked( Variable<BoolData>[] args, Statement owner ) throws CompileException {

      PrintStream ps = ProgramTree.output;

      Variable<BoolData> out = new Variable<BoolData>( BoolData.equals( args[0].getData(), args[1].getData() ) );
      String op = "equ";
      if( !out.getData().is_constant() ) {
	String[] actual_args = new String[ 2 ];
	actual_args[0] = args[0].cur_name();
	actual_args[1] = args[1].cur_name();
	ps.print( out.new_name() + " " + op );
	for( int i = 0; i < args.length; i++){
	  ps.print( " " + actual_args[i] );
	}
	ps.println();
      }
      return out;
    }
    public Variable compile_func( Variable[] args, Statement owner ) throws CompileException{
      return compile_checked( (Variable<BoolData>[]) args, owner );
    }
  }

  
  static class IncFunction extends Function {
    public static final String NAME = "inc";
    private static final Variable<IntTypeData> one = new Variable<IntTypeData>( new IntTypeData( 1 ) );
    public IncFunction(){
      super(NAME, new Type[] { Type.IntType }, 1 );
    }
    public AbstractVariable compile_func( Variable[] args, Statement owner ) throws CompileException{
      return Function.call(Function.AddFunction.NAME, new Variable[] {
	  one,
	  args[0] }, owner );
    }
  }

  static class LessThanFunction extends Function {
    public static final String NAME = "<";
    public LessThanFunction(){
      super(NAME, new Type[] { Type.IntType, Type.IntType }, 2 );
    }
    public Variable<BoolData> compile_checked( Variable<IntTypeData>[] args, Statement owner ) throws CompileException {
      PrintStream ps = ProgramTree.output;
      boolean signed = false;
      for( int i = 0; i < args.length; i++){
	if( args[i].getData().signed() ){
	  signed = true;
	}
      }
      Variable<BoolData> out = new Variable<BoolData>( IntTypeData.lessthan( args[0].getData(), args[1].getData() ) );
      String op = signed ? "lts" : "ltu";
      if( !out.getData().is_constant() ) {
	int len = Variable.maxArgLength( args );
	String[] actual_args = Variable.padArgsToLength( args, len );
	ps.print( out.new_name() + " " + op + " ");
	ps.print( actual_args[1] + " " + actual_args[0] );
	ps.println();
      }
      return out;
    }
    public Variable compile_func( Variable[] args, Statement owner ) throws CompileException{
      return compile_checked( (Variable<IntTypeData>[]) args, owner );
    }
  }

  static class GreaterThanFunction extends Function {
    public static final String NAME = ">";
    public GreaterThanFunction(){
      super( NAME, new Type[] { Type.IntType, Type.IntType }, 2 );
    }
    public AbstractVariable compile_func( Variable[] args, Statement owner ) throws CompileException{
      return Function.call( Function.LessThanFunction.NAME,
			    new Variable[] { args[1], args[0] },
			    owner );
    }
  }

  static class Subtraction extends Function {
    public Subtraction(){
      super("-", new Type[] { Type.IntType, Type.IntType }, 2 );
    }
    public Variable compile_func( Variable[] args, Statement owner ) throws CompileException {
      IntTypeData data = IntTypeData.subtraction
	( (IntTypeData) args[0].getData(), (IntTypeData) args[1].getData() );
						 
     PrintStream ps = ProgramTree.output;
     Variable<IntTypeData> out = new Variable<IntTypeData>( data );
     if( !out.getData().is_constant() ){
       String[] actual_args = Variable.padArgsToLength( args, data.bit_count() );
       ps.print( out.new_name() + " sub" );
       for( int i = 0; i < args.length; i++){
	 ps.print( " "+actual_args[i] );
       }
       ps.println();
     }
     return out;
    }
  }

  static class Negate extends Function {
    public static final String NAME = "-";
    public Negate(){
      super("-", new Type[] { Type.IntType }, 1 );
    }
    public Variable compile_func( Variable[] args, Statement owner ) throws CompileException {
      // safe cast because types are already checked
      IntTypeData a = IntTypeData.negate( (IntTypeData) args[0].getData() );
      Variable out = new Variable( a );
      if( ! a.is_constant() ){
	ProgramTree.output.println( out.new_name() + " negate " + args[0].cur_name() );
      }
      return out;
    }
  }

  static class Length extends Function {
    public Length(){
      super("length", new Type[] {Type.ArrayType }, 1 );
    }
    public Variable compile_func( Variable[] args, Statement owner ) throws CompileException {
      ArrayVariable arg = ArrayVariable.get_from_abstract_var( args[0] );
      return new Variable<IntTypeData>( new IntTypeData( arg.getData().getSize() ) );
    }
  }

  static class BitWidth extends Function {
    public static String NAME = "bit_width";
    public BitWidth(){
      super(NAME,new Type[] {Type.ANYTYPE}, 1 );
    }
    public Variable compile_func( Variable[] args, Statement owner ) throws CompileException {
      Variable ans = new Variable
	( new IntTypeData( args[0].getData().bit_count() ) );
      return ans;
    }
  }


}