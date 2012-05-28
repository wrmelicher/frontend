package frontend;
import java.io.PrintStream;
public class FunctionExp extends Expression {
  private Function func;
  private Expression[] args;
  private Variable outvar;
  public FunctionExp( int line, Function afunc, Expression[] some_args ){
    super( line );
    func = afunc;
    args = some_args;
  }
  public Variable returnVar(){
    return outvar;
  }
  public void compile( PrintStream os ) throws CompileException {
    Variable[] vargs = new Variable[ args.length ];
    for( int i = 0; i < args.length; i++){
      args[i].compile(os);
      vargs[i] = args[i].returnVar();
    }
    outvar = func.compile( os, vargs, this );
  }

}