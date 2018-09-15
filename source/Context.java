import java.util.HashMap;

public class Context {
 
  private final HashMap<String, Object> values = new HashMap<>();
 
  public <T> void put( String key, T value, Class<T> valueType ) {
    values.put( key, value );
  }
 
  public <T> T get( String key, Class<T> valueType ) {
    return ( T )values.get( key );
  }
}
