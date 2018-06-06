import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Auther: Lee
 * @Date: 2018/6/6 08:42
 * @Description:
 */

@ServerEndpoint(value = "/entry")
public class FirstDemo {
    public FirstDemo() {
        System.out.println("已经初始化了了乐乐了乐乐");
    }

    private static Integer online = 0;
    private static CopyOnWriteArraySet<FirstDemo> copy = new CopyOnWriteArraySet<>();
    private Session session;

    @OnOpen
    public void demoOpen(Session session){
        this.session = session;
        copy.add(this);
        addOnline();
    }

    private synchronized void addOnline() {
        FirstDemo.online++;
        System.out.println("当前在线人数为："+FirstDemo.online);
    }
    @OnMessage
    public void demoSendMessage(String message,Session session){
        sendMessage(message);
        System.out.println("客户端发送消息："+message);
        System.out.println("websocket数量："+copy.size());
        for (FirstDemo fd:copy){
            fd.sendMessage("服务端发来的消息！");
        }

    }

    private void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
            System.out.println("========================发送了信息："+message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void demoClose(){
        copy.remove(this);
        offOnLine();
    }

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
