/*******************************************************************************
 * CogTool Copyright Notice and Distribution Terms
 * CogTool 1.2, Copyright (c) 2005-2012 Carnegie Mellon University
 * This software is distributed under the terms of the FSF Lesser
 * Gnu Public License (see LGPL.txt). 
 * 
 * CogTool is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * CogTool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CogTool; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * CogTool makes use of several third-party components, with the 
 * following notices:
 * 
 * Eclipse SWT
 * Eclipse GEF Draw2D
 * 
 * Unless otherwise indicated, all Content made available by the Eclipse 
 * Foundation is provided to you under the terms and conditions of the Eclipse 
 * Public License Version 1.0 ("EPL"). A copy of the EPL is provided with this 
 * Content and is also available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * CLISP
 * 
 * Copyright (c) Sam Steingold, Bruno Haible 2001-2006
 * This software is distributed under the terms of the FSF Gnu Public License.
 * See COPYRIGHT file in clisp installation folder for more information.
 * 
 * ACT-R 6.0
 * 
 * Copyright (c) 1998-2007 Dan Bothell, Mike Byrne, Christian Lebiere & 
 *                         John R Anderson. 
 * This software is distributed under the terms of the FSF Lesser
 * Gnu Public License (see LGPL.txt).
 * 
 * Apache Jakarta Commons-Lang 2.1
 * 
 * This product contains software developed by the Apache Software Foundation
 * (http://www.apache.org/)
 * 
 * Mozilla XULRunner 1.9.0.5
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/.
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * 
 * The J2SE(TM) Java Runtime Environment
 * 
 * Copyright 2009 Sun Microsystems, Inc., 4150
 * Network Circle, Santa Clara, California 95054, U.S.A.  All
 * rights reserved. U.S.  
 * See the LICENSE file in the jre folder for more information.
 ******************************************************************************/

package edu.cmu.cs.hcii.cogtool.view;

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.swt.graphics.Cursor;

/**
 * Holds the resize widgets
 * @author alexeiser
 *
 */
public class InteractionFigure extends Clickable
{
    public interface IEnterExitListener
    {
        public void observeEnter();
        public void observeExit();
    }

    protected static class FigureFilterSearch implements TreeSearch
    {
        /**
         * The filter to use for this search
         */
        protected Class<? extends IFigure> filterClass;

        public FigureFilterSearch(Class<? extends IFigure> toFilter)
        {
            filterClass = toFilter;
        }

        public void setFilter(Class<? extends IFigure> newFigureFilter)
        {
            filterClass = newFigureFilter;
        }

        /**
         * used to test if the figure passes the filter.
         */

        public boolean accept(IFigure figure)
        {
            return filterClass.isInstance(figure);
        }

        /**
         * Prune this figure from the search space.
         */

        public boolean prune(IFigure figure)
        {
            return false;
        }
    }

    /**
     * An ONLY for the figure filtering search.
     * By default it returns all figures.
     */
    protected static final FigureFilterSearch figureFilter =
        new FigureFilterSearch(IFigure.class);

    protected IEnterExitListener enterExitListener;
    protected MouseMotionListener motionListener;
    protected MouseListener clickListener;

    public InteractionFigure(IEnterExitListener enterExitL,
                             MouseMotionListener motionL,
                             MouseListener clickL,
                             Cursor figureCursor)
    {
        enterExitListener = enterExitL;
        motionListener = motionL;
        clickListener = clickL;

        setLayoutManager(new XYLayout());

        setCursor(figureCursor);
    }

    public MouseMotionListener getMouseMotionListener()
    {
        return motionListener;
    }

    public MouseListener getClickListener()
    {
        return clickListener;
    }

    public void setMouseMotionListener(MouseMotionListener motionL)
    {
        if (motionListener != motionL) {
            motionListener = motionL;
        }
    }

    public void setClickListener(MouseListener clickL)
    {
        if (clickListener != clickL) {
            clickListener = clickL;
        }
    }

    // Mouse event forwarders

    @Override
    public void handleMouseMoved(MouseEvent evt)
    {
        if (motionListener != null) {
            motionListener.mouseMoved(evt);
        }
    }

    @Override
    public void handleMousePressed(MouseEvent evt)
    {
        if (clickListener != null) {
            clickListener.mousePressed(evt);
        }
    }

    @Override
    public void handleMouseDoubleClicked(MouseEvent evt)
    {
        if (clickListener != null) {
            clickListener.mouseDoubleClicked(evt);
        }
    }

    @Override
    public void handleMouseReleased(MouseEvent evt)
    {
        if (clickListener != null) {
            clickListener.mouseReleased(evt);
        }
    }

    @Override
    public void handleMouseEntered(MouseEvent evt)
    {
        enterExitListener.observeEnter();
        if (motionListener != null) {
            motionListener.mouseEntered(evt);
        }
    }

    @Override
    public void handleMouseExited(MouseEvent evt)
    {
        enterExitListener.observeExit();
        if (motionListener != null) {
            motionListener.mouseExited(evt);
        }
    }

    @Override
    public void handleMouseHover(MouseEvent evt)
    {
        if (motionListener != null) {
            motionListener.mouseHover(evt);
        }
    }

    @Override
    public void handleMouseDragged(MouseEvent evt)
    {
        if (motionListener != null) {
            motionListener.mouseDragged(evt);
        }
    }

    public IFigure findFigureAt(int x,
                                int y,
                                Class<? extends IFigure> filterClass)
    {
        figureFilter.setFilter(filterClass);

        return findFigureAt(x, y, figureFilter);
    }
}