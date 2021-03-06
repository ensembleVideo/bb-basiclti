/*
    basiclti - Building Block to provide support for Basic LTI
    Copyright (C) 2018  Stephen P Vickers

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

    Contact: stephen@spvsoftwareproducts.com
 */
package org.oscelot.blackboard.lti;

import blackboard.data.navigation.NavigationItem.NavigationType;
import blackboard.data.navigation.NavigationItem.ComponentType;
import blackboard.data.navigation.Mask;

import com.spvsoftwareproducts.blackboard.utils.B2Context;

public final class Mashup extends Placement {

    public Mashup(B2Context b2Context, Tool tool) {

        super(b2Context, tool, "vtbe", "mashup", "vtbe/link.jsp?course_id=@X@course.pk_string@X@&content_id=@X@content.pk_string@X@&globalNavigation=false", Constants.TOOL_MASHUP);
        if (tool == null) {
            boolean enabledMashup = b2Context.getSetting(Constants.MASHUP_PARAMETER, Constants.DATA_FALSE).equals(Constants.DATA_TRUE);
            if (enabledMashup) {
                if (this.navApplication == null) {
                    this.createNavigationApplication(false);
                }
                if (this.navApplication == null) {
                    this.delete();
                } else if (this.navItem == null) {
                    this.createNavigationItem();
                    if (this.navItem == null) {
                        this.delete();
                    }
                } else {
                    this.setName(b2Context.getResourceString("plugin.name", b2Context.getVendorId() + "-" + b2Context.getHandle()));
                    this.setDescription(b2Context.getResourceString("plugin.description", ""));
                    this.setIsAvailable(true);
                }
            } else if ((this.navApplication != null) || (this.navItem != null)) {
                this.delete();
            }
        } else if (tool.getHasMashup().equals(Constants.DATA_TRUE)) {
            if (this.navApplication == null) {
                this.createNavigationApplication(false);
            }
            if (this.navApplication == null) {
                this.delete();
            } else if (this.navItem == null) {
                this.createNavigationItem();
                if (this.navItem == null) {
                    this.delete();
                }
            } else {
                this.setName(tool.getName());
                this.setDescription(tool.getDescription());
                this.setIsAvailable();
            }
        } else if (!tool.getIsEnabled().equals(Constants.DATA_TRUE)
                || (this.navApplication != null) || (this.navItem != null)) {
            this.delete();
        }
        this.persist();

    }

    @Override
    public void setIsAvailable(boolean isAvailable) {

        super.setIsAvailable(isAvailable);
        this.navApplication.setIsCourseTool(isAvailable);
        this.navApplication.setIsOrgTool(isAvailable);
        this.navApplication.setIsGroupTool(isAvailable);

    }

    @Override
    protected void createNavigationItem() {

        super.createNavigationItem();
        this.navItem.setSubGroup("vtbe_mashup");
        this.navItem.setNavigationType(NavigationType.COURSE);
        this.navItem.setComponentType(ComponentType.MENU_ITEM);
        this.navItem.setIsEnabledMask(new Mask(3));
        this.navItem.setEntitlementUid("system.generic.VIEW");

    }

}
