package generic;

import com.github.dockerjava.api.model.HostConfig;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CmdModifierTest {

    // hostname {
    @Rule
    public GenericContainer theCache = new GenericContainer<>("redis:3.0.2")
            .withCreateContainerCmdModifier(cmd -> cmd.withHostName("the-cache"));
    // }

    // memory {
//    @Rule
    public GenericContainer memoryLimitedRedis = new GenericContainer<>("redis:3.0.2")
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(HostConfig.newHostConfig()
                .withMemory((long) 4 * 1024 * 1024)));
//                .withMemorySwap((long) 4 * 1024 * 1024)));
    // }


    @Test
    public void testHostnameModified() throws IOException, InterruptedException {
        final Container.ExecResult execResult = theCache.execInContainer("hostname");
        assertEquals("the-cache", execResult.getStdout().trim());
    }

//    @Test
    public void testMemoryLimitModified() throws IOException, InterruptedException {
        final Container.ExecResult execResult = memoryLimitedRedis.execInContainer("cat", "/sys/fs/cgroup/memory/memory.limit_in_bytes");
        assertEquals("8388608", execResult.getStdout().trim());
    }
}
