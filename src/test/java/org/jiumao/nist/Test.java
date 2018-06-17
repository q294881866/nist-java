package org.jiumao.nist;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import org.jiumao.nist.CryptoRandomStream.StringCryptoRandomStream;
import org.jiumao.nist.random.ApproximateEntropy;
import org.jiumao.nist.random.BlockFrequency;
import org.jiumao.nist.random.CumulativeSumForward;
import org.jiumao.nist.random.CumulativeSumReverse;
import org.jiumao.nist.random.DiscreteFourierTransform;
import org.jiumao.nist.random.Frequency;
import org.jiumao.nist.random.LempelZivCompression;
import org.jiumao.nist.random.LinearComplexity;
import org.jiumao.nist.random.LongestRunOfOnes;
import org.jiumao.nist.random.NonOverlappingTemplateMatchings;
import org.jiumao.nist.random.OverlappingTemplateMatchings;
import org.jiumao.nist.random.RandomExcursions;
import org.jiumao.nist.random.Rank;
import org.jiumao.nist.random.Runs;
import org.jiumao.nist.random.Serial;
import org.jiumao.nist.random.Universal;


public class Test {

    @org.junit.Test
    public void fileToBinary() throws Exception {
        FileReader in = new FileReader("secure.txt");
        BufferedReader br = new BufferedReader(in);
        FileWriter out = new FileWriter("binary.txt");
        String line = null;
        StringBuffer sb = new StringBuffer(128);
        while ((line = br.readLine()) != null) {
            sb.append(line.contains("Failed") ? "0" : "1");
        }

        out.write(sb.toString());
        br.close();
        out.close();
    }


    @org.junit.Test
    public void randomTest() throws Exception {
        FileReader in = new FileReader("binary.txt");
        BufferedReader br = new BufferedReader(in);
        String binaryStream = br.readLine();
        br.close();
        
        StringCryptoRandomStream scr = StringCryptoRandomStream.ofBinaryString(binaryStream);
        boolean ApproximateEntropy = new ApproximateEntropy().isRandom(scr);
        System.out.println("BlockFrequency:"+ApproximateEntropy);
        boolean BlockFrequency = new BlockFrequency().isRandom(scr);
        System.out.println("BlockFrequency:"+BlockFrequency);
        boolean CumulativeSumForward = new CumulativeSumForward().isRandom(scr);
        System.out.println("CumulativeSumForward:"+CumulativeSumForward);
        boolean CumulativeSumReverse = new CumulativeSumReverse().isRandom(scr);
        System.out.println("CumulativeSumReverse:"+CumulativeSumReverse);
        boolean DiscreteFourierTransform = new DiscreteFourierTransform().isRandom(scr);
        System.out.println("DiscreteFourierTransform:"+DiscreteFourierTransform);
        boolean Frequency = new Frequency().isRandom(scr);
        System.out.println("Frequency:"+Frequency);
//        boolean LempelZivCompression = new LempelZivCompression().isRandom(scr);
//        System.out.println("LempelZivCompression:"+LempelZivCompression);
        boolean LinearComplexity = new LinearComplexity(9).isRandom(scr);
        System.out.println("LinearComplexity:"+LinearComplexity);
        boolean LongestRunOfOnes = new LongestRunOfOnes().isRandom(scr);
        System.out.println("LongestRunOfOnes:"+LongestRunOfOnes);
        boolean NonOverlappingTemplateMatchings = new NonOverlappingTemplateMatchings().isRandom(scr);
        System.out.println("NonOverlappingTemplateMatchings:"+NonOverlappingTemplateMatchings);
        boolean OverlappingTemplateMatchings = new OverlappingTemplateMatchings().isRandom(scr);
        System.out.println("OverlappingTemplateMatchings:"+OverlappingTemplateMatchings);
        boolean RandomExcursions = new RandomExcursions().isRandom(scr);
        System.out.println("RandomExcursions:"+RandomExcursions);
        boolean Rank = new Rank().isRandom(scr);
        System.out.println("Rank:"+Rank);
        boolean Runs = new Runs().isRandom(scr);
        System.out.println("Runs:"+Runs);
        boolean Serial = new Serial().isRandom(scr);
        System.out.println("Serial:"+Serial);
        boolean Universal = new Universal().isRandom(scr);
        System.out.println("Universal:"+Universal);
    }
}
