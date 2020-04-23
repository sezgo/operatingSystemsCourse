package com.example.helloworld;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/*Question 3
Filter all document whose file size is larger that 50KB in directory whose path is specified in the variable filePath. Print the result to the standard output.
Use the standard classes from the Java IO library. Enter the complete code solution (Java code) of the task in the response field.
 */

/*Question 4
Write basic Java application which traverses all documents in a given directory and its subtree, and searches for files with ‘.jpg’ or ‘.bmp’ extension which have been edited over the last 7 days.
The path of the directory is passed as program argument.  Print the absolute path of the files which meet the requirements.

Solution: The solution (Java code) should be placed here, using copy-paste.
 */
class DirectoryFilter implements FilenameFilter {
    String ext1;
    String ext2;

    public DirectoryFilter(String ext1, String ext2) {
        this.ext1 = ext1;
        this.ext2 = ext2;
    }

    public boolean accept(File dir,  String name) {
        String s = new File(name).getName();
        return s.indexOf(ext1) != -1 || s.indexOf(ext2) != -1;
    }
}


public class HelloWorld {
    public static void main(String[] args) {

        File f = new File("C:\\Users\\xxx\\Desktop\\");
        System.out.println(f.exists());
        System.out.println(f.length());


        File[] files = f.listFiles();

        /*Answer 3
        for(File file: files) {
            if (file.isDirectory()) continue;
            if (file.length() < 50) continue;
            System.out.println(file + " - " + file.length());
        }
        */
        if (f.exists()) traverse(f);

        BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(System.out));
    }


    public static void traverse(File file) {
        File[] files = file.listFiles(new DirectoryFilter(".bmp", ".png"));

        for(File f: files) {
            if (f.isDirectory()){
                traverse(f);
            }
            LocalDateTime fileDate = new Timestamp(f.lastModified()).toLocalDateTime();
            if (LocalDateTime.now().compareTo(fileDate) >= 7)
                System.out.println(f);
        }
    }

}
