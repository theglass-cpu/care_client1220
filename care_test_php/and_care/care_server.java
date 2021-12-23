// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.ResultSet;
// import java.sql.Statement;
// import java.util.HashMap;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class care_server {


    Connection con;
    Statement stmt;
    ResultSet rs;
    public static HashMap<String, Socket> socketHashMap;
    String url = "jdbc:mysql://3.37.212.160:3306/care?allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&useSSL=false";
    String id = "server";
    String pw = "985621aA";


    public care_server(){

        try{
            //드라이버 로딩 (Mysql 또는 Oracle 중에 선택하시면 됩니다.)
            Class.forName("com.mysql.cj.jdbc.Driver");    //mysql
            //Class.forName("oracle.jdbc.driver.OracleDriver");    //oracle
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getConnection(){

        try{
            //커넥션을 가져온다.
            con = DriverManager.getConnection(url, id, pw);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void getData(){

        try{

            stmt = con.createStatement();
            //데이터를 가져온다.
            rs = stmt.executeQuery("select * from care_cs");

            while(rs.next()){
                //출력
                System.out.println(rs.getString("cs_id"));
             


            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void server_start() throws IOException {
        ServerSocket s_socket = new ServerSocket(8888);
        System.out.println("사용자 기다리는중");
        while(true){
             Socket c_socket = s_socket.accept();
            System.out.println("사용자 들어옴");
            care_server_manager c_thread = new care_server_manager();
            c_thread.setSocket(c_socket);
            c_thread.start();

        }

    }


    public static void main(String[] args) throws IOException{
        socketHashMap = new HashMap<>();
        care_server server = new care_server();

        server.getConnection();
        server.getData();
        server_start();

    }
}
