package opendota.opendotaAPI;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
@Component
public class OpendotaAPI {
    RestTemplate restTemplate;
    public OpendotaAPI() {
        restTemplate = new RestTemplate();
    }
    public String fetchPlayerData(String accountId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.opendota.com/api/players/" + accountId);
        return restTemplate.getForObject(builder.toUriString(), String.class);
    }
}
