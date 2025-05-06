package TOOL;

public class Validation {
    public static boolean isEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    public static boolean isPhoneNumber(String phoneNumber) {
        // SĐT: bắt đầu bằng 0, theo sau là các đầu số hợp lệ (3x, 5x, 7x, 8x, 9x), 10 số
        String phoneRegex = "^0(3[2-9]|5[2-9]|7[06-9]|8[1-9]|9[0-9])\\d{7}$";
        return phoneNumber.matches(phoneRegex);
    }

    public static boolean isCCCD(String cccd) {
        // CCCD: 12 chữ số, không có ký tự đặc biệt
        String cccdRegex = "^\\d{12}$";
        return cccd.matches(cccdRegex);
    }

    public static boolean isName(String str) {
        // Chỉ chứa chữ cái tiếng việt và khoảng trắng
        String nameRegex = "^[\\p{L} ]+$";
        return str.matches(nameRegex);
    }

}
