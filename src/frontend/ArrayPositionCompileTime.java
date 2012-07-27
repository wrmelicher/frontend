package frontend;

public class ArrayPositionCompileTime extends ArrayPosition {
  private boolean changed = false;
  private int pos;
  public ArrayPositionCompileTime( TypeData t, int p ){
    super( t );
    pos = p;
  }

  public void read_value(){
    getParent().state_index( pos, new_name() );
    setData( getParent().getData().any_elem() );
  }
  
  @Override
  public void compile_assignment
    ( Variable other,
      Statement owner ) throws CompileException {
    if( getParent() != null ){
      ArrayData parentData = getParent().getData();
      if( other.getType() != parentData.at(0).getType() ){
	throw owner.error( "Cannot assign type "
			   +other.getType().name()+
			   " to array of type "
			   +parentData.at(0).getType().name() );
      }
    }
    super.compile_assignment( other, owner );
    // assume that all size adjustments have already been made
  }
}
