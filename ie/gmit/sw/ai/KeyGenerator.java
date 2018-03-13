package ie.gmit.sw.ai;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class KeyGenerator {

	private static char[] key = {'A','B','C','D','E','F','G','H','I','K',
			'L','M','N','O','P','Q','R','S','T','U','V','X','W','Y','Z'};
	
	public static String GenerateKey()
	{
		int index;
		Random random = ThreadLocalRandom.current();
		for (int i = key.length - 1; i > 0; i--) 
		{
			index = random.nextInt(i + 1);
			if (index != i) 
			{
				key[index] ^= key[i];
				key[i] ^= key[index];
				key[index] ^= key[i];
			}
		}
		return String.valueOf(key);
	}
	
}
