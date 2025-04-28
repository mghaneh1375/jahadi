package four.group.jahadi.Utility;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTransformers;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static four.group.jahadi.Utility.StaticValues.DEV_MODE;

public class Utility {
    private static final SimpleDateFormat sdfSSSXXX;
    private static final ZoneId tehranZoneId = ZoneId.of("Asia/Tehran");
    private static final SimpleDateFormat simpleDateFormat;
    private static final DateTimeFormatter dateTimeFormatter;

    static {
        sdfSSSXXX = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdfSSSXXX.setTimeZone(TimeZone.getTimeZone("UTC"));
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneOffset.UTC);
    }

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom rnd = new SecureRandom();
    private static final Random random = new Random();

    public static String convertDateToSimpleJalali(Date date) {
        String[] splited = simpleDateFormat.format(date).split("-");
        return JalaliCalendar.gregorianToJalali(new JalaliCalendar.YearMonthDate(splited[0], splited[1], splited[2])).format("/");
    }
    public static String convertUTCDateToJalali(Date date) {
        String[] dateTime = dateTimeFormatter.format(convertToUTCInstant(date)).split(" ");
        String[] splited = dateTime[0].split("-");
        return JalaliCalendar.gregorianToJalali(new JalaliCalendar.YearMonthDate(splited[0], splited[1], splited[2])).format("/") + " - " + dateTime[1];
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static int convertStringToDate(String date) {
        return Integer.parseInt(date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10));
    }

    public static String convertPersianDigits(String number) {

        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {

            char ch = number.charAt(i);

            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';

            chars[i] = ch;
        }

        return new String(chars);
    }

    public static JSONObject convertPersian(JSONObject jsonObject) {

        for (String key : jsonObject.keySet()) {
            if(key.toLowerCase().contains("password") ||
                    key.equalsIgnoreCase("code") ||
                    key.equals("NID")
            )
                jsonObject.put(key, Utility.convertPersianDigits(jsonObject.get(key).toString()));
            else if (jsonObject.get(key) instanceof Integer)
                jsonObject.put(key, Integer.parseInt(Utility.convertPersianDigits(jsonObject.getInt(key) + "")));
            else if (jsonObject.get(key) instanceof String) {
                String str = Utility.convertPersianDigits(jsonObject.getString(key));
                if(str.charAt(0) == '0' ||
                        key.equalsIgnoreCase("phone") ||
                        key.equalsIgnoreCase("tel") ||
                        key.equals("NID")
                )
                    jsonObject.put(key, str);
                else {
                    try {
                        jsonObject.put(key, Integer.parseInt(str));
                    } catch (Exception x) {
                        jsonObject.put(key, str);
                    }
                }
            }
        }

        return jsonObject;
    }

    public static boolean validationNationalCode(String code) {

        if (code.length() != 10)
            return false;

        try {
            long nationalCode = Long.parseLong(code);
            byte[] arrayNationalCode = new byte[10];

            //extract digits from number
            for (int i = 0; i < 10; i++) {
                arrayNationalCode[i] = (byte) (nationalCode % 10);
                nationalCode = nationalCode / 10;
            }

            //Checking the control digit
            int sum = 0;
            for (int i = 9; i > 0; i--)
                sum += arrayNationalCode[i] * (i + 1);
            int temp = sum % 11;
            if (temp < 2)
                return arrayNationalCode[0] == temp;
            else
                return arrayNationalCode[0] == 11 - temp;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static void printException(Exception x) {

        x.printStackTrace();
        System.out.println(x.getMessage());
        int limit = x.getStackTrace().length > 5 ? 5 : x.getStackTrace().length;
        for (int i = 0; i < limit; i++)
            System.out.println(x.getStackTrace()[i]);

    }

    public static int randInt() {

        if (DEV_MODE)
            return 111111;

        int r = 0;
        for (int i = 0; i < 6; i++) {
            int x = random.nextInt(10);

            while (x == 0)
                x = random.nextInt(10);

            r += x * Math.pow(10, i);
        }

        return r;
    }

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static Date getCurrDate() {
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }

    public static Date getDate(Date date) {
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }

    public static String printNullableField(Object obj) {
        return obj == null ? null : String.format("\"%s\"", obj);
    }

    public static String printNullableInteger(Integer obj) {
        return obj == null ? null : String.format("%d", obj);
    }

    public static boolean isUtcAfter(Date dateToCheck, Date referenceDate) {
        Instant utcInstantToCheck = convertToUTCInstant(dateToCheck);
        Instant utcReferenceInstant = convertToInstant(referenceDate);
        return utcInstantToCheck.isAfter(utcReferenceInstant);
    }

    public static boolean isUtcBefore(Date dateToCheck, Date referenceDate) {
        Instant utcInstantToCheck = convertToUTCInstant(dateToCheck);
        Instant utcReferenceInstant = convertToInstant(referenceDate);
        return utcInstantToCheck.isBefore(utcReferenceInstant);
    }

    public static Instant convertToInstant(Date date) {
        Instant instant = date.toInstant();
        ZonedDateTime tehranTime = instant.atZone(tehranZoneId);
        ZonedDateTime utcTime = tehranTime.withZoneSameLocal(ZoneOffset.UTC);
        return utcTime.toInstant();
    }

    public static Instant convertToUTCInstant(Date date) {
        Instant instant = date.toInstant();
        ZonedDateTime tehranTime = instant.atZone(tehranZoneId);
        ZonedDateTime utcTime = tehranTime.withZoneSameInstant(ZoneOffset.UTC);
        return utcTime.toInstant();
    }

    public static String printNullableDate(Date obj) {
        if (obj == null) return null;
        return "\"" + sdfSSSXXX.format(obj) + "\"";
    }

    public static String toStringOfList(List<?> objects) {
        if(objects == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < objects.size(); i++) {
            sb.append("\"").append(objects.get(i).toString()).append("\"");
            if (i < objects.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static String toStringOfPairValue(List<PairValue> pairValues) {
        if(pairValues == null || pairValues.size() == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < pairValues.size(); i++) {
            PairValue pairValue = pairValues.get(i);
            if(pairValue.getKey() == null || pairValue.getValue() == null)
                continue;
            sb.append(String.format("{\"key\":\"%s\", \"value\":\"%s\"}", pairValue.getKey().toString(), pairValue.getValue().toString()));
            if (i < pairValues.size() - 1)
                sb.append(", ");
        }
        return sb.append("]").toString();
    }

    public static String toStringOfHasMap(HashMap<?, Integer> hashMap) {
        if(hashMap == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        AtomicInteger i = new AtomicInteger(0);
        final int size = hashMap.keySet().size();
        hashMap.keySet().forEach(s -> {
            sb.append(String.format("\"%s\":%d", s.toString(), hashMap.get(s)));
            if(i.get() < size - 1)
                sb.append(", ");
            i.getAndIncrement();
        });
        return sb.append("}").toString();
    }

    private static final ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSourceNameTransformer(NameTransformers.JAVABEANS_MUTATOR)
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass) {
        return entityList.stream()
                .map(entity -> modelMapper.map(entity, outCLass))
                .collect(Collectors.toList());
    }

    public static <S, D> D map(final S source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }
    public static <D, T> D map(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    public static Map<String, String> tabIcons = new HashMap<>(){{
        put("آزمایشگاه", "icon-laboratory");
        put("غربالگری", "icon-health-1");
        put("متخصص ها", "icon-doctor-1");
        put("پزشک", "icon-advice");
        put("توان بخشی", "icon-disability");
        put("پاراکلینیک", "icon-injection-1");
    }};
}
