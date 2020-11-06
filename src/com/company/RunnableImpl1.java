package com.company;

import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 刘芋池
 * @Description
 * @create 2020/11/6 12:15
 */
public class RunnableImpl1 implements Runnable{
    private static int now=0;
    static File file=new File("C:\\Users\\29858\\Documents\\1.txt");
    private AtomicReference<String> atomicReference;
    static RandomAccessFile raf=null;
    final static int len=2;
    RunnableImpl1(){
        try {
            raf=new RandomAccessFile(file,"rw");
//            this.ato=new AtomicReference<File>(file1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private  String readFile(File filer) throws IOException {//将一个文件读取到String中
        FileInputStream is=null;
        StringBuilder stringBuilder=new StringBuilder("s");
        if(filer.length()!=0){
            is=new FileInputStream(filer);
            InputStreamReader streamReader=new InputStreamReader(is);
            BufferedReader reader=new BufferedReader(streamReader);
            String line="";
            while ((line=reader.readLine())!=null){
                stringBuilder.append(line);
            }
            reader.close();
            is.close();

        }
        return String.valueOf(stringBuilder);
    }

    @Override
    public void run() {
        try {
            String str=readFile(file);
            atomicReference=new AtomicReference<String>(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true){
            FileWriter fos = null;
            try {
                fos = new FileWriter("D:\\.temp\\3.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] b = new byte[len];
            try {
                raf.seek(now);
                int temp = raf.read(b);
                if (temp == -1) {
                    return;
                }
                while (true) {
                    String prev = atomicReference.get();
                    String next = prev.substring(temp - 1);
                    if (atomicReference.compareAndSet(prev, next)) {
                        now += temp;
                        String str = new String(b);
                        System.out.println(Thread.currentThread().getName() + "正在读取" + str);
                        System.out.println(Thread.currentThread().getName() + "正在写入" + str);
                        fos.write(str);
                        fos.flush();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

