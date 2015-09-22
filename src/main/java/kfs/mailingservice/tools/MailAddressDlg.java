package kfs.mailingservice.tools;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import kfs.kfsvaalib.fields.KfsEditorField;
import kfs.kfsvaalib.kfsForm.MFieldGroup;
import kfs.kfsvaalib.listener.KfsButtonClickListener;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailAddress;
import kfs.mailingservice.service.MailingService;

/**
 *
 * @author pavedrim
 */
public class MailAddressDlg extends Window implements KfsEditorField.Editor<MailAddress> {

    private final UI ui;
    private final MFieldGroup fg;
    protected final KfsI18n i18n;
    private final String i18nPref;
    private final MailingService maSvc;
    private MailAddress ma;
    private KfsRefresh kfsRefresh;

    public MailAddressDlg(UI ui, KfsI18n i18n, MailingService maSvc) {
        this(ui, i18n, maSvc, "MailAddressDlg", "MailAddressDlg");
    }

    public MailAddressDlg(UI ui, KfsI18n i18n, MailingService maSvc,
            String i18nPrefix, String formName) {
        super(i18n.getMsg(i18nPrefix + ".title"));
        this.ui = ui;
        this.i18n = i18n;
        this.maSvc = maSvc;
        this.i18nPref = i18nPrefix;
        fg = new MFieldGroup(i18n, formName, null, MailAddress.class);
        FormLayout form = new FormLayout(fg.getSortedComponents());
        form.addComponent(new Button(i18n.getMsg(i18nPrefix + ".save"), new KfsButtonClickListener(this, "saveClick")));
        form.setMargin(true);
        setContent(form);
        setModal(true);
        setWidth("500px");
    }

    @Override
    public void show(MailAddress ma, KfsRefresh kfsRefresh) {
        this.ma = ma;
        this.kfsRefresh = kfsRefresh;
        fg.setItems(ma);
        setCaption(getKfsInfo(ma));
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
        maSvc.mailAddressSave(ma);
        Notification.show(i18n.getMsg(i18nPref + ".saved"));
        if (kfsRefresh != null) {
            kfsRefresh.kfsRefresh();
        }
        close();
    }

    @Override
    public String getKfsInfo(MailAddress ma) {
        if (ma == null) {
            return "";
        }
        if (ma.getName().length() > 0) {
            return ma.getName() + "<" + ma.getAddress() + ">";
        }
        return ma.getAddress();
    }

    @Override
    public MailAddress getKfsValue() {
        return ma;
    }
}
