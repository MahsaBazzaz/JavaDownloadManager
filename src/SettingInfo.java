import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class SettingInfo implements Serializable{

    private static final long serialVersionUID = -7110496132244189173L;
    public Integer numberOfDownloads = null;
    public File address = null;
    public Integer lookAndFeelIndex = null;

    public SettingInfo(Integer s,File f,int i){
        numberOfDownloads = s;
        address = f;
        lookAndFeelIndex = i;

    }

//    public File getAddress() {
//        System.out.println();
//        return address;
//    }
//
//    public Integer getLookAndFeelIndex() {
//        return lookAndFeelIndex;
//    }
//
//    public String getNumberOfDownloads() {
//        return numberOfDownloads;
//    }

}
