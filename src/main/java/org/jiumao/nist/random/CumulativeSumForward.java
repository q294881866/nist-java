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
 * 累积和测试(CumulativeSums)-Forward<p>
 * @author ppf@jiumao.org
 * @date 2018年3月22日
 */
public class CumulativeSumForward extends BaseRandom {

  /**
   * Random Test Class enumerator name
   */
    protected static final RandomTests  TEST = RandomTests.CumulativeSumForward;
  /**
   * Random Test Class minimum stream length
   */
    protected static final int MINIMUMLENGTH = 100;

  /**
   * "cuSum" result
   */
    protected double cuSum;

  /**
   * Constructor, default 
   */ 
    public CumulativeSumForward() {

    	super();

        this.cuSum = 0;
    }

  /**
   * Constructor with a MathematicalFunctions object instantiated 
   * 
   * @param     mathFuncObj   mathematicalFunctions object that will be used by this object
   */ 
    public CumulativeSumForward(MathematicalFunctions mathFuncObj) {
      
    	super(mathFuncObj);

    	this.cuSum = 0;
    }

  /**
   * Destructor, zeroes all data
   * 
   */ 
    public void finalize() {
      
    	this.cuSum = 0;
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
    	int    i, k, start, finish;
        double z, sum, sum1, sum2;
      
        if (bitStream.GetBitLength() < this.GetMinimumLength()) {
        	this.error = RandomTestErrors.InsufficientNumberOfBits;
        	this.random = false;
        	return this.random;
        }
        bitStream.SetBitPosition(0);
        this.error = RandomTestErrors.NoError;
        sum = 0.0;
        this.cuSum = 1;
        for(i = 0; i < (long)bitStream.GetBitLength(); i++) {
        	sum += 2*(int)bitStream.GetBitForward() - 1;
        	this.cuSum = this.mathFuncs.max(this.cuSum, Math.abs(sum));
        }
        z = this.cuSum;
        sum1 = 0.0;
        start = (-(int)bitStream.GetBitLength()/(int)z+1)/4;
        finish = (bitStream.GetBitLength()/(int)z-1)/4;
        for(k = start; k <= finish; k++)
        	sum1 += (this.mathFuncs.Normal((4*k+1)*z/Math.sqrt(bitStream.GetBitLength()))-this.mathFuncs.Normal((4*k-1)*z/Math.sqrt(bitStream.GetBitLength())));
        sum2 = 0.0;
        start = (-(int)bitStream.GetBitLength()/(int)z-3)/4;
        finish = (bitStream.GetBitLength()/(int)z-1)/4;
        for(k = start; k <= finish; k++)
        	sum2 += (this.mathFuncs.Normal((4*k+3)*z/Math.sqrt(bitStream.GetBitLength()))-this.mathFuncs.Normal((4*k+1)*z/Math.sqrt(bitStream.GetBitLength())));
        this.pValue = 1.0 - sum1 + sum2;
        if (this.mathFuncs.isNegative(this.pValue) || this.mathFuncs.isGreaterThanOne(this.pValue)) {
        	this.error = RandomTestErrors.PValueOutOfRange;
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
        this.cuSum = 0;
    }

  /**
   * Gets the type of the object
   * 
   * @return    RandomTests:     the concrete type class of the random number test, CumulativeSumForward test for this class
   */ 
    public RandomTests GetType() {
      
    	return CumulativeSumForward.TEST;
    }

  /**
   * Gets the minimum random stream length
   * 
   * @return    int:    minimum length in bits of streams that can be checked by this test 
   */ 
    public int GetMinimumLength() {
      
    	return CumulativeSumForward.MINIMUMLENGTH;
    }

  /**
   * Gets the maximum partial sum
   * 
   * @return    double:  maximum partial sum
   */ 
    public double GetCuSum() {
      
    	return this.cuSum;
    }

}
