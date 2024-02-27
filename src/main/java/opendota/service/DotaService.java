package opendota.service;

import opendota.openDotaApi.openDotaApi;
import org.springframework.stereotype.Service;

@Service
public class DotaService {
    openDotaApi api;
    public DotaService(openDotaApi api) {
            this.api = api;
    }
    public String getAccountData(String accountId) {
        return api.fetchPlayerData(accountId);
    }
}
