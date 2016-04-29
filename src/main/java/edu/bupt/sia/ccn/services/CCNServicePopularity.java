package edu.bupt.sia.ccn.services;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangkuang on 16-4-29.
 */
public class CCNServicePopularity {

    Hashtable<String, Integer> _CCNServicePopularity = new Hashtable<>();

    public CCNServicePopularity() {
        readFile("/var/tmp/ccnd.log"); //where the ccnd.log exists
    }


    public String RegexMatches(String textString) {
        String start = "ccnx:/";
        String end = "/=";
        String regex = String.format("(%s)(.*?)(%s)", start,end);
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(textString);

        if (match.find()) {
            return match.group(2);
        } else {
            return null;
        }
    }

    public void popularityCount(String outputString) {
        Map<String,Integer> map = new HashMap<String, Integer>();
        StringTokenizer st = new StringTokenizer(outputString,",.! \n");

        while (st.hasMoreTokens()) {
            String letter = st.nextToken();
            int count;
            if (map.get(letter) == null) {
                count = 1;
            } else {
                count = map.get(letter).intValue() + 1;
            }
            map.put(letter, count);
        }

        Set<CountTable> set = new TreeSet<CountTable>();
        for (String key : map.keySet()) {
            set.add(new CountTable(key, map.get(key)));
        }

        int sum = 0;
        for (Iterator<CountTable> it = set.iterator(); it.hasNext(); ) {
            CountTable w = it.next();
            sum = sum + w.getCount();
        }

        //int count = 1;
        for (Iterator<CountTable> it = set.iterator(); it.hasNext(); ) {
            CountTable w = it.next();
            double frequency = (double)w.getCount()/sum;
            int popularity = 0;
            if (frequency >0 && frequency <0.5) {
                popularity = 1;
            }
            if (frequency >=0.5 && frequency <1) {
                popularity = 2;
            }
            _CCNServicePopularity.put(w.getKey(), popularity); //CCNServicePopularity Table it is what we need
            //String servicePopularity = String.format("%.2f", frequency);
            System.out.println(w.getKey() + " popularity： "
                    + _CCNServicePopularity.get(w.getKey()));
            //if (count == 3)// generate the first 3
            //    break;
            //count++;
        }
    }

    public void readFile(String fileName) {
        String output = "";
        File file = new File(fileName);
        if (file.exists()) {
            if (file.isFile()) {
                try{
                    BufferedReader input = new BufferedReader (new FileReader(file));
                    StringBuffer buffer = new StringBuffer();
                    String text;
                    while ((text = input.readLine()) != null) {
                        if (text.contains("content_to") && !text.contains("selfreg") && !text.contains("localhost") && !text.contains("ccnx.org")) {
                            text = "ccnx:/" + RegexMatches(text);
                            buffer.append(text + "\n");
                        }
                    }
                    output = buffer.toString();

                    popularityCount(output);

                    File file2 = new File("/home/yangkuang/result.txt"); //result.txt after regex
                    try {
                        FileWriter fileWriter = new FileWriter(file2);
                        fileWriter.write(output);
                        fileWriter.close(); //
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (IOException ioException) {
                    System.err.println("File Error!");
                }
            } else if (file.isDirectory()) {
                String[] dir = file.list();
                output += "Directory contents:\n";

                for (int i=0; i<dir.length; i++) {
                    output += dir[i] +"\n";
                }
            }
        } else{
            System.err.println("Does not exist!");
        }
//        return output;
    }

    public Hashtable<String, Integer> get_CCNServicePopularity() {
        return _CCNServicePopularity;
    }

//    public void main (String args[]){
//        String str = readFile("/var/tmp/ccnd.log");
        //System.out.print(str);
//    }
}
