import javax.swing.*;
public class QueueList extends List {

    public QueueList() {
        super();

    }

    @Override
    public void setModel(ListModel<Download> model) {
        super.setModel(model);
    }

    @Override
    public ListModel<Download> getModel() {
        return super.getModel();
    }
}
