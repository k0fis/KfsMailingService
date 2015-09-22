package kfs.mailingservice.tools;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.lang.reflect.Field;
import kfs.kfsvaalib.comps.KfsButton;
import kfs.kfsvaalib.kfsTable.KfsTable;
import kfs.kfsvaalib.kfsTable.Pos;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailAddress;
import kfs.mailingservice.domain.MailForSent;
import kfs.mailingservice.domain.MailTemplate;
import kfs.mailingservice.service.MailingService;

/**
 *
 * @author pavedrim
 */
public class MailForSentList extends Window implements KfsRefresh,
        KfsTable.TableGeneratorFactory, Table.ColumnGenerator, Button.ClickListener {

    private static final int limit = 5000;
    private final UI ui;
    private final KfsI18n i18n;
    private final String i18nPref;
    private final KfsTable<MailForSent> table;
    private final String editLabel;
    private final MailingService msvc;
    private final MailForSentDlg editDlg;
    private final BeanItemContainer<MailForSent> cont;
    private final Button.ClickListener msClickListener;

    public MailForSentList(UI ui, KfsI18n i18n, MailingService msvc, Component... comps) {
        this(ui, i18n, new MailForSentDlg(ui, i18n, msvc), msvc, "MailForSentList", "MailForSentList", comps);
    }

    public MailForSentList(UI ui, KfsI18n i18n, MailForSentDlg edit, MailingService msvc,
        String tableName, String i18nPrefix, Component ... comps) {
        this.ui = ui;
        this.i18n = i18n;
        this.msvc = msvc;
        this.editDlg = edit;
        this.i18nPref = i18nPrefix;
        this.editLabel = i18n.getMsg(i18nPrefix + ".edit");
        this.msClickListener = new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                load(((KfsButton<MailForSent.MailStatus>)event.getButton()).getButtonData());
            }
        };
        HorizontalLayout hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.addComponent(new KfsButton<MailForSent.MailStatus>(getMsName(null), null, msClickListener));
        for (MailForSent.MailStatus ms : MailForSent.MailStatus.values()) {
            hl.addComponent(new KfsButton<>(getMsName(ms), ms, msClickListener));
        }
        this.cont = new BeanItemContainer<>(MailForSent.class);
        VerticalLayout vl = new VerticalLayout(hl,
                (table = new KfsTable<>(tableName, i18n, MailForSent.class,
                        i18n.getMsg(i18nPrefix + ".title"), cont, null, this)));
        table.setWidth("100%");
        table.setHeight("500px");
        vl.setSpacing(true);
        vl.setMargin(true);
        vl.addComponents(comps);
        setContent(vl);
        setSizeFull();
        setModal(true);
    }

    public void show() {
        load(null);
        ui.addWindow(this);
    }

    private void load(MailForSent.MailStatus ms) {
        cont.removeAllItems();
        table.setCaption(getMsName(ms));
        if (ms == null){
            cont.addAll(msvc.mailForSentLoad(limit));
        } else {
            cont.addAll(msvc.mailForSentLoad(ms,limit));
        }
    }

    private String getMsName(MailForSent.MailStatus ms) {
        if (ms == null){
            return i18n.getMsg(i18nPref+".noStatus");
        } else {
            return i18n.getMsg(ms.getClass().getSimpleName()+"."+ms.name());
        }
    }
    
    @Override
    public Table.ColumnGenerator getColumnGenerator(Class type, Field field, Pos pos) {
        if ((type == MailTemplate.class)
                || (type == MailForSent.class) && (field.getName().equals("id"))) {
            return this;
        }
        return null;
    }

    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        MailForSent ku = (MailForSent) itemId;
        if ("mailTemplate".equals(columnId)) {
            return new Label(ku.getMailTemplate().getName());
        } else if ("toAddress".equals(columnId)) {
            MailAddress ma = ku.getFromAddress();
            if (ma.getName().length() > 0) {
                return new Label(ma.getName());
            }
            return new Label(ma.getAddress());
        } else if ("fromAddress".equals(columnId)) {
            MailAddress ma = ku.getFromAddress();
            if (ma.getName().length() > 0) {
                return new Label(ma.getName());
            }
            return new Label(ma.getAddress());
        } else if ("id0".equals(columnId)) {
            KfsButton<MailForSent> edit = new KfsButton<>(editLabel, ku, this);
            edit.addStyleName("small");
            HorizontalLayout hl = new HorizontalLayout(edit);
            return hl;
        }
        return null;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (editDlg != null) {
            editDlg.show(((KfsButton<MailForSent>) event.getButton()).getButtonData(), null);
        }
    }

    @Override
    public void kfsRefresh() {
        table.refreshRowCache();
    }
}
