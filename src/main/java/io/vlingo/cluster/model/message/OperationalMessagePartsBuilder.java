// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.message;

import java.util.Set;

import io.vlingo.wire.message.MessagePartsBuilder;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Name;
import io.vlingo.wire.node.Node;

class OperationalMessagePartsBuilder {
  public static String payloadFrom(final String content) {
    final int index1 = content.indexOf('\n');
    if (index1 == -1) return "";
    final int index2 = content.indexOf('\n', index1+1);
    if (index2 == -1) return "";
    final String payload = content.substring(index2+1);
    
    return payload;
  }

  public static String saysIdFrom(final String content) {
    final String[] parts = content.split("\n");

    if (parts.length < 3) {
      return "";
    }

    final String saysId = MessagePartsBuilder.parseField(parts[2], "si=");
    
    return saysId;
  }

  static final Set<Node> nodesFrom(String content) {
    return MessagePartsBuilder.nodesFrom(content);
  }

  static final Node nodeFrom(final String content) {
    return MessagePartsBuilder.nodeFrom(content);
  }

  static final Id idFrom(final String content) {
    return MessagePartsBuilder.idFrom(content);
  }

  static final Name nameFrom(final String content) {
    return MessagePartsBuilder.nameFrom(content);
  }
}
