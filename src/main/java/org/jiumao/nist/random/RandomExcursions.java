package org.jiumao.nist.random;

import org.jiumao.nist.Base.BaseRandom;
import org.jiumao.nist.Base.RandomTests;
import org.jiumao.nist.CryptoRandomStream.BaseCryptoRandomStream;


/**
 * 随机偏移测试
 * <P>
 * 本检验主要是看一个累加和随机游动中具有 K
 * 个节点的循环的个数。累加和随机游动由于将关于“0”，“1”的部分和序列转化成适当的“-1”、“+1”序列产生的。一个随机游动循环由单位步长的一个序列组成，这个序列的起点和终点均是
 * 0。该检验的目的是确定在一个循环内的特殊状态对应的节点数是否与在随机序列中预计达到的节点数相背离。实际上，这个检验由八个检验（和结论）组成，一个检验和结论对应着一个特定的状态：-4，-3，-2，-1和+1，+2，+3，+4
 * 。
 * 
 * @author ppf@jiumao.org
 * @date 2018年3月22日
 */
public class RandomExcursions extends BaseRandom {
    /**
     * Random Test Class enumerator name
     */
    protected static final RandomTests TEST = RandomTests.RandomExcursions;
    double pi[][] =
            { { 0.0000000000, 0.00000000000, 0.00000000000, 0.00000000000, 0.00000000000, 0.0000000000 },
              { 0.5000000000, 0.25000000000, 0.12500000000, 0.06250000000, 0.03125000000, 0.0312500000 },
              { 0.7500000000, 0.06250000000, 0.04687500000, 0.03515625000, 0.02636718750, 0.0791015625 },
              { 0.8333333333, 0.02777777778, 0.02314814815, 0.01929012346, 0.01607510288, 0.0803755143 },
              { 0.8750000000, 0.01562500000, 0.01367187500, 0.01196289063, 0.01046752930, 0.0732727051 } };

    int stateX[] = { -4, -3, -2, -1, 1, 2, 3, 4 };
    int counter[] = { 0, 0, 0, 0, 0, 0, 0, 0 };


    @Override
    public boolean isRandom(BaseCryptoRandomStream stream) {
        int b, i, j, k, J, x;
        double sum, constraint;
        double nu[][] = new double[6][8];
        // TODO 这里可能错了，是bit长度还是int数长度
        int n = stream.GetBitLength();
        int S_k[] = new int[n];
        int cycle[] = new int[Math.max(1000, n / 100)];

        J = 0; /* DETERMINE CYCLES */
        S_k[0] = 2 * (int) stream.GetBitPosition(0) - 1;
        for (i = 1; i < n; i++) {
            S_k[i] = S_k[i - 1] + 2 * stream.GetBitPosition(i) - 1;
            if (S_k[i] == 0) {
                J++;
                if (J > cycle.length) {
                    return false;
                }
                cycle[J] = i;
            }
        }
        if (S_k[n - 1] != 0)
            J++;
        cycle[J] = n;

        constraint = Math.max(0.005 * Math.pow(n, 0.5), 500);
        if (J > constraint) {

            int cycleStart = 0;
            int cycleStop = cycle[1];
            for (k = 0; k < 6; k++)
                for (i = 0; i < 8; i++)
                    nu[k][i] = 0.;
            for (j = 1; j <= J; j++) { /* FOR EACH CYCLE */
                for (i = 0; i < 8; i++)
                    counter[i] = 0;
                for (i = cycleStart; i < cycleStop; i++) {
                    if ((S_k[i] >= 1 && S_k[i] <= 4) || (S_k[i] >= -4 && S_k[i] <= -1)) {
                        if (S_k[i] < 0)
                            b = 4;
                        else
                            b = 3;
                        counter[S_k[i] + b]++;
                    }
                }
                cycleStart = cycle[j] + 1;
                if (j < J)
                    cycleStop = cycle[j + 1];

                for (i = 0; i < 8; i++) {
                    if ((counter[i] >= 0) && (counter[i] <= 4))
                        nu[counter[i]][i]++;
                    else if (counter[i] >= 5)
                        nu[5][i]++;
                }
            }

            for (i = 0; i < 8; i++) {
                x = stateX[i];
                sum = 0.;
                for (k = 0; k < 6; k++)
                    sum += Math.pow(nu[k][i] - J * pi[(int) Math.abs(x)][k], 2)
                            / (J * pi[(int) Math.abs(x)][k]);
                pValue = super.mathFuncs.IGammaC(2.5, sum / 2.0);

                if (mathFuncs.isNegative(pValue) || mathFuncs.isGreaterThanOne(pValue))
                    System.out.println("WARNING:  P_VALUE IS OUT OF RANGE.\n");
                else
                    return pValue > 0.01;
            }
        }
        return false;
    }


    @Override
    public RandomTests GetType() {
        return TEST;
    }


    @Override
    public int GetMinimumLength() {
        // TODO Auto-generated method stub
        return 0;
    }

}
