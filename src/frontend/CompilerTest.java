package frontend;
import java.io.PrintStream;
import java.io.File;
import java.util.Scanner;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CompilerTest {
  private static ProgramTree t;
  private static String base_name;

  public interface Compiler {
    public ProgramTree tree() throws CompileException;
  }
  
  public static void compile( String base, Compiler c, PrintStream out ) {
    base_name = base;
    try{
      t = c.tree();
    } catch ( CompileException e ){
      System.err.println( e.getMessage() );
      System.exit(1);
    }
    inputs();
    ProgramTree.output = out;
    try {
      t.compile();
    } catch ( CompileException e ){
      System.err.println( e.getMessage() );
      System.exit(1);
    }
  }
  public static void compile( String base, Compiler c ) {
    try {
      compile( base, c, new PrintStream( new File( base + ".cir" ) ) );
    } catch ( FileNotFoundException e ){
      System.err.println( e.getMessage() );
      System.exit(1);
    }
  }

  public static void inputs() {
    try {
    List<DeclareInputStatement> ls = t.inputs();
    File client_file = new File( base_name+"_client.priv" );
    File server_file = new File( base_name+"_server.priv" );
    Scanner in = new Scanner( System.in );
    if( client_file.exists() && server_file.exists() ){
      System.out.println("Input files already exist, overwrite? (y/n):");
      String yes_no = in.nextLine();
      Pattern p = Pattern.compile("^yes|no|y|n$", Pattern.CASE_INSENSITIVE);
      Matcher m = p.matcher( yes_no );
      while( !m.matches() ){
	System.out.println("Input files already exist, overwrite? (y/n):");
	yes_no = in.nextLine();
	m = p.matcher( yes_no );
      }
      if( yes_no.startsWith( "n" ) || yes_no.startsWith( "N" ) ){
	return;
      }
    }
    PrintStream client_inputs = new PrintStream( client_file );
    PrintStream server_inputs = new PrintStream( server_file );

    for( DeclareInputStatement d : ls ){
      d.request_val( d.party() == 1 ? client_inputs : server_inputs, in );
    }
    client_inputs.close();
    server_inputs.close();
    } catch (FileNotFoundException e ){
      System.err.println( e.getMessage() );
      System.exit(1);
    }
  }
}