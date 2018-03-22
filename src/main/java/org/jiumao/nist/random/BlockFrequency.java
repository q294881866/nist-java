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
 * 快内频数检测
 * <p>
 *  检测长度为n的二进制数中单位为M的块1出现频率，即n/M个块
 * </p>
 * @author ppf@jiumao.org
 * @date 2018年3月22日
 */
public class BlockFrequency extends BaseRandom {

    /**
     * Random Test Class enumerator name
     */
    protected static final RandomTests TEST = RandomTests.BlockFrequency;

  /**
   * "chiSquared" result
   */
    protected double  chiSquared;
  /**
   * "blockNumber", number of blocks of the stream
   */
    protected int   blockNumber;
  /**
   * "bitsDiscarded", bits discarded from the test, number of bits excluded from the test
   */
    protected int   bitsDiscarded;

  /**
   * Constructor, default 
   */ 
    public BlockFrequency() {

    	super();
      
        this.blockLength = 128;
        this.alpha = 0.0;
        this.pValue = 0.0;
        this.random = false;
        this.error = RandomTestErrors.NoError; 
    }

  /**
   * Constructor with a MathematicalFunctions object instantiated 
   * 
   * @param     mathFuncObj   mathematicalFunctions object that will be used by this object
   */ 
    public BlockFrequency(MathematicalFunctions mathFuncObj) {
      
        super(mathFuncObj);
        super.Initialize();

        this.blockLength = 128;
        this.chiSquared = 0.0;
        this.blockNumber = 0;
        this.bitsDiscarded = 0;
    }

  /**
   * Destructor, zeroes all data
   * 
   */ 
    public void finalize() {
      
        super.Initialize();

        this.chiSquared = 0.0;
        this.blockNumber = 0;
        this.bitsDiscarded = 0;
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
    	int    i, j;
        double blockSum, arg1, arg2;
        double sum, pi, v;

        if (bitStream.GetBitLength() < this.GetMinimumLength()) {
        	this.error = RandomTestErrors.InsufficientNumberOfBits;
        	this.random = false;
        	return this.random;
        }
        bitStream.SetBitPosition(0);
        this.error = RandomTestErrors.NoError;
        this.blockNumber = (int)Math.floor((double)bitStream.GetBitLength()/(double)this.blockLength);  // Number of Stream blocks      
        sum = 0.0;
        for(i = 0; i < this.blockNumber; i++) {  // N=10000 for each substring block
        	pi = 0.0;
        	blockSum = 0.0;
        	bitStream.SetBitPosition(i*this.blockLength);
        	for(j = 0; j < this.blockLength; j++)  // m=100 compute the "i"th Pi Value */   
        		blockSum += bitStream.GetBitForward();   
        	pi = (double)blockSum/(double)this.blockLength;
        	v = pi - 0.5;
        	sum += v*v;
        }
        this.chiSquared = 4.0 * this.blockLength * sum;
        arg1 = (double) this.blockNumber/2.e0;
        arg2 = this.chiSquared/2.e0; 
        this.pValue = this.mathFuncs.IGammaC(arg1,arg2);
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
        this.bitsDiscarded = bitStream.GetBitLength()%this.blockLength;
        return this.random;
   }

  /**
   * Initializes the object
   * 
   */ 
    public void Initialize() {
  
        super.Initialize();
    }

  /**
   * Gets the type of the object
   * 
   * @return    RandomTests:     the concrete type class of the random number test, BlockFrequency test for this class
   */ 
    public RandomTests GetType() {
  
        return BlockFrequency.TEST;
    }

  /**
   * Gets the minimum random stream length
   * 
   * @return    int:    minimum length in bits of streams that can be checked by this test 
   */ 
    public int GetMinimumLength() {
    
        return BlockFrequency.MINIMUMLENGTH;
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
   * Gets the number of blocks tested
   * 
   * @return    int:    CryptoRandomStream number of blocks computed in last random test 
   */ 
    public int GetBlockNumber() {
  
        return this.blockNumber;
    }

  /**
   * Gets the number of bits discarded and not tested with last CryptoRandomStream computed
   * 
   * @return    int:    number of blocks of last CryptoRandomStream computed 
   */ 
    public int GetBitsDiscarded() {
  
        return this.bitsDiscarded;
    }
    
}
