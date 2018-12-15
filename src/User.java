import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Class modeling enduser of VOD system, each user has it's own thread and generates requests for VOD materials.
 */
public class User extends Thread{
    private Date birthday;
    private String email, creditCard;
    private long uid;
    Abonament abonament;


    public User() {
        birthday = RandGen.randDate();
        email = Integer.toString(RandGen.randInt(888888, 9999999), 35) + "@gmail.com";
        creditCard = Integer.toString(RandGen.randInt(0, 9999)) + (RandGen.randInt(0, 9999)) + (RandGen.randInt(0, 9999)) + (RandGen.randInt(0, 9999)) + (RandGen.randInt(0, 9999));
        uid = RandGen.randLong(0, Long.MAX_VALUE);
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
