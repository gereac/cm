/*******************************************************************************
 * Copyright (c) 2011 Kai Toedter and others.
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Kai Toedter - initial API and implementation
 ******************************************************************************/

package com.gcsf.cm.editors;

import java.io.ByteArrayInputStream;
import java.net.URL;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.internal.signedcontent.Base64;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.gcsf.cm.model.Contact;
import com.gcsf.cm.model.internal.AggregateNameObservableValue;

public class DetailComposite extends Composite {

  private final DetailsEditor myDirtyable;

  private Contact myOriginalContact;

  private Contact myClonedContact;

  private boolean myCommitChanges = false;

  private Label myImageLabel;

  private Image myDummyPortrait;

  private boolean myGeneralGroup;

  private final DataBindingContext myDbc;

  private WritableValue myContactValue;

  private final IObservableValue myScaledImage;

  public DetailComposite(DetailsEditor aDirtyable, final Composite aParent) {
    super(aParent, SWT.NONE);
    this.myDirtyable = aDirtyable;

    myContactValue = new WritableValue(
        ((ContactEditorInput) aDirtyable.getEditorInput()).getContact(), null);

    aParent.getShell().setBackgroundMode(SWT.INHERIT_DEFAULT);

    myDbc = new DataBindingContext();

    URL url = FileLocator.find(Platform.getBundle("com.gcsf.cm"), new Path( //$NON-NLS-1$
        "images/dummy.png"), null); //$NON-NLS-1$
    ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
    if (imageDescriptor != null) {
      myDummyPortrait = imageDescriptor.createImage();
    }

    final GridLayout layout = new GridLayout(1, false);
    layout.verticalSpacing = 5;
    setLayout(layout);

    // General
    final Composite composite = createComposite(this);

    createSeparator(composite, "General"); //$NON-NLS-1$

    createText(composite, "Title:", "title"); //$NON-NLS-1$ //$NON-NLS-2$
    createText(composite, "Name:", "name"); // Leads to Aggregate //$NON-NLS-1$ //$NON-NLS-2$
    // "firstName" "middleName"
    // "lastName"
    createText(composite, "Company:", "company"); //$NON-NLS-1$ //$NON-NLS-2$
    createText(composite, "Job Title:", "jobTitle"); //$NON-NLS-1$ //$NON-NLS-2$
    createText(composite, "Note:", "note"); //$NON-NLS-1$ //$NON-NLS-2$

    createVerticalSpace(composite);

    // Business Address
    createSeparator(composite, "Business Address "); //$NON-NLS-1$
    createText(composite, "Street:", "street"); //$NON-NLS-1$ //$NON-NLS-2$
    createText(composite, "City:", "city"); //$NON-NLS-1$ //$NON-NLS-2$
    createText(composite, "ZIP:", "zip"); //$NON-NLS-1$ //$NON-NLS-2$
    createText(composite, "State/Prov:", "state"); //$NON-NLS-1$ //$NON-NLS-2$
    createText(composite, "Country:", "country"); //$NON-NLS-1$ //$NON-NLS-2$
    createVerticalSpace(composite);

    // Business Phone
    createSeparator(composite, "Business Phones "); //$NON-NLS-1$
    createText(composite, "Phone:", "phone"); //$NON-NLS-1$ //$NON-NLS-2$
    createText(composite, "Mobile:", "mobile"); //$NON-NLS-1$ //$NON-NLS-2$
    createVerticalSpace(composite);

    // Business Internet
    createSeparator(composite, "Business Internet"); //$NON-NLS-1$
    createText(composite, "Email:", "email"); //$NON-NLS-1$ //$NON-NLS-2$
    createText(composite, "Web Page:", "webPage"); //$NON-NLS-1$ //$NON-NLS-2$
    createVerticalSpace(composite);

    // Bind the image
    final IObservableValue imageObservableValue = PojoObservables
        .observeDetailValue(myContactValue, "jpegString", String.class); //$NON-NLS-1$

    this.myScaledImage = new ComputedValue() {
      private Image myCurrentImage;

      @Override
      protected Object calculate() {
        String jpegString = (String) imageObservableValue.getValue();
        Image image = null;
        if (jpegString == null || jpegString.equals("")) { //$NON-NLS-1$
          image = myDummyPortrait;
        } else {
          byte[] imageBytes = Base64.decode(jpegString.getBytes());
          ByteArrayInputStream is = new ByteArrayInputStream(imageBytes);
          ImageData imageData = new ImageData(is);
          image = new Image(Display.getCurrent(), imageData);
        }
        ImageData imageData = image.getImageData();
        double ratio = imageData.height / 85.0;
        int width = (int) (imageData.width / ratio);
        int height = (int) (imageData.height / ratio);
        ImageData scaledImageData = imageData.scaledTo(width, height);
        if (myCurrentImage != null) {
          myCurrentImage.dispose();
          myCurrentImage = null;
        }
        myCurrentImage = new Image(Display.getCurrent(), scaledImageData);
        return myCurrentImage;
      }

      @Override
      public void dispose() {
        if (myCurrentImage != null) {
          myCurrentImage.dispose();
          myCurrentImage = null;
        }
        super.dispose();
      }

    };

    myDbc.bindValue(SWTObservables.observeImage(myImageLabel), myScaledImage,
        new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), null);

    addDisposeListener(new DisposeListener() {
      @Override
      public void widgetDisposed(DisposeEvent aEvent) {
        myDummyPortrait.dispose();
        myScaledImage.dispose();
      }
    });

    myCommitChanges = true;
  }

  private void setDirty(boolean aDirty) {
    myDirtyable.setDirty(aDirty);
  }

  public boolean checkEmptyString(Object aTestString) {
    if (aTestString == null || !(aTestString instanceof String)
        || ((String) aTestString).trim().length() == 0) {
      return false;
    }
    return true;
  }

  private void createSeparator(Composite aParent, String aText) {
    myGeneralGroup = aText.equals("General"); //$NON-NLS-1$

    final Label label = new Label(aParent, SWT.NONE);
    label.setText(aText + "     "); //$NON-NLS-1$
    // label.setData("org.eclipse.e4.ui.css.id", "SeparatorLabel");
    GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    gridData.horizontalSpan = 3;
    label.setLayoutData(gridData);

    // final Label separator = new Label(parent, SWT.SEPARATOR
    // | SWT.HORIZONTAL);
    // GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    // gridData.horizontalIndent = -100;
    // gridData.verticalIndent = 5;
    // gridData.horizontalSpan = 2;
    // separator.setLayoutData(gridData);
  }

  private void createVerticalSpace(Composite aParent) {
    final Label label2 = new Label(aParent, SWT.SEPARATOR | SWT.HORIZONTAL);

    label2.setVisible(false);
    GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
    gridData.horizontalSpan = 3;
    label2.setLayoutData(gridData);
  }

  private static Composite createComposite(final Composite aParent) {
    final Composite composite = new Composite(aParent, SWT.NONE);
    composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    composite.setLayout(new GridLayout(3, false));
    return composite;
  }

  private Text createText(final Composite aParent, final String aLabelText,
      final String aProperty) {
    final Label label = new Label(aParent, SWT.NONE);
    label.setText(aLabelText + "   "); // the extra space is due to a bug in //$NON-NLS-1$
    // font formatting when using css
    // styling
    GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    gridData.horizontalIndent = 20;
    label.setLayoutData(gridData);

    final Text text = new Text(aParent, SWT.NONE);

    GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
    gridData2.horizontalIndent = 0;
    if (!myGeneralGroup) {
      gridData2.horizontalSpan = 2;
    } else {
      gridData2.horizontalSpan = 1;
      if (aLabelText.equals("Title:")) { //$NON-NLS-1$
        // The label image is set with data binding
        myImageLabel = new Label(aParent, SWT.NONE);
        GridData gridData3 = new GridData();
        gridData3.verticalSpan = 5;
        myImageLabel.setLayoutData(gridData3);
      }
    }
    text.setLayoutData(gridData2);

    if (aProperty != null) {
      if (aProperty.equals("name")) { //$NON-NLS-1$
        myDbc.bindValue(SWTObservables.observeText(text, SWT.Modify),
            new AggregateNameObservableValue(myContactValue));
      } else {
        myDbc.bindValue(SWTObservables.observeText(text, SWT.Modify),
            PojoObservables.observeDetailValue(myContactValue, aProperty,
                String.class));
      }
    }

    text.addModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent aEvent) {
        if (myCommitChanges) {
          setDirty(true);
        }
      }
    });

    return text;
  }

  public Contact getOriginalContact() {
    return myOriginalContact;
  }

  public Contact getModifiedContact() {
    return myClonedContact;
  }

  private void setTextEnabled(Composite aComposite, boolean aEnabled) {
    for (Control control : aComposite.getChildren()) {
      if (control instanceof Composite) {
        setTextEnabled((Composite) control, aEnabled);
      } else if (control instanceof Text) {
        control.setEnabled(aEnabled);
      }
    }
  }

  public void update(final Contact aContact) {
    if (aContact == null) {
      myCommitChanges = false;
      setTextEnabled(this, false);
      myContactValue.setValue(null);
    } else {
      setTextEnabled(this, true);

      myCommitChanges = false;
      try {
        myClonedContact = (Contact) aContact.clone();
        myOriginalContact = aContact;
        myContactValue.setValue(myClonedContact);
        myCommitChanges = true;
      } catch (CloneNotSupportedException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
