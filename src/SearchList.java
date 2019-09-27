import javax.swing.*;

public class SearchList extends List {
    public SearchList(){
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
