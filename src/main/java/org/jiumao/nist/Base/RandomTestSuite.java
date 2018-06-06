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

package org.jiumao.nist.Base;

import org.jiumao.nist.CryptoRandomStream.BaseCryptoRandomStream;
import org.jiumao.nist.random.ApproximateEntropy;
import org.jiumao.nist.random.BlockFrequency;
import org.jiumao.nist.random.CumulativeSumForward;
import org.jiumao.nist.random.CumulativeSumReverse;
import org.jiumao.nist.random.DiscreteFourierTransformTest;
import org.jiumao.nist.random.Frequency;
import org.jiumao.nist.random.LongestRunOfOnes;
import org.jiumao.nist.random.Rank;
import org.jiumao.nist.random.Runs;
import org.jiumao.nist.random.Serial;
import org.jiumao.nist.random.Universal;

/**
 * Class implementing random number test suite to verify selected random number tests
 * 
 * @author      Angel Ferré @ DiceLock Security
 * @version     5.0.0.1
 * @since       2011-09-30
 */
public class RandomTestSuite {

   /**
    * Points the first random test in the suite
    */ 
    protected static final RandomTests FIRSTTEST = RandomTests.Frequency;

  /**
   * Random number test suite array holding random number test objects
   */ 
    protected BaseRandom[]         suite = new BaseRandom[RandomTests.NumberOfTests.ordinal()];
  /**
   * Boolean array indicating if the corresponding random number test object of the suite has been created from RandomTestSuite object
   */ 
    protected boolean[]                selfCreatedTest = new boolean[RandomTests.NumberOfTests.ordinal()];
  /**
   * Boolean showing if last computed bit stream was at random 
   */ 
    protected boolean                  random;
  /**
   * MathematicalFunctions object used by random number tests instantiated in the suite
   */ 
    protected MathematicalFunctions    mathFunctions;    
  /**
   * Boolean indicating if the corresponding "mathFunctions" object of the suite has been created from RandomTestSuite object
   */ 
    protected boolean                  selfCreatedMaths;
  /**
   * RandomTestErrors enumeration indicating the error, if any, of last computed bit stream 
   */ 
    protected RandomTestErrors         error;
  /**
   * Number of random number tests instantiated in the suite
   */ 
    protected int                      instantiatedTests;
  /**
   * Number of errors found when testing last computed bit stream 
   */ 
    protected int                      numberOfErrors;
  /**
   * RandomTests enumeration indicating the random number test that produced the error when testing last computed bit stream 
   */ 
    protected RandomTests              errorTest;
  /**
   * RandomTests enumeration indicating the random number test produced the non random property of last computed bit stream 
   */ 
    protected RandomTests              nonRandomTest;

  /**
   * Gets the first random test in the RandomTestSuite
   * 
   * @return    RandomTests:    enumeration value indicating the first random number test in the RandomTestSuite
   */ 
    public RandomTests GetFirstTest() {
            
    	return RandomTestSuite.FIRSTTEST;
    }

  /**
   * Gets the number of random tests that can be used in the RandomTestSuite
   * 
   * @return    RandomTests:    enumeration value indicating the upper enumeration pointing last random number test in the RandomTestSuite
   */ 
    public RandomTests GetMaximumNumberOfTests() {
            
        return RandomTests.NumberOfTests;
    }

  /**
   * Constructor, default, initializes suite and instantiates MathematicalFunctions  
   */ 
    public RandomTestSuite() {
        int i;
        
        for (i = this.GetFirstTest().ordinal(); i < this.GetMaximumNumberOfTests().ordinal(); i++) {
        	this.suite[i] = null;
        	this.selfCreatedTest[i] = false;
        }
        this.random = false;
        mathFunctions = new MathematicalFunctions();
        selfCreatedMaths = true;
        this.error = RandomTestErrors.NoError;
        this.instantiatedTests = 0;
        this.numberOfErrors = 0;
        this.errorTest = RandomTests.NotDefined;
        this.nonRandomTest = RandomTests.NotDefined;
    }

  /**
   * Constructor, initializes suite and assigns MathematicalFunctions parameter
   */ 
    public RandomTestSuite(MathematicalFunctions mathFuncs) {
        int i;

        for (i = this.GetFirstTest().ordinal(); i < this.GetMaximumNumberOfTests().ordinal(); i++) {
        	this.suite[i] = null;
        	this.selfCreatedTest[i] = false;
        }
        this.random = false;
        mathFunctions = mathFuncs;
        selfCreatedMaths = false;
        this.error = RandomTestErrors.NoError;
        this.instantiatedTests = 0;
        this.numberOfErrors = 0;
        this.errorTest = RandomTests.NotDefined;
        this.nonRandomTest = RandomTests.NotDefined;
    }

  /**
   * Destructor
   */ 
    public void finalize() {
    	int i;

        for (i = this.GetFirstTest().ordinal(); i < this.GetMaximumNumberOfTests().ordinal(); i++) {
        	if (this.suite[i] != null) {
        		this.suite[i] = null;
        		this.selfCreatedTest[i] = false;
        	}
        }
        this.random = false;
        if (selfCreatedMaths) {
        	mathFunctions = null;
        }
        this.error = RandomTestErrors.NoError;
        this.instantiatedTests = 0;
        this.numberOfErrors = 0;
        this.errorTest = RandomTests.NotDefined;
        this.nonRandomTest = RandomTests.NotDefined;
    }

    // ADDING RANDOM TESTS
    
  /**
   * Adds a random test to the suite
   * 
   * @param     test    RandomNumberTest object to be inserted in RandomTestSuite
   */ 
    public void Add(BaseRandom test) {

    	if (test != null) {
    		this.suite[test.GetType().ordinal()] = test;
    		this.selfCreatedTest[test.GetType().ordinal()] = false;
    		this.instantiatedTests++;
    	}
    }

  /**
   * Creates and adds a random test to the suite based in the enumerated random tests
   * 
   * @param     test    RandomTests enumeration value indicating the specific random number test to be added to the suite
   */ 
    public void Add(RandomTests test) {
          
    	switch (test) {
    		case Frequency: 
    			if (this.suite[RandomTests.Frequency.ordinal()] == null) {
    				this.suite[RandomTests.Frequency.ordinal()] = new Frequency(this.mathFunctions);
    				this.instantiatedTests++;
    			}
    			break;
    		case BlockFrequency: 
    			if (this.suite[RandomTests.BlockFrequency.ordinal()] == null) {
    				this.suite[RandomTests.BlockFrequency.ordinal()] = new BlockFrequency(this.mathFunctions);
    				this.instantiatedTests++;
    			}
    			break;
    		case Runs: 
    			if (this.suite[RandomTests.Runs.ordinal()] == null) {
    				this.suite[RandomTests.Runs.ordinal()] = new Runs(this.mathFunctions);
    				this.instantiatedTests++;
    			}
    			break;
    		case LongestRunOfOnes: 
    			if (this.suite[RandomTests.LongestRunOfOnes.ordinal()] == null) {
    				this.suite[RandomTests.LongestRunOfOnes.ordinal()] = new LongestRunOfOnes(this.mathFunctions);
    				this.instantiatedTests++;
    			}
    			break;
    		case CumulativeSumForward: 
    			if (this.suite[RandomTests.CumulativeSumForward.ordinal()] == null) {
    				this.suite[RandomTests.CumulativeSumForward.ordinal()] = new CumulativeSumForward(this.mathFunctions);
    				this.instantiatedTests++;
    			}
    			break;
    		case CumulativeSumReverse: 
    			if (this.suite[RandomTests.CumulativeSumReverse.ordinal()] == null) {
    				this.suite[RandomTests.CumulativeSumReverse.ordinal()] = new CumulativeSumReverse(this.mathFunctions);
    				this.instantiatedTests++;
    			}
    			break;
    		case Rank: 
    			if (this.suite[RandomTests.Rank.ordinal()] == null) {
    				this.suite[RandomTests.Rank.ordinal()] = new Rank(this.mathFunctions);
    				this.instantiatedTests++;
    			}
    			break;
    		case Universal: 
    			if (this.suite[RandomTests.Universal.ordinal()] == null) {
    				this.suite[RandomTests.Universal.ordinal()] = new Universal(this.mathFunctions);
    				this.instantiatedTests++;
    			}
    			break;
    		case ApproximateEntropy: 
    			if (this.suite[RandomTests.ApproximateEntropy.ordinal()] == null) {
    				this.suite[RandomTests.ApproximateEntropy.ordinal()] = new ApproximateEntropy(this.mathFunctions);
    				this.instantiatedTests++;
    			}
    			break;
    		case Serial: 
    			if (this.suite[RandomTests.Serial.ordinal()] == null) {
    				this.suite[RandomTests.Serial.ordinal()] = new Serial(this.mathFunctions);
    				this.instantiatedTests++;
    			}
    			break;
    		case DiscreteFourierTransform: 
    			if (this.suite[RandomTests.DiscreteFourierTransform.ordinal()] == null) {
    				this.suite[RandomTests.DiscreteFourierTransform.ordinal()] = new DiscreteFourierTransformTest(this.mathFunctions);
    				this.instantiatedTests++;
    			}
    			break;
    		default:
    			break;
    	}
    	this.selfCreatedTest[test.ordinal()] = true;
    }

  /**
   * Creates and adds all random test to the suite
   * 
   */ 
    public void AddAll() {
    	int i;

    	this.suite[RandomTests.Frequency.ordinal()] = new Frequency(this.mathFunctions);
    	this.suite[RandomTests.BlockFrequency.ordinal()] = new BlockFrequency(this.mathFunctions);
    	this.suite[RandomTests.Runs.ordinal()] = new Runs(this.mathFunctions);
    	this.suite[RandomTests.LongestRunOfOnes.ordinal()] = new LongestRunOfOnes(this.mathFunctions);
    	this.suite[RandomTests.CumulativeSumForward.ordinal()] = new CumulativeSumForward(this.mathFunctions);
    	this.suite[RandomTests.CumulativeSumReverse.ordinal()] = new CumulativeSumReverse(this.mathFunctions);
    	this.suite[RandomTests.Rank.ordinal()] = new Rank(this.mathFunctions);
    	this.suite[RandomTests.Universal.ordinal()] = new Universal(this.mathFunctions);
    	this.suite[RandomTests.ApproximateEntropy.ordinal()] = new ApproximateEntropy(this.mathFunctions);
    	this.suite[RandomTests.Serial.ordinal()] = new Serial(this.mathFunctions);
    	this.suite[RandomTests.DiscreteFourierTransform.ordinal()] = new DiscreteFourierTransformTest(this.mathFunctions);
    	for (i = this.GetFirstTest().ordinal(); i < this.GetMaximumNumberOfTests().ordinal(); i++) {
    		this.selfCreatedTest[i] = true;
    	}
    	this.instantiatedTests = RandomTests.NumberOfTests.ordinal();
    }

  /**
   * Creates and adds Frequency random test to the suite
   * 
   */ 
    public void AddFrequencyTest() {
          
    	this.suite[RandomTests.Frequency.ordinal()] = new Frequency(this.mathFunctions);
    	this.selfCreatedTest[RandomTests.Frequency.ordinal()] = true;
    	this.instantiatedTests++;
    }

  /**
   * Creates and adds Block Frequency random test to the suite
   * 
   */ 
    public void AddBlockFrequencyTest() {
          
    	this.suite[RandomTests.BlockFrequency.ordinal()] = new BlockFrequency(this.mathFunctions);
    	this.selfCreatedTest[RandomTests.BlockFrequency.ordinal()] = true;
    	this.instantiatedTests++;
    } 

  /**
   * Creates and adds Runs random test to the suite
   * 
   */ 
    public void AddRunsTest() {
          
    	this.suite[RandomTests.Runs.ordinal()] = new Runs(this.mathFunctions);
    	this.selfCreatedTest[RandomTests.Runs.ordinal()] = true;
    	this.instantiatedTests++;
    }

  /**
   * Creates and adds Longest Run Of Ones random test to the suite
   * 
   */ 
    public void AddLongestRunOfOnesTest() {
          
    	this.suite[RandomTests.LongestRunOfOnes.ordinal()] = new LongestRunOfOnes(this.mathFunctions);
    	this.selfCreatedTest[RandomTests.LongestRunOfOnes.ordinal()] = true;
    	this.instantiatedTests++;
    }

  /**
   * Creates and adds Cumulative Sum Forward random test to the suite
   * 
   */ 
    public void AddCumulativeSumForwardTest() {
          
    	this.suite[RandomTests.CumulativeSumForward.ordinal()] = new CumulativeSumForward(this.mathFunctions);
    	this.selfCreatedTest[RandomTests.CumulativeSumForward.ordinal()] = true;
    	this.instantiatedTests++;
    }

  /**
   * Creates and adds Cumulative Sum Reverse random test to the suite
   * 
   */ 
    public void AddCumulativeSumReverseTest() {
          
    	this.suite[RandomTests.CumulativeSumReverse.ordinal()] = new CumulativeSumReverse(this.mathFunctions);
    	this.selfCreatedTest[RandomTests.CumulativeSumReverse.ordinal()] = true;
    	this.instantiatedTests++;
    }

  /**
   * Creates and adds Rank random test to the suite
   * 
   */ 
    public void AddRankTest() {
          
    	this.suite[RandomTests.Rank.ordinal()] = new Rank(this.mathFunctions);
    	this.selfCreatedTest[RandomTests.Rank.ordinal()] = true;
    	this.instantiatedTests++;
    }

  /**
   * Creates and adds Universal random test to the suite
   * 
   */ 
    public void AddUniversalTest() {
          
    	this.suite[RandomTests.Universal.ordinal()] = new Universal(this.mathFunctions);
    	this.selfCreatedTest[RandomTests.Universal.ordinal()] = true;
    	this.instantiatedTests++;
    }

  /**
   * Creates and adds Approximate Entropy random test to the suite
   * 
   */ 
    public void AddApproximateEntropyTest() {
          
    	this.suite[RandomTests.ApproximateEntropy.ordinal()] = new ApproximateEntropy(this.mathFunctions);
    	this.selfCreatedTest[RandomTests.ApproximateEntropy.ordinal()] = true;
    	this.instantiatedTests++;
    }

  /**
   * Creates and adds Serial random test to the suite
   * 
   */ 
    public void AddSerialTest() {
          
    	this.suite[RandomTests.Serial.ordinal()] = new Serial(this.mathFunctions);
    	this.selfCreatedTest[RandomTests.Serial.ordinal()] = true;
    	this.instantiatedTests++;
    }

  /**
   * Creates and adds Discrete Fourier Transform random test to the suite
   * 
   */ 
    public void AddDiscreteFourierTransformTest() {
          
    	this.suite[RandomTests.DiscreteFourierTransform.ordinal()] = new DiscreteFourierTransformTest(this.mathFunctions);
    	this.selfCreatedTest[RandomTests.DiscreteFourierTransform.ordinal()] = true;
    	this.instantiatedTests++;
    }

    // GETTING RANDOM TESTS
    
  /**
   * Gets a random test to the suite based in the enumerated random tests
   * 
   * @param     test              RandomTests enumeration value pointing the random number test to be returned
   * @return    BaseRandomTest:   Random number test object selected
   */ 
    public BaseRandom GetRandomTest(RandomTests test) {
          
        return this.suite[test.ordinal()];
    }

  /**
   * Gets Frequency random test of the suite
   * 
   * @return    Frequency     Frequency random test object in the suite
   */ 
    public Frequency GetFrequencyTest() {
          
        return (Frequency)this.suite[RandomTests.Frequency.ordinal()];
    }

  /**
   * Gets Block Frequency random test of the suite
   * 
   * @return    BlockFrequency     Block Frequency random test object in the suite
   */ 
    public BlockFrequency GetBlockFrequencyTest() {
          
        return (BlockFrequency)this.suite[RandomTests.BlockFrequency.ordinal()];
    }

  /**
   * Gets Runs random test of the suite
   * 
   * @return    Runs     Runs random test object in the suite
   */ 
    public Runs GetRunsTest() {
          
        return (Runs)this.suite[RandomTests.Runs.ordinal()];
    }

  /**
   * Gets Longest Run Of Ones random test of the suite
   * 
   * @return    LongestRunOfOnes     Longest Run Of Ones random test object in the suite
   */ 
    public LongestRunOfOnes GetLongestRunOfOnesTest() {
          
    	return (LongestRunOfOnes)this.suite[RandomTests.LongestRunOfOnes.ordinal()];
    }

  /**
   * Gets Cumulative Sum Forward random test of the suite
   * 
   * @return    CumulativeSumForward     Cumulative Sum Forward random test object in the suite
   */ 
    public CumulativeSumForward GetCumulativeSumForwardTest() {
          
        return (CumulativeSumForward)this.suite[RandomTests.CumulativeSumForward.ordinal()];
    }

  /**
   * Gets Cumulative Sum Reverse random test of the suite
   * 
   * @return    CumulativeSumReverse     Cumulative Sum Reverse random test object in the suite
   */ 
    public CumulativeSumReverse GetCumulativeSumReverseTest() {
          
        return (CumulativeSumReverse)this.suite[RandomTests.CumulativeSumReverse.ordinal()];
    }

  /**
   * Gets Rank random test of the suite
   * 
   * @return    Rank     Rank random test object in the suite
   */ 
    public Rank GetRankTest() {
          
        return (Rank)this.suite[RandomTests.Rank.ordinal()];
    }

  /**
   * Gets Universal random test of the suite
   * 
   * @return    Universal     Universal random test object in the suite
   */ 
    public Universal GetUniversalTest() {
          
        return (Universal)this.suite[RandomTests.Universal.ordinal()];
    }

  /**
   * Gets Approximate Entropy random test of the suite
   * 
   * @return    ApproximateEntropy     Approximate Entropy random test object in the suite
   */ 
    public ApproximateEntropy GetApproximateEntropyTest() {
          
        return (ApproximateEntropy)this.suite[RandomTests.ApproximateEntropy.ordinal()];
    }

  /**
   * Gets Serial random test of the suite
   * 
   * @return    Serial     Serial random test object in the suite
   */ 
    public Serial GetSerialTest() {
          
        return (Serial)this.suite[RandomTests.Serial.ordinal()];
    }

  /**
   * Gets Discrete Fourier Transform random test of the suite
   * 
   * @return    DiscreteFourierTransformTest     Discrete Fourier Transform random test object in the suite
   */ 
    public DiscreteFourierTransformTest GetDiscreteFourierTransformTest() {
          
        return (DiscreteFourierTransformTest)this.suite[RandomTests.DiscreteFourierTransform.ordinal()];
    }

    // REMOVING RANDOM TESTS

  /**
   * Removes a random test to the suite
   * 
   * @param     test      Random number test object to be removed from the suite
   */ 
    public void Remove(BaseRandom test) {
        RandomTests randomTest;

        randomTest = test.GetType();
        if ((this.suite[randomTest.ordinal()] != null) && (this.suite[randomTest.ordinal()] == test)) {
        	if (this.selfCreatedTest[randomTest.ordinal()]) {
        		this.suite[randomTest.ordinal()] = null;
        	}
        	this.suite[randomTest.ordinal()] = null;
        	this.selfCreatedTest[randomTest.ordinal()] = false;
        	this.instantiatedTests--;
        }
    }

  /**
   * Removes a random test to the suite based in the enumerated random tests
   * 
   * @param     test      RandomTests enumeration value indicating the random test to be removed from the suite
   */ 
    public void Remove(RandomTests test) {
          
    	if (this.suite[test.ordinal()] != null) {
    		if (this.selfCreatedTest[test.ordinal()]) {
    			this.suite[test.ordinal()] = null;
    		}
    		this.suite[test.ordinal()] = null;
    		this.selfCreatedTest[test.ordinal()] = false;
    		this.instantiatedTests--;
    	}
    }

  /**
   * Removes all random test of the suite
   * 
   */ 
    public void RemoveAll() {
    	int i;

    	for (i = this.GetFirstTest().ordinal(); i < this.GetMaximumNumberOfTests().ordinal(); i++) {
    		if (this.suite[i] != null) {
    			if (this.selfCreatedTest[i]) {
    				this.suite[i] = null;
    			}
    			this.suite[i] = null;
    			this.selfCreatedTest[i] = false;
    		}
    	}
    	this.instantiatedTests = 0;
    }

  /**
   * Removes Frequency random test from the suite
   * 
   */ 
    public void RemoveFrequencyTest() {
          
    	if (this.suite[RandomTests.Frequency.ordinal()] != null) {
    		if (this.selfCreatedTest[RandomTests.Frequency.ordinal()]) {
    			this.suite[RandomTests.Frequency.ordinal()] = null;
    		}
    		this.suite[RandomTests.Frequency.ordinal()] = null;
    		this.selfCreatedTest[RandomTests.Frequency.ordinal()] = false;
    		this.instantiatedTests--;
    	}
    }

  /**
   * Removes Block Frequency random test from the suite
   * 
   */ 
    public void RemoveBlockFrequencyTest() {
          
    	if (this.suite[RandomTests.BlockFrequency.ordinal()] != null) {
    		if (this.selfCreatedTest[RandomTests.BlockFrequency.ordinal()]) {
    			this.suite[RandomTests.BlockFrequency.ordinal()] = null;
    		}
    		this.suite[RandomTests.BlockFrequency.ordinal()] = null;
    		this.selfCreatedTest[RandomTests.BlockFrequency.ordinal()] = false;
    		this.instantiatedTests--;
    	}
    }

  /**
   * Removes Runs random test from the suite
   * 
   */ 
    public void RemoveRunsTest() {
          
    	if (this.suite[RandomTests.Runs.ordinal()] != null) {
    		if (this.selfCreatedTest[RandomTests.Runs.ordinal()]) {
    			this.suite[RandomTests.Runs.ordinal()] = null;
    		}
    		this.suite[RandomTests.Runs.ordinal()] = null;
    		this.selfCreatedTest[RandomTests.Runs.ordinal()] = false;
    		this.instantiatedTests--;
    	}
    }

  /**
   * Removes Longest Run Of Ones random test from the suite
   * 
   */ 
    public void RemoveLongestRunOfOnesTest() {
          
    	if (this.suite[RandomTests.LongestRunOfOnes.ordinal()] != null) {
    		if (this.selfCreatedTest[RandomTests.LongestRunOfOnes.ordinal()]) {
    			this.suite[RandomTests.LongestRunOfOnes.ordinal()]= null;
    		}
    		this.suite[RandomTests.LongestRunOfOnes.ordinal()] = null;
    		this.selfCreatedTest[RandomTests.LongestRunOfOnes.ordinal()] = false;
    		this.instantiatedTests--;
    	}
    }

  /**
   * Removes Cumulative Sum Forward random test from the suite
   * 
   */ 
    public void RemoveCumulativeSumForwardTest() {
          
    	if (this.suite[RandomTests.CumulativeSumForward.ordinal()] != null) {
    		if (this.selfCreatedTest[RandomTests.CumulativeSumForward.ordinal()]) {
    			this.suite[RandomTests.CumulativeSumForward.ordinal()] = null;
    		}
    		this.suite[RandomTests.CumulativeSumForward.ordinal()] = null;
    		this.selfCreatedTest[RandomTests.CumulativeSumForward.ordinal()] = false;
    		this.instantiatedTests--;
    	}
    }

  /**
   * Removes Cumulative Sum Reverse random test from the suite
   * 
   */ 
    public void RemoveCumulativeSumReverseTest() {
          
    	if (this.suite[RandomTests.CumulativeSumReverse.ordinal()] != null) {
    		if (this.selfCreatedTest[RandomTests.CumulativeSumReverse.ordinal()]) {
    			this.suite[RandomTests.CumulativeSumReverse.ordinal()] = null;
    		}
    		this.suite[RandomTests.CumulativeSumReverse.ordinal()] = null;
    		this.selfCreatedTest[RandomTests.CumulativeSumReverse.ordinal()] = false;
    		this.instantiatedTests--;
    	}
    }

  /**
   * Removes Rank random test from the suite
   * 
   */ 
    public void RemoveRankTest() {
          
    	if (this.suite[RandomTests.Rank.ordinal()] != null) {
    		if (this.selfCreatedTest[RandomTests.Rank.ordinal()]) {
    			this.suite[RandomTests.Rank.ordinal()] = null;
    		}
    		this.suite[RandomTests.Rank.ordinal()] = null;
    		this.selfCreatedTest[RandomTests.Rank.ordinal()] = false;
    		this.instantiatedTests--;
    	}
    }

  /**
   * Removes Universal random test from the suite
   * 
   */ 
    public void RemoveUniversalTest() {
          
    	if (this.suite[RandomTests.Universal.ordinal()] != null) {
    		if (this.selfCreatedTest[RandomTests.Universal.ordinal()]) {
    			this.suite[RandomTests.Universal.ordinal()] = null;
    		}
    		this.suite[RandomTests.Universal.ordinal()] = null;
    		this.selfCreatedTest[RandomTests.Universal.ordinal()] = false;
    		this.instantiatedTests--;
    	}
    }

  /**
   * Removes Approximate Entropy random test from the suite
   * 
   */ 
    public void RemoveApproximateEntropyTest() {
          
    	if (this.suite[RandomTests.ApproximateEntropy.ordinal()] != null) {
    		if (this.selfCreatedTest[RandomTests.ApproximateEntropy.ordinal()]) {
    			this.suite[RandomTests.ApproximateEntropy.ordinal()] = null;
    		}
    		this.suite[RandomTests.ApproximateEntropy.ordinal()] = null;
    		this.selfCreatedTest[RandomTests.ApproximateEntropy.ordinal()] = false;
    		this.instantiatedTests--;
    	}
    }

  /**
   * Removes Serial random test from the suite
   * 
   */ 
    public void RemoveSerialTest() {
          
    	if (this.suite[RandomTests.Serial.ordinal()] != null) {
    		if (this.selfCreatedTest[RandomTests.Serial.ordinal()]) {
    			this.suite[RandomTests.Serial.ordinal()] = null;
    		}
    		this.suite[RandomTests.Serial.ordinal()] = null;
    		this.selfCreatedTest[RandomTests.Serial.ordinal()] = false;
    		this.instantiatedTests--;
    	}
    }

  /**
   * Removes Discrete Fourier Transform random test from the suite
   * 
   */ 
    public void RemoveDiscreteFourierTransformTest() {
          
    	if (this.suite[RandomTests.DiscreteFourierTransform.ordinal()] != null) {
    		if (this.selfCreatedTest[RandomTests.DiscreteFourierTransform.ordinal()]) {
    			this.suite[RandomTests.DiscreteFourierTransform.ordinal()] = null;
    		}
    		this.suite[RandomTests.DiscreteFourierTransform.ordinal()] = null;
    		this.selfCreatedTest[RandomTests.DiscreteFourierTransform.ordinal()] = false;
    		this.instantiatedTests--;
    	}
    }

    // CHECKING RANDOMNESS

  /**
   * Tests the BaseCryptoRandomStream untill an error is found with all instantiated random tests and returns the boolean random value
   * 
   * @param     stream      bit stream to be checked for randomness properties
   * @return    boolean:    boolean indicating if "stream" tested presents random properties
   *            true:       "stream" is a randomized bit stream
   *            false:      "stream" is not a randomized bit stream
   */ 
    public boolean IsRandom(BaseCryptoRandomStream stream) {
    	int i;
			
    	this.random = true;
    	i = this.GetFirstTest().ordinal();
    	while ((i < this.GetMaximumNumberOfTests().ordinal()) && (this.random)) {
    		if (this.suite[i] != null) {
    			this.random &= this.suite[i].isRandom(stream);
    			nonRandomTest = this.suite[i].GetType();
    			if (this.suite[i].GetError() != RandomTestErrors.NoError) {
    				errorTest = this.suite[i].GetType();
    				this.numberOfErrors = 1;
    			}
    		}
    		i++;
    	}
    	return this.random;
    }

  /**
   * Tests the BaseCryptoRandomStream with all instantiated random tests and returns the random value
   * 
   * @param     stream      bit stream to be checked for randomness properties
   * @return    boolean:    boolean indicating if "stream" tested presents random properties
   *            true:       "stream" is a randomized bit stream
   *            false:      "stream" is not a randomized bit stream
   */ 
    public boolean TestRandom(BaseCryptoRandomStream stream) {
    	int i;
			
    	this.random = true;
    	for ( i = this.GetFirstTest().ordinal(); i < this.GetMaximumNumberOfTests().ordinal(); i++ ) {
    		if (this.suite[i] != null) {
    			this.random &= this.suite[i].isRandom(stream);
    			if (!(this.suite[i].IsRandom())) {
    				if (this.suite[i].GetError() != RandomTestErrors.NoError) {
    					if (this.error == RandomTestErrors.NoError)
    						this.error = this.suite[i].GetError();
    					else 
    						if (this.error != this.suite[i].GetError())
    							this.error = RandomTestErrors.MultipleErrors;
    					numberOfErrors++;
    				}
    			}
    		}
    	}
    	return this.random;
    }

    // INITIALIZE SUITE
    
  /**
   * Initializes all random tests in the suite
   * 
   */ 
    public void Initialize() {
    	int i;
			
    	for ( i = this.GetFirstTest().ordinal(); i < this.GetMaximumNumberOfTests().ordinal(); i++ ) {
    		if (this.suite[i] != null) {
    			this.suite[i].Initialize();
    		}
    	}
    	this.random = false;
    	this.error = RandomTestErrors.NoError;
    	this.numberOfErrors = 0;
    	this.errorTest = RandomTests.NotDefined;
    }

  /**
   * Sets Alpha all random tests in the suite
   * 
   * @param     alpha     "alpha" confidence parameter to verify randomness 
   */ 
    public void SetAlpha(double alpha) {
    	int i;

    	for (i = 0; i < RandomTests.NumberOfTests.ordinal(); i++)
    		if (this.suite[i] != null)
    			this.suite[i].setAlpha(alpha);
    }

    // GETTING SUITE RESULTS

  /**
   * Gets the RandomTestSuite random state of the last executed BaseCryptoRandomStream
   * 
   * @return    boolean:      returns randomness of last verified bit stream
   */ 
    public boolean IsRandom() {
          
    	return this.random;
    }

  /**
   * Gets the number of Random Tests that contains the suite
   * 
   * @return    int:    number of random number tests instantiated in the suite
   */ 
    public int GetInstantiatedTests() {
          
        return this.instantiatedTests;
    }

  /**
   * Gets the minimum random stream length in bits corresponding
   * to random number test with higher random stream length
   * 
   * @return    int:    length in bits of the shorter bit stream that can be verified with random number tests instantiated in the suite
   */ 
    public int GetMinimumLength() {
    	int minimumMax;

    	minimumMax = 0;
    	for(RandomTests test : RandomTests.values()) {
    		if ( this.Exist(test) ) {
    			if ( this.GetRandomTest(test).GetMinimumLength() > minimumMax ) {
    				minimumMax = this.GetRandomTest(test).GetMinimumLength();
    			}
    		}
    	}
    	return minimumMax;
    }

  /**
   * Gets the corresponding random number test 
   * with higher minimum random stream length in bits 
   * 
   * @return    RandomTests:    enumeration value of the random number test with higher minimum random stream length in bits 
   */ 
    public RandomTests GetMinimumLengthRandomTest() {
    	int minimumMax;
    	RandomTests randomTest;

    	minimumMax = 0;
    	randomTest = RandomTests.NotDefined;
    	for(RandomTests test : RandomTests.values()) {
    		if ( this.Exist(test) ) {
    			if ( this.GetRandomTest(test).GetMinimumLength() > minimumMax ) {
    				minimumMax = this.GetRandomTest(test).GetMinimumLength();
    				randomTest = test;
    			}
    		}
    	}
    	return randomTest;
    }

  /**
   * Gets the random test in the RandomTestSuite that does not verify last bit stream as randomized
   * 
   * @return    RandomTests     enumeration value pointing the failed random test of last verified bit stream
   */ 
    public RandomTests GetNonRandomTest() {
          
    	return this.nonRandomTest;
    }

  /**
   * Gets the RandomTestError error of the last executed bit stream
   * 
   * @return    RandomTestErrors    error of the last verified bit stream
   */ 
    public RandomTestErrors GetError() {
          
        return this.error;
    }

  /**
   * Gets the RandomTest that produced the error of last executed bit stream
   * 
   * @return    RandomTests    enumeration value pointing random number test that produced the error of last executed bit stream
   */ 
    public RandomTests GetErrorTest() {
          
    	return this.errorTest;
    }

  /**
   * Indicates if a random test object exists in the suite
   * 
   * @param     test        RandomTests enumeration value to verify if it has been instantiated in the suite
   * @return    boolean:    indicates if random test is instantiated in the suite
   *            true:       random test instantiated
   *            false:      random test not instantiated
   */ 
    public boolean Exist(RandomTests test) {
          
    	return (this.suite[test.ordinal()] != null);
    }

}
