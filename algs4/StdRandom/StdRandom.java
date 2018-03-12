/**********************************************************************
 *FileName: StdRandom
 *Author:   luibebetter
 *Date:     2018/1/2116:06
 *Description:Random number generating class
 **********************************************************************/

package algs4.StdRandom;

import java.util.Random;

/********************************
 *@author: luibebetter
 *@create: 2018/1/21
 *Description:
 ********************************/
public final class StdRandom {
	private static Random random;
	private static long seed;

	static{
		seed=System.currentTimeMillis();
		random=new Random(seed);
	}

	// Returns a random real number uniformly in [0, 1).
	public static double uniform(){
		return random.nextDouble();
	}

	//generating an random integer in [0,n)
	public static int uniform(int n){
		if (n <= 0) throw new IllegalArgumentException("argument must be positive: " + n);
		return random.nextInt(n);
	}

	////generating an random integer in [m,n)
	public static int uniform(int m, int n){
		if ((n <= m) || ((long) n - m >= Integer.MAX_VALUE)) {
			throw new IllegalArgumentException("invalid range: [" + m + ", " + n + ")");
		}
		return m + uniform(n - m);
	}

	//
	public static boolean bernoulli(double p){
		/********************************
		 * Description:Returns a random boolean from a Bernoulli
		   distribution with success probability <em>p</em>.
		 *
		 * @param p the probabaility of returning true
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		if(p<0.0||p>1.0)  throw new IllegalArgumentException("calls bernoulli() with illegal arguments");
		return uniform()<p;
	}

	//shuffle an integer array
	public static void shuffle(int [] a){
		validateNotNull(a);
		int n=a.length;
		for(int i=0;i<n;i++){
			int t=i+uniform(n-i);
			int tmp=a[t];
			a[t]=a[i];
			a[i]=tmp;
		}
	}

	//shuffle an object array
	public static void shuffle(Object []a){
		validateNotNull(a);
		for (int i = 0; i < a.length; i++) {
			int j=uniform(i+1);
			Object tmp=a[j];
			a[j]=a[i];
			a[i]=tmp;
		}
	}


	/**
	 * Returns a random integer from the specified discrete distribution.
	 *
	 * @param  probabilities the probability of occurrence of each integer
	 * @return a random integer from a discrete distribution:
	 *         {@code i} with probability {@code probabilities[i]}
	 * @throws IllegalArgumentException if {@code probabilities} is {@code null}
	 * @throws IllegalArgumentException if sum of array entries is not (very nearly) equal to {@code 1.0}
	 * @throws IllegalArgumentException unless {@code probabilities[i] >= 0.0} for each index {@code i}
	 */
	public static int discrete(double[] probabilities) {
		if (probabilities == null) throw new IllegalArgumentException("argument array is null");
		double EPSILON = 1E-14;
		double sum = 0.0;
		for (int i = 0; i < probabilities.length; i++) {
			if (!(probabilities[i] >= 0.0))
				throw new IllegalArgumentException("array entry " + i + " must be nonnegative: " + probabilities[i]);
			sum += probabilities[i];
		}
		if (sum > 1.0 + EPSILON || sum < 1.0 - EPSILON)
			throw new IllegalArgumentException("sum of array entries does not approximately equal 1.0: " + sum);

		// the for loop may not return a value when both r is (nearly) 1.0 and when the
		// cumulative sum is less than 1.0 (as a result of floating-point roundoff error)
		while (true) {
			double r = uniform();
			sum = 0.0;
			for (int i = 0; i < probabilities.length; i++) {
				sum = sum + probabilities[i];
				if (sum > r) return i;
			}
		}
	}

	private static void  validateNotNull(Object x){
		if (x == null) {
			throw new IllegalArgumentException("argument is null");
		}
	}
}
