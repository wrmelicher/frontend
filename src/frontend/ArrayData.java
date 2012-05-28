package frontend;

public class ArrayData extends TypeData {
  private TypeData elem_type;
  private int size;
  
  public ArrayData( TypeData elem, int s ){
    super( Type.ArrayType );
    elem_type = elem;
    size = s;
  }

  public int getSize(){
    return size;
  }

  public TypeData getElementData(){
    return elem_type;
  }

  public int bit_count(){
    return size * elem_type.bit_count();
  }
}