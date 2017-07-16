package net.wimpi.modbustcp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.procimg.SimpleDigitalIn;
import net.wimpi.modbus.procimg.SimpleDigitalOut;
import net.wimpi.modbus.procimg.SimpleInputRegister;
import net.wimpi.modbus.procimg.SimpleProcessImage;
import net.wimpi.modbus.procimg.SimpleRegister;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    ModbusTCPListener listener = null;
    SimpleProcessImage spi = null;

    int port = 30000;//Android 1024 以下端口属于系统端口，需要root权限
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //prepare a process image
        spi = new SimpleProcessImage();
        //线圈寄存器
        spi.addDigitalOut(new SimpleDigitalOut(true));
        spi.addDigitalOut(new SimpleDigitalOut(true));
        spi.addDigitalOut(new SimpleDigitalOut(true));
        spi.addDigitalOut(new SimpleDigitalOut(true));
        //状态寄存器
        spi.addDigitalIn(new SimpleDigitalIn(false));
        spi.addDigitalIn(new SimpleDigitalIn(true));
        spi.addDigitalIn(new SimpleDigitalIn(false));
        spi.addDigitalIn(new SimpleDigitalIn(true));
        spi.addDigitalIn(new SimpleDigitalIn(true));
        spi.addDigitalIn(new SimpleDigitalIn(true));
        spi.addDigitalIn(new SimpleDigitalIn(true));
        spi.addDigitalIn(new SimpleDigitalIn(true));
        //保持寄存器
        spi.addRegister(new SimpleRegister(251));
        spi.addRegister(new SimpleRegister(13));
        spi.addRegister(new SimpleRegister(240));
        //输入寄存器
        spi.addInputRegister(new SimpleInputRegister(45));


        //create the coupler holding the image
        ModbusCoupler.getReference().setProcessImage(spi);
        ModbusCoupler.getReference().setMaster(false);
        ModbusCoupler.getReference().setUnitID(15);

        new Thread(networkTask).start();
        Log.e(TAG, "本机的IP = " + getHostIP());

    }
    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            listener = new ModbusTCPListener(3);
            listener.setPort(port);
            listener.start();
        }
    };
    /**
     * 获取ip地址
     * @return
     */
    public static String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;
    }
}

