package apps.framework.object;

import java.io.Reader;

@SuppressWarnings("rawtypes")
public class CmResMap<K, V> extends CmMap{
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public Object get(Object key) {
		return super.get(key.toString().toLowerCase());
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object put(Object key, Object value) {
		if (value instanceof oracle.sql.CLOB) {
			return map.put(key.toString().toLowerCase() , clobToString((oracle.sql.CLOB)value));
		}
		/*else if (value instanceof java.lang.String) {
			return map.put(key.toString().toLowerCase() , ((String)value).replaceAll("\"", "&#034;"));
		}*/
		else {
			return map.put(key.toString().toLowerCase() , value);
		}
	}
	
	/**
	 * 
	 * @param clob
	 * @return
	 */
	private String clobToString ( oracle.sql.CLOB clob ) {
		StringBuilder	sbf		= new StringBuilder();
		Reader			rd		= null;
		char[]			buf		= new char[1024];
		int				readCnt	= 0;
		
		try {
			rd	= clob.getCharacterStream(0l);
			while ((readCnt = rd.read(buf, 0, 1024)) != -1 ) {
				sbf.append(buf, 0, readCnt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rd != null){
					rd.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return sbf.toString();
	}
}
