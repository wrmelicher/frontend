package frontend.functions;

import frontend.*;

public class Slice extends Function {
  public static final String NAME = "slice";
  public Slice(){
    super(NAME, new Type[] { Type.ArrayType, Type.IntType, Type.IntType } );
  }
  public AbstractVariable compile_func
    ( Variable[] args,
      Statement owner ) throws CompileException {
    int start = Variable.get_val_from_var( args[1], owner );
    int end = Variable.get_val_from_var( args[2], owner );

    ArrayVariable arr = ArrayVariable.get_from_abstract_var( args[0] );
    if( start >= arr.getData().getSize() ||
	start < 0 ||
	end < 0 ||
	end > arr.getData().getSize() ||
	end <= start )
      throw owner.error("improper array bounds");
    arr.join_indices();

    TypeData elem_data = arr.getData().getElementData();
    
    ArrayVariable ans = new ArrayVariable
      ( new ArrayData(elem_data, end - start ) );

    ProgramTree.output.println( ans.new_name() +
				" select "+
				arr.cur_name() + " " + 
				(elem_data.bit_count() * start) + " " +
				(elem_data.bit_count() * end) );
    return ans;
  }
}
