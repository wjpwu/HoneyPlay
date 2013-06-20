package models.bank;

import java.util.*;

public class Cities {
    
	
	
	
	public static Map<String,TreeMap> optionsMap(String a)
	{
		Map<String,TreeMap> opmap = new HashMap<String,TreeMap>();
		//account options
		TreeMap<String,String> map = new TreeMap<String,String>();
		map.put("aaa1", "622588592****6273");
		map.put("aaa2", "622588592****6274");
		opmap.put("acct", map);
		
		//city options
//		<option value="鞍山">A - 鞍山</option>
//		<option value="北京">B - 北京</option>
//		<option value="滨海">B - 滨海</option>
		TreeMap<String,String> cityMap = new TreeMap<String,String>();
		cityMap.put("鞍山", "A - 鞍山");
		cityMap.put("北京", "B - 北京");
		opmap.put("city", cityMap);
		
		return opmap;
	}
	
	
	
	public static Map<String,String> accounts()
	{
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("aaa1", "622588592****6273");
		map.put("aaa2", "622588592****6274");
		return map;
	}
	
	public static Map<String,String> cities()
	{
		Map<String,String> cityMap = new LinkedHashMap<String,String>();
		cityMap.put("鞍山", "A - 鞍山");
		cityMap.put("北京", "B - 北京");
		return cityMap;
	}
	
    public static List<String> list() {
        List<String> all = new ArrayList<String>();
        all.add("France");
        all.add("Austria");
        all.add("Belgium");
        all.add("Bulgaria");
        all.add("Cyprus");
        all.add("Czech Republic");
        all.add("Denmark");
        all.add("Estonia");
        all.add("Finland");
        all.add("French Guiana");
        all.add("Germany");
        all.add("Gibraltar");
        all.add("Greece");
        all.add("Guadeloupe");
        all.add("Hungary");
        all.add("Ireland");
        all.add("Italy");
        all.add("Latvia");
        all.add("Lithuania");
        all.add("Luxembourg");
        all.add("Malta");
        all.add("Martinique");
        all.add("Netherlands");
        all.add("Poland");
        all.add("Portugal");
        all.add("Reunion");
        all.add("Romania");
        all.add("Slovak (Republic)");
        all.add("Slovenia");
        all.add("Spain");
        all.add("Sweden");
        all.add("United Kingdom");
        return all;
    }
    
}