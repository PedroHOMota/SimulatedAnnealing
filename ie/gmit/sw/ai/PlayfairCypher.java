package ie.gmit.sw.ai;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayfairCypher 
{
	private int[] rowIndices = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private int[] columnIndices = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private char[][] digraph= new char[5][5];
	
	public PlayfairCypher(String key) 
	{
		getDistinctsChars(key);

	}
	
	public char[][] getDigraph()
	{
		return this.digraph;
	}
	public void setDigraph(char[][] digraph)
	{
		this.digraph = digraph;
	}
	
	public int[] getRowIndexes()
	{
		return rowIndices;
	}
	public void setRowIndexes(int[] rowIndices)
	{
		this.rowIndices = rowIndices;
	}
	
	public int[] getColumnIndices()
	{
		return columnIndices;
	}
	public void setColumnIndices(int[] columnIndices)
	{
		this.columnIndices = columnIndices;
	}
	
 	private String getDistinctsChars(String txt)
	{
 		int counter=0;
		String enc="";
		int ri=0 , ci=0;
		txt = txt.toUpperCase().replaceAll("[^A-IK-Za-ik-z0-9]", "");
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
				if(ri==5)
				{
					ci++;
					ri=0;
				}
				
				rowIndices[txt.charAt(i)-65]=ri;
				columnIndices[txt.charAt(i)-65]=ci;
				digraph[ci][ri]=txt.charAt(i);
				
				ri++;
				counter++;
			}
		}
		
		return txt;
		
	}
	
	private String[] CreatePairs(String txt)
	{
		String[] pairs;
		
		txt = txt.toUpperCase().replaceAll("[^A-Za-z0-9]", "");
		Pattern p = Pattern.compile("(.)\\1");
		Matcher m = p.matcher(txt);

		while (m.find())
		{
			txt=txt.replace(m.group(0), m.group(0).charAt(0) + "X");
		}
		
		txt += (txt.length() % 2 != 0) ? "X" : ""; //If the txt is odd add a X to the end to amke it even
	
		pairs = txt.split("(?<=\\G.{2})");
		
		return pairs;
		
		
	}
	
	public String Encrypt(String txt)
	{
		String[] pairs = CreatePairs(txt);
		String aux="";
		
		for (int i = 0; i < pairs.length; i++) 
		{
			char[] letters=pairs[i].toCharArray();
			
			if(columnIndices[letters[0]-65]==columnIndices[letters[1]-65]) //same row
			{
				//Pegar a letra imediata abaixo
				//[coluna][linha]
				int auxLinha = 0;
				auxLinha = rowIndices[letters[0]-65];
				auxLinha++;
				//System.out.println("L1:"+auxLinha);
				if(auxLinha>4)
					auxLinha=0;
				
				aux+=""+digraph[columnIndices[letters[0]-65]][auxLinha];
				
				auxLinha = rowIndices[letters[1]-65];
				auxLinha++;
				//System.out.println("L2:"+auxLinha);
				if(auxLinha>4)
					auxLinha=0;
				
				aux+=""+digraph[columnIndices[letters[1]-65]][auxLinha];
				
			}
			else if(rowIndices[letters[0]-65]==rowIndices[letters[1]-65]) //same column
			{
				//Pegar a letra da direita imediata
				//[coluna][linha]
				int auxColuna = 0;
				auxColuna = columnIndices[letters[0]-65];
				auxColuna++;
				if(auxColuna>4)
					auxColuna=0;
				
				aux+=""+digraph[auxColuna][rowIndices[letters[0]-65]];
				
				auxColuna = columnIndices[letters[1]-65];
				auxColuna++;
				if(auxColuna>4)
					auxColuna=0;
				
				aux+=""+digraph[auxColuna][rowIndices[letters[1]-65]];
			}
			else
			{
				//minha linha coluna da outra letra
				//[coluna][linha]
				aux+=digraph[columnIndices[letters[0]-65]][rowIndices[letters[1]-65]]+""+
						digraph[columnIndices[letters[1]-65]][rowIndices[letters[0]-65]];
			}
		}
		return aux;
	}
	
	public String Decrypt(String txt)
	{
		String pairs[]=txt.split("(?<=\\G.{2})");
		
		@SuppressWarnings("unused")
		String aux="";
		
		for (int i = 0; i < pairs.length; i++) 
		{
			char[] letters=pairs[i].toCharArray();
			
			if(columnIndices[letters[0]-65]==columnIndices[letters[1]-65]) //same row
			{
				//Pegar a letra imediata abaixo
				//[coluna][linha]
				int auxLinha = 0;
				auxLinha = rowIndices[letters[0]-65];
				auxLinha--;
				//System.out.println("L1:"+auxLinha);
				if(auxLinha<0)
					auxLinha=4;
				
				aux+=""+digraph[columnIndices[letters[0]-65]][auxLinha];
				
				auxLinha = rowIndices[letters[1]-65];
				auxLinha--;
				//System.out.println("L2:"+auxLinha);
				if(auxLinha<0)
					auxLinha=4;
				
				aux+=""+digraph[columnIndices[letters[1]-65]][auxLinha];
				
			}
			else if(rowIndices[letters[0]-65]==rowIndices[letters[1]-65]) //same column
			{
				//Pegar a letra da direita imediata
				//[coluna][linha]
				int auxColuna = 0;
				auxColuna = columnIndices[letters[0]-65];
				auxColuna--;
				if(auxColuna<0)
					auxColuna=4;
				
				aux+=""+digraph[auxColuna][rowIndices[letters[0]-65]];
				
				auxColuna = columnIndices[letters[1]-65];
				auxColuna--;
				if(auxColuna<0)
					auxColuna=4;
				
				aux+=""+digraph[auxColuna][rowIndices[letters[1]-65]];
			}
			else
			{
				//minha linha coluna da outra letra
				//[coluna][linha]
				
						
				aux+=""+digraph[columnIndices[letters[0]-65]][rowIndices[letters[1]-65]]
						+""+digraph[columnIndices[letters[1]-65]][rowIndices[letters[0]-65]];
				
			}
		}
		
		return aux;
	}
	
	public String toString()
	{
		String aux="";
		
		for (int i = 0; i < 5; i++) 
		{
			for (int j = 0; j < 5; j++) 
			{
				aux+=digraph[i][j];
			}
		}
		
		return aux;
	}
	
	public void printMatrix()
	{
		for (int i = 0; i < 5; i++) 
		{
			for (int j = 0; j < 5; j++)
			{
				System.out.print("\t"+digraph[i][j]);
			}
			System.out.println();
		}
		System.out.println();
		/*for (int i = 0; i < columnIndices.length; i++) 
		{
			if(i%4==0)
			{
				System.out.println();
			}
			if(i==9) continue;
			System.out.print("\t"+digraph[columnIndices[i]][rowIndices[i]]);
		}*/
	}
	
}
