package no.nb.microservices.catalogplaylist.it;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import no.nb.microservices.catalogplaylist.Application;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, RibbonClientConfiguration.class})
@WebIntegrationTest({"server.port=0"})
public class IntegrationTest {
    @Value("${local.server.port}")
    int port;

    @Autowired
    ILoadBalancer loadBalancer;

    MockWebServer mockWebServer;
    RestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void setup() throws Exception{
        String item1 = IOUtils.toString(getClass().getResourceAsStream("/item1.json"));
        String item2 = IOUtils.toString(getClass().getResourceAsStream("/item2.json"));

        mockWebServer = new MockWebServer();
        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest recordedRequest) throws InterruptedException {
                if (recordedRequest.getPath().equals("/catalog/v1/items/4321?expand=relatedItems")) {
                    return new MockResponse().setBody(item1).setResponseCode(200).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                } else if (recordedRequest.getPath().equals("/catalog/v1/items/1234?expand=relatedItems")) {
                    return new MockResponse().setBody(item2).setResponseCode(200).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                }
                return new MockResponse().setResponseCode(404);
            }
        };
        mockWebServer.setDispatcher(dispatcher);
        mockWebServer.start();

        BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) loadBalancer;
        baseLoadBalancer.setServersList(Arrays.asList(new Server(mockWebServer.getHostName(), mockWebServer.getPort())));
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void testMusicPlaylist() {
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:" + port + "/catalog/v1/playlist/4321/jwplayer.rss", String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void testRadioPlaylist() {
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:" + port + "/catalog/v1/playlist/1234/jwplayer.rss", String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }


}

@Configuration
class RibbonClientConfiguration {
    @Bean
    public ILoadBalancer ribbonLoadBalancer() {
        return new BaseLoadBalancer();
    }
}