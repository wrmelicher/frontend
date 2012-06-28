package frontend;

import java.io.*;

public class CompilerTest {
  
  public static void main( String[] args ){
    if( args.length < 1 ){
      System.out.println("usage: CompilerTest <in-fname> [-i]");
      System.exit(1);
    }
    Parser comp;
    File input_file = new File( args[0] );
    try{
      comp = new Parser( new FileInputStream( input_file ) );
    } catch ( FileNotFoundException e ){
      System.out.println( e.getMessage() );
      System.exit(1);
      comp = null;
    }
    ProgramTree t = comp.tree();
    File output_file = new File( input_file.getParent(), input_file.getName()+".gcil" );
    PrintStream out;
    try {
      out = new PrintStream( output_file );
    } catch (FileNotFoundException e ){
      System.out.println( e.getMessage() );
      System.exit(1);
      out = null;
    }
    compile( t, out );
    if( args.length >2 && args[1].equals("-i") ){
      GenerateInputs g = new GenerateInputs( t, input_file );
      g.inputs();
    }
  }
  
  public static void compile( ProgramTree tree, PrintStream out ) {
    ProgramTree.output = out;
    try {
      tree.compile();
    } catch ( CompileException e ){
      System.err.println( e.getMessage() );
      System.exit(1);
    }
  }
}