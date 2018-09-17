package hello;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
public class Txt {
	public static void writer_me(String file, String conent) {
		BufferedWriter out = null ;
		try {
		out = new BufferedWriter(new OutputStreamWriter(
		new FileOutputStream(file, true),"utf-8"));
		out.write(conent+"\r\n");
		} catch (Exception e) {
		e.printStackTrace();
		} finally {
		try {
		out.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
		}
		}
	
	public  ArrayList read_me(String File) throws IOException {
        String pathname = File; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
        File filename = new File(pathname); // 要读取以上路径的input。txt文件  
        InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(  
			        new FileInputStream(filename),"utf-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 建立一个输入流对象reader  
        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
        String line = "";  
        ArrayList list =new ArrayList();
        while ((line = br.readLine()) != null) {  			
        	//	System.out.println(line); // 一次读入一行数据
            list.add(line);
             
        } 
        return list;
	}
}

