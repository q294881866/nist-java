package org.jiumao.nist.random;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math.random.RandomGenerator;
import org.jiumao.nist.Base.NIST;
import org.jiumao.nist.Base.RandomnessUtils;


/**
 * 压缩检测
 * <P>
 * 本检测主要是看整个序列中不同模式积累的数目（单词数目）。检验目的是判定待测序列能够被压缩到什么程度。若序列不能被明显的压缩，则该序列就是非随机的。一个随机序列有特征数个不同模式。
 * 
 * @author ppf@jiumao.org
 * @date 2018年3月22日
 */
public class LempelZivCompression extends NIST {

    @Override
    public double pValue(final RandomGenerator e, final int n) {
        final Set<Word> words = new HashSet<Word>();
        NumberStrategy number = new LongStrategy(1, 0);
        for (int i = 0; i < n; i++) {
            number.or(e.nextBoolean());
            final Word w = number.toWord();
            number = words.add(w) ? new LongStrategy(1, 0) : number.timesTwo();
        }
        final double mean = 69586.25;
        final double variance = Math.sqrt(448718.70);
        final double pValue = RandomnessUtils.erfc((mean - words.size()) / Math.sqrt(2 * variance)) / 0.5;
        return pValue;
    }

    private static interface NumberStrategy {
        Word toWord();


        void or(boolean bit);


        NumberStrategy timesTwo();
    }

    private static final class BigIntegerStrategy implements NumberStrategy {

        private BigInteger pattern;
        private int length;


        BigIntegerStrategy(final BigInteger initialValue, final int initialLength) {
            pattern = initialValue;
            length = initialLength;
        }


        public Word toWord() {
            return new Word(pattern, length);
        }


        public void or(final boolean bit) {
            if (bit) {
                pattern = pattern.or(BigInteger.ONE);
            }
        }


        public NumberStrategy timesTwo() {
            pattern = pattern.shiftLeft(1);
            length++;
            return this;
        }
    }

    private static final class LongStrategy implements NumberStrategy {

        private long pattern;
        private int length;


        LongStrategy(final long initialValue, final int initialLength) {
            pattern = initialValue;
            length = initialLength;
        }


        public Word toWord() {
            return new Word(pattern, length);
        }


        public void or(final boolean bit) {
            pattern |= bit ? 1 : 0;
        }


        public NumberStrategy timesTwo() {
            if (length > 62) {
                final NumberStrategy s = new BigIntegerStrategy(BigInteger.valueOf(pattern), length);
                s.timesTwo();
                return s;
            }
            pattern <<= 1;
            length++;
            return this;
        }
    }

    private static final class Word {
        private final Number pattern;
        private final int length;


        Word(final Number pattern, final int length) {
            this.pattern = pattern;
            this.length = length;
        }


        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + length;
            result = prime * result + (pattern == null ? 0 : pattern.hashCode());
            return result;
        }


        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Word other = (Word) obj;
            if (length != other.length) {
                return false;
            }
            if (pattern == null) {
                if (other.pattern != null) {
                    return false;
                }
            }
            else if (!pattern.equals(other.pattern)) {
                return false;
            }
            return true;
        }
    }
}
