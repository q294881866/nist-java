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
 * 游程检测
 * <P>
 * 检测连续k位同为1/0的变化程度: <em>例如：</em>11001100相等于1010变化序列
 * 
 * @author ppf@jiumao.org
 * @date 2018年3月22日
 */
public class Runs extends BaseRandom {

    /**
     * Random Test Class enumerator name
     */
    protected static final RandomTests TEST = RandomTests.Runs;
    /**
     * Random Test Class minimum stream length
     */
    protected static final int MINIMUMLENGTH = 100;

    /**
     * "pi" result
     */
    protected double pi;
    /**
     * "totalNumberRuns" result, total number of runs
     */
    protected double totalNumberRuns;
    /**
     * "argument" result, parameter for error function in double precision
     */
    protected double argument;


    /**
     * Constructor, default
     */
    public Runs() {

        super();

        this.pi = 0.0;
        this.totalNumberRuns = 0.0;
        this.argument = 0.0;
    }


    /**
     * Constructor with a MathematicalFunctions object instantiated
     * 
     * @param mathFuncObj mathematicalFunctions object that will be used by this
     *            object
     */
    public Runs(MathematicalFunctions mathFuncObj) {

        super(mathFuncObj);

        this.pi = 0.0;
        this.totalNumberRuns = 0.0;
        this.argument = 0.0;
    }


    /**
     * Destructor, zeroes all data
     * 
     */
    public void finalize() {

        this.pi = 0.0;
        this.totalNumberRuns = 0.0;
        this.argument = 0.0;
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
        int i, r[];
        double product, sum;

        if (bitStream.GetBitLength() < this.GetMinimumLength()) {
            this.error = RandomTestErrors.InsufficientNumberOfBits;
            this.random = false;
            return this.random;
        }
        bitStream.SetBitPosition(0);
        this.error = RandomTestErrors.NoError;
        r = new int[bitStream.GetBitLength()];
        if (r == null) {
            this.error = RandomTestErrors.InsufficientMemory;
            this.random = false;
            return this.random;
        }
        sum = 0.0;
        for (i = 0; i < bitStream.GetBitLength(); i++)
            sum += bitStream.GetBitForward();
        this.pi = sum / bitStream.GetBitLength();
        for (i = 0; i < bitStream.GetBitLength() - 1; i++) {
            if (bitStream.GetBitPosition(i) == bitStream.GetBitPosition(i + 1))
                r[i] = 0;
            else
                r[i] = 1;
        }
        this.totalNumberRuns = 0;
        for (i = 0; i < bitStream.GetBitLength() - 1; i++)
            this.totalNumberRuns += r[i];
        this.totalNumberRuns++;
        product = this.pi * (1.e0 - this.pi);
        this.argument = Math.abs(this.totalNumberRuns - 2.e0 * bitStream.GetBitLength() * product)
                / (2.e0 * Math.sqrt(2.e0 * bitStream.GetBitLength()) * product);
        this.pValue = this.mathFuncs.ErFc(this.argument);
        if (this.pValue < this.alpha) {
            this.random = false;
        }
        else {
            this.random = true;
        }
        r = null;
        if (this.mathFuncs.isNegative(this.pValue) || this.mathFuncs.isGreaterThanOne(this.pValue)) {
            this.random = false;
            this.error = RandomTestErrors.PValueOutOfRange;
        }
        return this.random;
    }


    /**
     * Initializes the object
     * 
     */
    public void Initialize() {

        super.Initialize();
        this.pi = 0.0;
        this.totalNumberRuns = 0.0;
        this.argument = 0.0;
    }


    /**
     * Gets the type of the object
     * 
     * @return RandomTests: the concrete type class of the random number test,
     *         Runs test for this class
     */
    public RandomTests GetType() {

        return Runs.TEST;
    }


    /**
     * Gets the minimum random stream length
     * 
     * @return int: minimum length in bits of streams that can be checked by
     *         this test
     */
    public int GetMinimumLength() {

        return Runs.MINIMUMLENGTH;
    }


    /**
     * Gets the "pi" result
     * 
     * @return double: ·"pi" result
     */
    public double GetPi() {

        return this.pi;
    }


    /**
     * Gets the total number of runs result
     * 
     * @return double: ·gets total number of runs
     */
    public double GetTotalNumberRuns() {

        return this.totalNumberRuns;
    }


    /**
     * Gets the argument result
     * 
     * @return double: ·gets argument parameter of error function in double
     *         precision
     */
    public double GetArgument() {

        return this.argument;
    }

}
