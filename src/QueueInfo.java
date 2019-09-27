import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

public class QueueInfo implements Serializable {

    private static final long serialVersionUID = 1983515163485755412L;
    public ArrayList<Download> queueInfo= new ArrayList<>();
    public QueueInfo(DefaultListModel<Download> downloads){
        for(int i=0;i<downloads.getSize();i++){
            queueInfo.add(i,downloads.get(i));
        }
    }
}
