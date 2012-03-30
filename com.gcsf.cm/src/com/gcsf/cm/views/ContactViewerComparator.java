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

package com.gcsf.cm.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.gcsf.cm.model.Contact;

class ContactViewerComparator extends ViewerComparator {

  @Override
  public int compare(final Viewer aViewer, final Object aObj1, final Object aObj2) {

    if (aObj1 instanceof Contact && aObj2 instanceof Contact) {
      String lastName1 = ((Contact) aObj1).getLastName();
      String lastName2 = ((Contact) aObj2).getLastName();
      if (lastName1 == null) {
        lastName1 = ""; //$NON-NLS-1$
      }
      if (lastName2 == null) {
        lastName2 = ""; //$NON-NLS-1$
      }
      return lastName1.compareTo(lastName2);
    } else {
      throw new IllegalArgumentException("Can only compare two Contacts."); //$NON-NLS-1$
    }

  }
}
