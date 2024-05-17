package four.group.jahadi.Utility;

import four.group.jahadi.Kavenegar.KavenegarApi;
import four.group.jahadi.Kavenegar.excepctions.ApiException;
import four.group.jahadi.Kavenegar.excepctions.HttpException;
import four.group.jahadi.Kavenegar.models.SendResult;
import four.group.jahadi.Validator.PhoneValidator;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static four.group.jahadi.Utility.StaticValues.DEV_MODE;

public class Utility {

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();
    private static Random random = new Random();

    public static String convertDateToJalali(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String[] dateTime = simpleDateFormat.format(date).split(" ");
        String[] splited = dateTime[0].split("-");
        return JalaliCalendar.gregorianToJalali(new JalaliCalendar.YearMonthDate(splited[0], splited[1], splited[2])).format("/") + " - " + dateTime[1];
    }


    public static String getToday(String delimeter) {
        Locale loc = new Locale("en_US");
        SolarCalendar sc = new SolarCalendar();
        return String.valueOf(sc.year) + delimeter + String.format(loc, "%02d",
                sc.month) + delimeter + String.format(loc, "%02d", sc.date);
    }

    public static int convertStringToDate(String date) {
        return Integer.parseInt(date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10));
    }

    public static String convertIntToDate(Integer d) {
        String date = d + "";
        return date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8);
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

        if(DEV_MODE)
            return true;

        receptor = convertPersianDigits(receptor);

        if(!PhoneValidator.isValid(receptor)) {
            System.out.println("not valid phone num");
            return false;
        }

        try {
            KavenegarApi api = new KavenegarApi("6D3779666A7065566E323932566E526B69756F44564530554752435771647443423336474D6B6F7579556B3D");
            SendResult Result = api.verifyLookup(receptor, token, token2, token3, template);

            if(Result.getStatus() == 6 ||
                    Result.getStatus() == 11 ||
                    Result.getStatus() == 13 ||
                    Result.getStatus() == 14 ||
                    Result.getStatus() == 100
            )
                return false;

            return true;
        } catch (HttpException ex) {
            System.out.print("HttpException  : " + ex.getMessage());
        } catch (ApiException ex) {
            System.out.print("ApiException : " + ex.getMessage());
        }

        return false;
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
}
