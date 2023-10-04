package ua.com.beautysmart.servicebot.domain.novaposhta.functions.paidstorage;


import ua.com.beautysmart.servicebot.domain.entities.Sender;

import java.util.List;

public interface PaidStorageTtnService {

    List<TTN> getTtnForPaidStorage(Sender sender, int day);
}
