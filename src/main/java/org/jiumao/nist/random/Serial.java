//
// Creator:    http://www.dicelocksecurity.com
// Version:    vers.5.0.0.1
//
// Copyright 2011 DiceLock Security, LLC. All rights reserved.
//
//                               DISCLAIMER
//
// THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
// AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
// PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
// OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
// WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
// OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
// ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
// 
// DICELOCK IS A REGISTERED TRADEMARK OR TRADEMARK OF THE OWNERS.
// 
// Environment:
// java version "1.6.0_29"
// Java(TM) SE Runtime Environment (build 1.6.0_29-b11)
// Java HotSpot(TM) Server VM (build 20.4-b02, mixed mode)
// 

package org.jiumao.nist.random;

import org.jiumao.nist.Base.BaseRandom;
import org.jiumao.nist.Base.MathematicalFunctions;
import org.jiumao.nist.Base.RandomTestErrors;
import org.jiumao.nist.Base.RandomTests;
import org.jiumao.nist.CryptoRandomStream.BaseCryptoRandomStream;

/**
 * 串行测试<P>
 * @author ppf@jiumao.org
 * @date 2018年3月22日
 */
public class Serial extends BaseRandom {

  /**
   * Random Test Class enumerator name
   */
    protected static final RandomTests  TEST = RandomTests.Serial;
  /**
   * Random Test Class minimum stream length
   */
    protected static final int MINIMUMLENGTH = 128;

  /**
   * "blockLentgh" parameter, block length in bits for serial test 
   */
    protected int     blockLength;
  /**
   * "pvalue2" parameter, confidence level for randomness based on delta2 result 
   */
    protected double  pvalue2;        
  /**
   * "psim" result
   */
    protected double  psim; 
  /**
   * "psim1" result
   */
    protected double  psim1; 
  /**
   * "psim2" result
   */
    protected double  psim2; 
  /**
   * "delta1" result, base to check "pValue" confidence level
   */
    protected double  delta1; 
  /**
   * "delta2" result, base to check "pvalue2" confidence level
   */
    protected double  delta2;

  /**
   * Calculates Psi2 function of bitStream CryptoRandomStream based on m, blocklength, parameter
   * 
   * @param   m           block length in bits
   * @param   bitStream   CryptoRandomStream to calculate Psi2 on
   * @return  double:     Psi2 calculation value
   */
    protected double psi2(int m, BaseCryptoRandomStream bitStream) {
    	int     i, j, k, powLen;
    	double  sum, numOfBlocks;
    	int     P[];
      
    	if ((m == 0) || (m == -1)) 
    		return 0.0;
    	numOfBlocks = bitStream.GetBitLength();
    	powLen = (int)Math.pow(2,m+1)-1;
    	P = new int[powLen];
    	if (P == null) {
    		this.error = RandomTestErrors.InsufficientMemory;
    		return 0.0;
    	}
    	for(i = 1; i < powLen-1; i++) 
    		P[i] = 0;
    	for(i = 0; i < numOfBlocks; i++) {     
    		k = 1;
    		for(j = 0; j < m; j++) {
    			if (bitStream.GetBitPosition((i+j)%bitStream.GetBitLength()) == 0)
    				k *= 2;
    			else if (bitStream.GetBitPosition((i+j)%bitStream.GetBitLength()) == 1)
    				k = 2*k+1;
    		}
    		P[k-1]++;
    	}
    	sum = 0.0;
    	for(i = (int)Math.pow(2,m)-1; i < (int)Math.pow(2,m+1)-1; i++)
    		sum += Math.pow(P[i],2);
    	sum = (sum * Math.pow(2,m)/(double)bitStream.GetBitLength()) - (double)bitStream.GetBitLength();
    	P = null;
    	return sum;
    }

  /**
   * Constructor, default 
   */ 
    public Serial() {

    	super();

        this.blockLength = 16;
		this.pvalue2 = 0.0;				
		this.psim = 0.0; 
		this.psim1 = 0.0; 
		this.psim2 = 0.0; 
		this.delta1 = 0.0; 
		this.delta2 = 0.0;
    }

  /**
   * Constructor with a MathematicalFunctions object instantiated 
   * 
   * @param     mathFuncObj   mathematicalFunctions object that will be used by this object
   */ 
    public Serial(MathematicalFunctions mathFuncObj) {
                          
        super(mathFuncObj);
        
		this.blockLength = 16;
		this.pvalue2 = 0.0;				
		this.psim = 0.0; 
		this.psim1 = 0.0; 
		this.psim2 = 0.0; 
		this.delta1 = 0.0; 
		this.delta2 = 0.0;
    }

  /**
   * Destructor, zeroes all data
   * 
   */ 
    public void finalize() {
                                                               
		this.blockLength = 0;
		this.pvalue2 = 0.0;				
		this.psim = 0.0; 
		this.psim1 = 0.0; 
		this.psim2 = 0.0; 
		this.delta1 = 0.0; 
		this.delta2 = 0.0;
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
                                                               
    	if (bitStream.GetBitLength() < (long)this.GetMinimumLength()) {
    		this.error = RandomTestErrors.InsufficientNumberOfBits;
    		this.random = false;
    		return this.random;
    	}
    	this.error = RandomTestErrors.NoError;
    	this.psim = psi2(this.blockLength, bitStream);
    	this.psim1 = psi2(this.blockLength-1, bitStream);
    	this.psim2 = psi2(this.blockLength-2, bitStream);
    	this.delta1 = this.psim - this.psim1;
    	this.delta2 = this.psim - 2.0*this.psim1 + this.psim2;
    	this.pValue = this.mathFuncs.IGammaC(Math.pow(2,this.blockLength-1)/2,this.delta1/2.0);
    	if (Double.isNaN(this.pValue)) {
    		this.pValue = 0;
    		this.error = RandomTestErrors.MathematicianNAN;
    		this.random = false;
    		return this.random;
    	}
    	this.pvalue2 = this.mathFuncs.IGammaC(Math.pow(2,this.blockLength-2)/2,this.delta2/2.0);
    	if (Double.isNaN(this.pvalue2)) {
    		this.pvalue2 = 0;
    		this.error = RandomTestErrors.MathematicianNAN;
    		this.random = false;
    		return this.random;
    	}
    	if (this.pValue < this.alpha) {
    		this.random = false;
    	}
    	else {
    		this.random = true;
}
    	if (this.pvalue2 < this.alpha) {
    		this.random = false;
    	}
    	return this.random;
    }

  /**
   * Initializes the object
   * 
   */ 
    public void Initialize() {
                                                               
    	super.Initialize();
		this.pvalue2 = 0.0;				
		this.psim = 0.0; 
		this.psim1 = 0.0; 
		this.psim2 = 0.0; 
		this.delta1 = 0.0; 
		this.delta2 = 0.0;
    }

  /**
   * Gets the type of the object
   * 
   * @return    RandomTests:     the concrete type class of the random number test, Serial test for this class
   */ 
    public RandomTests GetType() {
                                                               
    	return Serial.TEST;
    }

  /**
   * Gets the minimum random stream length
   * 
   * @return    int:    minimum length in bits of streams that can be checked by this test 
   */ 
    public int GetMinimumLength() {
                                                               
    	return Serial.MINIMUMLENGTH;
    }

  /**
   * Sets "blockLength" parameter
   * 
   * @param     block    sets the block length in bits that Serial test will use for checking randomness
   */ 
    public void SetBlockLength(int block) {
                                                               
    	this.blockLength = block;
    }

  /**
   * Gets "block length" parameter, block length in bits
   * 
   * @return    int:    gets the block length that Serial test is using for checking randomness
   */ 
    public int GetBlockLength() {
                                                               
    	return this.blockLength;
    }

  /**
   * Gets pvalue2 parameter
   * 
   * @return    double:   second pValue confidence level
   */
    public double GetPValue2() {
                                                               
    	return this.pvalue2;
    }

  /**
   * Gets "psim" result  
   * 
   * @return    double:   psim intermediate result
   */
    public double GetPsim() {
                                                               
    	return this.psim;
    }

  /**
   * Gets "psim1" result  
   * 
   * @return    double:   psim1 intermediate result to evaluate pValue
   */
    public double GetPsim1() {
                                                               
    	return this.psim1;
    } 

  /**
   * Gets "psim2" result  
   * 
   * @return    double:   psim2 intermediate result to evaluate pvalue2
   */
    public double GetPsim2() {
                                                               
    	return this.psim2;
    } 

  /**
   * Gets the "delta1" result  
   * 
   * @return    double:   delta1 intermediate result to evaluate pValue
   */
    public double GetDelta1() {
                                                               
    	return this.delta1;
    } 

  /**
   * Gets the "delta2" result  
   * 
   * @return    double:   delta2 intermediate result to evaluate pvalue2
   */
    public double GetDelta2() {
                                                               
    	return this.delta2;
    }

  /**
   * Gets the "BlockSizeRecommended" for the indicated stream length
   * 
   * @param     length    integer size of bit stream in bits 
   * @return    int:      integer recomending the block size, length, parameter for the bit stream length
   */
    public int MaximumBlockSizeRecommended(int length) {
                                                               
        return (int)this.mathFuncs.max(1,(Math.log(length)/Math.log(2)-2));
    }

}
