package kfs.mailingservice.tools;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.lang.reflect.Field;
import kfs.kfsvaalib.comps.KfsButton;
import kfs.kfsvaalib.comps.KfsButtonConfirm;
import kfs.kfsvaalib.kfsTable.KfsTable;
import kfs.kfsvaalib.kfsTable.Pos;
import kfs.kfsvaalib.listener.KfsButtonClickListener;
import kfs.kfsvaalib.listener.KfsButtonConfirmCallback;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailAttach;
import kfs.mailingservice.domain.MailTemplate;
import kfs.mailingservice.domain.MailTemplateAttach;
import kfs.mailingservice.service.MailingService;

/**
 *
 * @author pavedrim
 */
public class MailTemplateAttachList extends VerticalLayout implements KfsRefresh,
        KfsTable.TableGeneratorFactory, Table.ColumnGenerator {

    private final MailTemplateAttachDlg editDlg;
    private final MailingService maSvc;
    private final BeanItemContainer<MailTemplateAttach> cont;
    private final String editLabel;
    private final String delLabel;
    private final String delTitleLabel;
    private final String delQuestionLabel;
    private final String delYesLabel;
    private final String delNoLabel;
    private final UI ui;
    private final Window parent;
    private final Button addButton;
    private MailTemplate template;

    public MailTemplateAttachList(UI ui, KfsI18n i18n, MailingService maSvc, MailTemplateAttachDlg edit, Window parent) {
        this(ui, i18n, maSvc, edit, parent, "MailAttachList", "MailAttachList");
    }

    public MailTemplateAttachList(UI ui, KfsI18n i18n, MailingService maSvc, MailTemplateAttachDlg edit, Window parent,
            String tableName, String i18nPrefix) {
        this.ui = ui;
        this.editDlg = edit;
        this.maSvc = maSvc;
        this.parent = parent;
        this.editLabel = i18n.getMsg(i18nPrefix + ".edit");
        this.delLabel = i18n.getMsg(i18nPrefix + ".del");
        this.delTitleLabel = i18n.getMsg(i18nPrefix + ".delTitle");
        this.delQuestionLabel = i18n.getMsg(i18nPrefix + ".delQuestion");
        this.delYesLabel = i18n.getMsg(i18nPrefix + ".delYes");
        this.delNoLabel = i18n.getMsg(i18nPrefix + ".delNo");
        this.cont = new BeanItemContainer<>(MailTemplateAttach.class);
        KfsTable<MailTemplateAttach> table;
        addComponents(
                (table = new KfsTable<>(tableName, i18n, MailTemplateAttach.class,
                        i18n.getMsg(i18nPrefix + ".title"), cont, null, this)),
                (addButton = new Button(i18n.getMsg(i18nPrefix + ".add"), new KfsButtonClickListener(this, "newClick")))
        );
        table.setWidth("100%");
        table.setHeight("500px");
        setSpacing(true);
        setMargin(true);
    }

    @Override
    public void kfsRefresh() {
        cont.removeAllItems();
        cont.addAll(maSvc.mailTemplateAttachLoad(template));
    }

    public void show(MailTemplate template) {
        this.template = template;
        addButton.setEnabled(this.template != null);
        kfsRefresh();
    }

    @Override
    public Table.ColumnGenerator getColumnGenerator(Class type, Field field, Pos pos) {
        if (field.getName().equals("id") || field.getName().equals("attach")) {
            return this;
        }
        return null;
    }

    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        MailTemplateAttach ku = (MailTemplateAttach) itemId;
        if ("name0".equals(columnId)) {
            return new Label(ku.getAttach().getAttachName());
        } else if ("id0".equals(columnId)) {
            Button edit = new KfsButton<>(editLabel, ku, new KfsButtonClickListener(this, "editClick"));
            edit.addStyleName("small");
            Button del = new KfsButtonConfirm(delLabel, ui, delTitleLabel, delQuestionLabel,
                    delYesLabel, delNoLabel, new KfsButtonConfirmCallback(this, "delClick"),
                    null, null, parent, ku);
            del.addStyleName("small");
            HorizontalLayout hl = new HorizontalLayout(edit, del);
            return hl;
        }
        return null;
    }

    public void newClick(Button.ClickEvent event) {
        if (editDlg != null) {
            MailTemplateAttach ma = new MailTemplateAttach();
            ma.setTemplate(template);
            ma.setAttach(new MailAttach());
            cont.addItem(ma);
            editDlg.show(ma, this);
        }
    }

    public void editClick(Button.ClickEvent event) {
        if (editDlg != null) {
            editDlg.show(((KfsButton<MailTemplateAttach>) event.getButton()).getButtonData(), this);
        }
    }

    public void delClick(MailTemplateAttach ma) {
        if (ma != null) {
            if (ma.getId() != null) {
                maSvc.mailTemplateAttachDelete(ma);
            }
            cont.removeItem(ma);
        }
    }

}
