package kfs.mailingservice.tools;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import kfs.kfsvaalib.listener.KfsButtonClickListener;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.service.MailingService;

/**
 *
 * @author pavedrim
 */
public class MailTemplateListWin extends Window implements KfsRefresh {

    private final UI ui;
    private final MailTemplateList l;
    private final MailingService maSvc;
    private final MailTemplateDlg edit;

    public MailTemplateListWin(UI ui, KfsI18n i18n, MailingService maSvc, Component... comps) {
        this(ui, i18n, maSvc, new MailTemplateDlg(ui, i18n, maSvc), "MailTemplateListWin", comps);
    }

    public MailTemplateListWin(UI ui, KfsI18n i18n, MailingService maSvc, MailTemplateDlg edit,
            String i18nPrefix, Component... comps) {
        super(i18n.getMsg(i18nPrefix + ".title"));
        this.ui = ui;
        this.edit = edit;
        this.maSvc = maSvc;
        l = new MailTemplateList(ui, i18n, maSvc);
        VerticalLayout vl = new VerticalLayout();
        vl.addComponent(new Button(i18n.getMsg(i18nPrefix + ".add"), 
                new KfsButtonClickListener(this, "clickAdd")));
        vl.addComponent(l);
        vl.addComponents(comps);
        vl.setSpacing(true);
        vl.setMargin(true);
        setContent(vl);
        setSizeFull();
        setModal(true);
    }

    public void show() {
        kfsRefresh();
        ui.addWindow(this);
    }

    @Override
    public void kfsRefresh() {
        l.show(maSvc.mailTemplateLoad(), this);
    }

    public void clickAdd(Button.ClickEvent event) {
        edit.show(maSvc.mailTemplateInit(), this);
    }
}
