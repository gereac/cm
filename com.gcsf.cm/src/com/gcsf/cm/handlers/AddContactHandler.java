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

package com.gcsf.cm.handlers;

import java.util.HashMap;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.services.IEvaluationService;

import com.gcsf.cm.model.Contact;

public class AddContactHandler extends AbstractHandler implements IHandler {

  @SuppressWarnings("rawtypes")
  @Override
  public Object execute(ExecutionEvent aEvent) throws ExecutionException {
    ICommandService commandService = (ICommandService) PlatformUI
        .getWorkbench().getService(ICommandService.class);
    IEvaluationService evaluationService = (IEvaluationService) PlatformUI
        .getWorkbench().getService(IEvaluationService.class);

    Contact newContact = new Contact();
    evaluationService.getCurrentState().addVariable("new_contact", newContact); //$NON-NLS-1$

    try {
      Command theCommand = commandService
          .getCommand("com.gcsf.cm.commands.openEditor"); //$NON-NLS-1$
      theCommand.executeWithChecks(new ExecutionEvent(theCommand,
          new HashMap(), null, evaluationService.getCurrentState()));
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (NotDefinedException e) {
      e.printStackTrace();
    } catch (NotEnabledException e) {
      e.printStackTrace();
    } catch (NotHandledException e) {
      e.printStackTrace();
    }

    return null;
  }

}
