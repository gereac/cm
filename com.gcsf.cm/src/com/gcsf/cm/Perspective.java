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

package com.gcsf.cm;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.gcsf.cm.views.ContactsListView;

public class Perspective implements IPerspectiveFactory {

  /**
   * The ID of the perspective as specified in the extension.
   */
  public static final String ID = "com.gcsf.cm.perspective"; //$NON-NLS-1$

  @Override
  public void createInitialLayout(IPageLayout aLayout) {
    String editorArea = aLayout.getEditorArea();
    aLayout.setEditorAreaVisible(true);

    aLayout.addStandaloneView(ContactsListView.VIEW_ID, true, IPageLayout.LEFT,
        0.25f, editorArea);
    aLayout.getViewLayout(ContactsListView.VIEW_ID).setCloseable(false);
  }
}
