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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.gcsf.cm.editors.ContactEditorInput;
import com.gcsf.cm.editors.DetailsEditor;
import com.gcsf.cm.model.Contact;

public class OpenEditorHandler extends AbstractHandler implements IHandler {

  @Override
  public Object execute(ExecutionEvent aEvent) throws ExecutionException {
    Contact theContact = null;
    IEvaluationContext context = (IEvaluationContext) aEvent
        .getApplicationContext();
    Object varValue = context.getVariable("new_contact"); //$NON-NLS-1$
    if (varValue != null) {
      theContact = (Contact) varValue;
    } else {
      theContact = new Contact();
    }
    IWorkbenchWindow window = HandlerUtil
        .getActiveWorkbenchWindowChecked(aEvent);
    final IWorkbenchPage page = window.getActivePage();
    final ContactEditorInput input = new ContactEditorInput(theContact);

    Display.getDefault().asyncExec(new Runnable() {
      @Override
      public void run() {
        try {
          page.closeAllEditors(true);
          page.openEditor(input, DetailsEditor.EDITOR_ID);
        } catch (PartInitException e) {
          throw new RuntimeException(e);
        }
      }

    });

    // IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(aEvent);
    // final IWorkbenchPage page = window.getActivePage();
    // // Get the selection
    // ISelection selection = page.getSelection(ContactsListView.VIEW_ID);
    //
    // if (selection != null && selection instanceof IStructuredSelection) {
    // Object obj = ((IStructuredSelection) selection).getFirstElement();
    // // If we had a selection lets open the editor
    // if (obj != null) {
    // Contact contact = (Contact) obj;
    // final ContactEditorInput input = new ContactEditorInput(contact);
    //
    // Display.getDefault().asyncExec(new Runnable() {
    // @Override
    // public void run() {
    // try {
    // // System.out.println("closing of editors triggered ...");
    // page.closeAllEditors(true);
    // // System.out.println("closing of editors done ... ");
    // page.openEditor(input, DetailsEditor.EDITOR_ID);
    // } catch (PartInitException e) {
    // throw new RuntimeException(e);
    // }
    // }
    //
    // });
    //
    // } else {
    // System.out.println("the selection object is null");
    // }
    // }
    return null;
  }
}
