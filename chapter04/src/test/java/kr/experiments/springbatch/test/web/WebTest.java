package kr.experiments.springbatch.test.web;

import lombok.extern.slf4j.Slf4j;
import org.fest.util.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.experiments.springbatch.test.web.WebTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 6:45
 */
@Slf4j
public class WebTest {

    private static final String BASE_URL = "http://localhost:8089/sb04/joblauncher";

    private Server server;
    private CountDownLatch countDownLatch;
    private Map<String, String> jobParams;

    @Before
    public void setUp() throws Exception {
        startWebContainer();
    }

    @After
    public void tearDown() throws Exception {
        stopWebContainer();
    }

    @Test
    public void webEmbedded() throws Exception {

        assertThat(countDownLatch.await(10, TimeUnit.SECONDS)).isTrue();
    }

    private void startWebContainer() throws Exception {
        Server server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(8089);
        connector.setHost("127.0.0.1");
        server.addConnector(connector);

        WebAppContext wac = new WebAppContext();
        wac.setContextPath("/sb04");
        log.info("Current directory=[{}]", Files.currentFolder());
        wac.setWar("chapter04/src/test/resources/spring/web/webapp/");
        server.setHandler(wac);
        server.setStopAtShutdown(true);

        server.start();
        ApplicationContext context = getWebAppSpringContext(wac);
        countDownLatch = context.getBean(CountDownLatch.class);
        jobParams = context.getBean("params", Map.class);
    }

    private void stopWebContainer() throws Exception {
        if (server != null && server.isRunning())
            server.stop();
    }

    private ApplicationContext getWebAppSpringContext(WebAppContext wac) {
        ApplicationContext context =
            WebApplicationContextUtils.getWebApplicationContext(wac.getServletContext());
        return context;
    }
}
