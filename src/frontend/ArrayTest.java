package frontend;

public class ArrayTest implements CompilerTest.Compiler {

  public static void main( String[] args ){
    CompilerTest.compile( args[0], new ArrayTest() );
  }

  public ProgramTree tree() throws CompileException {
    ProgramTree tree = new ProgramTree();

    return tree;
  }
}