package opendota.service;

import opendota.opendotaAPI.OpendotaAPI;
import org.springframework.stereotype.Service;

@Service
public class DotaService {
    OpendotaAPI api;
    public DotaService(OpendotaAPI api) {
            this.api = api;
    }
    public String getAccountData(String accountId) {
        return api.fetchPlayerData(accountId);
    }
}
