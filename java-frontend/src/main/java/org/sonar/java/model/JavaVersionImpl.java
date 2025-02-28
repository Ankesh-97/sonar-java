/*
 * SonarQube Java
 * Copyright (C) 2012-2022 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.model;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.java.api.JavaVersion;

public class JavaVersionImpl implements JavaVersion {

  private static final Logger LOG = Loggers.get(JavaVersionImpl.class);

  private static final int JAVA_6 = 6;
  private static final int JAVA_7 = 7;
  private static final int JAVA_8 = 8;
  private static final int JAVA_9 = 9;
  private static final int JAVA_10 = 10;
  private static final int JAVA_12 = 12;
  private static final int JAVA_14 = 14;
  private static final int JAVA_15 = 15;
  private static final int JAVA_16 = 16;
  private static final int JAVA_17 = 17;
  public static final int MAX_SUPPORTED = JAVA_17;

  private final int javaVersion;

  public JavaVersionImpl() {
    this.javaVersion = -1;
  }

  public JavaVersionImpl(int javaVersion) {
    this.javaVersion = javaVersion;
  }

  public static JavaVersion fromString(String javaVersion) {
    try {
      String cleanedVersion = javaVersion.startsWith("1.") ? javaVersion.substring(2) : javaVersion;
      int versionAsInt = Integer.parseInt(cleanedVersion);
      return new JavaVersionImpl(versionAsInt);
    } catch (NumberFormatException e) {
      LOG.warn("Invalid java version (got \"" + javaVersion + "\"). "
        + "The version will be ignored. Accepted formats are \"1.X\", or simply \"X\" "
        + "(for instance: \"1.5\" or \"5\", \"1.6\" or \"6\", \"1.7\" or \"7\", etc.)");
      return new JavaVersionImpl();
    }
  }

  @Override
  public boolean isJava6Compatible() {
    return notSetOrAtLeast(JAVA_6);
  }

  @Override
  public boolean isJava7Compatible() {
    return notSetOrAtLeast(JAVA_7);
  }

  @Override
  public boolean isJava8Compatible() {
    return notSetOrAtLeast(JAVA_8);
  }

  @Override
  public boolean isJava9Compatible() {
    return JAVA_9 <= javaVersion;
  }

  @Override
  public boolean isJava10Compatible() {
    return JAVA_10 <= javaVersion;
  }

  @Override
  public boolean isJava12Compatible() {
    return JAVA_12 <= javaVersion;
  }

  @Override
  public boolean isJava14Compatible() {
    return JAVA_14 <= javaVersion;
  }

  @Override
  public boolean isJava15Compatible() {
    return JAVA_15 <= javaVersion;
  }

  @Override
  public boolean isJava16Compatible() {
    return JAVA_16 <= javaVersion;
  }

  private boolean notSetOrAtLeast(int requiredJavaVersion) {
    return isNotSet() || requiredJavaVersion <= javaVersion;
  }

  @Override
  public String java6CompatibilityMessage() {
    return compatibilityMessage(JAVA_6);
  }

  @Override
  public String java7CompatibilityMessage() {
    return compatibilityMessage(JAVA_7);
  }

  @Override
  public String java8CompatibilityMessage() {
    return compatibilityMessage(JAVA_8);
  }

  private String compatibilityMessage(int expected) {
    return isNotSet() ? (" (sonar.java.source not set. Assuming " + expected + " or greater.)") : "";
  }

  @Override
  public int asInt() {
    return javaVersion;
  }

  @Override
  public boolean isSet() {
    return javaVersion != -1;
  }

  @Override
  public boolean isNotSet() {
    return javaVersion == -1;
  }

  @Override
  public String effectiveJavaVersionAsString() {
    if (isNotSet()) {
      return Integer.toString(MAX_SUPPORTED);
    }
    return Integer.toString(javaVersion);
  }

  @Override
  public String toString() {
    return isNotSet() ? "none" : Integer.toString(javaVersion);
  }
}
