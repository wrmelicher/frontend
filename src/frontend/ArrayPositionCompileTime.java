package frontend;

public class ArrayPositionCompileTime extends ArrayPosition {
  private int pos;
  public ArrayPosition( ArrayVariable par, int place ){
    super( par );
    pos = place;
  }
  public void compile_assignment( PrintStream ps, Variable other, AssignmentExp owner ) throws CompileException {
    super( ps, other );
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
      ps.println( getParent().new_name() + " concat " + other.cur_name() + " " + after );
    } else if( pos == 0 ) {
      String after = Variable.temp_var_name();
      ps.println( after + " select " + getParent().cur_name() + " " + parentData.getElementData().bit_count() + " " + parent.bit_count() );
      ps.println( getParent().new_name() + " concat " + other.cur_name() + " " + after );
    } else {
      String before = Variable.temp_var_name();
      String after = Variable.temp_var_name();

      ps.println( before + " select " + getParent().cur_name() + " 0 " + (parentData.getSize() * pos ) );
      ps.println( after + " select " + getParent().cur_name() + " " + ( parentData.getElementData().bit_count() * ( pos + 1 ))+ " " + getParent().bit_count() );
      ps.println( getParent().new_name() + " concat " + before + " " + other.cur_name() + " " + after );
    }
  }
}