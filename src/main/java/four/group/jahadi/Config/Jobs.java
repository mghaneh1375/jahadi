package four.group.jahadi.Config;

import four.group.jahadi.Repository.ActivationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static four.group.jahadi.Utility.StaticValues.ONE_MIN_MSEC;
import static four.group.jahadi.Utility.StaticValues.SMS_RESEND_MSEC;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Jobs {

    private final ActivationRepository activationRepository;
    private final CacheManager cacheManager;

    @Scheduled(fixedRate = 600000, initialDelay = 5000)
    public void scheduleFixedRateTask() {
        activationRepository.deleteExpired(System.currentTimeMillis() - SMS_RESEND_MSEC);
        // پاک کردن رکوردهایی که بیش از 10 دقیقه از زمان active شدنش میگذرد
        activationRepository.deleteValidatedExpired(System.currentTimeMillis() - ONE_MIN_MSEC * 10);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void evictDailyCache() {
        Cache cache = cacheManager.getCache("firstGroupWithActiveTrip");
        if (cache != null) {
            cache.clear();
        }

        cache = cacheManager.getCache("groupStatisticData");
        if (cache != null) {
            cache.clear();
        }
    }

    @Scheduled(cron = "0 0 */5 * * *")
    public void evictPerStateCache() {
        Cache cache = cacheManager.getCache("areasPerProvince");
        if (cache != null) {
            cache.clear();
        }
        cache = cacheManager.getCache("membersPerProvince");
        if (cache != null) {
            cache.clear();
        }
        cache = cacheManager.getCache("patientsPerProvince");
        if (cache != null) {
            cache.clear();
        }
        cache = cacheManager.getCache("drugsPerProvince");
        if (cache != null) {
            cache.clear();
        }
        cache = cacheManager.getCache("externalServicesPerProvince");
        if (cache != null) {
            cache.clear();
        }
        cache = cacheManager.getCache("finantialExternalServicesPerProvince");
        if (cache != null) {
            cache.clear();
        }
        cache = cacheManager.getCache("perProvinceTrip");
        if (cache != null) {
            cache.clear();
        }
        cache = cacheManager.getCache("patientsStat");
        if (cache != null) {
            cache.clear();
        }
        cache = cacheManager.getCache("patientsAnswers");
        if (cache != null) {
            cache.clear();
        }
        cache = cacheManager.getCache("group-members-report");
        if (cache != null) {
            cache.clear();
        }
        cache = cacheManager.getCache("group-trips-per-month");
        if (cache != null) {
            cache.clear();
        }
        cache = cacheManager.getCache("group-all-trips");
        if (cache != null) {
            cache.clear();
        }
        cache = cacheManager.getCache("group-total-recepted-patients");
        if (cache != null) {
            cache.clear();
        }
    }
}
