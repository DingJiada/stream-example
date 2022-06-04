package com.shouzhi;

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author WX
 * @date 2020-11-12 14:02:37
 */
public class NetStateTest {
    @Test
    public void test1() throws IOException {
        InetAddress inet;
        inet = InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 });
        System.out.println("Sending Ping Request to " + inet);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");

        inet = InetAddress.getByAddress(new byte[] { (byte) 220, (byte) 181, 112, (byte)244 });
        System.out.println("Sending Ping Request to " + inet);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");

        inet = InetAddress.getByAddress(new byte[] { (byte) 192, (byte) 168, 1, (byte)185 });
        System.out.println("Sending Ping Request to " + inet);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");

        inet = InetAddress.getByAddress(new byte[] { (byte) 192, (byte) 168, 1, (byte)222 });
        System.out.println("Sending Ping Request to " + inet);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");

        inet = InetAddress.getByAddress(new byte[] { (byte) 192, (byte) 168, 1, (byte)65 });
        System.out.println("Sending Ping Request to " + inet);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");

        inet = InetAddress.getByAddress(new byte[] { (byte) 192, (byte) 168, 1, (byte)64 });
        System.out.println("Sending Ping Request to " + inet);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");
    }

    @Test
    public void test2() throws IOException {
        String ipAddress = "127.0.0.1";
        InetAddress inet = InetAddress.getByName(ipAddress);

        System.out.println("Sending Ping Request to " + ipAddress);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");

        ipAddress = "220.181.112.244";
        inet = InetAddress.getByName(ipAddress);

        System.out.println("Sending Ping Request to " + ipAddress);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");


        ipAddress = "192.168.1.222";
        inet = InetAddress.getByName(ipAddress);

        System.out.println("Sending Ping Request to " + ipAddress);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");


        ipAddress = "192.168.1.185";
        inet = InetAddress.getByName(ipAddress);

        System.out.println("Sending Ping Request to " + ipAddress);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");


        ipAddress = "192.168.1.65";
        inet = InetAddress.getByName(ipAddress);

        System.out.println("Sending Ping Request to " + ipAddress);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");


        ipAddress = "192.168.1.64";
        inet = InetAddress.getByName(ipAddress);

        System.out.println("Sending Ping Request to " + ipAddress);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");
    }

    @Test
    public void test3(){
        boolean reachable = false;
        Process process;
        try {
            String ipAddress = "127.0.0.1";
            process = Runtime.getRuntime().exec("ping " + ipAddress);
            int returnVal = process.waitFor();
            reachable = returnVal == 0?true:false;
            System.out.println("Sending Ping Request to " + ipAddress);
            System.out.println(reachable ? "Host is reachable" : "Host is NOT reachable");

            ipAddress = "220.181.112.244";
            process = Runtime.getRuntime().exec("ping " + ipAddress);
            returnVal = process.waitFor();
            reachable = returnVal == 0?true:false;
            System.out.println("Sending Ping Request to " + ipAddress);
            System.out.println(reachable ? "Host is reachable" : "Host is NOT reachable");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Test
    public void test4() throws IOException {
        long startTime = System.currentTimeMillis();    //获取开始时间
        String ipAddress = "192.168.1.64";
        InetAddress inet = InetAddress.getByName(ipAddress);

        System.out.println("Sending Ping Request to " + ipAddress);
        System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("程序运行时间："+(endTime - startTime)+" ms");
    }

}
