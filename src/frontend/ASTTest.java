package frontend;

public class ASTTest {
  public static void main( String[] args ) throws CompileException{
    ProgramTree tree = new ProgramTree();

    // a variable that goes to 8
    Variable a = new IntVariable( "a", new IntTypeData( 7, false ) );
    Variable b = new IntVariable( "b", new IntTypeData( 31, false) );

    // answer. size doesnt matter
    Variable sum = new IntVariable( "sum", new IntTypeData( 1, false ) );
    
    tree.addStatement( new DeclareInputStatement( 1, a, 1 ) );
    tree.addStatement( new DeclareInputStatement( 2, b, 2 ) );

    tree.addStatement( new DeclareOutput( 3, sum ) );

    FunctionExp add = new FunctionExp( 5, Function.from_name("+"),
				       new Expression[] {
					 new VariableExp( 6, a ),
					 new VariableExp( 7, b )
				       } );
    AssignmentExp assign = new AssignmentExp( 4, sum, add );
    
    tree.addStatement( assign );

    tree.compile( System.out );
  }
}