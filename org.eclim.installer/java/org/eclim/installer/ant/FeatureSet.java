/**
 * Copyright (C) 2005 - 2013  Eric Van Dewoestine
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.eclim.installer.ant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.types.PatternSet;

import org.formic.InstallContext;
import org.formic.Installer;

/**
 * PatternSet built to include files based on selected features.
 *
 * @author Eric Van Dewoestine
 */
public class FeatureSet
  extends PatternSet
{
  private static final Pattern VERSIONED = Pattern.compile("\\d+$");

  public FeatureSet()
  {
    super();

    // standard includes
    createInclude().setName("**/org.eclim/**/*");
    createInclude().setName("**/org.eclim_*/**/*");
    createInclude().setName("**/org.eclim.core/**/*");
    createInclude().setName("**/org.eclim.core_*/**/*");
    createInclude().setName("**/org.eclim.vimplugin/**/*");
    createInclude().setName("**/org.eclim.vimplugin_*/**/*");

    // feature based includes
    InstallContext context = Installer.getContext();
    String[] keys = context.getKeysByPrefix("featureList");
    for (int ii = 0; ii < keys.length; ii++){
      String key = keys[ii];
      Boolean value = Boolean.valueOf(context.getValue(key).toString());
      if(value.booleanValue()){
        createFeatureIncludes(key);

        // handle per version features by removing the version suffix.
        Matcher matcher = VERSIONED.matcher(key);
        if (matcher.find()){
          createFeatureIncludes(matcher.replaceFirst(""));
        }
      }
    }
  }

  private void createFeatureIncludes(String feature)
  {
    String name = "**/org.eclim." + feature.substring(feature.indexOf('.') + 1);
    createInclude().setName(name + "/**/*");
    createInclude().setName(name + "_*/**/*");
  }
}
