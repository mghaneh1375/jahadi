package four.group.jahadi.Utility;

import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Kavenegar.KavenegarApi;
import four.group.jahadi.Kavenegar.excepctions.ApiException;
import four.group.jahadi.Kavenegar.excepctions.HttpException;
import four.group.jahadi.Kavenegar.models.SendResult;
import four.group.jahadi.Validator.PhoneValidator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTransformers;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static four.group.jahadi.Utility.StaticValues.DEV_MODE;
import static org.apache.poi.ss.usermodel.DateUtil.isADateFormat;
import static org.apache.poi.ss.usermodel.DateUtil.isValidExcelDate;

public class Utility {
    private static final SimpleDateFormat sdfSSSXXX;
    public static final ZoneId tehranZoneId = ZoneId.of("Asia/Tehran");
    private static final SimpleDateFormat simpleDateFormat;
    private static final DateTimeFormatter dateTimeFormatter;

    static {
        sdfSSSXXX = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdfSSSXXX.setTimeZone(TimeZone.getTimeZone("UTC"));
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneOffset.UTC);
    }

    public static LocalDateTime getExcelDate(Object value, String exceptionMessage) {
        if (!(value instanceof Date) &&
                !datePattern.matcher(value.toString()).matches() &&
                !gregorianDatePattern.matcher(value.toString()).matches()
        )
            throw new InvalidFieldsException(exceptionMessage);

        if (value instanceof Date)
            return getLocalDateTime((Date) value);

        if (datePattern.matcher(value.toString()).matches())
            return getLocalDateTime(Utility.convertJalaliToGregorianDate(value.toString()));

        return LocalDateTime.parse(value.toString() + "T00:00:00")
                .atZone(tehranZoneId).toLocalDateTime();
    }

    public static boolean isCellDateFormatted(Cell cell) {
        if (cell == null) {
            return false;
        }

        boolean bDate = false;

        double d = 0;

        if (cell.getCellType() == CellType.NUMERIC) {
            d = cell.getNumericCellValue();
            bDate = isValidExcelDate(d);
        }

        if (bDate) {
            CellStyle style = cell.getCellStyle();
            if (style == null)
                return false;
            int i = style.getDataFormat();
            String f = style.getDataFormatString();
            return isADateFormat(i, f);
        }

        return false;
    }

    public static final Pattern datePattern = Pattern.compile(
            "^[1-4]\\d{3}-((0[1-6]-((3[0-1])|([1-2][0-9])|(0[1-9])))|((1[0-2]|(0[7-9]))-(30|([1-2][0-9])|(0[1-9]))))$"
    );

    public static final Pattern gregorianDatePattern = Pattern.compile(
            "^((?:19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$"
    );
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom rnd = new SecureRandom();
    private static final Random random = new Random();

    public static String convertUTCDateToJalali(LocalDateTime date) {
        String[] dateTime = dateTimeFormatter.format(date).split(" ");
        String[] splited = dateTime[0].split("-");
        return JalaliCalendar.gregorianToJalali(new JalaliCalendar.YearMonthDate(splited[0], splited[1], splited[2])).format("/") + " - " + dateTime[1];
    }

    public static Date convertJalaliToGregorianDate(String jaladiDate) {
        String[] splited = jaladiDate.split("-");
        Date date = new Date();
        JalaliCalendar.YearMonthDate gregorian = JalaliCalendar.jalaliToGregorian(
                new JalaliCalendar.YearMonthDate(
                        splited[0], splited[1], splited[2]
                )
        );
        date.setYear(gregorian.getYear() - 1900);
        date.setMonth(gregorian.getMonth());
        date.setDate(gregorian.getDate());
        return date;
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
            if (key.toLowerCase().contains("password") ||
                    key.equalsIgnoreCase("code") ||
                    key.equals("NID")
            )
                jsonObject.put(key, Utility.convertPersianDigits(jsonObject.get(key).toString()));
            else if (jsonObject.get(key) instanceof Integer)
                jsonObject.put(key, Integer.parseInt(Utility.convertPersianDigits(jsonObject.getInt(key) + "")));
            else if (jsonObject.get(key) instanceof String) {
                String str = Utility.convertPersianDigits(jsonObject.getString(key));
                if (str.charAt(0) == '0' ||
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

    public static String generateErr(String msg) {
        return new JSONObject()
                .put("status", "nok")
                .put("msg", msg)
                .toString();
    }

    public static String generateErr(String msg, PairValue... pairValues) {

        JSONObject jsonObject = new JSONObject()
                .put("status", "nok")
                .put("msg", msg);

        for (PairValue p : pairValues)
            jsonObject.put(p.getKey().toString(), p.getValue());

        return jsonObject.toString();
    }

    public static String generateSuccessMsg(String key, Object val, PairValue... pairValues) {

        JSONObject jsonObject = new JSONObject()
                .put("status", "ok")
                .put(key, val);

        for (PairValue p : pairValues)
            jsonObject.put(p.getKey().toString(), p.getValue());

        return jsonObject.toString();

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
        } catch (Exception e) {
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

    public static int randIntForGroupCode() {

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

    public static boolean sendSMS(String receptor, String token,
                                  String token2, String token3,
                                  String template
    ) {
        if (DEV_MODE)
            return true;

        receptor = convertPersianDigits(receptor);

        if (!PhoneValidator.isValid(receptor)) {
            System.out.println("not valid phone num");
            return false;
        }

        try {
            KavenegarApi api = new KavenegarApi("6D3779666A7065566E323932566E526B69756F44564530554752435771647443423336474D6B6F7579556B3D");
            SendResult Result = api.verifyLookup(receptor, token, token2, token3, template);

            return Result.getStatus() != 6 &&
                    Result.getStatus() != 11 &&
                    Result.getStatus() != 13 &&
                    Result.getStatus() != 14 &&
                    Result.getStatus() != 100;
        } catch (HttpException ex) {
            System.out.print("HttpException  : " + ex.getMessage());
        } catch (ApiException ex) {
            System.out.print("ApiException : " + ex.getMessage());
        }

        return false;
    }

    public static LocalDateTime getCurrLocalDateTime() {
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return Instant.ofEpochMilli(date.getTime())
                .atZone(tehranZoneId)
                .toLocalDateTime();
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

    public static Date getLastDate(Date date) {
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return date;
    }

    public static LocalDateTime getLocalDateTime(Date date) {
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return Instant.ofEpochMilli(date.getTime())
                .atZone(tehranZoneId)
                .toLocalDateTime();
    }

    public static LocalDateTime getLocalDateTime(LocalDateTime date) {
        return date.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalDateTime getLastLocalDateTime(Date date) {
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return Instant.ofEpochMilli(date.getTime())
                .atZone(tehranZoneId)
                .toLocalDateTime();
    }

    public static String printNullableField(Object obj) {
        return obj == null ? null : String.format("\"%s\"", obj).replace("\n", " ");
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

    public static String printNullableDate(LocalDateTime obj) {
        if (obj == null) return null;
        return "\"" + obj.toString() + "\"";
    }

    public static String toStringOfList(List<?> objects) {
        if (objects == null) return "[]";
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
        if (pairValues == null || pairValues.size() == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < pairValues.size(); i++) {
            PairValue pairValue = pairValues.get(i);
            if (pairValue.getKey() == null || pairValue.getValue() == null)
                continue;
            sb.append(String.format("{\"key\":\"%s\", \"value\":\"%s\"}", pairValue.getKey().toString(), pairValue.getValue().toString()));
            if (i < pairValues.size() - 1)
                sb.append(", ");
        }
        return sb.append("]").toString();
    }

    public static String toStringOfHasMap(HashMap<?, Integer> hashMap) {
        if (hashMap == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        AtomicInteger i = new AtomicInteger(0);
        final int size = hashMap.keySet().size();
        hashMap.keySet().forEach(s -> {
            sb.append(String.format("\"%s\":%d", s.toString(), hashMap.get(s)));
            if (i.get() < size - 1)
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
}
