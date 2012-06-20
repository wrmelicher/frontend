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
    if( ProgramTree.DEBUG > 1 )
      ProgramTree.error.println("Accessing function \""+s+"\"");
    List<Function> f = func_map.get(s);
    return f;
  }
  public Type[] getTypes(){
    return required_types;
  }
  public int num_args(){
    return required_args;
  }
  public boolean satisfies( AbstractVariable[] args, Statement owner ) throws CompileException {
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
    for( Function f : funcs ){
      try{
	if( f.satisfies( args, owner ) ){
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
    return match.compile( args, owner );
  }

  public AbstractVariable compile( AbstractVariable[] args, Statement owner ) throws CompileException {
    return compile_func( args, owner );
  }
  
  public abstract AbstractVariable compile_func( AbstractVariable[] args, Statement owner ) throws CompileException;

  static {
    new AddFunction();
    new IntEqualsFunction();
    new BoolEqualsFunction();
    new IncFunction();
    new LessThanFunction();
    //new Conditional();
    //new ArrayAt();
    //new Set();
    new Subtraction();
    new Negate();
  }
  
  static class AddFunction extends Function {
    public static final String NAME = "+";
    public AddFunction(){
      super( NAME, new Type[] { Type.IntType, Type.IntType}, 2 );
    }
    public AbstractVariable<IntTypeData> compile_checked( AbstractVariable<IntTypeData>[] args, Statement owner ) throws CompileException {
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
    public AbstractVariable compile_func( AbstractVariable[] args, Statement owner ) throws CompileException{
      return compile_checked( (AbstractVariable<IntTypeData>[]) args, owner );
    }
  }
  
  static class IntEqualsFunction extends Function {
    public static final String NAME = "equals";
    public IntEqualsFunction(){
      super(NAME, new Type[] { Type.IntType, Type.IntType }, 2 );
    }
    public AbstractVariable<BoolData> compile_checked( AbstractVariable<IntTypeData>[] args, Statement owner ) throws CompileException {

      PrintStream ps = ProgramTree.output;

      AbstractVariable<BoolData> out = new Variable<BoolData>( IntTypeData.equals( args[0].getData(), args[1].getData() ) );
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
    public AbstractVariable compile_func( AbstractVariable[] args, Statement owner ) throws CompileException{
      return compile_checked( (AbstractVariable<IntTypeData>[]) args, owner );
    }
  }

  static class BoolEqualsFunction extends Function {
    public static final String NAME = "equals";
    public BoolEqualsFunction(){
      super(NAME, new Type[] { Type.BoolType, Type.BoolType }, 2 );
    }
    public AbstractVariable<BoolData> compile_checked( AbstractVariable<BoolData>[] args, Statement owner ) throws CompileException {

      PrintStream ps = ProgramTree.output;

      AbstractVariable<BoolData> out = new Variable<BoolData>( BoolData.equals( args[0].getData(), args[1].getData() ) );
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
    public AbstractVariable compile_func( AbstractVariable[] args, Statement owner ) throws CompileException{
      return compile_checked( (AbstractVariable<BoolData>[]) args, owner );
    }
  }

  
  static class IncFunction extends Function {
    public static final String NAME = "inc";
    private static final AbstractVariable<IntTypeData> one = new Variable<IntTypeData>( new IntTypeData( 1 ) );
    public IncFunction(){
      super(NAME, new Type[] { Type.IntType }, 1 );
    }
    public AbstractVariable compile_func( AbstractVariable[] args, Statement owner ) throws CompileException{
      return Function.call(Function.AddFunction.NAME, new AbstractVariable[] {
	  one,
	  args[0] }, owner );
    }
  }

  static class LessThanFunction extends Function {
    public static final String NAME = "lessthan";
    public LessThanFunction(){
      super(NAME, new Type[] { Type.IntType, Type.IntType }, 2 );
    }
    public AbstractVariable<BoolData> compile_checked( AbstractVariable<IntTypeData>[] args, Statement owner ) throws CompileException {
      PrintStream ps = ProgramTree.output;
      boolean signed = false;
      for( int i = 0; i < args.length; i++){
	if( args[i].getData().signed() ){
	  signed = true;
	}
      }
      AbstractVariable<BoolData> out = new Variable<BoolData>( IntTypeData.lessthan( args[0].getData(), args[1].getData() ) );
      String op = signed ? "lts" : "ltu";
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
    public AbstractVariable compile_func( AbstractVariable[] args, Statement owner ) throws CompileException{
      return compile_checked( (AbstractVariable<IntTypeData>[]) args, owner );
    }
  }
  /*
  static class Conditional extends Function {
    public static String NAME = "?";
    public Conditional(){
      super(NAME, new Type[] { Type.BoolType, Type.ANYTYPE, Type.ANYTYPE }, 3 );
    }
    public AbstractVariable compile_func( AbstractVariable[] args, Statement owner ) throws CompileException {
      if( ! (args[1].getType() == args[2].getType() ) ){
	throw owner.error("Arguments 2 and 3 must have the same type");
      }
      BoolData choseData = (BoolData) args[0].getData();
      AbstractVariable out;
      if( choseData.is_constant() ){
	if( choseData.poss_value() == BoolData.TRUE ){
	  out = args[1];
	} else {
	  out = args[2];
	}
      } else {
	TypeData one = args[1].getData();
	TypeData two = args[2].getData();
	
	out = new Variable( one.conditional( two ) );
	
	String[] actual_args = Variable.padArgsToLength( new AbstractVariable[] { args[1], args[2] } , out.getData().bit_count() );

	ProgramTree.output.println( out.new_name() + " chose " + args[0].cur_name() + " " + " " +actual_args[0] + " " + actual_args[1] );
      }
      return out;
    }
    }

  static class ArrayAt extends Function {
    public ArrayAt(){
      super("at", new Type[] { Type.ArrayType, Type.IntType }, 2 );
    }
    public AbstractVariable compile_func( AbstractVariable[] args, Statement owner ) throws CompileException {
      ArrayVariable arr;
      if( args[0] instanceof ArrayVariable ){
	arr = (ArrayVariable) args[0];
      } else {
	arr = (ArrayVariable) ( (FunctionArgument) args[0] ).getVar();
      }
      ArrayAccessExp e = new ArrayAccessExp( owner.getLine(), arr, (AbstractVariable<IntTypeData>) args[2] );
      e.compile();
      return e.returnVar();
    }
  }



  static class Set extends Function {
    public Set(){
      super("set", new Type[] { Type.ANYTYPE, Type.ANYTYPE }, 2 );
    }
    public AbstractVariable compile_func( AbstractVariable[] args, Statement owner ) throws CompileException {
      AssignmentExp e = new AssignmentExp( owner.getLine(), args[0],
					   new VariableExp( owner.getLine(), args[1] ) );
      e.compile();
      return e.returnVar();
    }
  }
  */
  static class Subtraction extends Function {
    public Subtraction(){
      super("sub", new Type[] { Type.IntType, Type.IntType }, 2 );
    }
    public AbstractVariable compile_func( AbstractVariable[] args, Statement owner ) throws CompileException {
      IntTypeData data = IntTypeData.subtraction
	( (IntTypeData) args[0].getData(), (IntTypeData) args[1].getData() );
						 
     PrintStream ps = ProgramTree.output;
     AbstractVariable<IntTypeData> out = new Variable<IntTypeData>( data );
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
    public Negate(){
      super("minus", new Type[] { Type.IntType }, 1 );
    }
    public AbstractVariable compile_func( AbstractVariable[] args, Statement owner ) throws CompileException {
      // safe cast because types are already checked
      IntTypeData a = IntTypeData.negate( (IntTypeData) args[0].getData() );
      AbstractVariable out = new Variable( a );
      if( ! a.is_constant() ){
	ProgramTree.output.println( out.new_name() + " negate " + args[0].cur_name() );
      }
      return out;
    }
  }
}