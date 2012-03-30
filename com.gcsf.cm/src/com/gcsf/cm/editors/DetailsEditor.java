/*******************************************************************************
 * Copyright (c) 2012 GCSF and others.
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Catalin Gerea - initial API and implementation
 ******************************************************************************/

package com.gcsf.cm.editors;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.gcsf.cm.model.Contact;

public class DetailsEditor extends EditorPart {

  public static final String EDITOR_ID = "com.gcsf.cm.editor.details"; //$NON-NLS-1$

  protected boolean myDirty = false;

  private DetailComposite myDetailComposite;

  private ContactEditorInput myInput;

  private Contact myContact;

  public DetailsEditor() {
  }

  @Override
  public void createPartControl(Composite aParent) {
    myDetailComposite = new DetailComposite(this, aParent);
    updatePartTitle(myContact);
    aParent.layout(true, true);
    myDetailComposite.update(myContact);
  }

  @Override
  public void setFocus() {
    // nothing to do yet
  }

  private String getName(Contact aContact, String aCharSet) {
    StringBuilder builder = new StringBuilder();
    builder.append("N;").append(aCharSet).append(':'); //$NON-NLS-1$
    builder.append(aContact.getLastName()).append(';');
    builder.append(aContact.getFirstName()).append(';');
    builder.append(aContact.getMiddleName());

    String title = aContact.getTitle();
    if (title.length() != 0) {
      builder.append(';').append(title);
    }

    builder.append('\n');
    return builder.toString();
  }

  private void saveAsVCard(Contact aContact, String aFileName)
    throws IOException {
    String charSet = "CHARSET=" + Charset.defaultCharset().name(); //$NON-NLS-1$
    String vCard = "BEGIN:VCARD" + "\nVERSION:2.1" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        + getName(aContact, charSet) + "FN;" + charSet + ":" //$NON-NLS-1$ //$NON-NLS-2$
        + aContact.getFirstName() + " " + aContact.getLastName() + "\nORG;" //$NON-NLS-1$ //$NON-NLS-2$
        + charSet + ":" + aContact.getCompany() + "\nTITLE:" //$NON-NLS-1$ //$NON-NLS-2$
        + aContact.getJobTitle() + "\nNOTE:" + aContact.getNote() //$NON-NLS-1$
        + "\nTEL;WORK;VOICE:" + aContact.getPhone() + "\nTEL;CELL;VOICE:" //$NON-NLS-1$ //$NON-NLS-2$
        + aContact.getMobile() + "\nADR;WORK;" + charSet + ":" + ";;" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        + aContact.getStreet() + ";" + aContact.getCity() + ";" //$NON-NLS-1$ //$NON-NLS-2$
        + aContact.getState() + ";" + aContact.getZip() + ";" //$NON-NLS-1$ //$NON-NLS-2$
        + aContact.getCountry() + "\nURL;WORK:" + aContact.getWebPage() //$NON-NLS-1$
        + "\nEMAIL;PREF;INTERNET:" + aContact.getEmail() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$

    if (!aContact.getJpegString().equals("")) { //$NON-NLS-1$
      vCard += "PHOTO;TYPE=JPEG;ENCODING=BASE64:\n " + aContact.getJpegString() //$NON-NLS-1$
          + "\n"; //$NON-NLS-1$
    }

    vCard += "END:VCARD\n"; //$NON-NLS-1$

    PrintWriter out = new PrintWriter(aFileName, "Cp1252"); //$NON-NLS-1$
    out.println(vCard);
    out.close();
  }

  @Override
  public void doSave(IProgressMonitor aMonitor) {
    try {
      if (aMonitor == null) {
        aMonitor = new NullProgressMonitor();
      }
      aMonitor.beginTask("Saving myContact details to vCard...", 16); //$NON-NLS-1$

      Contact originalContact = myDetailComposite.getOriginalContact();
      Contact modifiedContact = myDetailComposite.getModifiedContact();

      saveAsVCard(modifiedContact, modifiedContact.getSourceFile());

      originalContact.setCity(modifiedContact.getCity());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setCompany(modifiedContact.getCompany());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setCountry(modifiedContact.getCountry());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setEmail(modifiedContact.getEmail());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setFirstName(modifiedContact.getFirstName());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setJobTitle(modifiedContact.getJobTitle());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setLastName(modifiedContact.getLastName());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setMiddleName(modifiedContact.getMiddleName());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setMobile(modifiedContact.getMobile());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setNote(modifiedContact.getNote());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setPhone(modifiedContact.getPhone());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setState(modifiedContact.getState());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setStreet(modifiedContact.getStreet());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setTitle(modifiedContact.getTitle());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setWebPage(modifiedContact.getWebPage());
      Thread.sleep(50);
      aMonitor.worked(1);

      originalContact.setZip(modifiedContact.getZip());
      Thread.sleep(50);
      aMonitor.worked(1);

      updatePartTitle(originalContact);
      aMonitor.done();

      setDirty(false);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean isSaveOnCloseNeeded() {
    return true;
  }

  private void updatePartTitle(Contact aContact) {
    StringBuffer title = new StringBuffer("Details of "); //$NON-NLS-1$
    title.append(aContact.getFirstName()).append(' ')
        .append(aContact.getLastName());
    setPartName(title.toString());
    // uiItem.setLabel(title.toString());
  }

  @Override
  public void doSaveAs() {
    // nothing to do
  }

  @Override
  public void init(IEditorSite aSite, IEditorInput aInput)
    throws PartInitException {
    if (!(aInput instanceof ContactEditorInput)) {
      throw new RuntimeException("Wrong myInput"); //$NON-NLS-1$
    }

    this.myInput = (ContactEditorInput) aInput;
    setSite(aSite);
    setInput(aInput);
    myContact = myInput.getContact();
  }

  @Override
  public boolean isDirty() {
    return myDirty;
  }

  @Override
  public boolean isSaveAsAllowed() {
    return false;
  }

  protected void setDirty(boolean aValue) {
    myDirty = aValue;
    firePropertyChange(PROP_DIRTY);
  }

  @Override
  public void dispose() {
    super.dispose();
  }
}
