package frontend;
import java.io.PrintStream;
public class ConstantIntExp extends Expression {
  private int value;
  public ConstantIntExp( int line, int avalue ){
    super( line );
    value = avalue;
  }
  public Variable returnVar(){
    return null;
  }
  public void compile( PrintStream os ) throws CompileException{
    
  }
}