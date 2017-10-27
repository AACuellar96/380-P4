
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
                int byteAmt=(int) Math.pow(2,(packetN+1));
                byte[] sequence = new byte[40+byteAmt];
                sequence[0]=0x60;
                sequence[1]=0x00;
                sequence[2]=0;
                sequence[3]=0;
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
                // Src Address
                sequence[8]=0;
                sequence[9]=0;
                sequence[10]=0;
                sequence[11]=0;
                sequence[12]=0;
                sequence[13]=0;
                sequence[14]=0;
                sequence[15]=0;
                sequence[16]=0;
                sequence[17]=0;
                sequence[18]=(byte) 0xFF;
                sequence[19]=(byte) 0xFF;
                sequence[20]=(byte) 0x6F;
                sequence[21]=(byte) 0x6F;
                sequence[22]=(byte) 0x0B;
                sequence[23]=(byte) 0x6F;
                // TODO: Dest Address
                sequence[24]=0;
                sequence[25]=0;
                sequence[26]=0;
                sequence[27]=0;
                sequence[28]=0;
                sequence[29]=0;
                sequence[30]=0;
                sequence[31]=0;
                sequence[32]=0;
                sequence[33]=0;
                sequence[34]=(byte) 0xFF;
                sequence[35]=(byte) 0xFF;
                sequence[36]=(byte) 0x12;
                sequence[37]=(byte) 0xDD;
                sequence[38]=(byte) 0x66;
                sequence[39]=(byte) 0xB6;
                out.write(sequence);
                System.out.println("Response: 0x"+(Integer.toHexString(is.read())+Integer.toHexString(is.read())+Integer.toHexString(is.read())+Integer.toHexString(is.read())).toUpperCase());
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
