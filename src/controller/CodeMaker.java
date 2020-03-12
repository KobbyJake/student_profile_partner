
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package controller;

/**
 *
 * @author Kobby
 */
public class CodeMaker {
    public static enum UserType { SUPERVISOR, REGISTRAR, VERIFYER,VALIDATOR }
    public static enum Validators { NONE, THREE, FIVE,SEVEN, NINE }
    public static enum Feedback { TEXT, TEXT_AND_AUDIO}
    public static enum UserStatus { ACTIVE, BLOCKED, INACTIVE }
    public static enum CodeType { ALPHA, ALPHANUMERIC, NUMERIC }
    public static enum CodeLength { FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,TEN }
    public static enum CodeStatus { VERIFIED, USED, EXPIRED }
    public static enum MessageMode { SMS, SMS_AND_EMAIL,SMS_EMAIL_WITH_FILE, MANUAL}
    public static enum EventStatus { PENDING, RUNNING, COMPLETED }
    public static enum CurrentActivity { REGISTRATION, VERIFICATION, VOTING,REGISTVERIFICATION,REGISTVERIFIVOTING }

    public static String generateCode(int length, CodeType mode) throws Exception {
        StringBuffer buffer     = new StringBuffer();
        String       characters = "";

        switch (mode) {
        case ALPHA :
            characters = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ";

            break;

        case ALPHANUMERIC :
            characters = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ1234567890";

            break;

        case NUMERIC :
            characters = "1234567890";

            break;
        }

        int charactersLength = characters.length();

        for (int i = 0; i < length; i++) {
            double index = Math.random() * charactersLength;

            buffer.append(characters.charAt((int) index));
        }

        return buffer.toString();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
