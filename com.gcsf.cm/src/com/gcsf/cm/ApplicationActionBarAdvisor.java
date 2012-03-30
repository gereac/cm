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

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

  public ApplicationActionBarAdvisor(IActionBarConfigurer aConfigurer) {
    super(aConfigurer);
  }

  @Override
  protected void makeActions(final IWorkbenchWindow aWindow) {
    // nothing to do
  }

  @Override
  protected void fillMenuBar(IMenuManager aMenuBar) {
    // nothing to do
  }

  @Override
  protected void fillCoolBar(ICoolBarManager aCoolBar) {
    // nothing to do
  }
}
