// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.application.attributes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AttributeSet {
  public final String name;
  private final Map<String, TrackedAttribute> attributes;
  
  protected static AttributeSet named(final String name) {
    return new AttributeSet(name);
  }
  
  protected TrackedAttribute addIfAbsent(final Attribute<?> attribute) {
    final TrackedAttribute maybeAttribute = find(attribute);
    
    if (maybeAttribute.isAbsent()) {
      final TrackedAttribute nowPresent = TrackedAttribute.of(attribute);
      
      attributes.put(nowPresent.id, nowPresent);
      
      return nowPresent;
    }
    
    return maybeAttribute;
  }
  
  protected TrackedAttribute attributeNamed(final String name) {
    return find(name);
  }
  
  protected TrackedAttribute remove(final Attribute<?> attribute) {
    final TrackedAttribute maybeAttribute = find(attribute);
    
    if (maybeAttribute.isPresent()) {
      return attributes.remove(maybeAttribute.id);
    }
    
    return maybeAttribute;
  }
  
  protected TrackedAttribute replace(final Attribute<?> attribute) {
    final TrackedAttribute maybeAttribute = find(attribute);
    
    if (maybeAttribute.isPresent()) {
      final TrackedAttribute newlyTracked = maybeAttribute.withAttribute(attribute);
      attributes.put(maybeAttribute.id, newlyTracked);
      return newlyTracked;
    }
    
    return maybeAttribute;
  }

  @Override
  public int hashCode() {
    return 31 * this.name.hashCode() + this.attributes.hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || other.getClass() != AttributeSet.class) {
      return false;
    }
    
    AttributeSet otherAttributeSet = (AttributeSet) other;
    
    return this.name.equals(otherAttributeSet.name) &&
            this.attributes.equals(otherAttributeSet.attributes);
  }

  @Override
  public String toString() {
    return "AttributeSet[name=" + this.name + ", attributes=[" + this.attributes + "]]";
  }
  
  private AttributeSet(final String name) {
    this.name = name;
    this.attributes = new ConcurrentHashMap<>(128, 0.75f, 16);
  }
  
  private TrackedAttribute find(final Attribute<?> attribute) {
    return find(attribute.name);
  }
  
  private TrackedAttribute find(final String name) {
    for (final TrackedAttribute id : attributes.values()) {
      if (id.attribute.name.equals(name)) {
        return id;
      }
    }
    
    return TrackedAttribute.Absent;
  }
}
