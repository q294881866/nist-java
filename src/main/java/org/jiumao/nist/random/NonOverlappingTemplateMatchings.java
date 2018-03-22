package org.jiumao.nist.random;

import org.jiumao.nist.Base.BaseRandom;
import org.jiumao.nist.Base.RandomTests;
import org.jiumao.nist.CryptoRandomStream.BaseCryptoRandomStream;


/**
 * 非重叠模块匹配检验(字符串匹配算法)
 * <p>
 * 此检测主要是看提前设置好的目标数据串发生地次数。目的是探测那些产生太多给出的非周期模式的发生器。对于非重叠模块匹配检验以及后面会谈到的重叠模块匹配检验方法，我们都是使用一个 m-bit 的窗口来搜素一个特定的 m-bit 模式。如果这个模式没有被找到，则窗口向后移动一位。如果模式被发现，则窗口移动到一发现的模式的后一位，重复前面的步骤继续搜素下一个模式。
 * 
 * @author ppf@jiumao.org
 * @date 2018年3月22日
 */
public class NonOverlappingTemplateMatchings extends BaseRandom {

    int numOfTemplates[] = { 0, 0, 2, 4, 6, 12, 20, 40, 74, 148, 284, 568, 1116, //
                             2232, 4424, 8848, 17622, 35244, 70340, 140680, 281076, 562152 };
    static final int N = 8;


    /**
     * NOTE: Should additional templates lengths beyond 21 be desired, they must
     * first be constructed, saved into files and then the corresponding number
     * of nonperiodic templates for that file be stored in the m-th position in
     * the numOfTemplates variable.
     */
    @Override
    public boolean isRandom(BaseCryptoRandomStream stream) {
        int n = this.GetBlockLength(), m = stream.GetBitLength();
        int W_obs, nu[] = new int[6], Wj[] = new int[n];
        double sum, chi2, p_value, lambda, pi[] = new double[6], varWj;
        int i, j, jj, k, match, SKIP, K = 5;

        int[] sequence = new int[m];
        int M = n / N;

        lambda = (M - m + 1) / Math.pow(2, m);
        varWj = M * (1.0 / Math.pow(2.0, m) - (2.0 * m - 1.0) / Math.pow(2.0, 2.0 * m));

        if (((mathFuncs.isNegative(lambda)) || (mathFuncs.isZero(lambda)))) {
            System.out.println("erro");
        }
        else {

            if (numOfTemplates[m] < MAXNUMOFTEMPLATES)
                SKIP = 1;
            else
                SKIP = (int) (numOfTemplates[m] / MAXNUMOFTEMPLATES);
            numOfTemplates[m] = (int) numOfTemplates[m] / SKIP;

            sum = 0.0;
            for (i = 0; i < 2; i++) { /* Compute Probabilities */
                pi[i] = Math.exp(-lambda + i * Math.log(lambda) - mathFuncs.LGamma(i + 1));
                sum += pi[i];
            }
            pi[0] = sum;
            for (i = 2; i <= K; i++) { /* Compute Probabilities */
                pi[i - 1] = Math.exp(-lambda + i * Math.log(lambda) - mathFuncs.LGamma(i + 1));
                sum += pi[i - 1];
            }
            pi[K] = 1 - sum;

            for (jj = 0; jj < Math.min(MAXNUMOFTEMPLATES, numOfTemplates[m]); jj++) {
                sum = 0;

                for (k = 0; k < m; k++) {
                    sequence[k] = stream.GetULPosition(k);
                    // sequence[k] = bit; TODO 这个bit是干嘛的
                }
                for (k = 0; k <= K; k++)
                    nu[k] = 0;
                for (i = 0; i < N; i++) {
                    W_obs = 0;
                    for (j = 0; j < M - m + 1; j++) {
                        match = 1;
                        for (k = 0; k < m; k++) {
                            if (sequence[k] != stream.GetBitPosition(i * M + j + k)) {
                                match = 0;
                                break;
                            }
                        }
                        if (match == 1) {
                            W_obs++;
                            j += m - 1;
                        }
                    }
                    Wj[i] = W_obs;
                }
                sum = 0;
                chi2 = 0.0; /* Compute Chi Square */
                for (i = 0; i < N; i++) {
                    chi2 += Math.pow(((double) Wj[i] - lambda) / Math.pow(varWj, 0.5), 2);
                }
                p_value = mathFuncs.IGammaC(N / 2.0, chi2 / 2.0);

                if (mathFuncs.isNegative(p_value) || mathFuncs.isGreaterThanOne(p_value))
                    return false;
                else
                    return true;
            }
        }

        return false;
    }


    @Override
    public RandomTests GetType() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int GetMinimumLength() {
        // TODO Auto-generated method stub
        return 0;
    }

}