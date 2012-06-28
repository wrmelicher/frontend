package frontend;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.PrintStream;
import java.io.File;
import java.util.List;

public class GenerateInputs {
  private ProgramTree t;
  private File input_file;
  public GenerateInputs( ProgramTree tree, File i ){
    t = tree;
    input_file = i;
  }
  public void inputs() {
    try {
      List<DeclareInputStatement> ls = t.inputs();
      File client_file = new File( input_file.getParent(), input_file.getName()+"_client.priv" );
      File server_file = new File( input_file.getParent(), input_file.getName()+"_server.priv" );
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