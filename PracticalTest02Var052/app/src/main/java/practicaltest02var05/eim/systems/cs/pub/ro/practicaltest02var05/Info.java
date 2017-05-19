package practicaltest02var05.eim.systems.cs.pub.ro.practicaltest02var05;

/**
 * Created by root on 5/19/17.
 */

public class Info {
    private String value;
    private Integer minute;

    public Info() {
        this.value = null;
        this.minute = null;
    }

    public Info(String value, Integer minute) {
        this.value = value;
        this.minute = minute;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return "Info{" +
                "value='" + value + '\'' +
                ", minute=" + minute +
                '}';
    }
}
