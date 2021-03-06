// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import java.util.concurrent.atomic.AtomicReference;

import io.vlingo.actors.Logger;
import io.vlingo.actors.World;
import io.vlingo.common.Tuple2;

public class Cluster {
  
  private static AtomicReference<ClusterSnapshotControl> control = new AtomicReference<>();
  private static World world;

  public static final synchronized Tuple2<ClusterSnapshotControl, Logger> controlFor(final String name) throws Exception {
    if (world != null) {
      throw new IllegalArgumentException("Cluster snapshot control already exists.");
    }
    return controlFor(World.start("vlingo-cluster"), name);
  }

  public static final synchronized Tuple2<ClusterSnapshotControl, Logger> controlFor(final World world, final String name) throws Exception {
    if (control.get() != null) {
      throw new IllegalArgumentException("Cluster snapshot control already exists.");
    }

    Cluster.world = world;

    final Tuple2<ClusterSnapshotControl, Logger> control = ClusterSnapshotControl.instance(world, name);
    
    Cluster.control.set(control._1);
    
    return control;
  }

  public static boolean isRunning() {
    return control.get() != null;
  }

  public static boolean isRunning(final boolean expected, final int retries) {
    for (int idx = 0; idx < retries; ++idx) {
      if (isRunning() == expected) {
        return expected;
      }
      try { Thread.sleep(500); } catch (Exception e) { }
    }
    return !expected;
  }

  static final synchronized void reset() {
    control.set(null);
  }
}
