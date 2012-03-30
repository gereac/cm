/*******************************************************************************
 * Copyright (c) 2009, 2010 Siemens AG and others.
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.gcsf.cm.Activator;
import com.gcsf.cm.model.Contact;
import com.gcsf.cm.model.IContactsRepository;

public class VCardContactsRepository implements IContactsRepository {

  private final IObservableList myContacts;

  public VCardContactsRepository() {
    List<Contact> contacts = new ArrayList<Contact>();
    try {
      for (File file : getContacts()) {
        Contact contact = readFromVCard(file.getAbsolutePath());
        contacts.add(contact);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    this.myContacts = new WritableList(contacts, null);
  }

  private File[] getContacts() throws Exception {
    File[] localContacts = getLocalContacts();
    if (localContacts.length == 0) {
      IPath path = Activator.getDefault().getStateLocationBis();
      byte[] buffer = new byte[8192];
      Bundle bundle = Platform.getBundle("com.gcsf.cm"); //$NON-NLS-1$

      for (Enumeration<?> contacts = getStoredContacts(); contacts
          .hasMoreElements();) {
        String bundlePath = (String) contacts.nextElement();
        if (!bundlePath.endsWith(".vcf")) { //$NON-NLS-1$
          continue;
        }

        InputStream inputStream = FileLocator.openStream(bundle, new Path(
            bundlePath), false);
        FileOutputStream outputStream = new FileOutputStream(path.append(
            bundlePath.substring(bundlePath.indexOf('/') + 1)).toFile());

        int read = inputStream.read(buffer);
        while (read != -1) {
          outputStream.write(buffer, 0, read);
          read = inputStream.read(buffer);
        }

        inputStream.close();
        outputStream.close();
      }

      return getLocalContacts();
    }
    return localContacts;
  }

  private File[] getLocalContacts() {
    IPath path = Activator.getDefault().getStateLocationBis();
    File directory = path.toFile();
    return directory.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File aDir, String aName) {
        return aName.endsWith(".vcf"); //$NON-NLS-1$
      }
    });
  }

  private Enumeration<?> getStoredContacts() throws Exception {
    Bundle bundle = Platform.getBundle("com.gcsf.cm"); //$NON-NLS-1$
    return bundle.getEntryPaths("vcards"); //$NON-NLS-1$
  }

  @Override
  public void addContact(final Contact aContact) {
    myContacts.add(aContact);
  }

  @Override
  public IObservableList getAllContacts() {
    return myContacts;
  }

  @Override
  public void removeContact(final Contact aContact) {
    myContacts.remove(aContact);
  }

  /**
   * Reads a Contact from a VCard. This method cannot parse a generic VCard, but
   * can only parse VCards created with Microsoft Outlook. The intention is not
   * to provide a generic VCard reader but an easy way to get contact data
   * (including pictures) in the repository.
   * 
   * @param aFileName
   *          the vcard file
   * @return the created Contact
   */
  private Contact readFromVCard(String aFileName) {
    Contact contact = new Contact();
    contact.setSourceFile(aFileName);
    BufferedReader bufferedReader = null;
    String charSet = "Cp1252"; //$NON-NLS-1$

    /*
     * first try to guess the char set (currently not working under some JVMs
     */

    /*
     * try { bufferedReader = new BufferedReader(new InputStreamReader( new
     * FileInputStream(fileName))); String line; while ((line =
     * bufferedReader.readLine()) != null) { int index =
     * line.indexOf("CHARSET="); if (index != -1) { int endIndex = index + 8;
     * while (line.charAt(endIndex) != ':' && line.charAt(endIndex) != ';') {
     * endIndex += 1; } charSet = line.substring(index + 8, endIndex); break; }
     * } } catch (FileNotFoundException e) { // TODO Auto-generated catch block
     * e.printStackTrace();
     * 
     * } catch (IOException e) { // TODO Auto-generated catch block
     * e.printStackTrace(); } finally { try { if (bufferedReader != null) {
     * bufferedReader.close(); } } catch (IOException e) { // TODO
     * Auto-generated catch block e.printStackTrace(); } }
     */

    // Then parse the vCard
    try {
      InputStreamReader inputStreamReader = new InputStreamReader(
          new FileInputStream(aFileName), charSet);
      bufferedReader = new BufferedReader(inputStreamReader);
      String line;
      String value;
      while ((line = bufferedReader.readLine()) != null) {
        value = getVCardValue(line, "N"); //$NON-NLS-1$
        if (value != null) {
          String[] result = value.split(";"); //$NON-NLS-1$

          if (result.length > 0) {
            contact.setLastName(result[0]);
          }
          if (result.length > 1) {
            contact.setFirstName(result[1]);
          }
          if (result.length > 2) {
            contact.setMiddleName(result[2]);
          }
          if (result.length > 3) {
            contact.setTitle(result[3]);
          }
          continue;
        }
        value = getVCardValue(line, "TEL;WORK"); //$NON-NLS-1$
        if (value != null) {
          contact.setPhone(value);
          continue;
        }
        value = getVCardValue(line, "TEL;CELL"); //$NON-NLS-1$
        if (value != null) {
          contact.setMobile(value);
          continue;
        }
        value = getVCardValue(line, "ADR;WORK"); //$NON-NLS-1$
        if (value != null) {
          String[] result = value.split(";"); //$NON-NLS-1$

          if (result.length > 2) {
            contact.setStreet(result[2]);
          }
          if (result.length > 3) {
            contact.setCity(result[3]);
          }
          if (result.length > 4) {
            contact.setState(result[4]);
          }
          if (result.length > 5) {
            contact.setZip(result[5]);
          }
          if (result.length > 6) {
            contact.setCountry(result[6]);
          }
          continue;
        }
        value = getVCardValue(line, "EMAIL;PREF;INTERNET"); //$NON-NLS-1$
        if (value != null) {
          contact.setEmail(value);
          continue;
        }
        value = getVCardValue(line, "URL;WORK"); //$NON-NLS-1$
        if (value != null) {
          contact.setWebPage(value);
          continue;
        }
        value = getVCardValue(line, "ORG"); //$NON-NLS-1$
        if (value != null) {
          contact.setCompany(value);
          continue;
        }
        value = getVCardValue(line, "TITLE"); //$NON-NLS-1$
        if (value != null) {
          contact.setJobTitle(value);
          continue;
        }
        value = getVCardValue(line, "NOTE"); //$NON-NLS-1$
        if (value != null) {
          contact.setNote(value);
          continue;
        }
        value = getVCardValue(line, "PHOTO;TYPE=JPEG;ENCODING=BASE64"); //$NON-NLS-1$
        if (value != null) {
          line = bufferedReader.readLine();
          StringBuilder builder = new StringBuilder();
          while (line != null && line.length() > 0 && line.charAt(0) == ' ') {
            builder.append(line.trim());
            line = bufferedReader.readLine();
          }
          String jpegString = builder.toString();

          contact.setJpegString(jpegString);
          continue;
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return contact;
  }

  private String getVCardValue(String aLine, String aToken) {
    if (aLine.startsWith(aToken + ":") || aLine.startsWith(aToken + ";")) { //$NON-NLS-1$ //$NON-NLS-2$
      String value = aLine.substring(aLine.indexOf(':') + 1);
      return value;
    }
    return null;
  }

}