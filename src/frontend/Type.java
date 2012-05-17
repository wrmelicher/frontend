package frontend;

import java.util.Map;
import java.util.HashMap;

public class Type {
  private static Map<String, Type> type_map = new HashMap<String,Type>();
  String name;

  public Type( String a_name ) {
    name = a_name;
    type_map.put( a_name, this );
  }
  public static Type from_name( String a_name ){
    return type_map.get( a_name );
  }
}