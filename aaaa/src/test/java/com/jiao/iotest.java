package com.jiao;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author 18067
 * @Date 2021/9/15 17:48
 */
public class iotest {
    @Test
    public void testIo() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("D:\\document\\java初学者常用单词.txt"));
        String biji = "";
        String line = "";
        int count = 0;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            biji += line;
            count++;
        }

        System.out.println(count);
    }

    @Test
    public void testdate() {
        Date date = new Date();
        System.out.println(date);
        long time = date.getTime();
        System.out.println(time);
        Date date1 = new Date(1632664448189L);
        System.out.println(date1);
    }

    @Test
    public void arrayTest() {

        Map map = new HashMap();
        map.put("a", "s");
        Object o1 = map.remove(4);
        System.out.println("======================" + o1);
        LinkedList list = new LinkedList();
        // list.add("ss");
        //Object o = list.remove();
        Object o = list.poll();
        Object o2 = list.remove();
        System.out.println(o);
        System.out.println(o2);

    }


}
