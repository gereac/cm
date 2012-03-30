/*******************************************************************************
 * Copyright (c) GCSF Thales and others.
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

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * This workbench advisor creates the window advisor, and specifies the
 * perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

  @Override
  public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
      IWorkbenchWindowConfigurer aConfigurer) {
    return new ApplicationWorkbenchWindowAdvisor(aConfigurer);
  }

  @Override
  public void initialize(IWorkbenchConfigurer aConfigurer) {
    aConfigurer.setSaveAndRestore(true);
  }

  @Override
  public String getInitialWindowPerspectiveId() {
    return Perspective.ID;
  }

}
