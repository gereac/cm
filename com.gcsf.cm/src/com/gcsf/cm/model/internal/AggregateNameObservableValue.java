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

package com.gcsf.cm.model.internal;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.observable.value.WritableValue;

public class AggregateNameObservableValue extends AbstractObservableValue {

  private final IObservableValue[] myObservableValues;

  private final IValueChangeListener myListener;

  private volatile boolean myIsUpdating;

  private String myCurrentStringValue;

  public AggregateNameObservableValue(WritableValue aValue) {
    String[] properties = new String[] { "firstName", "middleName", "lastName" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    myObservableValues = new IObservableValue[properties.length];
    myListener = new IValueChangeListener() {
      @Override
      public void handleValueChange(ValueChangeEvent aEvent) {
        if (!myIsUpdating) {
          fireValueChange(Diffs.createValueDiff(myCurrentStringValue,
              doGetValue()));
        }
      }
    };
    int i = 0;
    for (String property : properties) {
      myObservableValues[i] = PojoObservables.observeDetailValue(aValue,
          property, String.class);
      myObservableValues[i++].addValueChangeListener(myListener);
    }
  }

  @Override
  protected Object doGetValue() {
    StringBuilder builder = new StringBuilder();
    for (IObservableValue value : myObservableValues) {
      String stringValue = ((String) value.getValue());
      if (stringValue != null && stringValue.trim().length() > 0) {
        builder.append(stringValue.trim());
        builder.append(" "); //$NON-NLS-1$
      }
    }
    myCurrentStringValue = builder.toString().trim();
    return myCurrentStringValue;
  }

  @Override
  public void doSetValue(Object aValue) {
    Object oldValue = doGetValue();
    String[] nameValues = ((String) aValue).split(" "); //$NON-NLS-1$

    try {
      myIsUpdating = true;
      if (nameValues.length == 3) {
        for (int i = 0; i < 3; i++) {
          myObservableValues[i].setValue(nameValues[i]);
        }
      } else if (nameValues.length == 2) {
        myObservableValues[0].setValue(nameValues[0]);
        myObservableValues[1].setValue(""); //$NON-NLS-1$
        myObservableValues[2].setValue(nameValues[1]);
      } else if (nameValues.length == 1) {
        myObservableValues[0].setValue(nameValues[0]);
        myObservableValues[1].setValue(""); //$NON-NLS-1$
        myObservableValues[2].setValue(""); //$NON-NLS-1$
      } else {
        for (int i = 0; i < 3; i++) {
          myObservableValues[i].setValue(""); //$NON-NLS-1$
        }
      }
    } finally {
      myIsUpdating = false;
    }
    doGetValue();
    fireValueChange(Diffs.createValueDiff(oldValue, aValue));
  }

  @Override
  public Object getValueType() {
    return String.class;
  }

  @Override
  public synchronized void dispose() {
    super.dispose();
    for (int i = 0; i < myObservableValues.length; i++) {
      myObservableValues[i].removeValueChangeListener(myListener);
    }
  }
}
