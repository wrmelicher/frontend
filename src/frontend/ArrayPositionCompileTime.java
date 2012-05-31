package frontend;
import java.io.PrintStream;

public class ArrayPositionCompileTime extends ArrayPosition {
  private int pos;
  public ArrayPositionCompileTime( ArrayVariable par, int place ){
    super( par );
    pos = place;
  }
  public void compile_assignment( PrintStream ps, Variable other, AssignmentExp owner ) throws CompileException {
    super.compile_assignment( ps, other, owner );
    // assume that all size adjustments have already been made

    ArrayData parentData = (ArrayData) getParent().getData();
    if( pos >= parentData.getSize() ){
      throw owner.error( "Index "+pos+" is greater than array length" );
    }
    if( pos < 0 ){
      throw owner.error( "Cannot access a negative array index" );
    }
    
    if( pos == parentData.getSize() - 1 ){
      String before = Variable.temp_var_name();
      ps.println( before + " select " + getParent().cur_name() + " 0 " + (parentData.getSize() * pos ) );
      ps.println( getParent().new_name() + " concat " + other.cur_name() + " " + before );
    } else if( pos == 0 ) {
      String after = Variable.temp_var_name();
      ps.println( after + " select " + getParent().cur_name() + " " + parentData.getElementData().bit_count() + " " + parentData.bit_count() );
      ps.println( getParent().new_name() + " concat " + other.cur_name() + " " + after );
    } else {
      String before = Variable.temp_var_name();
      String after = Variable.temp_var_name();

      ps.println( before + " select " + getParent().cur_name() + " 0 " + (parentData.getSize() * pos ) );
      ps.println( after + " select " + getParent().cur_name() + " " + ( parentData.getElementData().bit_count() * ( pos + 1 ))+ " " + parentData.bit_count() );
      ps.println( getParent().new_name() + " concat " + before + " " + other.cur_name() + " " + after );
    }
  }
}