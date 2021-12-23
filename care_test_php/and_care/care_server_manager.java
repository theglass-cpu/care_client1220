

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class care_server_manager  extends Thread {


    private Socket m_socket;
    private String m_id;

     @Override
     public void run() {
         super.run();
         try {
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
             //소켓에서 입력한값 inputstream 으로 읽어오고 buffer 에 다음
             String text;
             m_id=bufferedReader.readLine();
             //buffer담은값 읽어와서 string 변수에 담음
             System.out.println(m_id);
             care_server.socketHashMap.put(m_id,m_socket);
             System.out.println("지정된소켓번호 출력"+care_server.socketHashMap.get(m_id));
             //클라이언트 id 랑 소켓 값담음


             while(true){


                 // text= bufferedReader.readLine();
                 // String[] strArr = text.split(",");
              //    PrintWriter printWriter = new PrintWriter(care_server.socketHashMap.get(strArr[0]).getOutputStream());
                 // //어떤 소켓에게 메세지를 보낼지 선택하여준다 =>출력스트림 printwriter
                 // // 메세지 자체 출력스트림 =>getoutputstream

                 // printWriter.println(m_id+" : "+strArr[1]);
                 // //내 아이디랑 보낼 메세지
                 // printWriter.flush();
                 // //전송




             }

         } catch (IOException e) {
             e.printStackTrace();
         }

     }

     public void setSocket(Socket socket){
         m_socket=socket;
     }
}
