package base;
import java.util.HashMap;
import java.util.Map;

public class DataHolder {
	public static Map<String, Object> data = new HashMap<String, Object>();

	private static void debug() {
		System.out.println(data);
	}
	private static void debug(String string) {
		System.out.println(data);
		System.out.println(string);
	}
	public static void set(String key, String value) {
		data.put(key, value);
		debug();
	}

	public static void set(String key, int value) {
		set(key, "" + value);
	}

	public static String getString(String key, String defValue) {
		return data.getOrDefault(key, defValue).toString();
	}

	public static String getString(String key) {
		return getString(key, "");
	}

	public static int getInt(String key, int defValue) {
		Object result = data.get(key);
		if (result == null) {
			return defValue;
		}
		if (result instanceof Integer) {
			return ((Integer) result).intValue();
		}
		try {
			return Integer.parseInt(result.toString());
		} catch (Exception e) {
			return defValue;
		}
	}

	public static int getInt(String key) {
		return getInt(key, 0);
	}

	public static int inc(String key, int inc) {
		data.put(key, getInt(key) + inc);
		debug();
		return getInt(key);
	}

	public static int inc(String key) {
		return inc(key, 1);
	}

	public static int dec(String key, int dec) {
		data.put(key, getInt(key) - dec);
		debug();
		return getInt(key);
	}

	public static int dec(String key) {
		return dec(key, 1);
	}

	public static boolean hasKey(String key) {
		debug(key+" : "+data.containsKey(key));
		return data.containsKey(key);
	}

	public static boolean is(String key, String value) {
		debug(key+" == "+value+" : "+data.containsKey(key));
		return hasKey(key) && getString(key).equals(value);
	}
}
