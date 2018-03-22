
package org.jiumao.nist.random;

import org.jiumao.nist.Base.BaseRandom;
import org.jiumao.nist.Base.MathematicalFunctions;
import org.jiumao.nist.Base.RandomTestErrors;
import org.jiumao.nist.Base.RandomTests;
import org.jiumao.nist.CryptoRandomStream.*;

/**
 * 近似熵检测<P>同序列检验一样，近似熵检验主要看的也是整个序列中所有可能的重叠 m-bit 模式的频率。目的是将两相邻长度(m和m+1)的重叠子块的频数与随机情况下预期的频数相比较。
 * @author ppf@jiumao.org
 * @date 2018年3月22日
 */
public class ApproximateEntropy extends BaseRandom {

  /**
   * Random Test Class enumerator name
   */
    protected static final RandomTests  TEST =  RandomTests.ApproximateEntropy;
  /**
   * Random Test Class minimum stream length
   */
    protected static final int MINIMUMLENGTH = 1024;

  /**
   * "blockLength" length in bits of block division
   */
    protected int     blockLength;
  /**
   * "chiSquared" result
   */
    protected double  chiSquared;
  /**
   * "phi" result
   */
    protected double  phi;
  /**
   * "phiPlusOne" result
   */
    protected double  phiPlusOne;
  /**
   * "apEn" result
   */
    protected double  apEn;
  /**
   * "blockSizeExceeded" result
   * true:  if "blockLength" greater than recommended
   * false: if "blockLength" can be used to verify Approximate Entropy test
   */
    protected boolean blockSizeExceeded;    
  /**
   * "maximumBlockSizeRecommended" based on stream length in bits
   */
    protected int     maximumBlockSizeRecommended;   

  /**
   * Constructor, default 
   */ 
    public ApproximateEntropy() {

    	super();

        this.blockLength = 10;
        this.chiSquared = 0.0;
        this.phi = 0.0;
        this.phiPlusOne = 0.0;
        this.apEn = 0.0;
        this.blockSizeExceeded = false;    
        this.maximumBlockSizeRecommended = 0;   
    }

  /**
   * Constructor with a MathematicalFunctions object instantiated 
   * 
   * @param     mathFuncObj   mathematicalFunctions object that will be used by this object
   */ 
    public ApproximateEntropy(MathematicalFunctions mathFuncObj) {
    
        super(mathFuncObj);
		
        this.blockLength = 10;
		this.chiSquared = 0.0;
		this.phi = 0.0;
		this.phiPlusOne = 0.0;
		this.apEn = 0.0;
		this.blockSizeExceeded = false;		
		this.maximumBlockSizeRecommended = 0;	 
    }

  /**
   * Destructor, zeroes all data
   * 
   */ 
    public void finalize() {
    
		this.blockLength = 0;
		this.chiSquared = 0.0;
		this.phi = 0.0;
		this.phiPlusOne = 0.0;
		this.apEn = 0.0;
		this.blockSizeExceeded = false;		
		this.maximumBlockSizeRecommended = 0;	 
    }

  /**
   * Gets the BaseRandomTest random state of the last executed BaseCryptoRandomStream
   * 
   * @return    boolean indication if last computed CryptoRandomStream was a randomized stream
   *            true:   last verified stream was randomized
   *            false:  last verified stream was not randomized
   */ 
    public boolean IsRandom() {
    
		return super.IsRandom();
    }

  /**
   * Tests the BaseCryptoRandomStream executed and returns the random value
   * 
   * @param     bitStream   bitStream to be verified for randomness properties
   * @return    boolean     indication if CryptoRandomStream is a randomized stream
   *            true:       last verified stream was randomized
   *            false:      last verified stream was not randomized
   */ 
    public boolean isRandom(BaseCryptoRandomStream bitStream) {
    	int     i, j, k, r, blockSize;
		int     powLen, index;
        double  sum, numOfBlocks;
		double  ApEn[];
		int     P[];

        ApEn = new double[2];
		if ( bitStream.GetBitLength() < this.GetMinimumLength() ) {
			this.error = RandomTestErrors.InsufficientNumberOfBits;
			this.random = false;
			return this.random;
		}
		this.error = RandomTestErrors.NoError;
		if (this.blockLength > (int)this.MaximumBlockSizeRecommended(bitStream.GetBitLength())) {
			this.blockSizeExceeded = true;
			this.maximumBlockSizeRecommended = this.MaximumBlockSizeRecommended(bitStream.GetBitLength());
			this.error = RandomTestErrors.ResultsInaccurate;
			this.random = false;
			return this.random ;
        } 
        r = 0;
        for(blockSize = this.blockLength; blockSize <= this.blockLength+1; blockSize++) {
        	if (blockSize == 0) {
        		ApEn[0] = 0.00;
        		r++;
        	}
        	else {
        		numOfBlocks = bitStream.GetBitLength();
        		powLen = (int)Math.pow(2, blockSize+1)-1;
        		P = new int[powLen];
        		if ( P == null ){
        			this.error = RandomTestErrors.InsufficientMemory;
        			this.random = false;
        			return this.random;
        		}
        		for(i = 1; i < powLen-1; i++) P[i] = 0;
        		for(i = 0; i < numOfBlocks; i++) { 
        			k = 1;
        			for(j = 0; j < blockSize; j++) {
        				if (bitStream.GetBitPosition((i+j)%bitStream.GetBitLength()) == 0)
        					k *= 2;
        				else if (bitStream.GetBitPosition((i+j)%bitStream.GetBitLength()) == 1)
        					k = 2*k+1;
        			}
        			P[k-1]++;
        		}
        		sum = 0.0;
        		index = (int)Math.pow(2,blockSize)-1;
        		for(i = 0; i < (int)Math.pow(2,blockSize); i++) {
        			if (P[index] > 0) sum += P[index]*Math.log(P[index]/numOfBlocks);
        			index++;
        		}
        		sum /= numOfBlocks;
        		ApEn[r] = sum;
        		r++;
        		P = null;
        	}
        }
        this.apEn = ApEn[0] - ApEn[1];
		this.phi = ApEn[0];
		this.phiPlusOne = ApEn[1];
		this.chiSquared = 2.0*bitStream.GetBitLength()*(Math.log(2) - this.apEn);
		this.pValue = this.mathFuncs.IGammaC(Math.pow(2,this.blockLength-1),this.chiSquared/2.);
		if (Double.isNaN(this.pValue)) {
			this.pValue = 0;
			this.error = RandomTestErrors.MathematicianNAN;
			this.random = false;
		}
		else {
			if (this.pValue < this.alpha) {
				this.random = false;
			}
			else {
				this.random = true;
			}
		}
		return this.random;
    }

  /**
   * Initializes the object
   * 
   */ 
    public void Initialize() {
    
		super.Initialize();
		
		this.chiSquared = 0.0;
		this.phi = 0.0;
		this.phiPlusOne = 0.0;
		this.apEn = 0.0;
		this.blockSizeExceeded = false;		
		this.maximumBlockSizeRecommended = 0;	 
    }

  /**
   * Gets the type of the object
   * 
   * @return    RandomTests:     the concrete type class of the random number test, ApproximateEntroy test for this class
   */ 
    public RandomTests GetType() {
    
		return ApproximateEntropy.TEST;
    }

  /**
   * Gets the minimum random stream length
   * 
   * @return    int:    minimum length in bits of streams that can be checked by this test 
   */ 
    public int GetMinimumLength() {
    
    	return ApproximateEntropy.MINIMUMLENGTH;
    }

  /**
   * Sets the "m" parameter, block length in bits
   * 
   * @param     blockLength    sets the block length in bits that Approximate Entropy test will use for checking randomness
   */ 
    public void SetBlockLength(int blockLength) {
    
		this.blockLength = blockLength;
    }

  /**
   * Gets the "m" parameter, block length in bits
   * 
   * @return    int:    gets the block length that Approximate Entropy test is using for checking randomness
   */ 
    public int GetBlockLength() {
    
		return this.blockLength;
    }

  /**
   * Gets the "ChiSquared" result
   * 
   * @return    double:    gets the "chi squared" result of last random test computed
   */ 
    public double GetChiSquared() {
    
    	return this.chiSquared;
    }

  /**
   * Gets the "Phi" result
   * 
   * @return    double:    gets the "phi" result of last random test computed
   */ 
    public double GetPhi() {
    
    	return this.phi;
    }

  /**
   * Gets the "PhiPlusOne" result
   * 
   * @return    double:    gets the "phi plus one" result of last random test computed
   */ 
    public double GetPhiPlusOne() {
    
    	return this.phiPlusOne;
    }

  /**
   * Gets the "apEn" result
   * 
   * @return    double:    gets the "ApEn" result of last random test computed
   */ 
    public double GetApEn() {
    
    	return this.apEn;
    }

  /**
   * Gets the "BlockSizeExceeded" result
   * 
   * @return    boolean:    gets if block size has been exceeded
   */ 
    public boolean GetBlockSizeExceeded() {
    
    	return this.blockSizeExceeded;
    }

  /**
   * Gets the "BlockSizeRecommended" result
   * 
   * @return    int:      gets maximum block size recommended to perform random number test
   */ 
    public int GetMaximumBlockSizeRecommended() {
    
    	return this.maximumBlockSizeRecommended;
    }   

  /**
   * Gets the "BlockSizeRecommended" for the indicated stream length
   * 
   * @param     length  length in bits of the stream for which to perform randomness checking
   * @return    int:    gets maximum block size recommended according to length 
   */ 
    public int MaximumBlockSizeRecommended(int length) {
    
        return (int)this.mathFuncs.max(1,(Math.log(length)/Math.log(2)-2));
    }

}
