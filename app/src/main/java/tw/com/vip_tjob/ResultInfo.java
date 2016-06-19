package tw.com.vip_tjob;

/**
 * Created by fusun on 2016/6/19.
 */
enum InfoType {
    Info, Warnning, Error
}

public class ResultInfo {

    public ResultInfo() {
        result = true;
    }

    public boolean result;
    public String message;
    public int id;
    public String no;
    public String aspnetid;
    public boolean sessionout;
    public InfoType infoType;
    public boolean hasData;
}


