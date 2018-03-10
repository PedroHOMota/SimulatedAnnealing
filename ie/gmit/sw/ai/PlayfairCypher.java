package ie.gmit.sw.ai;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PlayfairCypher 
{
	private int[] rowIndices = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,};
	private int[] columnIndices = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,};
	
	private char[][] digraph= new char[5][5];
	
	public PlayfairCypher() {}
	
	public void encrypt(String txt)
	{
		int counter=0;
		String enc="";
		int ri=0 , ci=0;
		txt = txt.toUpperCase().replaceAll("[^A-Za-z0-9]", "");
		Pattern p = Pattern.compile("(.)\\1");
		Matcher m = p.matcher(txt);

		while (m.find())
		{
			txt=txt.replace(m.group(0), m.group(0).charAt(0) + "X");
		}
		
		for(int i=0;counter<25;i++)
		{
			if(rowIndices[txt.charAt(i)-65]==-1)
			{
				if(ri==25)
				{
					ci++;
					ri=0;
				}
				i++;
				rowIndices[txt.charAt(i)-65]=ri;
				columnIndices[txt.charAt(i)-65]=ci;
				digraph[ci][ri]=txt.charAt(i);
				ri++;
			}
		}
		
	}
	
}
