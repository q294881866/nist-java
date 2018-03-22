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
 * 频率检测
 * <p>
 * 检测二进制0/1所占比率接近一半
 * </p>
 * 
 * @author ppf@jiumao.org
 * @date 2018年3月22日
 */
public class Frequency extends BaseRandom {

    /**
     * Random Test Class enumerator name
     */
    protected static final RandomTests TEST = RandomTests.Frequency;
    /**
     * Random Test Class minimum stream length
     */
    protected static final int MINIMUMLENGTH = 100;

    /**
     * "sum" result
     */
    protected int sum;
    /**
     * "sumDiv_n" result
     */
    protected double sumDiv_n;


    /**
     * Constructor, default
     */
    public Frequency() {

        super();

        this.sum = 0;
        this.sumDiv_n = 0.0;
    }


    /**
     * Constructor with a MathematicalFunctions object instantiated
     * 
     * @param mathFuncObj mathematicalFunctions object that will be used by this
     *            object
     */
    public Frequency(MathematicalFunctions mathFuncObj) {

        super(mathFuncObj);

        this.sum = 0;
        this.sumDiv_n = 0.0;
    }


    /**
     * Destructor, zeroes all data
     * 
     */
    public void finalize() {

        super.finalize();
        this.sum = 0;
        this.sumDiv_n = 0.0;
    }


    /**
     * Gets the BaseRandomTest random state of the last executed
     * BaseCryptoRandomStream
     * 
     * @return boolean indication if last computed CryptoRandomStream was a
     *         randomized stream true: last verified stream was randomized
     *         false: last verified stream was not randomized
     */
    public boolean IsRandom() {

        return super.IsRandom();
    }


    /**
     * Tests the BaseCryptoRandomStream executed and returns the random value
     * 
     * @param bitStream bitStream to be verified for randomness properties
     * @return boolean indication if CryptoRandomStream is a randomized stream
     *         true: last verified stream was randomized false: last verified
     *         stream was not randomized
     */
    public boolean isRandom(BaseCryptoRandomStream bitStream) {
        int i;
        double f, s_obs, sum;
        double sqrt2 = 1.41421356237309504880;
        short bitTemp;

        if (bitStream.GetBitLength() < this.GetMinimumLength()) {
            this.error = RandomTestErrors.InsufficientNumberOfBits;
            this.random = false;
            return this.random;
        }
        bitStream.SetBitPosition(0);
        this.error = RandomTestErrors.NoError;
        sum = 0.0;
        for (i = 0; i < bitStream.GetBitLength(); i++) {
            bitTemp = (bitStream.GetBitPosition(i));
            sum += ((2 * bitTemp) - 1);
        }
        s_obs = Math.abs(sum) / Math.sqrt(bitStream.GetBitLength());
        f = s_obs / sqrt2;
        this.pValue = this.mathFuncs.ErFc(f);
        if (this.pValue < this.getAlpha()) {
            this.random = false;
        }
        else {
            this.random = true;
        }
        this.sum = (int) sum;
        this.sumDiv_n = sum / bitStream.GetBitLength();
        return this.random;
    }


    /**
     * Initializes the object
     * 
     */
    public void Initialize() {

        super.Initialize();
        this.sum = 0;
        this.sumDiv_n = 0.0;
    }


    /**
     * Gets the type of the object
     * 
     * @return RandomTests: the concrete type class of the random number test,
     *         Frequency test for this class
     */
    public RandomTests GetType() {

        return Frequency.TEST;
    }


    /**
     * Gets the minimum random stream length
     * 
     * @return int: minimum length in bits of streams that can be checked by
     *         this test
     */
    public int GetMinimumLength() {

        return Frequency.MINIMUMLENGTH;
    }


    /**
     * Gets the "sum" result
     * 
     * @return int: "sum" result of last computed CryptoRandomStream
     */
    public int GetSum() {

        return this.sum;
    }


    /**
     * Gets the "sumDiv_n" result
     * 
     * @return double: "sumDiv_n" result of last computed CryptoRandomStream
     */
    public double GetSumDiv_n() {

        return this.sumDiv_n;
    }

}
