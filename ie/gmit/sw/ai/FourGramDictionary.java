package ie.gmit.sw.ai;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class FourGramDictionary 
{
	private long size = 0l;
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
				size+=Long.parseLong(line.substring(line.indexOf(" ")+1));
			}
			
			reader.close();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public double NGramScore(String txt)
	{
		if(dic.size()==0)
			return -1;
		try 
		{
			return Math.log10(Double.parseDouble(dic.get(txt)))/Math.log10(size);
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
