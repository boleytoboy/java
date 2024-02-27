package opendota.service;

import opendota.opendotaapi.OpenDotaApi;
import org.springframework.stereotype.Service;

@Service
public class DotaService {
    OpenDotaApi api;
    public DotaService(OpenDotaApi api) {
            this.api = api;
    }
    public String getAccountData(String accountId) {
        return api.fetchPlayerData(accountId);
    }
}
