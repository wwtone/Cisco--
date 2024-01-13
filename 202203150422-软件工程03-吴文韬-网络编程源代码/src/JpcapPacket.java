import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import java.io.IOException;

public class JpcapPacket{
    public static void main(String[] args) {
        //绑定网络设备
        //获取网卡列表
        NetworkInterface[] kk = JpcapCaptor.getDeviceList(); //一组NetworkInterface对象，可看作网卡数组

        for (NetworkInterface n : kk) {
            System.out.println(n.name + "   |   " + n.description);
        }
        System.out.println("以上为网卡列表");

        JpcapCaptor jpcap = null;
        // 限定抓取数据包的多少个字节
        int temp = 1512;
        try {
            jpcap = JpcapCaptor.openDevice(kk[2], temp, true, 50); //获取网卡连接及获取包限时时间
        } catch (IOException e) {
            e.printStackTrace();
        }

        //抓包
        int i = 0;
        while (i < 5) {
            Packet packet = jpcap.getPacket();
            if (packet instanceof IPPacket && ((IPPacket) packet).version == 4) { //抓的是ipv4
                i++;
                IPPacket ip = (IPPacket) packet;

                System.out.println("版本号："+ip.version);
                System.out.println("总长度：" + ip.length);
                System.out.println("标识：" + ip.ident);
                System.out.println("标志位MF：" + ip.more_frag);
                System.out.println("标志位DF：" + ip.dont_frag);
                System.out.println("片偏移：" + ip.offset);
                String protocol = "";    //用于确认协议
                switch (new Integer(ip.protocol)) {
                    case 1:
                        protocol = "ICMP";
                        break;
                    case 2:
                        protocol = "IGMP";
                        break;
                    case 6:
                        protocol = "TCP";
                        break;
                    case 8:
                        protocol = "EGP";
                        break;
                    case 9:
                        protocol = "IGP";
                        break;
                    case 17:
                        protocol = "UDP";
                        break;
                    case 41:
                        protocol = "IPv6";
                        break;
                    case 89:
                        protocol = "OSPF";
                        break;
                    default:
                        protocol = "Other";
                        break;
                }
                System.out.println("协议：" + protocol);
                System.out.println("源地址：" + ip.src_ip.getHostAddress());
                System.out.println("目的地址：" + ip.dst_ip.getHostAddress());
                System.out.println("");
            }
        }
    }
}