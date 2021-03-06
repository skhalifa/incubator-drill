/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.Override;
import java.util.Vector;

import org.apache.drill.exec.vector.AllocationHelper;
import org.apache.drill.exec.vector.ValueVector.Mutator;
import org.apache.drill.exec.vector.complex.IndexHolder;
import org.apache.drill.exec.vector.complex.reader.FieldReader;

<@pp.dropOutputFile />
<#list vv.types as type>
<#list type.minor as minor>
<#list ["", "Nullable", "Repeated"] as mode>
<#assign name = mode + minor.class?cap_first />
<#assign eName = name />
<#assign javaType = (minor.javaType!type.javaType) />


<@pp.changeOutputFile name="/org/apache/drill/exec/vector/complex/impl/${eName}WriterImpl.java" />
<#include "/@includes/license.ftl" />

package org.apache.drill.exec.vector.complex.impl;

<#include "/@includes/vv_imports.ftl" />

/* This class is generated using freemarker and the ComplexWriters.java template */
@SuppressWarnings("unused")
public class ${eName}WriterImpl extends AbstractFieldWriter {
  
  private final ${name}Vector.Mutator mutator;
  final ${name}Vector vector;
  
  public ${eName}WriterImpl(${name}Vector vector, AbstractFieldWriter parent){
    super(parent);
    this.mutator = vector.getMutator();
    this.vector = vector;
  }

  public MaterializedField getField(){
    return vector.getField();
  }

  public int getValueCapacity() {
    return vector.getValueCapacity();
  }

  public void checkValueCapacity() {
    inform(vector.getValueCapacity() > idx());
  }

  public void allocate(){
    inform(vector.allocateNewSafe());
  }
  
  public void clear(){
    vector.clear();
  }
  
  protected int idx(){
    return super.idx();
  }
  
  protected void inform(boolean ok){
    super.inform(ok);
  }
  
  <#if mode == "Repeated">

  public void write(${minor.class?cap_first}Holder h){
    if(ok()){
      // update to inform(addSafe) once available for all repeated vector types for holders.
      inform(mutator.addSafe(idx(), h));
      vector.setCurrentValueCount(idx());
    }
  }
  
  public void write(Nullable${minor.class?cap_first}Holder h){
    if(ok()){
      // update to inform(addSafe) once available for all repeated vector types for holders.
      inform(mutator.addSafe(idx(), h));
      vector.setCurrentValueCount(idx());
    }
  }

  public void setPosition(int idx){
    if (ok()){
      super.setPosition(idx);
      inform(mutator.startNewGroup(idx));
    }
  }
  
  
  <#else>
  
  public void write(${minor.class}Holder h){
    if(ok()){
      // update to inform(setSafe) once available for all vector types for holders.
      inform(mutator.setSafe(idx(), h));
      vector.setCurrentValueCount(idx());
    }
  }
  
  public void write(Nullable${minor.class}Holder h){
    if(ok()){
      // update to inform(setSafe) once available for all vector types for holders.
      inform(mutator.setSafe(idx(), h));
      vector.setCurrentValueCount(idx());
    }
  }

  </#if>

}

<@pp.changeOutputFile name="/org/apache/drill/exec/vector/complex/writer/${eName}Writer.java" />
<#include "/@includes/license.ftl" />

package org.apache.drill.exec.vector.complex.writer;

<#include "/@includes/vv_imports.ftl" />
@SuppressWarnings("unused")
public interface ${eName}Writer extends BaseWriter{
  public void write(${minor.class}Holder h);
}



</#list>
</#list>
</#list>


