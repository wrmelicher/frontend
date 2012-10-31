package frontend;

import java.io.*;
import org.apache.commons.cli.*;

public class CompilerTest {
  private static final String DEBUG_STR = "d";
  public static Option debug_level_option(){
    OptionBuilder.withArgName(DEBUG_STR);
    OptionBuilder.withLongOpt("d");
    OptionBuilder.isRequired(false);
    OptionBuilder.withDescription("The debug level of the compiler. A higher number prints out more");
    OptionBuilder.hasArg(true);
    return OptionBuilder.create();
  }

  private static final String INPUT_STR = "i";
  public static Option input_level_option(){
    OptionBuilder.withLongOpt("i");

    OptionBuilder.isRequired(false);
    OptionBuilder.withDescription("Will generate the input files for the circuit file");
    OptionBuilder.hasOptionalArg();
    return OptionBuilder.create();
  }
  private static final String CSE_OPTS = "c";
  public static Option cse_option(){
    OptionBuilder.withLongOpt("c");

    OptionBuilder.isRequired(false);
    OptionBuilder.withDescription("will not use common subexpression optimizations to save memory");
    OptionBuilder.hasOptionalArg();
    return OptionBuilder.create();
  }

  private static Options opts = new Options();

  private static void usage(){
    HelpFormatter hf = new HelpFormatter();
    hf.printHelp("options: ",opts);
  }
  
  public static void main( String[] args ) {
    opts.addOption(debug_level_option());
    opts.addOption(input_level_option());
    opts.addOption(cse_option());
    CommandLineParser cmd = new PosixParser();
    CommandLine line;
    boolean rnd = false;
    try {
      line = cmd.parse(opts,args,false);
      String d = line.getOptionValue(DEBUG_STR,"0");
      String r = line.getOptionValue(INPUT_STR,"");
      String c = line.getOptionValue(CSE_OPTS,"some");
      if( r.equals("random") ){
	rnd = true;
      }
      if( c.equals("some") ){
	Expression.cse_opts = false;
	System.out.println("poop");
      }
      ProgramTree.DEBUG = Integer.parseInt(d);
    } catch( org.apache.commons.cli.ParseException e ){
      System.out.println(e.getMessage());
      usage();
      System.exit(1);
      line = null;
    } catch( NumberFormatException e ){
      System.out.println(e.getMessage());
      usage();
      System.exit(1);
      line = null;
    }
    boolean input = line.hasOption(INPUT_STR);

    String[] left_args = line.getArgs();
    if(left_args.length != 1){
      System.out.println("requires file name");
      usage();
    }
    File input_file = new File( left_args[0] );
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
    File output_file = new File( input_file.getParent(),
				 input_file.getName()+".gcil" );
    PrintStream out;
    try {
      out = new PrintStream( output_file );
    } catch (FileNotFoundException e ){
      System.out.println( e.getMessage() );
      System.exit(1);
      out = null;
    }
    if(input){
      GenerateInputs g = new GenerateInputs( t, input_file, rnd );
      g.inputs();
    }
    compile( t, out );

  }
  
  public static void compile( ProgramTree tree, PrintStream out ) {
    ProgramTree.output = out;
    frontend.Function.InitFunctions();
    try {
      tree.compile();
    } catch ( CompileException e ){
      System.err.println( e.getMessage() );
      System.exit(1);
    }
  }
}
