package kfs.mailingservice.tools;

import com.vaadin.data.Validator;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import kfs.kfsvaalib.kfsForm.MFieldGroup;
import kfs.kfsvaalib.listener.KfsButtonClickListener;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.mailingservice.domain.MailAttach;
import kfs.mailingservice.utils.MailingServiceException;

/**
 *
 * @author pavedrim
 */
public class MailAttachField extends CustomField<MailAttach> implements Receiver, SucceededListener {

    private final Button dl;
    private final MFieldGroup fg;
    private final String ulCaption;
    private final String secondUlErr;
    private ByteArrayOutputStream bos;

    public MailAttachField(KfsI18n i18n, String i18nPrefix, String formName) {
        secondUlErr = i18n.getMsg(i18nPrefix + ".errSecondUp");
        ulCaption = i18n.getMsg(i18nPrefix + ".imageUl");
        dl = new Button(i18n.getMsg(i18nPrefix + ".imageDl"), new KfsButtonClickListener(this, "dlClick"));
        fg = new MFieldGroup(i18n, formName, null, MailAttach.class);

    }

    public CharSequence validate(String prefix) {
        return fg.validate(prefix);
    }

    @Override
    protected Component initContent() {
        Upload ul = new Upload(ulCaption, this);
        ul.setButtonCaption(ulCaption);
        ul.addSucceededListener(this);

        HorizontalLayout hl = new HorizontalLayout(ul, dl);
        hl.setComponentAlignment(ul, Alignment.BOTTOM_LEFT);
        hl.setComponentAlignment(dl, Alignment.BOTTOM_LEFT);
        hl.setSpacing(true);

        FormLayout form = new FormLayout(fg.getSortedComponents());
        form.addComponent(hl);
        form.setMargin(true);
        bos = null;
        return form;
    }

    @Override
    protected void setInternalValue(MailAttach newValue) {
        fg.setItems(newValue);
        super.setInternalValue(newValue);
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
        return MailAttach.class;
    }

    public void dlClick(Button.ClickEvent event) {
        if ((getInternalValue() != null) && (getInternalValue().getImage() != null)
                && (getInternalValue().getImage().length > 0)) {
            StreamResource stream = new StreamResource(
                    new StreamResource.StreamSource() {
                        @Override
                        public InputStream getStream() {
                            return new ByteArrayInputStream(getInternalValue().getImage());
                        }
                    }, getInternalValue().getAttachName());
            stream.setMIMEType(getInternalValue().getContentType());
            FileDownloader fileDownloader = new FileDownloader(stream);
            fileDownloader.extend(dl);
        }
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        if (bos == null) {
            getInternalValue().setAttachName(filename);
            getInternalValue().setContentId(filename);
            getInternalValue().setContentType(mimeType);
            fg.setItems(getInternalValue());
            bos = new ByteArrayOutputStream();
            return bos;
        } else {
            Notification.show(secondUlErr, Notification.Type.ERROR_MESSAGE);
            return null;
        }
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        getInternalValue().setImage(bos.toByteArray());
        bos = null;
    }

}
