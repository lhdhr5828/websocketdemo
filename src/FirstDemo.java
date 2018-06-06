import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Auther: Lee
 * @Date: 2018/6/6 08:42
 * @Description: 每次有web连接就会新建一个FirstDemo对象
 */

@ServerEndpoint(value = "/entry")
public class FirstDemo {
    public FirstDemo() {
        System.out.println("已经初始化了了乐乐了乐乐");
    }
    private String name ;
    private static Integer online = 0;
    private static CopyOnWriteArraySet<FirstDemo> copy = new CopyOnWriteArraySet<>();
    private Session session;

    //建立socket连接
    @OnOpen
    public void demoOpen(Session session){
        this.session = session;
        copy.add(this);
        addOnline();
    }


    //接收信息
    @OnMessage
    public void demoSendMessage(String message,Session session){
        //接到信息后首先返回信息
        sendMessage("你好我收到了你的hello！当前在线："+online+"人");
        System.out.println("客户端发送消息："+message);
        System.out.println("websocket数量："+copy.size());
//        for (FirstDemo fd:copy){
//            fd.sendMessage("服务端发来的消息！");
//        }

    }

    private void sendMessage(String message) {
        try {
            //发送客户端信息
            this.session.getBasicRemote().sendText(message);
            System.out.println("========================发送了信息："+message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //socket关闭后将对象移除
    @OnClose
    public void demoClose(){
        copy.remove(this);
        offOnLine();
    }

    //建立连接后增加
    private synchronized void addOnline() {
        FirstDemo.online++;
        System.out.println("当前在线人数为："+FirstDemo.online);
    }
    //断开连接后减去
    private synchronized void offOnLine() {
        FirstDemo.online--;
        System.out.println("当前在线人数为："+FirstDemo.online);
    }

    @OnError
    public void demoError(Session session, Throwable throwable) throws SocketException {
        throw  new SocketException(throwable.getLocalizedMessage());

    }

    public static Integer getOnline() {
        return online;
    }
}
