package ie.gmit.sw.ai;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class FourGramDictionary 
{
	private static Map<String, String> dic=new HashMap<String, String>();
	
	public FourGramDictionary(String path) 
	{
		try 
		{
			BufferedReader reader=new BufferedReader(new FileReader(path));
			String line="";
		
		
			while((line=reader.readLine())!=null)
			{
				dic.put(line.substring(0,line.indexOf(" ")),line.substring(line.indexOf(" ")+1));
			}
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int NGramScore(String txt)
	{
		if(dic.size()==0)
			return -1;
		try 
		{
			return Integer.parseInt(dic.get(txt));
		}
		catch (Exception e) 
		{
			return 0;
		}
	}

	public int getSize()
	{
		return dic.size();
	}
}
