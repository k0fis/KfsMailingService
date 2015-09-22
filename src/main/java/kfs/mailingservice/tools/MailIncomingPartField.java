package kfs.mailingservice.tools;

import com.vaadin.data.Validator;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import kfs.kfsvaalib.kfsForm.MFieldGroup;
import kfs.kfsvaalib.listener.KfsButtonClickListener;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.mailingservice.domain.MailIncomingPart;
import kfs.mailingservice.utils.MailingServiceException;

/**
 *
 * @author pavedrim
 */
public class MailIncomingPartField extends CustomField<MailIncomingPart> {

    private final Button dl;
    private final MFieldGroup fg;

    public MailIncomingPartField(KfsI18n i18n, String i18nPrefix, String formName) {
        dl = new Button(i18n.getMsg(i18nPrefix + ".imageDl"), new KfsButtonClickListener(this, "dlClick"));
        fg = new MFieldGroup(i18n, formName, null, MailIncomingPart.class);

    }

    public CharSequence validate(String prefix) {
        return fg.validate(prefix);
    }

    @Override
    protected Component initContent() {
        HorizontalLayout hl = new HorizontalLayout(dl);
        hl.setSpacing(true);

        FormLayout form = new FormLayout(fg.getSortedComponents());
        form.addComponent(hl);
        form.setMargin(true);
        return form;
    }

    @Override
    protected void setInternalValue(MailIncomingPart newValue) {
        super.setInternalValue(newValue);
        fg.setItems(newValue);
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        super.commit();
        try {
            fg.commit();
        } catch (Exception ex) {
            throw new MailingServiceException("Cannot commit data", ex);
        }
    }

    @Override
    public Class getType() {
        return MailIncomingPart.class;
    }

    public void dlClick(Button.ClickEvent event) {
        if ((getInternalValue() != null) && (getInternalValue().getData() != null)
                && (getInternalValue().getData().length > 0)) {
            StreamResource stream = new StreamResource(
                    new StreamResource.StreamSource() {
                        @Override
                        public InputStream getStream() {
                            return new ByteArrayInputStream(getInternalValue().getData());
                        }
                    }, getInternalValue().getFilename());
            stream.setMIMEType(getInternalValue().getContentType());
            FileDownloader fileDownloader = new FileDownloader(stream);
            fileDownloader.extend(dl);
        }
    }

}
