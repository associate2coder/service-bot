package ua.com.beautysmart.servicebotbackend.domain.novaposhta.functions.paidstorage;


import ua.com.beautysmart.servicebotbackend.domain.entities.Sender;

import java.util.List;

public interface PaidStorageTtnService {

    List<TTN> getTtnForPaidStorage(Sender sender, int day);
}
