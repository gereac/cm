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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.gcsf.cm.model.Contact;

public class ContactEditorInput implements IEditorInput {

  private Contact myContact;

  public ContactEditorInput(Contact aContact) {
    if (aContact == null) {
      throw new IllegalArgumentException();
    }
    this.myContact = aContact;
  }

  public Contact getContact() {
    return myContact;
  }

  @Override
  public Object getAdapter(Class aAdapter) {
    return null;
  }

  @Override
  public boolean exists() {
    return false;
  }

  @Override
  public ImageDescriptor getImageDescriptor() {
    return null;
  }

  @Override
  public String getName() {
    return myContact.getEmail();
  }

  @Override
  public IPersistableElement getPersistable() {
    return null;
  }

  @Override
  public String getToolTipText() {
    return myContact.toString();
  }

}
