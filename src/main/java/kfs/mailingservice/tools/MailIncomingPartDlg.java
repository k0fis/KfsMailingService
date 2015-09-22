package kfs.mailingservice.tools;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import kfs.kfsvaalib.fields.KfsEditorField;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailIncomingPart;

/**
 *
 * @author pavedrim
 */
public class MailIncomingPartDlg extends Window implements KfsEditorField.Editor<MailIncomingPart> {

    private final UI ui;
    private final MailIncomingPartField data;
    private MailIncomingPart ma;
    private KfsRefresh kfsRefresh;

    public MailIncomingPartDlg(UI ui, KfsI18n i18n) {
        this(ui, i18n, "MailIncomingPartDlg", "MailIncomingPartDlg");
    }

    public MailIncomingPartDlg(UI ui, KfsI18n i18n,
            String i18nPrefix, String formName) {
        super(i18n.getMsg(i18nPrefix + ".title"));
        this.ui = ui;
        data = new MailIncomingPartField(i18n, i18nPrefix, formName);
        setContent(data);
        setModal(true);
    }

    @Override
    public void show(MailIncomingPart ma, KfsRefresh kfsRefresh) {
        this.ma = ma;
        this.kfsRefresh = kfsRefresh;
        data.setValue(ma);
        ui.addWindow(this);
    }

    @Override
    public String getKfsInfo(MailIncomingPart ma) {
        return ma.getFilename();
    }

    @Override
    public MailIncomingPart getKfsValue() {
        return ma;
    }

}
