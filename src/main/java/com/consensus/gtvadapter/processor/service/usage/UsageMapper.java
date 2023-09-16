package com.consensus.gtvadapter.processor.service.usage;

import com.consensus.gtvadapter.common.models.gtv.usage.UsageCreationGtvData;
import com.consensus.gtvadapter.common.models.gtv.usage.UsageUom;
import com.consensus.gtvadapter.common.models.rawdata.IspUsageData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.time.ZoneOffset.UTC;

@Component
@RequiredArgsConstructor
public class UsageMapper {

    private static final DateTimeFormatter ISP_DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(UTC);
    private static final Map<String, String> resourceTypes = Map.of(
            "INBOX_GENERIC", "local",
            "INBOX_ONLY", "local",
            "INBOX_TOLLFREE", "toll-free",
            "INBOX_ONLY_TOLLFREE", "toll-free");

    //TODO Finish mappings
    public UsageCreationGtvData mapToUsageCreationGtvData(IspUsageData ispUsageData) {
        ZonedDateTime billingDateTime = ZonedDateTime.parse(ispUsageData.getBillingDateTime(), ISP_DATE_PATTERN);
        UsageCreationGtvData.UsageCreationGtvDataBuilder usageEventBuilder = UsageCreationGtvData.builder()
                .startTime(billingDateTime)
                .endTime(billingDateTime)
                .usageUom(UsageUom.COUNT)
                .usageAmount(getUsageAmount(ispUsageData.getDuration(), ispUsageData.getPages()))
                .referenceId(ispUsageData.getMsgId())
                .sequenceId(ispUsageData.getCustomerKey())
                .text03(ispUsageData.getCurrencyCode())
                .text04(ispUsageData.getCustomerKey() + "|" + ispUsageData.getServiceKey());
        if (isInbound(ispUsageData)) {
            usageEventBuilder
                    .serviceResourceIdentifier(ispUsageData.getPhoneNumber())
                    .text01("receive")
                    .text02(resourceTypes.get(ispUsageData.getResourceType()));
        } else {
            usageEventBuilder
                    .serviceResourceIdentifier(ispUsageData.getServiceKey())
                    .text01("send")
                    .text02(ispUsageData.getServiceType().toLowerCase())
                    .text05(ispUsageData.getMessageToEmail());
            //.number01("TBD")
        }
        return usageEventBuilder.build();
    }

    private boolean isInbound(IspUsageData ispUsageData) {
        return resourceTypes.containsKey(ispUsageData.getResourceType());
    }

    private Integer getUsageAmount(Integer duration, Integer pages) {
        int durationInMinutes = Math.round(duration / 60f);

        if (durationInMinutes > pages) {
            return durationInMinutes;
        } else {
            return pages;
        }
    }
}
