package kfs.mailingservice.tools;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import kfs.kfsvaalib.fields.KfsEditorField;
import kfs.kfsvaalib.kfsForm.MFieldGroup;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailIncoming;
import kfs.mailingservice.service.MailingService;


/**
 *
 * @author pavedrim
 */
public class MailIncomingDlg extends Window implements KfsEditorField.Editor<MailIncoming>{

    private final UI ui;
    private final MFieldGroup fg;
    private final MailIncomingPartList partList;
    private final MailingService maSvc;
    private MailIncoming ma;

    public MailIncomingDlg(UI ui, KfsI18n i18n, MailingService maSvc, 
            MailIncomingPartDlg mailIncomingPartDlg) {
        this(ui, i18n, maSvc, mailIncomingPartDlg, "MailIncomingDlg", "MailIncomingDlg");
    }

    public MailIncomingDlg(UI ui, KfsI18n i18n,MailingService maSvc, 
            MailIncomingPartDlg mailIncomingPartDlg,
            String i18nPrefix, String formName) {
        super(i18n.getMsg(i18nPrefix + ".title"));
        this.ui = ui;
        this.maSvc = maSvc;
        partList = new MailIncomingPartList(i18n, mailIncomingPartDlg);
        fg = new MFieldGroup(i18n, formName, null, MailIncoming.class);
        FormLayout form = new FormLayout(fg.getSortedComponents());
        form.setMargin(true);
        HorizontalLayout hl = new HorizontalLayout(form, partList);
        hl.setComponentAlignment(form, Alignment.TOP_LEFT);
        hl.setComponentAlignment(hl, Alignment.TOP_LEFT);
        hl.setSpacing(true);
        setContent(hl);
        setModal(true);
    }

    @Override
    public void show(MailIncoming ma, KfsRefresh kfsRefresh) {
        this.ma = ma;
        fg.setItems(ma);
        partList.show(maSvc.partLoad(ma), null);
        ui.addWindow(this);
    }

    @Override
    public String getKfsInfo(MailIncoming ma) {
        return ma.getSubject();
    }

    @Override
    public MailIncoming getKfsValue() {
        return ma;
    }


}
