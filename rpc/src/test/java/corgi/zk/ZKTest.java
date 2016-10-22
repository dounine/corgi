package corgi.zk;

import com.dounine.corgi.rpc.zk.ZkClient;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by huanghuanlai on 2016/10/15.
 */
public class ZKTest {

    ZkClient client = new ZkClient("localhost:2181",3000);


    @Test
    public void testList(){
        List<String> list = client.getChildren("/rpc/corgi/spring/test_zk/code/UserApiImpl/1_0_0");
        for(String host : list){
            System.out.println(host);
        }
    }

    @Test
    public void testHost(){
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreate(){
        client.createPersistent("/nihao");
        try {
            client.createEpseq("/nihao/node", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        List<String> list = client.getChildren("/nihao");
        for(String host : list){
            System.out.println(client.getData("/nihao/"+host).toString());
        }
    }
}
