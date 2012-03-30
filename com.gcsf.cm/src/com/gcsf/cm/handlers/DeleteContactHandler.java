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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.gcsf.cm.editors.ContactEditorInput;
import com.gcsf.cm.model.Contact;
import com.gcsf.cm.model.ContactsRepositoryFactory;
import com.gcsf.cm.views.ContactsListView;

public class DeleteContactHandler extends AbstractHandler implements IHandler {

  @Override
  public Object execute(ExecutionEvent aEvent) throws ExecutionException {

    Contact contact = null;
    IEditorPart activeEditor = null;

    IWorkbenchWindow window = HandlerUtil
        .getActiveWorkbenchWindowChecked(aEvent);
    IWorkbenchPage page = window.getActivePage();
    activeEditor = page.getActiveEditor();

    if (activeEditor != null) {
      // we have an editor open ... will take the contact from editor
      ContactEditorInput input = (ContactEditorInput) activeEditor
          .getEditorInput();
      contact = input.getContact();
    } else {
      // no editor open ... take the contact from contact view
      // the current selection in the contacts view
      ISelection selection = page.getSelection(ContactsListView.VIEW_ID);
      if (selection != null && !selection.isEmpty()) {
        if (selection instanceof IStructuredSelection) {
          contact = (Contact) ((IStructuredSelection) selection)
              .getFirstElement();
        }
      }
    }

    if (contact != null) {
      boolean deleteConfirmation = MessageDialog.openConfirm(window.getShell(),
          "Delete Contact Confirmation", //$NON-NLS-1$
          "Are you sure that you want to delete the selected contact <" //$NON-NLS-1$
              + contact + ">? "); //$NON-NLS-1$
      if (deleteConfirmation) {
        if (activeEditor != null) {
          // we have to close the editor ... no save is needed in this case
          // even if the editor is myDirty
          page.closeEditor(activeEditor, false);
        }
        ContactsRepositoryFactory.getContactsRepository()
            .removeContact(contact);
      }
    } else {
      System.out.println("nothing to do ... no contact available"); //$NON-NLS-1$
    }
    return null;
  }

}
