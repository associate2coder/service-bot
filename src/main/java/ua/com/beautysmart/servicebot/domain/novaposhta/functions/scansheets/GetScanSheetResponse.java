package ua.com.beautysmart.servicebot.domain.novaposhta.functions.scansheets;

import lombok.Getter;
import ua.com.beautysmart.servicebot.domain.novaposhta.common.NovaPoshtaBaseResponse;

/**
 * Author: associate2coder
 */

@Getter
public class GetScanSheetResponse extends NovaPoshtaBaseResponse {

    private ScanSheet[] data;

}
