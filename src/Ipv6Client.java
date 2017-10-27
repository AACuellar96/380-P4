
import java.io.*;
import java.net.Socket;
public final class Ipv6Client {
    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("18.221.102.182",38004)) {
            System.out.println("Connected to server.");
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            BufferedReader brIS = new BufferedReader(new InputStreamReader(System.in));
            PrintStream out = new PrintStream((socket.getOutputStream()),true,"UTF-8");
            for(int packetN=0;packetN<12;packetN++) {
                System.out.println("Packet " + (packetN+1));
                // .5B + .5B + 1B+ 2B + 2B + 3/8B + 1 5/8 B + 1B + 1B + 2B + 4B + 4B + DATA
                int byteAmt=(int) Math.pow(2,(packetN+1));
                byte[] sequence = new byte[40+byteAmt];
                sequence[0]=0x60;
                sequence[1]=0x00;
                sequence[2]=0;
                sequence[3]=0;
                // In octets. Same thing apparently?
                String hex = Integer.toHexString(byteAmt);
                if (hex.length() < 4)
                    while (hex.length() < 4)
                        hex = "0" + hex;
                sequence[4]=(byte) Integer.parseInt(hex.substring(0, 2).toUpperCase(), 16);
                sequence[5]=sequence[11] = (byte) Integer.parseInt(hex.substring(2).toUpperCase(), 16);

                //Next header
                sequence[6]=0x11;

                //Hop Limit
                sequence[7]=0x14;

                // TODO: Src Address
                sequence[8]=0x40;
                sequence[9]=0;
                sequence[10]=0x32;
                sequence[11]=0x06;
                sequence[12]=0x11;
                sequence[13]=0x11;
                sequence[14]=0x11;
                sequence[15]=0x11;
                sequence[16]=0x12;
                sequence[17]=(byte) 0xDD;
                sequence[18]=(byte) 0x66;
                sequence[19]= (byte) 0xB6;
                sequence[20]=0x40;
                sequence[21]=0;
                sequence[22]=0x32;
                sequence[23]=0x06;
                // TODO: Dest Address
                sequence[24]=0x40;
                sequence[25]=0;
                sequence[26]=0x32;
                sequence[27]=0x06;
                sequence[28]=0x11;
                sequence[29]=0x11;
                sequence[30]=0x11;
                sequence[31]=0x11;
                sequence[32]=0x12;
                sequence[33]=(byte) 0xDD;
                sequence[34]=(byte) 0x66;
                sequence[35]= (byte) 0xB6;
                sequence[36]=0x40;
                sequence[37]=0;
                sequence[38]=0x32;
                sequence[39]=0x06;

                out.write(sequence);
                System.out.println("0x"+(Integer.toHexString(is.read())+Integer.toHexString(is.read())+Integer.toHexString(is.read())+Integer.toHexString(is.read())).toUpperCase());
            }
            is.close();
            isr.close();
            br.close();
            brIS.close();
            socket.close();
            System.out.println("Disconnected from server.");
        }
    }
}
