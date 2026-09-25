package four.group.jahadi.Service;

import four.group.jahadi.Models.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class SerialNumberService {
    private final MongoTemplate mongoTemplate;
    private final ConcurrentHashMap<String, AtomicLong> localCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> cacheLimits = new ConcurrentHashMap<>();

    private static final int BATCH_SIZE = 100;

    public String generate(
            String prefix, String counterKey, int digitLength
    ) {
        String cacheKey = prefix + ":" + counterKey;

        AtomicLong localCounter = localCache.get(cacheKey);
        Long limit = cacheLimits.get(cacheKey);

        if (localCounter == null || localCounter.get() > limit) {
            synchronized (this) {
                localCounter = localCache.get(cacheKey);
                limit = cacheLimits.get(cacheKey);

                if (localCounter == null || localCounter.get() > limit) {
                    loadBatch(cacheKey, prefix, counterKey);
                    localCounter = localCache.get(cacheKey);
                    limit = cacheLimits.get(cacheKey);
                }
            }
        }

        long nextValue = localCounter.getAndIncrement();
        return prefix + String.format("%0" + digitLength + "d", nextValue);
    }

    private synchronized void loadBatch(String cacheKey, String prefix, String counterKey) {
        Query query = new Query(Criteria
                .where("counter_key").is(counterKey)
                .and("prefix").is(prefix));

        Update update = new Update().inc("currentValue", BATCH_SIZE);
        Counter counter = mongoTemplate.findAndModify(
                query, update,
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                Counter.class
        );

        if (counter == null) {
            Counter newCounter = Counter.builder()
                    .counterKey(counterKey)
                    .prefix(prefix)
                    .currentValue((long) BATCH_SIZE)
                    .build();

            mongoTemplate.save(newCounter);
            localCache.put(cacheKey, new AtomicLong(1));
            cacheLimits.put(cacheKey, (long) BATCH_SIZE);
        } else {
            long startValue = counter.getCurrentValue() - BATCH_SIZE + 1;
            localCache.put(cacheKey, new AtomicLong(startValue));
            cacheLimits.put(cacheKey, counter.getCurrentValue());
        }
    }
}