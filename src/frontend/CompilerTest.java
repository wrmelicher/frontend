package frontend;

import java.io.*;

public class CompilerTest {
  
  public static void main( String[] args ){
    
    if( args.length < 1 ){
      System.out.println("usage: CompilerTest <in-fname> [-i]");
      System.exit(1);
    }
    File input_file = new File( args[0] );
    InputStream in;
    try{
      if( args[0].equals("-") ){
	in = System.in;
      } else {
	in = new FileInputStream( input_file );
      }
    } catch ( FileNotFoundException e ){
      System.out.println( e.getMessage() );
      System.exit(1);
      in = null;
    }

    Parser comp = new Parser( in );

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
    if( args.length >= 2 && args[1].equals("-i") ){
      // TODO: make this a real flag
      GenerateInputs g = new GenerateInputs( t, input_file );
      g.inputs();
    }
    compile( t, out );

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
