/*******************************************************************************
 * Copyright (c) 2009 Siemens AG and others.
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Kai Toedter - initial implementation
 ******************************************************************************/

package com.gcsf.cm.model;

import org.eclipse.core.databinding.observable.list.IObservableList;

public interface IContactsRepository {
  IObservableList getAllContacts();

  void addContact(Contact aContact);

  void removeContact(Contact aContact);
}
