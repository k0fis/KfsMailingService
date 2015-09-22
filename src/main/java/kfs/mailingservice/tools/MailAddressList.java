package kfs.mailingservice.tools;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import kfs.kfsvaalib.comps.KfsButton;
import kfs.kfsvaalib.comps.KfsButtonConfirm;
import kfs.kfsvaalib.kfsTable.KfsTable;
import kfs.kfsvaalib.kfsTable.Pos;
import kfs.kfsvaalib.listener.KfsButtonClickListener;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailAddress;

/**
 *
 * @author pavedrim
 */
public class MailAddressList extends VerticalLayout implements KfsRefresh,
        KfsTable.TableGeneratorFactory, Table.ColumnGenerator {

    private final String editLabel;
    private final String delLabel;
    private final String delTitleLabel;
    private final String delQuestionLabel;
    private final String delYesLabel;
    private final String delNoLabel;
    private final UI ui;
    private final KfsTable<MailAddress> table;
    private final MailAddressDlg editDlg;
    private final BeanItemContainer<MailAddress> cont;
    private KfsRefresh kfsRefresh;

    public MailAddressList(UI ui, KfsI18n i18n, MailAddressDlg edit, 
            String tableName, String i18nPrefix) {
        this.ui = ui;
        this.editDlg = edit;
        this.editLabel = i18n.getMsg(i18nPrefix + ".edit");
        this.delLabel = i18n.getMsg(i18nPrefix + ".del");
        this.delTitleLabel = i18n.getMsg(i18nPrefix + ".delTitle");
        this.delQuestionLabel = i18n.getMsg(i18nPrefix + ".delQuestion");
        this.delYesLabel = i18n.getMsg(i18nPrefix + ".delYes");
        this.delNoLabel = i18n.getMsg(i18nPrefix + ".delNo");

        this.cont = new BeanItemContainer<>(MailAddress.class);
        addComponents(
                new Button(i18n.getMsg(i18nPrefix + ".addNew"),
                        new KfsButtonClickListener(this, "addClick")),
                (table = new KfsTable<>(tableName, i18n, MailAddress.class,
                        i18n.getMsg(i18nPrefix + ".title"), cont, null, this)));
        table.setWidth("100%");
        table.setHeight("500px");
        setSpacing(true);
        setMargin(true);
    }

    public void addClick(Button.ClickEvent event) {
        MailAddress ma = new MailAddress();
        ma.setAddress("");
        ma.setName("");
        cont.addItem(ma);
        editDlg.show(ma, this);
    }

    public List getKfsValue() {
        return new ArrayList(cont.getItemIds());
    }

    public void show(List mal, KfsRefresh kfsRefresh) {
        this.kfsRefresh = kfsRefresh;
        cont.removeAllItems();
        cont.addAll(mal);
    }

    @Override
    public Table.ColumnGenerator getColumnGenerator(Class type, Field field, Pos pos) {
        if ((type == MailAddress.class) && (field.getName().equals("name"))) {
            return this;
        }
        return null;
    }

    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.addStyleName("small");
        MailAddress ku = (MailAddress) itemId;
        Button b;
        b = new KfsButton<>(editLabel, ku, new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                editDlg.show(((KfsButton<MailAddress>) event.getButton()).getButtonData(),
                        MailAddressList.this);
            }
        });
        b.addStyleName("small");
        hl.addComponent(b);
        b = new KfsButtonConfirm(delLabel, ui, delTitleLabel, delQuestionLabel,
                delYesLabel, delNoLabel, new KfsButtonConfirm.IButtonConfirmCallback<MailAddress>() {

                    @Override
                    public void kfsButtonCallback(MailAddress data) {
                        cont.removeItem(data);
                    }
                },
                null, null, null, ku);
        b.addStyleName("small");
        hl.addComponent(b);
        return hl;
    }

    @Override
    public void kfsRefresh() {
        table.refreshRowCache();
        if (kfsRefresh != null) {
            kfsRefresh.kfsRefresh();
        }
    }
}
