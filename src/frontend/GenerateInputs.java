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
  private boolean random;
  
  public GenerateInputs( ProgramTree tree, File i, boolean rnd ){
    t = tree;
    input_file = i;
    random = rnd;
  }
  
  public GenerateInputs( ProgramTree tree, File i ){
    this( tree, i, false );
  }
  
  public void inputs() {
    try {
      List<DeclareInputStatement> ls = t.inputs();
      File client_file = new File( input_file.getParent(), input_file.getName()+"_client.priv" );
      File server_file = new File( input_file.getParent(), input_file.getName()+"_server.priv" );
      Scanner in = new Scanner( System.in );
      PrintStream client_inputs = new PrintStream( client_file );
      PrintStream server_inputs = new PrintStream( server_file );

      if( !random )
	for( DeclareInputStatement d : ls )
	  d.request_val( d.party() == 1 ? client_inputs : server_inputs, in );
      else
	for( DeclareInputStatement d : ls )
	  d.request_val( d.party() == 1 ? client_inputs : server_inputs );
      
      client_inputs.close();
      server_inputs.close();
    } catch (FileNotFoundException e ){
      System.err.println( e.getMessage() );
      System.exit(1);
    }
  }
}
