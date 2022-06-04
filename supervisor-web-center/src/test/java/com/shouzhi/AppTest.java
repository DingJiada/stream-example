package com.shouzhi;

import com.shouzhi.pojo.db.SysUser;
import com.xkzhangsan.time.calculator.DateTimeCalculatorUtil;
import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author WX
 * @date 2019-12-17 14:52:30
 */
public class AppTest {

    // 利用ProcessBuilder调用系统进程
    /*@Test
    public void testProcessBuilder() throws IOException {
        // 创建ProcessBuilder对象
        ProcessBuilder processBuilder = new ProcessBuilder();
        // 将标准输入流和错误流合并，即不管是正常输出还是错误输出都在一个输入流中能取到
        processBuilder.redirectErrorStream(true);
        // 设置第三方应用程序的命令，如ping程序
        // processBuilder.command("ping","127.0.0.1");
        processBuilder.command("ipconfig", "-all");
        // 启动一个进程
        Process process = processBuilder.start();
        // 通过标准输入流拿到正常的和错误的信息
        InputStream inputStream = process.getInputStream();
        // 转换字符流, 因为含有中文
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");
        int len = -1;
        char[] cbuf = new char[1024];
        while ((len = inputStreamReader.read(cbuf))!=-1){
            String str = new String(cbuf, 0, len);
            System.out.println(str);
        }
        inputStreamReader.close();
        inputStream.close();
    }


    // 利用ProcessBuilder调用FFMPEG
    @Test
    public void testFFMPEG() throws IOException {
        // 创建ProcessBuilder对象
        ProcessBuilder processBuilder = new ProcessBuilder();
        // 将标准输入流和错误流合并，即不管是正常输出还是错误输出都在一个输入流中能取到
        processBuilder.redirectErrorStream(true);
        // 设置第三方应用程序的命令，如ping程序

        // ffmpeg -i bootstrap.mp4 -hls_time 10 -hls_list_size 0 -hls_segment_filename ./chunk/bootstrap_%05d.ts ./chunk/bootstrap.m3u8
        String basePath = "E://java//StreamingMedia//srcfile//";
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add(basePath+"svn.mp4");
        command.add("-hls_time");
        command.add("10");
        command.add("-hls_list_size");
        command.add("0");
        command.add("-hls_segment_filename");
        command.add(basePath+"chunk/svn_%05d.ts");
        command.add(basePath+"chunk/svn.m3u8");
        processBuilder.command(command);

        // 启动一个进程
        Process process = processBuilder.start();
        // 通过标准输入流拿到正常的和错误的信息
        InputStream inputStream = process.getInputStream();
        // 转换字符流, 因为含有中文
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");
        int len = -1;
        char[] cbuf = new char[1024];
        while ((len = inputStreamReader.read(cbuf))!=-1){
            String str = new String(cbuf, 0, len);
            System.out.println(str);
        }
        inputStreamReader.close();
        inputStream.close();
        // process.destroy();
    }*/

    @Test
    public void test3() {
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("1");
        ArrayList<String> list2 = new ArrayList<>();
        list2.add("2");
        ArrayList<String> list3 = new ArrayList<>();
        list3.add("2");
        List<ArrayList<String>> arrayLists = new ArrayList<>();
        arrayLists.add(list1);
        arrayLists.add(list2);
        arrayLists.add(list3);

        List<ArrayList<String>> arrayLists2 = new ArrayList<>(arrayLists);

        arrayLists.remove(1);

        System.out.println(arrayLists.size());
        System.out.println(arrayLists2.size());
    }

}
