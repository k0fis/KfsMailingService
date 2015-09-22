package kfs.mailingservice.tools;

import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.List;
import kfs.kfsvaalib.fields.KfsEditorField;
import kfs.kfsvaalib.fields.KfsSelectField;
import kfs.kfsvaalib.kfsForm.KfsField;
import kfs.kfsvaalib.kfsForm.MFieldGroup;
import kfs.kfsvaalib.listener.KfsButtonClickListener;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailAddress;
import kfs.mailingservice.domain.MailFooter;
import kfs.mailingservice.domain.MailTemplate;
import kfs.mailingservice.service.MailingService;

/**
 *
 * @author pavedrim
 */
public class MailTemplateDlg extends Window implements KfsEditorField.Editor<MailTemplate>,
        MFieldGroup.MFieldFactory {

    private final UI ui;
    private final KfsI18n i18n;
    private final MFieldGroup fg;
    private final String i18nPref;
    private final MailingService maSvc;
    private final MailTemplateAttachList alist;
    private final KfsEditorField edMailAddress;
    private final KfsSelectField mailFooterSelect;
    private final KfsEditorField ccListSelect;
    private MailTemplate ma;
    private KfsRefresh kfsRefresh;

    public MailTemplateDlg(UI ui, KfsI18n i18n, MailingService maSvc) {
        this(ui, i18n, maSvc, new MailTemplateAttachDlg(ui, i18n, maSvc),
                "MailTemplateDlg", "MailTemplateDlg");
    }

    public MailTemplateDlg(UI ui, KfsI18n i18n, MailingService maSvc, MailTemplateAttachDlg edit,
            String i18nPrefix, String formName) {
        super(i18n.getMsg(i18nPrefix + ".title"));
        this.ui = ui;
        this.i18n = i18n;
        this.maSvc = maSvc;
        this.i18nPref = i18nPrefix;
        MailAddressDlg de = new MailAddressDlg(ui, i18n, maSvc);
        edMailAddress = new KfsEditorField<>(i18n.getMsg(i18nPrefix + ".editAddress"),
                MailAddress.class, de, null);
        mailFooterSelect = new KfsSelectField<MailFooter>(ui, MailFooter.class, i18n, "MailFooterSelect",
                i18nPrefix, true, i18n.getMsg(i18nPrefix + ".selFooter"), null) {

                    @Override
                    protected List<MailFooter> getValues() {
                        return MailTemplateDlg.this.maSvc.mailFooterLoad();
                    }

                    @Override
                    public String getKfsInfo(MailFooter value) {
                        if (value == null) {
                            return "";
                        }
                        return value.getName();
                    }

                };
        ccListSelect = new KfsEditorField<>(i18n.getMsg(i18nPrefix + ".editCCAddress"),
                List.class, new MailAddressListDlg(ui, i18n, de, "MailTemplateCcSelect"), null);

        alist = new MailTemplateAttachList(ui, i18n, maSvc, edit, edit);
        alist.setWidth("500px");
        fg = new MFieldGroup(i18n, formName, this, MailTemplate.class);
        FormLayout form = new FormLayout(fg.getSortedComponents());
        form.addComponent(new Button(i18n.getMsg(i18nPrefix + ".save"),
                new KfsButtonClickListener(this, "saveClick")));
        form.setMargin(true);
        form.setWidth("600px");
        HorizontalLayout hl = new HorizontalLayout(form, alist);
        hl.setSpacing(true);
        hl.setMargin(true);
        setContent(hl);
        setModal(true);
    }

    @Override
    public void show(MailTemplate ma, KfsRefresh kfsRefresh) {
        this.ma = ma;
        this.kfsRefresh = kfsRefresh;
        fg.setItems(ma);
        alist.setEnabled(ma.getId() != null);
        if (ma.getId() != null) {
            alist.show(ma);
        }
        ui.addWindow(this);
    }

    public void saveClick(Button.ClickEvent event) {
        try {
            fg.commit();
        } catch (Exception ex) {
            CharSequence val = fg.validate("");
            Notification.show(i18n.getMsg(i18nPref + ".cannotSave") + ":\n" + val,
                    Notification.Type.WARNING_MESSAGE);
        }
        maSvc.mailTemplateSave(ma);
        Notification.show(i18n.getMsg(i18nPref + ".saved"));
        if (kfsRefresh != null) {
            kfsRefresh.kfsRefresh();
        }
        if (alist.isEnabled())
        close(); 
        else {
            alist.setEnabled(true);
            alist.show(ma);
        }
    }

    @Override
    public Field createField(Class objectClass, String filedName, KfsField kfsField, Class fieldClass) {
        if (fieldClass.equals(MailAddress.class)) {
            return edMailAddress;
        } else if (fieldClass.equals(MailFooter.class)) {
            return mailFooterSelect;
        } else if (filedName.equals("ccList")) {
            return ccListSelect;
        }

        return null;
    }

    @Override
    public String getKfsInfo(MailTemplate ma) {
        if (ma == null) {
            return "";
        }
        return ma.getName();
    }

    @Override
    public MailTemplate getKfsValue() {
        return ma;
    }
}
