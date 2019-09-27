import javax.swing.*;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class DownloadInfo implements Serializable {
    private static final long serialVersionUID = -5070799380715045965L;
    public ArrayList<Download> downloadsInfo = new ArrayList<>();
    public DownloadInfo(DefaultListModel<Download> downloads){
        for(int i=0;i<downloads.getSize();i++){
            downloadsInfo.add(i,downloads.get(i));
        }
    }
}
