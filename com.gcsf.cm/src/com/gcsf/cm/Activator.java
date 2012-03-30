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

package com.gcsf.cm;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "com.gcsf.cm"; //$NON-NLS-1$

  private static final String F_META_AREA = ".metadata"; //$NON-NLS-1$

  private static final String F_PLUGIN_DATA = ".plugins"; //$NON-NLS-1$

  // The shared instance
  private static Activator ourPlugin;

  private BundleContext myContext;

  private IPath myStateLocation;

  @SuppressWarnings("rawtypes")
  private ServiceTracker myLocationServiceTracker;

  /**
   * The constructor
   */
  public Activator() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
   * )
   */
  @Override
  public void start(BundleContext aContext) throws Exception {
    ourPlugin = this;
    this.myContext = aContext;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
   * )
   */
  @Override
  public void stop(BundleContext aContext) throws Exception {
    this.myContext = null;
    ourPlugin = null;
  }

  /**
   * @author Kai Toedter - Siemens AG
   * */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public IPath getStateLocationBis() {
    try {
      if (myStateLocation == null) {
        Filter filter = myContext.createFilter(Location.INSTANCE_FILTER);
        if (myLocationServiceTracker == null) {
          myLocationServiceTracker = new ServiceTracker(myContext, filter, null);
          myLocationServiceTracker.open();
        }
        Location location = (Location) myLocationServiceTracker.getService();
        if (location != null) {
          IPath path = new Path(location.getURL().getPath());
          myStateLocation = path.append(F_META_AREA).append(F_PLUGIN_DATA)
              .append(myContext.getBundle().getSymbolicName());
          myStateLocation.toFile().mkdirs();
        }
      }
    } catch (InvalidSyntaxException e) {
      // ignore this. It should never happen as we have tested the above format.
    }
    return myStateLocation;
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static Activator getDefault() {
    return ourPlugin;
  }

  /**
   * Returns an image descriptor for the image file at the given plug-in
   * relative path
   * 
   * @param aPath
   *          the path
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor(String aPath) {
    return imageDescriptorFromPlugin(PLUGIN_ID, aPath);
  }
}
