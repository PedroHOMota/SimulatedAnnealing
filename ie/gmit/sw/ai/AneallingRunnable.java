package ie.gmit.sw.ai;

public class AneallingRunnable implements Runnable 
{
	String key; double score; double temp;
	//String generatedKey; double generatedScore;
	String txt;
	SimulatedAnnealing sa = new SimulatedAnnealing();
	private int tID;
	public AneallingRunnable(String currentKey, double score,double temp, String txt, int tID)//String generatedKey,double generatedScore,String txt) 
	{
		this.key = currentKey;
		this.score=score;
		this.temp = temp;
		//this.generatedKey = generatedKey;
		//this.generatedScore = generatedScore;
		this.txt=txt;
		this.tID = tID;
	}
	@Override
	public void run() 
	{
		double delta = 0;
		PlayfairCypher aux;
		for (int i = 40000; i > 0; i--) 
		{
			String newKey=sa.ShuffleKey(key);
			aux=new PlayfairCypher(newKey);
			double newScore = sa.Score(aux.Decrypt(txt));
			delta = newScore - score;
			if(delta > 0)
			{
				key=newKey;
				score=newScore;
			}
			else 
			{
				double prob = Math.pow(Math.E,delta/temp);
				if(prob>0.5)
				{
					key=newKey;
					score=newScore;
				}
			}
		}
		if(tID==1)
		{
			SharedDataBetweenThreads.keyT1 = key;
			SharedDataBetweenThreads.scoreT1 = score;
		}
		else
		{
			SharedDataBetweenThreads.keyT2 = key;
			SharedDataBetweenThreads.scoreT2 = score;
		}
		
		System.out.println("T"+tID+" finished "+key+" score "+score);
	}

}
