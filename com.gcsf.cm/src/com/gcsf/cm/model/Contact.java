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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Contact implements Cloneable {

  private final PropertyChangeSupport myChangeSupport;

  private String mySourceFile = null;

  private String myFirstName = ""; //$NON-NLS-1$

  private String myMiddleName = ""; //$NON-NLS-1$

  private String myLastName = ""; //$NON-NLS-1$

  private String myTitle = ""; //$NON-NLS-1$

  private String myCompany = ""; //$NON-NLS-1$

  private String myJobTitle = ""; //$NON-NLS-1$

  private String myStreet = ""; //$NON-NLS-1$

  private String myCity = ""; //$NON-NLS-1$

  private String myZip = ""; //$NON-NLS-1$

  private String myState = ""; //$NON-NLS-1$

  private String myCountry = ""; //$NON-NLS-1$

  private String myEmail = ""; //$NON-NLS-1$

  private String myWebPage = ""; //$NON-NLS-1$

  private String myPhone = ""; //$NON-NLS-1$

  private String myMobile = ""; //$NON-NLS-1$

  private String myNote = ""; //$NON-NLS-1$

  private String myJpegString = ""; //$NON-NLS-1$

  public Contact() {
    myChangeSupport = new PropertyChangeSupport(this);
  }

  public Contact(String aFirstName, String aLastName, String aEmail) {
    this();
    this.myFirstName = aFirstName;
    this.myLastName = aLastName;
    this.myEmail = aEmail;
  }

  public void addPropertyChangeListener(String aPropertyName,
      PropertyChangeListener aListener) {
    myChangeSupport.addPropertyChangeListener(aPropertyName, aListener);
  }

  public void removePropertyChangeListener(String aPropertyName,
      PropertyChangeListener aListener) {
    myChangeSupport.removePropertyChangeListener(aPropertyName, aListener);
  }

  public void setSourceFile(String aSourceFile) {
    this.mySourceFile = aSourceFile;
  }

  public String getSourceFile() {
    return mySourceFile;
  }

  public String getFirstName() {
    return myFirstName;
  }

  public void setFirstName(String aFirstName) {
    String oldFirstName = this.myFirstName;
    this.myFirstName = aFirstName;
    myChangeSupport.firePropertyChange("myFirstName", oldFirstName, aFirstName); //$NON-NLS-1$
  }

  public String getLastName() {
    return myLastName;
  }

  public void setLastName(String aLastName) {
    String oldLastName = this.myLastName;
    this.myLastName = aLastName;
    myChangeSupport.firePropertyChange("myLastName", oldLastName, aLastName); //$NON-NLS-1$
  }

  public String getMiddleName() {
    return myMiddleName;
  }

  public void setMiddleName(String aMiddleName) {
    this.myMiddleName = aMiddleName;
  }

  public String getTitle() {
    return myTitle;
  }

  public void setTitle(String aTitle) {
    this.myTitle = aTitle;
  }

  public String getCompany() {
    return myCompany;
  }

  public void setCompany(String aCompany) {
    this.myCompany = aCompany;
  }

  public String getJobTitle() {
    return myJobTitle;
  }

  public void setJobTitle(String aJobTitle) {
    this.myJobTitle = aJobTitle;
  }

  public String getStreet() {
    return myStreet;
  }

  public void setStreet(String aStreet) {
    this.myStreet = aStreet;
  }

  public String getCity() {
    return myCity;
  }

  public void setCity(String aBusinessCity) {
    this.myCity = aBusinessCity;
  }

  public String getZip() {
    return myZip;
  }

  public void setZip(String aBusinessZip) {
    this.myZip = aBusinessZip;
  }

  public String getState() {
    return myState;
  }

  public void setState(String aBusinessState) {
    this.myState = aBusinessState;
  }

  public String getCountry() {
    return myCountry;
  }

  public void setCountry(String aBusinessCountry) {
    this.myCountry = aBusinessCountry;
  }

  public String getEmail() {
    return myEmail;
  }

  public void setEmail(String aBusinessEmail) {
    this.myEmail = aBusinessEmail;
  }

  public String getWebPage() {
    return myWebPage;
  }

  public void setWebPage(String aBusinessWebPage) {
    this.myWebPage = aBusinessWebPage;
  }

  public String getPhone() {
    return myPhone;
  }

  public void setPhone(String aBusinessPhone) {
    this.myPhone = aBusinessPhone;
  }

  public String getMobile() {
    return myMobile;
  }

  public void setMobile(String aBusinessMobile) {
    this.myMobile = aBusinessMobile;
  }

  public String getNote() {
    return myNote;
  }

  public void setNote(String aComment) {
    this.myNote = aComment;
  }

  public void setJpegString(String aJpegString) {
    this.myJpegString = aJpegString;
  }

  public String getJpegString() {
    return myJpegString;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public String toString() {
    return myFirstName + " " + myLastName + " " + myEmail; //$NON-NLS-1$ //$NON-NLS-2$
  }
}
