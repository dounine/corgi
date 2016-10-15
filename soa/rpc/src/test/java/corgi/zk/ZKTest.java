package corgi.zk;

import com.dounine.corgi.rpc.zk.Client;
import org.junit.Test;

/**
 * Created by huanghuanlai on 2016/10/15.
 */
public class ZKTest {

    @Test
    public void testCreate(){
        Client client = new Client("localhost:2181");
        System.out.println(client.getChildren("/"));
    }
}
