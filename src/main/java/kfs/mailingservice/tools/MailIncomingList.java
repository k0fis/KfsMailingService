package kfs.mailingservice.tools;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.lang.reflect.Field;
import java.util.List;
import kfs.kfsvaalib.comps.KfsButton;
import kfs.kfsvaalib.kfsTable.KfsTable;
import kfs.kfsvaalib.kfsTable.Pos;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailAddress;
import kfs.mailingservice.domain.MailIncoming;
import kfs.mailingservice.service.MailingService;

/**
 *
 * @author pavedrim
 */
public class MailIncomingList extends VerticalLayout implements
        KfsTable.TableGeneratorFactory, Table.ColumnGenerator, Button.ClickListener {

    private final UI ui;
    private final String showLabel;
    private final MailIncomingDlg editDlg;
    private final BeanItemContainer<MailIncoming> cont;

    public MailIncomingList(UI ui, KfsI18n i18n, MailIncomingDlg edit, MailingService mservice) {
        this(ui, i18n, edit, mservice, "MailIncomingList", "MailIncomingList");
    }

    public MailIncomingList(UI ui, KfsI18n i18n, MailIncomingDlg edit, MailingService mservice,
            String tableName, String i18nPrefix) {
        this.ui = ui;
        this.editDlg = edit;
        this.showLabel = i18n.getMsg(i18nPrefix + ".show");
        this.cont = new BeanItemContainer<>(MailIncoming.class);
        KfsTable<MailIncoming> table;
        addComponent(
                (table = new KfsTable<>(tableName, i18n, MailIncoming.class,
                        i18n.getMsg(i18nPrefix + ".title"), cont, null, this)));
        table.setWidth("100%");
        table.setHeight("500px");
        setSpacing(true);
        setMargin(true);
    }

    public void show(List<MailIncoming> lst, KfsRefresh kfsRefresh) {
        cont.removeAllItems();
        cont.addAll(lst);
    }

    @Override
    public Table.ColumnGenerator getColumnGenerator(Class type, Field field, Pos pos) {
        if ((type == MailIncoming.class) && (field.getName().equals("id"))) {
            return this;
        }
        return null;
    }

    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        if (itemId instanceof MailAddress) {
            MailAddress ma = (MailAddress) itemId;
            if (ma.getName().length() > 0) {
                return new Label(ma.getName());
            }
            return new Label(ma.getAddress());
        } else if (itemId instanceof MailIncoming) {
            MailIncoming ku = (MailIncoming) itemId;
            KfsButton<MailIncoming> edit = new KfsButton<>(showLabel, ku, this);
            edit.addStyleName("small");
            HorizontalLayout hl = new HorizontalLayout(edit);
            return hl;
        }
        return null;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (editDlg != null) {
            editDlg.show(((KfsButton<MailIncoming>) event.getButton()).getButtonData(), null);
        }
    }
}
