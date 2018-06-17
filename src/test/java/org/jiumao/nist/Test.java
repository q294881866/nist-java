package org.jiumao.nist;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;


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
        
    }
}
