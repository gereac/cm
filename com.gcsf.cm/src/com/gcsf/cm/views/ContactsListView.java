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

package com.gcsf.cm.views;

import java.util.HashMap;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.IEvaluationService;

import com.gcsf.cm.model.Contact;
import com.gcsf.cm.model.ContactsRepositoryFactory;

public class ContactsListView extends ViewPart {

  public static final String VIEW_ID = "com.gcsf.cm.view.contacsList"; //$NON-NLS-1$

  private TableViewer myContactsViewer;

  /**
   * {@inheritDoc}
   */
  @Override
  public void createPartControl(Composite aParent) {
    // Table composite (because of TableColumnLayout)
    final Composite tableComposite = new Composite(aParent, SWT.NONE);
    tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    final TableColumnLayout tableColumnLayout = new TableColumnLayout();
    tableComposite.setLayout(tableColumnLayout);

    // Table viewer
    myContactsViewer = new TableViewer(tableComposite, SWT.FULL_SELECTION);
    myContactsViewer.getTable().setHeaderVisible(true);
    myContactsViewer.getTable().setLinesVisible(true);
    myContactsViewer.setComparator(new ContactViewerComparator());
    myContactsViewer.getTable().addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent aEvent) {
        // for (TableItem aItem : myContactsViewer.getTable().getSelection()) {
        // System.out.println("item: " + aItem);
        // }
        if (aEvent.item != null) {
          if (((TableItem) aEvent.item).getData() != null) {
            Contact selectedContact = (Contact) ((TableItem) aEvent.item)
                .getData();
            openMyEditor(selectedContact);
          }
        }
      }
    });

    // First name column
    final TableViewerColumn firstNameColumn = new TableViewerColumn(
        myContactsViewer, SWT.NONE);
    firstNameColumn.getColumn().setText("First Name"); //$NON-NLS-1$
    tableColumnLayout.setColumnData(firstNameColumn.getColumn(),
        new ColumnWeightData(40));

    // Last name column
    final TableViewerColumn lastNameColumn = new TableViewerColumn(
        myContactsViewer, SWT.NONE);
    lastNameColumn.getColumn().setText("Last Name"); //$NON-NLS-1$
    tableColumnLayout.setColumnData(lastNameColumn.getColumn(),
        new ColumnWeightData(60));

    ObservableListContentProvider contentProvider = new ObservableListContentProvider();

    myContactsViewer.setContentProvider(contentProvider);

    IObservableMap[] attributes = BeansObservables.observeMaps(
        contentProvider.getKnownElements(), Contact.class, new String[] {
            "firstName", "lastName" }); //$NON-NLS-1$//$NON-NLS-2$
    myContactsViewer
        .setLabelProvider(new ObservableMapLabelProvider(attributes));

    myContactsViewer.setInput(ContactsRepositoryFactory.getContactsRepository()
        .getAllContacts());

    GridLayoutFactory.fillDefaults().generateLayout(aParent);

    getSite().setSelectionProvider(myContactsViewer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFocus() {
    // myContactsViewer.getControl().setFocus();
  }

  @SuppressWarnings("rawtypes")
  private void openMyEditor(Contact aContact) {
    ICommandService commandService = (ICommandService) getViewSite()
        .getService(ICommandService.class);
    IEvaluationService evaluationService = (IEvaluationService) getViewSite()
        .getService(IEvaluationService.class);
    evaluationService.getCurrentState().addVariable("new_contact", aContact); //$NON-NLS-1$
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
  }

  @Override
  public void dispose() {
    super.dispose();
  }

}