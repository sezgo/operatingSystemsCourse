package os.labs.lab1;/*
Write a Java program which will use I/O streams to read the content of the file source.txt, and then write out the lines which start with number in interval [0-9] to the empty file destination.txt. The read and write should be done using buffered streams.

Example:

source.txt                  destination.txt

1.Оперативни системи          1.Оперативни системи
Компјутерски системи          67Калкулус
67Калкулус
Note: You should create these two files yourself and fill izvor.txt with some arbitrary content.

Solution: The solution (Java code) should be placed here, using copy-paste.
 */

import java.io.*;

public class osLab1_6 {
    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\szgnm\\Desktop\\New folder\\source.txt";
        String dest = "C:\\Users\\szgnm\\Desktop\\New folder\\dest.txt";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
        BufferedWriter bw = new BufferedWriter(new FileWriter(dest));
        File file = new File(path);
        String line = null;
        StringBuilder sb = new StringBuilder();

        System.out.println(file.getCanonicalPath());

        while ((line = br.readLine()) != null) {
            char c = line.charAt(0);
            if (c <= '0' || c >= '9') continue;
            bw.write(line + "\n");
        }
        br.close();
        bw.close();
    }
}
