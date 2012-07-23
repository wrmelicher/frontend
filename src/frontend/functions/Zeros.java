package frontend.functions;
import frontend.*;
import java.util.List;
import java.util.LinkedList;

public class Zeros extends Function {
  public static final String NAME = "zeros";
  public Zeros(){
    super( NAME, new Type[] { Type.IntType } );
  }
  private static final Variable ZERO =
    new Variable( new IntTypeData(0) );
  public AbstractVariable compile_func
    ( Variable[] args, 
      Statement owner ) throws CompileException {
    if( !args[0].getData().is_constant() ){
      throw owner.error(NAME+" operation requires size of array be known at compile time");
    }
    int num = ((IntTypeData)args[0].getData()).value();
    List<Expression> zeros = new LinkedList<Expression>();
    for( int i = 0; i < num; i++){
      zeros.add(new VariableExp(owner.getLine(), ZERO));
    }
    LiteralArrayExp ans_exp = new LiteralArrayExp( owner.getLine(), zeros );
    ans_exp.compile();
    return ans_exp.returnVar();
  }
}
