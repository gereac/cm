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
package com.gcsf.cm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Catalin Gerea
 * 
 */
public class EditorOpenDirty {

  private static SWTWorkbenchBot myBot;

  @Before
  public void runBeforeEveryTest() {
    System.out.println("This is run before every test");
  }

  @After
  public void runAfterEveryTest() {
    System.out.println("This is run after every test");
  }

  @BeforeClass
  public static void beforeClass() throws Exception {
    myBot = new SWTWorkbenchBot();
  }

  @Test
  public void testToolbarEditorOpenDirty() throws Exception {
    List<SWTBotView> views = myBot.views(WidgetMatcherFactory
        .withPartName("Contact List"));
    assertEquals(1, views.size());
    assertEquals("No editor open", 0, myBot.editors().size());
    SWTBotTable table = myBot.viewByTitle("Contact List").bot().table();
    table.select("Lars");
    assertEquals("One editor open", 1, myBot.editors().size());
    SWTBotEditor editor = myBot.activeEditor();
    assertEquals("Details of Lars Vogel", editor.getTitle());
    assertFalse("Editor without changes -> not dirty", editor.isDirty());
    SWTBot editorBot = editor.bot();
    SWTBotText text = editorBot.textWithLabel("Name:   ");
    assertFalse("Focus to name field", text.isActive());
    text.setText("Heike Muster");
    assertTrue("Editor has become dirty", editor.isDirty());
    SWTBotToolbarButton saveButton = myBot.toolbarButtonWithTooltip("Save");
    assertTrue("Save button not enabled", saveButton.isEnabled());
    SWTBotToolbarButton addButton = myBot.toolbarButtonWithTooltip("Add contact");
    assertTrue("Add button enabled", addButton.isEnabled());
    SWTBotToolbarButton removeButton = myBot
        .toolbarButtonWithTooltip("Delete contact");
    assertTrue("Delete button enabled", removeButton.isEnabled());
    SWTBotMenu saveMenuItem = myBot.menu("File").menu("Save");
    assertTrue("Save menu item enabled", saveMenuItem.isEnabled());
    editor.close();
  }

  @AfterClass
  public static void sleep() {
    myBot.sleep(100);
  }
}
